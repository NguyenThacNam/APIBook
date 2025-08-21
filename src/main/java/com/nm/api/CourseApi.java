package com.nm.api;

import com.nm.entity.Course;
import com.nm.service.CourseService;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/course")
public class CourseApi {

	@Autowired
	private CourseService courseService;

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public List<Course> getAllCourse() {
		return courseService.getAllCourse();
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{id}")
	public ResponseEntity<Course> getCourseId(@PathVariable Long id) {
		return courseService.getByIdCourse(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<Course> addCourse(@RequestPart("course") Course course,
			@RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
		try {
			Course saved = courseService.addCourse(course, imageFile);
			return ResponseEntity.ok(saved);
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestPart("course") Course course,
			@RequestPart(value = "imageFile", required = false) MultipartFile imageFile) {
		try {
			return courseService.updateCourse(id, course, imageFile).map(ResponseEntity::ok)
					.orElse(ResponseEntity.notFound().build());
		} catch (IOException e) {
			return ResponseEntity.internalServerError().build();
		}

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCourse(@PathVariable Long id) {
		try {
			courseService.deleteCourse(id);
			return ResponseEntity.ok().build();
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Lỗi khi xóa khóa học.");
		}
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/by-category/{categoryId}")
	public List<Course> getCoursesByCategory(@PathVariable Long categoryId) {
		return courseService.getCoursesByCategoryId(categoryId);
	}

}
