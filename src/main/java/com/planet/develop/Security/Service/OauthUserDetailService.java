package com.planet.develop.Security.Service;

import com.planet.develop.Entity.User;
import com.planet.develop.Security.Enum.MemberRole;
import com.planet.develop.Repository.UserRepository;
import com.planet.develop.Security.DTO.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OauthUserDetailService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        String clientName = userRequest.getClientRegistration().getClientName();
        OAuth2User oAuth2User = super.loadUser(userRequest);

        // <naver>
        Map<String, String> NaverResponse = (Map<String, String>) oAuth2User.getAttributes().get("response");
        // <kakao>
        Map<String, String> KakaoAccount = (Map<String, String>) oAuth2User.getAttributes().get("kakao_account");
        Map<String, String> KakaoProperties = (Map<String, String>) oAuth2User.getAttributes().get("properties");

        String email = null;
        String name = null;

        if(clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        }
        if(clientName.equals("Naver")) {
            email = NaverResponse.get("email");
            name = NaverResponse.get("name");
        }
        if(clientName.equals("Kakao")) {
            email = KakaoAccount.get("email");
            name = KakaoProperties.get("nickname");
        }

        User user = saveSocialMember(email, name);
        AuthMemberDTO clubAuthMember = new AuthMemberDTO(
                user.getUserId(),
                user.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        clubAuthMember.setName(user.getUserName());
        return clubAuthMember;
    }

    /** db에 소셜 로그인 이메일과 이름 저장 */
    private User saveSocialMember(String email, String name) {
        Optional<User> result = userRepository.findByEmail(email);
        if (result.isPresent())
            return result.get();
        User user = User.builder()
                .userId(email)
                .userName(name)
                .build();
        user.addMemberRole(MemberRole.USER);
        userRepository.save(user);
        return user;
    }

}
