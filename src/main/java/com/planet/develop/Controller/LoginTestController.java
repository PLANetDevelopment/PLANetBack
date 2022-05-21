package com.planet.develop.Controller;

import com.planet.develop.Security.DTO.AuthMemberDTO;
import lombok.extern.java.Log;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@Log
@PreAuthorize("hasRole('USER')")
public class LoginTestController {

    @GetMapping("/user")
    public String exUser(@AuthenticationPrincipal AuthMemberDTO authMemberDTO) {
        return authMemberDTO.getEmail();
    }

    @GetMapping("/principal")
    public String exUser(Principal principal) {
        return principal.getName();
    }

}
