package com.preproject.seb_pre_15.auth.config;


//import com.preproject.seb_pre_15.auth.filter.JwtExceptionFilter;
import com.preproject.seb_pre_15.auth.filter.JwtVerificationFilter;
import com.preproject.seb_pre_15.auth.handler.MemberAuthenticationEntryPoint;
import com.preproject.seb_pre_15.auth.handler.OAuth2memberSuccessHandler;
import com.preproject.seb_pre_15.auth.jwt.JwtTokenizer;
import com.preproject.seb_pre_15.auth.utils.CustomAuthorityUtils;
import com.preproject.seb_pre_15.member.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfiguration {


    //TODO:

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberService memberService;

    public SecurityConfiguration(JwtTokenizer jwtTokenizer, CustomAuthorityUtils authorityUtils, MemberService memberService) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.memberService = memberService;
    }

    @Bean//개발환경
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .headers().frameOptions().sameOrigin() //h2 이용하기위한 설정
                .and()
                .csrf().disable()
//                .cors(withDefaults())
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()
                .exceptionHandling()
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint(jwtTokenizer,authorityUtils))
                .and()
                .apply(new CustomFilterConfigurer())
                .and()
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/members/**").hasAnyRole("ADMIN","USER")
                        .antMatchers(HttpMethod.GET,"/*/members").hasRole("ADMIN")
                        .antMatchers(HttpMethod.POST,"/**/questions").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH,"/**/questions/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE,"/**/questions/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST,"/**/answers").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH,"/**/answers/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE,"/**/answers/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST,"/**/answer-comment").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH,"/**/answer-comment/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE,"/**/answer-comment/**").hasRole("USER")
                        .antMatchers(HttpMethod.POST,"/**/question-comment").hasRole("USER")
                        .antMatchers(HttpMethod.PATCH,"/**/question-comment/**").hasRole("USER")
                        .antMatchers(HttpMethod.DELETE,"/**/question-comment/**").hasRole("USER")
                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2 -> oauth2.successHandler(new OAuth2memberSuccessHandler(jwtTokenizer,authorityUtils,memberService)))
                .logout()
                .logoutSuccessUrl("http://localhost:3000");
        return http.build();
    }

//    @Bean//실제
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
//        http
//
//                .csrf().disable()
//                .cors(withDefaults())
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
//                .formLogin().disable()
//                .httpBasic().disable()
//                .exceptionHandling()
//                .and()
//                .apply(new CustomFilterConfigurer())
//                .and()
//                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
//                .oauth2Login(oauth2 -> oauth2.successHandler(new OAuth2memberSuccessHandler(jwtTokenizer,authorityUtils)));
//        return http.build();
//    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOriginPatterns(Arrays.asList("{url}"));
//        configuration.setAllowedOrigins(Arrays.asList("*"));
//        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE"));
////        configuration.setAllowCredentials(Boolean.valueOf(true));
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000","http://localhost:8080", "https://659a-116-126-166-12.ngrok-free.app"));
        configuration.setAllowCredentials(true);
//        configuration.addAllowedMethod("*");
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "DELETE","OPTION"));
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("*");
        configuration.setMaxAge(3000L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer,HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) throws Exception {
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(jwtTokenizer,authorityUtils);
//            JwtExceptionFilter jwtExceptionFilter = new JwtExceptionFilter();
//            builder.addFilterBefore(jwtExceptionFilter, jwtVerificationFilter.getClass());
            builder.addFilterAfter(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class);
        }
    }
}
