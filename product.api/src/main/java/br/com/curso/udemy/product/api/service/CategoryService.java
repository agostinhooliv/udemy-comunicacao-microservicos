package br.com.curso.udemy.product.api.service;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.config.exception.ValidationException;
import br.com.curso.udemy.product.api.dto.CategoryRequest;
import br.com.curso.udemy.product.api.dto.CategoryResponse;
import br.com.curso.udemy.product.api.model.Category;
import br.com.curso.udemy.product.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductService productService;

    public CategoryResponse findByIdResponse(Integer id){
        return CategoryResponse.of(findById(id));
    }

    public List<CategoryResponse> findByAll(){
        return categoryRepository
                .findAll()
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());
    }

    public List<CategoryResponse> findByDescription(String description){

        List<CategoryResponse> categoryResponses = categoryRepository
                .findByDescriptionIgnoreCaseContaining(description)
                .stream()
                .map(CategoryResponse::of)
                .collect(Collectors.toList());

        if(ObjectUtils.isEmpty(description)){
            throw new ValidationException("The category description must be informed.");
        } if (ObjectUtils.isEmpty(categoryResponses)){
            throw new ValidationException("The category description was not found.");
        }

        return categoryResponses;
    }

    public Category findById(Integer id){
        if(ObjectUtils.isEmpty(id)){
            throw new ValidationException("The category id was not informed.");
        }

        return categoryRepository.
                findById(id)
                .orElseThrow(() -> new ValidationException("There's no category for the given id."));
    }

    public CategoryResponse save(CategoryRequest request){
        this.validateCategoryNameInformed(request);
        var category = categoryRepository.save(Category.of(request));
        return CategoryResponse.of(category);
    }

    public CategoryResponse update(CategoryRequest request, Integer id){
        this.validateCategoryNameInformed(request);
        var category = Category.of(request);
        category.setId(id);
        categoryRepository.save(category);
        return CategoryResponse.of(category);
    }

    private void validateCategoryNameInformed(CategoryRequest request){
        if(ObjectUtils.isEmpty(request.getDescription())){
            throw new ValidationException("The category description was not informed.");
        }
    }

    public SucessResponse delete(Integer id){
        this.validateInformedId(id);

        if(productService.existsByCategoryId(id)){
            throw new ValidationException("You cannot delete this category because it's already defined by a product.");
        }

        categoryRepository.deleteById(id);
        return SucessResponse.create("The category was deleted.");
    }

    private void validateInformedId(Integer id){
        if(ObjectUtils.isEmpty(id)){
            new ValidationException("The categgory id was not informed.");
        }
    }
}
