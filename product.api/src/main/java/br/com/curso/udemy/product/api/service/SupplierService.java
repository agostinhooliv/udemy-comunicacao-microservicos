package br.com.curso.udemy.product.api.service;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.config.exception.ValidationException;
import br.com.curso.udemy.product.api.dto.SupplierRequest;
import br.com.curso.udemy.product.api.dto.SupplierResponse;
import br.com.curso.udemy.product.api.model.Supplier;
import br.com.curso.udemy.product.api.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SupplierService {

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ProductService productService;

    public List<SupplierResponse> findByAll(){
        return supplierRepository
                .findAll()
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());
    }

    public List<SupplierResponse> findByName(String name){

        List<SupplierResponse> supplierResponses = supplierRepository
                .findByNameIgnoreCaseContaining(name)
                .stream()
                .map(SupplierResponse::of)
                .collect(Collectors.toList());

        if(ObjectUtils.isEmpty(name)) {
            throw new ValidationException("The supplier name must be informed.");
        } if(ObjectUtils.isEmpty(supplierResponses)){
            throw new ValidationException("The supplier name was not found.");
        }

        return supplierResponses;
    }

    public SupplierResponse findByIdResponse(Integer id){
        return SupplierResponse.of(findById(id));
    }

    public Supplier findById(Integer id){
        this.validateInformedId(id);
        return supplierRepository.
                findById(id)
                .orElseThrow(() -> new ValidationException("There's no supplier for the given id."));
    }

    public SupplierResponse save(SupplierRequest request){
        this.validateSupplierNameInformed(request);
        var supplier = supplierRepository.save(Supplier.of(request));
        return SupplierResponse.of(supplier);
    }

    public SupplierResponse update(SupplierRequest request, Integer id){
        this.validateSupplierNameInformed(request);
        var supplier = Supplier.of(request);
        supplier.setId(id);
        supplierRepository.save(supplier);
        return SupplierResponse.of(supplier);
    }

    private void validateSupplierNameInformed(SupplierRequest request){
        if(ObjectUtils.isEmpty(request.getName())){
            throw new ValidationException("The supplier name was not informed.");
        }
    }

    private void validateInformedId(Integer id){
        if(ObjectUtils.isEmpty(id)){
            new ValidationException("The supplier id was not informed.");
        }
    }

    public SucessResponse delete(Integer id){
        this.validateInformedId(id);

        if(productService.existsBySupplierId(id)){
            throw new ValidationException("You cannot delete this supplier because it's already defined by a product.");
        }

        supplierRepository.deleteById(id);
        return SucessResponse.create("The supplier was deleted.");
    }
}
