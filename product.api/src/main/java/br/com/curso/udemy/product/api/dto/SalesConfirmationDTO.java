package br.com.curso.udemy.product.api.dto;

import br.com.curso.udemy.product.api.enums.SalesStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesConfirmationDTO {

    private String salesId;
    private SalesStatus status;
}
