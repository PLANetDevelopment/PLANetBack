package com.planet.develop.Controller;

import com.planet.develop.Security.DTO.AuthMemberDTO;
import lombok.Getter;
import org.springframework.security.core.Authentication;

@Getter
public class BaseAuthentication {

    AuthMemberDTO authMember;

    public BaseAuthentication(Authentication authentication) {
        this.authMember = (AuthMemberDTO)authentication.getPrincipal();
    }

}
