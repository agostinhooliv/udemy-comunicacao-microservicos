package br.com.curso.udemy.product.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductRequest {

    private String name;
    @JsonProperty("quantity_avaliable")
    private Integer quantityAvailable;
    private Integer supplierId;
    private Integer categoryId;
}
