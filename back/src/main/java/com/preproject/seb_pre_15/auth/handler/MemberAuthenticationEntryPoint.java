package com.preproject.seb_pre_15.auth.handler;


import com.preproject.seb_pre_15.auth.jwt.JwtTokenizer;
import com.preproject.seb_pre_15.auth.utils.CustomAuthorityUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        Exception exception = (Exception) request.getAttribute("exception");
//        ErrorResponder.sendErrorResponse(response, HttpStatus.UNAUTHORIZED);

        if (exception instanceof ExpiredJwtException) {
            String refreshToken = request.getHeader("Refresh");



            Map<String, Object> claims = verifyJws(request);
            Map<String, Object>  newClaims = new HashMap<>();
            String username = (String) claims.get("sub");
            List<String> roles = authorityUtils.createRoles(username);
            newClaims.put("username",username);
            newClaims.put("roles", roles);

            Date expiration = jwtTokenizer.getTokenExpiration(jwtTokenizer.getAccessTokenExpirationMinutes());
            String base64EncodedSecretKey = jwtTokenizer.encodedBasedSecretKey(jwtTokenizer.getSecretKey());
            String accessToken = jwtTokenizer.generateAccessToken(newClaims,username,expiration, base64EncodedSecretKey);
            String newRefreshToken = refreshToken.replace("Bearer ", "");

            setAuthenticationToContext(newClaims);

            System.out.println("++++++++++++Refreshed Token"+SecurityContextHolder.getContext().getAuthentication());
//            URI redirectUri = createURI(accessToken, newRefreshToken);

//            response.setHeader("Authorization", accessToken);
//            response.setHeader("Refresh", newRefreshToken);
//            response.setStatus(HttpServletResponse.SC_FOUND);
//            response.setHeader("Location", redirectUri.toString());
//            response.setHeader("Location", "http://localhost:3000/");
            System.out.println("+++++++++++++++++++++++++++new token generated+++++++++++++++++++++++++++++");
            response.setHeader("Authorization",accessToken);


        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Set the unauthorized status code
            response.getWriter().write("Token expired or invalid"); // Set the error message
            logExceptionMessage(authException, exception);
        }

    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List)claims.get("roles"));

        Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);


        System.out.println("username:"+username);


        SecurityContextHolder.getContext().setAuthentication(authentication);
    }



    private URI createURI(String accessToken, String refreshToken) {
        MultiValueMap<String,String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(3000)
                .path("/mytokens")//redirect 받기 위한 주소
                .queryParams(queryParams)
                .build().toUri();
    }

    private Map<String, Object> verifyJws(HttpServletRequest request) {
        String jws = request.getHeader("Refresh").replace("Bearer ", "");
        String base64EncodedSecretKey = jwtTokenizer.encodedBasedSecretKey(jwtTokenizer.getSecretKey());
        Map<String,Object> claims = jwtTokenizer.getClaims(jws,base64EncodedSecretKey).getBody();
        return claims;
    }

    private void logExceptionMessage(AuthenticationException authException, Exception exception) {
        String message = exception != null ? exception.getMessage() : authException.getMessage();
        log.warn("Unauthorized error happened: {}", message);
    }

}

