package br.com.curso.udemy.product.api.repository;

import br.com.curso.udemy.product.api.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository <Product, Integer> {

    List<Product> findByNameIgnoreCaseContaining(String description);

    List<Product> findByCategoryId(Integer id);

    List<Product> findBySupplierId(Integer id);

    Boolean existsBySupplierId(Integer id);

    Boolean existsByCategoryId(Integer id);
}
