package com.nm.service;


import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;


import com.nm.entity.Category;
import com.nm.entity.Course;
import com.nm.repository.CategoryRepository;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository cateRepo;

   public List<Category> getAllCategory(){
	   return cateRepo.findAll();
   }
   
   public Category addCategory(Category category) {
	   if (cateRepo.existsByName(category.getName())) {
	        throw new RuntimeException("Tên category đã tồn tại!");
	    }
	   return cateRepo.save(category);
   }
   
   public Optional<Category> getByIdCategory(Long id){
	   return cateRepo.findById(id);
   }
   
   public Boolean deleteCategory(Long id) {
	   return cateRepo.findById(id).map(category ->{
		   cateRepo.delete(category);
		   return true;
	   }).orElse(false);
   }
   public Optional<Category> updateCategory(Long id , Category newCategory){
	   return cateRepo.findById(id).map(category ->{
		   category.setName(newCategory.getName());
		   category.setDescription(newCategory.getDescription());
		   category.setStatus(newCategory.getStatus());
		   return cateRepo.save(category);
	   });
   }
  
}
