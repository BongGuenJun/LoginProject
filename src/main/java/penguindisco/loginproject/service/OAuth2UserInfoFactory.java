package penguindisco.loginproject.service;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String provider, Map<String, Object> attributes) {
        if ("google".equalsIgnoreCase(provider)) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if ("naver".equalsIgnoreCase(provider)) {
            return new NaverOAuth2UserInfo(attributes);
        } else if ("kakao".equalsIgnoreCase(provider)) {
            return new KakaoOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + provider + " is not supported yet.");
        }
    }
}