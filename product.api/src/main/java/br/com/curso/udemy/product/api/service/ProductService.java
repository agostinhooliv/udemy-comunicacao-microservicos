package br.com.curso.udemy.product.api.service;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.config.exception.ValidationException;
import br.com.curso.udemy.product.api.dto.CategoryResponse;
import br.com.curso.udemy.product.api.dto.ProductRequest;
import br.com.curso.udemy.product.api.dto.ProductResponse;
import br.com.curso.udemy.product.api.model.Product;
import br.com.curso.udemy.product.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;

    public List<ProductResponse> findByAll(){
        return productRepository
                .findAll()
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> findByName(String name){

        List<ProductResponse> productResponses = productRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        if(ObjectUtils.isEmpty(name)) {
            throw new ValidationException("The product name must be informed.");
        } if(ObjectUtils.isEmpty(productResponses)){
            throw new ValidationException("The product name was not found.");
        }

        return productResponses;
    }

    public List<ProductResponse> findBySupplierId(Integer supplierId){

        List<ProductResponse> productResponses = productRepository
                .findBySupplierId(supplierId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        if(ObjectUtils.isEmpty(supplierId)) {
            throw new ValidationException("The supplier name must be informed.");
        } if(ObjectUtils.isEmpty(productResponses)){
            throw new ValidationException("The supplier name was not found.");
        }

        return productResponses;
    }

    public List<ProductResponse> findByCategoryId(Integer categoryId){

        List<ProductResponse> supplierResponses = productRepository
                .findByCategoryId(categoryId)
                .stream()
                .map(ProductResponse::of)
                .collect(Collectors.toList());

        if(ObjectUtils.isEmpty(categoryId)) {
            throw new ValidationException("The product id must be informed.");
        } if(ObjectUtils.isEmpty(supplierResponses)){
            throw new ValidationException("The category id was not found.");
        }

        return supplierResponses;
    }

    public Product findById(Integer id){
        return productRepository.
                findById(id)
                .orElseThrow(() -> new ValidationException("There's no product for the given id."));
    }

    public ProductResponse findByIdResponse(Integer id){
        return ProductResponse.of(findById(id));
    }

    public ProductResponse save(ProductRequest request){
        this.validateProductDataInformed(request);
        this.validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }
    public ProductResponse update(ProductRequest request, Integer id){
        this.validateProductDataInformed(request);
        this.validateCategoryAndSupplierInformed(request);
        var category = categoryService.findById(request.getCategoryId());
        var supplier = supplierService.findById(request.getSupplierId());
        var product = Product.of(request, supplier, category);
        product.setId(id);
        productRepository.save(Product.of(request, supplier, category));
        return ProductResponse.of(product);
    }

    public Boolean existsByCategoryId(Integer categpryId){
        return productRepository.existsByCategoryId(categpryId);
    }

    public Boolean existsBySupplierId(Integer supplierId){
        return productRepository.existsBySupplierId(supplierId);
    }

    public SucessResponse delete(Integer id){
        this.validateInformedId(id);

        productRepository.deleteById(id);
        return SucessResponse.create("The product was deleted.");
    }

    private void validateInformedId(Integer id){
        if(ObjectUtils.isEmpty(id)){
            new ValidationException("The categgory id was not informed.");
        }
    }

    private void validateProductDataInformed(ProductRequest request){
        if(ObjectUtils.isEmpty(request.getName())){
            throw new ValidationException("The product name was not informed.");
        }
        if(ObjectUtils.isEmpty(request.getQuantityAvailable())){
            throw new ValidationException("The product quantity was not informed.");
        }
        if(request.getQuantityAvailable() <= 0){
            throw new ValidationException("The quantity should not be less or equal to zero.");
        }
    }

    private void validateCategoryAndSupplierInformed(ProductRequest request){
        if(ObjectUtils.isEmpty(request.getCategoryId())){
            throw new ValidationException("The category id was not informed.");
        }
        if(ObjectUtils.isEmpty(request.getSupplierId())){
            throw new ValidationException("The supplier id was not informed.");
        }
    }
}
