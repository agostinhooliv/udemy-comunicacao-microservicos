package br.com.curso.udemy.product.api.controller;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.dto.ProductCheckStockRequest;
import br.com.curso.udemy.product.api.dto.ProductRequest;
import br.com.curso.udemy.product.api.dto.ProductResponse;
import br.com.curso.udemy.product.api.dto.ProductSalesResponse;
import br.com.curso.udemy.product.api.dto.ProductStockDTO;
import br.com.curso.udemy.product.api.service.ProductService;
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
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    public ProductService productService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductResponse save(@RequestBody ProductRequest productRequest) {
        return productService.save(productRequest);
    }

    @GetMapping
    public List<ProductResponse> findAll(){
        return productService.findByAll();
    }

    @GetMapping("{id}")
    public ProductResponse findById(@PathVariable Integer id){
        return productService.findByIdResponse(id);
    }

    @GetMapping("name/{name}")
    public List<ProductResponse>  findByName(@PathVariable String name){
        return productService.findByName(name);
    }

    @GetMapping("category/{categoryId}")
    public List<ProductResponse> findByCategoryId(@PathVariable Integer categoryId){
        return productService.findByCategoryId(categoryId);
    }

    @GetMapping("supplier/{supplierId}")
    public List<ProductResponse> findBySupplierId(@PathVariable Integer supplierId){
        return productService.findBySupplierId(supplierId);
    }

    @PutMapping("{id}")
    public ProductResponse update(@RequestBody ProductRequest productRequest, @PathVariable Integer id){
        return productService.update(productRequest, id);
    }

    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id){
        return productService.delete(id);
    }

    @PostMapping("check-stock")
    public SucessResponse checkProductStock(@RequestBody ProductCheckStockRequest productCheckStockRequest){
        return productService.checkProductStock(productCheckStockRequest);
    }

    @GetMapping("{id}/sales")
    public ProductSalesResponse findProductSales(@PathVariable Integer id){
        return productService.findProductsSales(id);
    }
}
