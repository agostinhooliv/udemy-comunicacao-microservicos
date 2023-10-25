package br.com.curso.udemy.product.api.model;

import br.com.curso.udemy.product.api.dto.CategoryRequest;
import br.com.curso.udemy.product.api.dto.SupplierRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.beans.BeanUtils;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "SUPPLIER")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "NAME", nullable = false)
    private String name;

    public static Supplier of(SupplierRequest request){
        var supplier = new Supplier();
        BeanUtils.copyProperties(request, supplier);
        return supplier;
    }
}
