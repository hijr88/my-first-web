package com.yh.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        /*HttpSession session = request.getSession();
        CustomUserDetails user = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        session.setAttribute("profileImage", user.getProfileImage());*/
        resultRedirectStrategy(request, response, authentication);
    }

    /**
     * @param authentication 인증에 성공한 사용자의 정보를 가지고 있는객체
     */
    protected void resultRedirectStrategy(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication) throws IOException {
        SavedRequest savedRequest = requestCache.getRequest(request, response); //클라이언트 요청명
        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();
            log.info(savedRequest.toString());
            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else { //요청명이 존재하지 않을경우 == 다른 url에서 직접 로그인창을 요청한경우
            String defaultUrl = "/";
            redirectStrategy.sendRedirect(request, response, defaultUrl);
        }
    }
}
