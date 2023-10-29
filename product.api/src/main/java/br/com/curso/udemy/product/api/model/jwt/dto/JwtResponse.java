package br.com.curso.udemy.product.api.model.jwt.dto;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

    private Integer id;
    private String name;
    private String email;

    public static JwtResponse getUser(Claims claims){
        try {
            return JwtResponse
                    .builder()
                    .id((Integer) claims.get("id"))
                    .name((String) claims.get("name"))
                    .email((String) claims.get("email"))
                    .build();
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
