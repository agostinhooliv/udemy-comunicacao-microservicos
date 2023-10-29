package br.com.curso.udemy.product.api.model.jwt.service;

import br.com.curso.udemy.product.api.config.exception.AuthenticationException;
import br.com.curso.udemy.product.api.model.jwt.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String BEARER = "bearer ";

    @Value("${app-config.secrets.api-secret}")
    private String apiSecret;

    public void validateAuthorization(String token){
        try {

            var acessToken = extractToken(token);
            var claims = Jwts
                    .parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(apiSecret.getBytes()))
                    .build()
                    .parseClaimsJws(acessToken)
                    .getBody();

            var user = JwtResponse.getUser(claims);

            if(ObjectUtils.isEmpty(user) || ObjectUtils.isEmpty(user.getId())){
                throw new AuthenticationException("The user is not valid.");
            }

        } catch (Exception ex){
            ex.printStackTrace();
            throw new AuthenticationException("Error while trying to process the access token");
        }
    }

    private String extractToken(String token){

        if(ObjectUtils.isEmpty(token)){
            throw new AuthenticationException("The acess token was not informed.");
        }
        if(token.toLowerCase().contains(BEARER)){
            return token.replace(BEARER, Strings.EMPTY);
        }

        return token;
    }
}
