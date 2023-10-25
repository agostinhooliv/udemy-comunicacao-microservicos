package br.com.curso.udemy.product.api.controller;

import br.com.curso.udemy.product.api.config.exception.SucessResponse;
import br.com.curso.udemy.product.api.dto.CategoryRequest;
import br.com.curso.udemy.product.api.dto.CategoryResponse;
import br.com.curso.udemy.product.api.service.CategoryService;
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
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    public CategoryService categoryService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public CategoryResponse save(@RequestBody CategoryRequest categoryRequest) {
        return categoryService.save(categoryRequest);
    }

    @GetMapping
    public List<CategoryResponse> findAll(){
        return categoryService.findByAll();
    }

    @GetMapping("{id}")
    public CategoryResponse findById(@PathVariable Integer id){
        return categoryService.findByIdResponse(id);
    }

    @GetMapping("description/{description}")
    public List<CategoryResponse> findByDescription(@PathVariable String description){
        return categoryService.findByDescription(description);
    }

    @PutMapping("{id}")
    public CategoryResponse update(@RequestBody CategoryRequest categoryRequest, @PathVariable Integer id){
        return categoryService.update(categoryRequest, id);
    }

    @DeleteMapping("{id}")
    public SucessResponse delete(@PathVariable Integer id){
        return categoryService.delete(id);
    }
}
