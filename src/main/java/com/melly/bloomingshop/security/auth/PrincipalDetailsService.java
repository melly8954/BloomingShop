package com.melly.bloomingshop.security.auth;

import com.melly.bloomingshop.domain.User;
import com.melly.bloomingshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// Spring Security 의 formLogin() 설정에서 .loginProcessingUrl()을 지정하면
// 이 URL로 POST 요청이 들어올 때 Spring Security 는 해당 요청을 처리하고
// 자동으로 UserDetailsService의 loadUserByUsername() 메서드를 호출하여 사용자를 인증한다.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // Spring Security에서 로그인 요청이 들어올 때 호출되는 메서드로,
    // 전달받은 loginId를 사용해 사용자 정보를 데이터베이스에서 조회하고,
    // UserDetails 인터페이스를 구현한 객체(PrincipalDetails)를 반환한다.
    // 반환된 UserDetails 객체는 AuthenticationManager에 의해 Authentication 객체에 저장된다.
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        User user = userRepository.findByLoginId(loginId);
        if (user == null) {
            throw new UsernameNotFoundException("존재하지 않는 계정입니다.");
        }
        return new PrincipalDetails(user);  // 계정 상태 검증은 CustomAuthenticationProvider에서 수행
    }
}
