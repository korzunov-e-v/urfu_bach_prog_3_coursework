package ru.ekorzunov.urfu_bach_prog_3_coursework.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.User;
import ru.ekorzunov.urfu_bach_prog_3_coursework.entity.UserAction;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserActionRepository;
import ru.ekorzunov.urfu_bach_prog_3_coursework.repository.UserRepository;

import java.security.Principal;
import java.util.Date;

@Component
@Configurable
public class LoggingInterceptor implements HandlerInterceptor {

    @Autowired
    UserActionRepository userActionRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Principal principal = request.getUserPrincipal();
        User user;
        if (principal != null) {
            user = userRepository.findByEmail(principal.getName());
        } else {
            user = null;
        }
        UserAction.StatusEnum status;

        if (ex != null) {
            status = UserAction.StatusEnum.FAILED;
        } else {
            status = UserAction.StatusEnum.SUCCESS;
        }

        HandlerMethod hm = (HandlerMethod) handler;

        UserAction userAction = new UserAction(
                0L,
                hm.getMethod().getName(),
                status,
                response.getStatus(),
                new Date(),
                user
        );

        userActionRepository.save(userAction);

    }

}
