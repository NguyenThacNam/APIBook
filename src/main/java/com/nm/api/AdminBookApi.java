package com.nm.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.nm.dto.PageContentDto;
import com.nm.entity.Book;
import com.nm.repository.BookRepository;
import com.nm.service.BookService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/admin/book")
@PreAuthorize("hasRole('ADMIN')") // Áp dụng cho toàn bộ controller
public class AdminBookApi {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;


    @GetMapping("/{id}/pdf-image-pages")
    public ResponseEntity<?> getPdfPagesAsImagesForAdmin(@PathVariable Long id) {
        try {
            Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sách"));

            List<PageContentDto> pages = bookService.extractPdfPagesAsImages(id);
            return ResponseEntity.ok(pages);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi khi đọc file PDF");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   
}
