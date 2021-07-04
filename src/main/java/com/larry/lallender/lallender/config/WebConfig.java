package com.larry.lallender.lallender.config;

import com.larry.lallender.lallender.dto.AuthUser;
import com.larry.lallender.lallender.exception.CalendarException;
import com.larry.lallender.lallender.exception.ErrorCode;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.larry.lallender.lallender.service.LoginService.LOGIN_SESSION_KEY;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginUserIdResolver());
    }

    public HandlerMethodArgumentResolver loginUserIdResolver() {
        return new HandlerMethodArgumentResolver() {

            @Override
            public boolean supportsParameter(MethodParameter parameter) {
                return AuthUser.class.isAssignableFrom(parameter.getParameterType());
            }

            @Override
            public Object resolveArgument(MethodParameter parameter,
                                          ModelAndViewContainer mavContainer,
                                          NativeWebRequest webRequest,
                                          WebDataBinderFactory binderFactory) throws Exception {
                final Long userId = (Long) webRequest.getAttribute(LOGIN_SESSION_KEY,
                                                                   WebRequest.SCOPE_SESSION);
                if (userId != null) {
                    return new AuthUser(userId);
                } else {
                    throw new CalendarException(ErrorCode.BAD_REQUEST);
                }
            }
        };
    }
}
