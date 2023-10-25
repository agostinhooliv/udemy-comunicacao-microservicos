package br.com.curso.udemy.product.api.config.exception;

import lombok.Data;

@Data
public class ExceptionDetails {

    private int status;
    private String message;
}
