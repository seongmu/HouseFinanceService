package com.kakao.housefinance.junit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ejb.access.EjbAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.kakao.housefinance.message.request.LoginForm;
import com.kakao.housefinance.message.response.JwtResponse;
import com.kakao.housefinance.model.Role;
import com.kakao.housefinance.model.RoleName;
import com.kakao.housefinance.model.User;
import com.kakao.housefinance.repo.RoleRepository;
import com.kakao.housefinance.repo.UserRepository;
import com.kakao.housefinance.security.jwt.JwtProvider;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JwtTest {

	@Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtProvider jwtProvider;
    
    // signup 단위테스트
	@Test
	public void registerUser() throws Exception {
		// given
		if (userRepository.existsByUserid("adam1")) {
			throw new EjbAccessException("Fail -> Userid is already taken!");
		}

		if (userRepository.existsByUsername("adamgkz")) {
			throw new EjbAccessException("Fail -> Username is already taken!");
		}

		if (userRepository.existsByEmail("adam@grokonez.com")) {
			throw new EjbAccessException("Fail -> Email is already taken!");
		}

		// Creating user's account
		User user = new User("adamgkz", "adamgkz", "adam@grokonez.com", encoder.encode("123456789"), "adam1");

		Set<String> strRoles = new HashSet<String>(); // signUpRequest.getRole();
		strRoles.add("user");
		strRoles.add("pm");
		Set<Role> roles = new HashSet<>();

		for (String role : strRoles) {
			switch (role) {
			case "admin":
				Role adminRole = roleRepository.findByName(RoleName.ROLE_ADMIN)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(adminRole);

				break;
			case "pm":
				Role pmRole = roleRepository.findByName(RoleName.ROLE_PM)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(pmRole);

				break;
			default:
				Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
						.orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
				roles.add(userRole);
			}        
        }
		
		user.setRoles(roles);
		
		// when
		userRepository.save(user);
		
		// then
		Optional<User> re = userRepository.findByUserid("adam1");

		System.out.println(re.get().getUserid());
    }
	
	// signin 단위테스트
	@Test
    public void authenticateUser() {

		// given
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        //loginRequest.getUsername(),
                		"jack1",
                        "123456789"
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // when
        String jwt = jwtProvider.generateJwtToken(authentication);
        
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        
        tokenMap.put("tokenType", new JwtResponse(jwt).getTokenType()); // 토큰 타입
        tokenMap.put("accessToken", new JwtResponse(jwt).getAccessToken()); // 액세스토큰
        
        // then
        ResponseEntity<?> re = ResponseEntity.ok(tokenMap);
        
        System.out.println("re : " + re.toString());
    }
	
	// refresh 단위테스트
	@Test
    public void refreshUser() {
		// given
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        //loginRequest.getUsername(),
                		"jack1",
                		"123456789"
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
       
        // Authorization 에 이미 발급 받은 토큰이 있는데 여기에 Bearer Token 을 넣는다면 클라이언트에서
        // 요청시 받는 토큰이 사라지므로 토큰 앞에 붙여서 전송하는것으로 변경함
        // when 
        // 서버에서 refresh 요청이 온 토큰을 받았다
        String accRefresh = "Bearer Token " + jwtProvider.generateJwtToken(authentication);
        
        // 앞쪽에 붙어있는 리프레시 요청 문자열 제거
        accRefresh = accRefresh.replace("Bearer Token ","");
        
        // when
        String jwtRefresh = jwtProvider.refreshGenerateJwtToken(authentication);
        
        Map<String, String> tokenMap = new HashMap<String, String>();
        
        tokenMap.put("tokenType", new JwtResponse(jwtRefresh).getTokenType()); // 토큰 타입
        tokenMap.put("refreshToken", new JwtResponse(jwtRefresh).getAccessToken()); // 리프레시 토큰
        
        // then
        ResponseEntity<?> re = ResponseEntity.ok(tokenMap);
        
        System.out.println("re : " + re.toString());

    }
}
