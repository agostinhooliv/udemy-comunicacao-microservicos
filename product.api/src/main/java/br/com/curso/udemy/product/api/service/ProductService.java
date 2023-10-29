package br.com.curso.udemy.product.api.service;

import br.com.curso.udemy.product.api.client.SalesClient;
import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.config.exception.ValidationException;
import br.com.curso.udemy.product.api.dto.ProductCheckStockRequest;
import br.com.curso.udemy.product.api.dto.ProductQuantityDTO;
import br.com.curso.udemy.product.api.dto.ProductRequest;
import br.com.curso.udemy.product.api.dto.ProductResponse;
import br.com.curso.udemy.product.api.dto.ProductSalesResponse;
import br.com.curso.udemy.product.api.dto.ProductStockDTO;
import br.com.curso.udemy.product.api.dto.SalesConfirmationDTO;
import br.com.curso.udemy.product.api.enums.SalesStatus;
import br.com.curso.udemy.product.api.model.Product;
import br.com.curso.udemy.product.api.rabbitmq.SalesConfirmationSender;
import br.com.curso.udemy.product.api.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SalesConfirmationSender salesConfirmationSender;
    @Autowired
    private SalesClient salesClient;

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

    @Transactional
    public void updateProductStock(ProductStockDTO productStockDTO){
        try{
            validateStockUpdateData(productStockDTO);

            var productsForUpdate = new ArrayList<Product>();

            productStockDTO
                    .getProducts()
                    .forEach(salesProduct -> {
                        var existingProduct = findById(salesProduct.getProductId());
                        if(salesProduct.getQuantity() > existingProduct.getQuantityAvailable()){
                            throw new ValidationException("The product %s is out of stock." +existingProduct.getId());
                        }

                        existingProduct.updateStock(salesProduct.getQuantity());
                        productsForUpdate.add(existingProduct);
                    });
            if(!ObjectUtils.isEmpty(productsForUpdate)){
                productRepository.saveAll(productsForUpdate);
            }
            salesConfirmationSender.sendSalesConfirmationMessage(new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.APPROVED));
        } catch (Exception ex) {
            log.error("Error while trying to update stock for message with error: {}", ex.getMessage(), ex);
            salesConfirmationSender.sendSalesConfirmationMessage(new SalesConfirmationDTO(productStockDTO.getSalesId(), SalesStatus.REJECTED));
        }
    }

    private void validateStockUpdateData(ProductStockDTO productStockDTO){
        if(ObjectUtils.isEmpty(productStockDTO) || ObjectUtils.isEmpty(productStockDTO.getSalesId())){
            throw  new ValidationException("The product data and sales id must be informed.");
        }

        if(ObjectUtils.isEmpty(productStockDTO.getProducts())){
            throw new ValidationException("The product of the sale must be informed.");
        }

        productStockDTO
                .getProducts()
                .forEach(salesProduct -> {
                    if(ObjectUtils.isEmpty(salesProduct.getProductId()) || ObjectUtils.isEmpty(salesProduct.getQuantity())){
                        throw new ValidationException("The product id and quantity must be informed.");
                    }
                });
    }

    public ProductSalesResponse findProductsSales(Integer id){

        var product = findById(id);

        try {
            var sales = salesClient
                    .findSalesByProductId(product.getId())
                    .orElseThrow(() -> new ValidationException("The sales was not found by this product."));
            return ProductSalesResponse.of(product, sales.getSales());
        } catch (Exception ex){
            throw new ValidationException("There was an error trying to get the product'' sales");
        }
    }

    public SucessResponse checkProductStock(ProductCheckStockRequest productCheckStockRequest){
        if(ObjectUtils.isEmpty(productCheckStockRequest) || ObjectUtils.isEmpty(productCheckStockRequest.getProducts())){
            throw new ValidationException("The request data must be informed.");

        }
        productCheckStockRequest
                .getProducts()
                .forEach(this::validateStock);
        return SucessResponse.create("The stock is ok");
    }

    private void validateStock(ProductQuantityDTO productQuantityDTO){
        if(ObjectUtils.isEmpty(productQuantityDTO.getProductId()) || ObjectUtils.isEmpty(productQuantityDTO.getQuantity())){
            throw new ValidationException("Product id and quantity must be informed.");
        }
        var product = findById(productQuantityDTO.getProductId());
        if(productQuantityDTO.getQuantity() > product.getQuantityAvailable()){
            throw new ValidationException(String.format("The product %s is out of stock. ", product.getId()));
        }
    }
}
