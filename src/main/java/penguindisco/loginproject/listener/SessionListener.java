package penguindisco.loginproject.listener;



import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;


@Component
public class SessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        // 세션이 생성될 때 초기화 작업 수행
        System.out.println("Session created: " + se.getSession().getId());
        // 세션이 유효한 경우에만 무효화
        if (se.getSession() != null && !se.getSession().isNew()) {
            se.getSession().invalidate(); // 세션 무효화 (초기화)
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        // 필요 시 세션이 파괴될 때 추가 작업 수행
        System.out.println("Session destroyed: " + se.getSession().getId());
    }
}