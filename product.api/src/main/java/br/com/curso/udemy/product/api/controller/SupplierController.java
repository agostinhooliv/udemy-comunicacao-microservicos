package br.com.curso.udemy.product.api.controller;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.dto.SupplierRequest;
import br.com.curso.udemy.product.api.dto.SupplierResponse;
import br.com.curso.udemy.product.api.service.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    @Autowired
    public SupplierService supplierService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public SupplierResponse save(@RequestBody SupplierRequest supplierRequest) {
        return supplierService.save(supplierRequest);
    }

    @GetMapping
    public List<SupplierResponse> findAll(){
        return supplierService.findByAll();
    }

    @GetMapping("{id}")
    public SupplierResponse findById(@PathVariable Integer id){
        return supplierService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<SupplierResponse> findByName(@PathVariable String name){
        return supplierService.findByName(name);
    }

    @PutMapping("{id}")
    public SupplierResponse update(@RequestBody SupplierRequest supplierRequest, @PathVariable Integer id){
        return supplierService.update(supplierRequest, id);
    }

    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id){
        return supplierService.delete(id);
    }
}
