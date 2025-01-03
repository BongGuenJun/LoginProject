package penguindisco.loginproject.service;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import penguindisco.loginproject.repository.UserRepository;
import penguindisco.loginproject.domain.Users;

public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User;
        try {
            oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        } catch (OAuth2AuthenticationException e) {
            OAuth2Error oauth2Error = new OAuth2Error("oauth2_error", "Failed to load user info from OAuth2 provider", null);
            throw new OAuth2AuthenticationException(oauth2Error, e);
        }

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        String email = null;
        String name = null;
        String providerId = null;

        try {
            if ("naver".equals(provider)) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");
                if (response == null) {
                    throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "No response from Naver OAuth2 provider");
                }
                providerId = (String) response.get("id");
                email = (String) response.get("email");
                name = (String) response.get("name");
            } else if ("google".equals(provider)) {
                providerId = oAuth2User.getName();
                email = (String) attributes.get("email");
                name = (String) attributes.get("name");
            } else if ("kakao".equals(provider)) {
                Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
                if (kakaoAccount == null) {
                    throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "No kakao_account attribute from Kakao OAuth2 provider");
                }
                providerId = String.valueOf(attributes.get("id"));
                email = (String) kakaoAccount.get("email");
                name = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
            }
        } catch (Exception e) {
            OAuth2Error oauth2Error = new OAuth2Error("oauth2_error", "Failed to parse user info from OAuth2 provider", null);
            throw new OAuth2AuthenticationException(oauth2Error, e);
        }

        String loginType = provider.toUpperCase();
        System.out.println("Login Type: " + loginType);
        System.out.println("Provider ID: " + providerId);
        System.out.println("Email: " + email);
        System.out.println("Name: " + name);

        if (email == null || name == null) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "Email or Name not found from OAuth2 provider");
        }

        final String emailFinal = email;
        final String nameFinal = name;
        final String loginTypeFinal = loginType;
        final String providerIdFinal = providerId;

        Users user = userRepository.findByEmail(emailFinal)
                .orElseGet(() -> createUser(emailFinal, nameFinal, loginTypeFinal, providerIdFinal));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                oAuth2User.getAttributes(),
                "email");
    }

    private Users createUser(String email, String name, String loginType, String providerId) {
        Users user = new Users();
        user.setEmail(email);
        user.setName(name);

        try {
            user.setLoginType(LoginType.valueOf(loginType));
        } catch (IllegalArgumentException e) {
            throw new OAuth2AuthenticationException(new OAuth2Error("oauth2_error"), "Unsupported loginType: " + loginType);
        }

        user.setProviderId(providerId);
        user.setPhone(null);
        user.setPhoneVerify(0);
        user.setZipcode(null);
        user.setAddress1(null);
        user.setAddress2(null);
        user.setPoint(0);

        return userRepository.save(user);
    }
}