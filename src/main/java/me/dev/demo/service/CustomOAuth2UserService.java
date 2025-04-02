package me.dev.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import me.dev.demo.repository.UserEntityRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import me.dev.demo.dto.UserDTO;
import me.dev.demo.dto.oauth2.*;
import me.dev.demo.entity.UserEntity;


@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

	private final UserEntityRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
    	// 부모 클래스의 메서드를 사용하여 객체를 생성함.
    	OAuth2User oAuth2User = super.loadUser(request);
        
        // 제공자
        String registration = request.getClientRegistration().getRegistrationId();
        
        // 제공자별로 객체를 구현하여 OAuth2Response 타입으로 반환할거다.
        // 이 다음 섹션에서 구현할거다.
        OAuth2Response oAuth2Response = null;
        if("google".equals(registration)){
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }else{
            return null;
        }

        // 제공자별 분기 처리(잠깐 주석처리할 예정정)
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
        UserEntity existData = userRepository.findByUsername(username);
        
        // 존재하지 않는다면 회원정보를 저장하고 CustomOAuth2User 반환
        if(existData == null) {
			UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setName(oAuth2Response.getName());
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setProfileImage(oAuth2Response.getProfileImage());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setProfileImage(oAuth2Response.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        } else {		// 회원정보가 존재한다면 조회된 데이터로 반환한다.
        	existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());
            existData.setProfileImage(oAuth2Response.getProfileImage());

            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(existData.getName());
            userDTO.setProfileImage(existData.getProfileImage());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);
        }
    }

}