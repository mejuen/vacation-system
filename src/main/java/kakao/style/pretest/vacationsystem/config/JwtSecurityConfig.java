package kakao.style.pretest.vacationsystem.config;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * SecurityConfigurerAdapter를 extends하여 configure 메소드를 오버라이드하여 위에서 만든
 * JwtFilter를 Security 로직에 적용하는 역할 수행
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    public JwtSecurityConfig(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void configure(HttpSecurity http){
        JwtFilter cuJwtFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(cuJwtFilter, UsernamePasswordAuthenticationFilter.class);

    }


}
