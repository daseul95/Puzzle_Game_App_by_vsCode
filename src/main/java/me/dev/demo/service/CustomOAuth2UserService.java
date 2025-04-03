package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import me.dev.demo.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import me.dev.demo.dto.UserDTO;
import me.dev.demo.dto.oauth2.*;
import me.dev.demo.domain.User;
import lombok.Builder;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserRepository userRepository;
    
    @Override
    public CustomOAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    	// OAuth2 제공자로부터 사용자 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(request);
        saveOrUpdate(oAuth2User);
        // 제공자
        String registration = request.getClientRegistration().getRegistrationId();
        
        // 제공자별로 객체를 구현하여 OAuth2Response 타입으로 반환할거다.
        // 이 다음 섹션에서 구현할거다.
        OAuth2Response oAuth2Response;
        if("google".equals(registration)){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        // 제공자별 분기 처리(잠깐 주석처리할 예정)
        // if("kakao".equals(registration)) {
        // 	oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        // } else if("naver".equals(registration)) {
        // 	oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        // } else if("google".equals(registration)) {
        // 	oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        // } else {
        // 	return null;
        // }
        
        // 사용자명을 제공자_회원아이디 형식으로만들어 저장할거다. 이 값은 redis에서도 key로 쓰일 예정
        String username = oAuth2Response.getProvider() + "_" + oAuth2Response.getProviderId();
        // 넘어온 회원정보가 이미 우리의 테이블에 존재하는지 확인
        User existData = userRepository.findByUserName(username);
        
        // 존재하지 않는다면 회원정보를 저장하고 CustomOAuth2User 반환
        if(existData == null) {
			// User user = new User();
            // user.setUserName(username);
            // user.setName(oAuth2Response.getName());
            // user.setEmail(oAuth2Response.getEmail());
            // user.setProfileImage(oAuth2Response.getProfileImage());
            // user.setRole("ROLE_USER");

            User user = User.builder().email(oAuth2Response.getEmail())
                                    .name(oAuth2Response.getName())
                                    .userName(username)
                                    .profileImage(oAuth2Response.getProfileImage())
                                    .role("ROLE_USER")
                                    .build();

            userRepository.save(user);

            UserDTO userDTO = new UserDTO();
            userDTO.setUserName(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setProfileImage(oAuth2Response.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {		// 회원정보가 존재한다면 조회된 데이터로 반환한다.
        	// existData.setEmail(oAuth2Response.getEmail());
            // existData.setName(oAuth2Response.getName());
            // existData.setProfileImage(oAuth2Response.getProfileImage());
            User existedData = User.builder().email(oAuth2Response.getEmail()).
            name(oAuth2Response.getName()).profileImage(oAuth2Response.getProfileImage()).build();

            userRepository.save(existedData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUserName(username);
            userDTO.setName(existData.getName());
            userDTO.setProfileImage(existData.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
    }

    private User saveOrUpdate(OAuth2User oAuth2User){
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name))
                .orElse(User.builder()
                        .email(email)
                        .name(name)
                        .build());
        return userRepository.save(user);
    }
}
    
