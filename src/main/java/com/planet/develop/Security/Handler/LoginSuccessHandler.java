package com.planet.develop.Security.Handler;

import com.planet.develop.Security.DTO.AuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Log4j2
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public LoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    int year = LocalDate.now().getYear();
    int month = LocalDate.now().getMonthValue();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        AuthMemberDTO authMember = (AuthMemberDTO)authentication.getPrincipal();
        LocalDate curDate = LocalDate.now();
        String redirectUrl = "https://updating.d9uf6j6zzft4r.amplifyapp.com";
        redirectStrategy.sendRedirect(request, response, redirectUrl);
    }

}
