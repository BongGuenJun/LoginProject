package penguindisco.loginproject.listener;



import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.web.WebApplicationInitializer;



public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        // 세션 리스너 등록
        servletContext.addListener(SessionListener.class);
    }
}