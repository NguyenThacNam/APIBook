package com.nm.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nm.entity.Category;
import com.nm.service.CategoryService;



@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/category")
public class CategoryApi {
    
	 @Autowired
	 private CategoryService categoryService;
	 
	 @GetMapping
	 
	 public List<Category> getAllCategory(){
		 return categoryService.getAllCategory();
	 }
	 
	 @GetMapping("/{id}")
	    public ResponseEntity<Category> getCategoryById(@PathVariable Long id) {
	        return categoryService.getByIdCategory(id)
	            .map(ResponseEntity::ok)
	            .orElse(ResponseEntity.notFound().build());
	    }
	 
	 @PostMapping
	    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
	        try {
	            Category saved = categoryService.addCategory(category);
	            return ResponseEntity.ok(saved);
	        } catch (RuntimeException e) {
	            return ResponseEntity.badRequest().body(null);
	        }
	    }
	 
	 @PutMapping("/{id}")
	 public ResponseEntity<Category> updateCategory(@PathVariable Long id , @RequestBody Category category){
		 try {
			 return categoryService.updateCategory(id, category)
					 .map(ResponseEntity::ok)
					 .orElse(ResponseEntity.notFound().build());
		 }catch (RuntimeException e) {
			 return ResponseEntity.badRequest().body(null);
	        }
	 }
	 @DeleteMapping("/{id}")
	 public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
	     boolean deleted = categoryService.deleteCategory(id);
	     return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
	 }
	 
	 
}
