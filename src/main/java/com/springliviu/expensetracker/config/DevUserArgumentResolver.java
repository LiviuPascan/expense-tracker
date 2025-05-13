package com.springliviu.expensetracker.config;

import com.springliviu.expensetracker.model.User;
import com.springliviu.expensetracker.security.UserDetailsImpl;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class DevUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class)
                && parameter.getParameterType().equals(UserDetailsImpl.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            org.springframework.web.bind.support.WebDataBinderFactory binderFactory
    ) {
        // 👇 фейковый пользователь с id = 1L
        User user = new User();
        user.setId(1L);
        return new UserDetailsImpl(user);
    }
}
