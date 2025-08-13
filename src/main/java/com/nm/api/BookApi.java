package com.nm.api;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.nm.dto.PageContentDto;
import com.nm.dto.PdfContentDto;
import com.nm.entity.Book;
import com.nm.repository.BookRepository;
import com.nm.service.BookService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/book")
public class BookApi {

    @Autowired
    private BookService bookService;
    
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBook();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> addBook(
            @RequestPart("book") Book book,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "pdfFile", required = false) MultipartFile pdfFile,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        try {
            Book saved = bookService.addBook(book, thumbnail, pdfFile, courseId);
            return ResponseEntity.ok(saved);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestPart("book") Book book,
            @RequestPart(value = "thumbnail", required = false) MultipartFile thumbnail,
            @RequestPart(value = "pdfFile", required = false) MultipartFile pdfFile,
            @RequestParam(value = "courseId", required = false) Long courseId) {
        try {
            return bookService.updateBook(id, book, thumbnail, pdfFile, courseId)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        boolean deleted = bookService.deleteBook(id);
        return deleted ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
    @GetMapping("/{id}/pdf-image-pages")
    public ResponseEntity<?> getPdfPagesAsImages(@PathVariable Long id) {
        try {
            List<PageContentDto> pages = bookService.extractPdfPagesAsImages(id);
            return ResponseEntity.ok(pages);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi khi đọc file PDF");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam String keyword) {
        return bookRepository.findByTitleContainingIgnoreCase(keyword);
    }




}
