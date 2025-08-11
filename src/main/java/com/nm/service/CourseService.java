package com.nm.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nm.entity.Course;
import com.nm.repository.CourseRepository;

@Service
public class CourseService {

	@Autowired
	private CourseRepository couRepo;
	
	public List<Course> getAllCourse(){
		return couRepo.findAll();
	}
	
	public Course addCourse(Course course, MultipartFile imageFile)  throws IOException {
	    if (imageFile != null && !imageFile.isEmpty()) {
	        String imageName = saveFile(imageFile); 
	        course.setImage(imageName);
	    }

	    return couRepo.save(course);
	}


	public Optional<Course> getByIdCourse(Long id){
		return couRepo.findById(id);
	}
	
	public Optional<Course> updateCourse(Long id, Course newCourse, MultipartFile imageFile) throws IOException {
	    return couRepo.findById(id).map(course -> {
	      
	        course.setName(newCourse.getName());
	        course.setDescription(newCourse.getDescription());
	        course.setPrice(newCourse.getPrice());
	        course.setCategory(newCourse.getCategory());

	       
	        if (imageFile != null && !imageFile.isEmpty()) {
	            String imageName = saveFile(imageFile);
	            course.setImage(imageName);
	        }

	        
	        return couRepo.save(course);
	    });
	}
	
	public Boolean deleteCourse(Long id) {
		return couRepo.findById(id).map(course -> {
			couRepo.delete(course);
			return true;
		}).orElse(false);
	}
	
	
	public String saveFile(MultipartFile file) {
	    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
	    Path path = Paths.get("uploads/" + fileName);
	    try {
	        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
	        return fileName;
	    } catch (IOException e) {
	        throw new RuntimeException("Lỗi khi lưu file", e);
	    }
	}
	 public List<Course> getCoursesByCategoryId(Long categoryId) {
	        return couRepo.findByCategoryId(categoryId); // ĐÚNG!
	    }

}
