package br.com.curso.udemy.product.api.repository;

import br.com.curso.udemy.product.api.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Integer> {

    List<Supplier> findByNameIgnoreCaseContaining(String description);
}
