package br.com.curso.udemy.product.api.config.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SucessResponse {

    private Integer status;
    private String message;

    public static SucessResponse create(String message){
        return SucessResponse
                .builder()
                .status(HttpStatus.OK.value())
                .message(message)
                .build();
    }
}
