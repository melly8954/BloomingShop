package com.melly.bloomingshop.security.config;

import com.melly.bloomingshop.security.auth.CustomAuthenticationFailureHandler;
import com.melly.bloomingshop.security.auth.CustomAuthenticationSuccessHandler;
import com.melly.bloomingshop.security.auth.PrincipalDetailsService;
import com.melly.bloomingshop.security.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity  // Spring Security 필터가 Spring의 필터 체인에 등록되고, 애플리케이션의 보안 구성이 가능해진다. / Spring Security의 웹 보안을 활성화
@EnableMethodSecurity(securedEnabled = true)   // @Secured 어노테이션 활성화 ( 구 EnableGlobalMethodSecurity )
public class SecurityConfig {
    // 이 값을 변경하면 화면 템플릿의 {{#loginUser}} 도 변경해야 함
    public static final String LOGINUSER = "login_user";

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final PrincipalDetailsService principalDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().permitAll()) // 모든 사용자가 접근할 수 있도록 허용
                .formLogin(form -> form
                        .loginPage("/user/login")
                        .usernameParameter("loginId")   // Spring Security 의 formLogin() 설정에서 기본적으로 username 파라미터 이름을 사용하므로 변경
                        .loginProcessingUrl("/api/user/login")
                        .successHandler(customAuthenticationSuccessHandler)  // 로그인 성공 후 핸들러 설정
                        .failureHandler(customAuthenticationFailureHandler)
                        .permitAll())
                .rememberMe(rememberMe -> rememberMe
                        .key(UUID.randomUUID().toString())  // remember-me 토큰을 식별하는 키
                        .tokenValiditySeconds(86400)  // remember-me 토큰의 유효 기간 (초 단위, 예: 1일)
                        .userDetailsService(principalDetailsService)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")  // 로그아웃 URL
                        .logoutSuccessUrl("/")  // 로그아웃 후 리다이렉트 URL
                        .invalidateHttpSession(true)  // 세션 무효화
                        .clearAuthentication(true)  // 인증 정보 지우기
                        .permitAll())  // 로그아웃 URL은 모두 허용
                .oauth2Login(oauth  -> oauth
                        .loginPage("/user/login")  // 구글 로그인이 완료된 뒤 후처리가 필요함
                        .failureHandler(customAuthenticationFailureHandler)
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(principalOauth2UserService)
                        ));
        return http.build();
    }
}
