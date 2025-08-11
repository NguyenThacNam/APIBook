package com.nm.service;

import java.util.Optional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;


import javax.imageio.ImageIO;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nm.dto.PageContentDto;
import com.nm.dto.PdfContentDto;
import com.nm.entity.Book;
import com.nm.entity.Course;
import com.nm.repository.BookRepository;
import com.nm.repository.CourseRepository;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public List<Book> getAllBook() {
        return bookRepo.findAll();
    }

    public Book addBook(Book book, MultipartFile thumbnailFile, MultipartFile pdfFile, Long courseId) throws IOException {
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            String thumbnailName = saveFile(thumbnailFile);
            book.setThumbnail(thumbnailName);
        }

        if (pdfFile != null && !pdfFile.isEmpty()) {
            String pdfFileName = saveFile(pdfFile);
            book.setPdfFile(pdfFileName);
        }

        if (courseId != null) {
            Course course = courseRepo.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId));
            book.setCourse(course);
        }

        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        return bookRepo.save(book);
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepo.findById(id);
    }

    public boolean deleteBook(Long id) {
        return bookRepo.findById(id).map(book -> {
            bookRepo.delete(book);
            return true;
        }).orElse(false);
    }

    public Optional<Book> updateBook(Long id, Book newBookData, MultipartFile thumbnailFile, MultipartFile pdfFile, Long courseId)
            throws IOException {
        return bookRepo.findById(id).map(book -> {
            book.setTitle(newBookData.getTitle());
            book.setContent(newBookData.getContent());
            book.setUpdatedAt(LocalDateTime.now());

            try {
                if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
                    String thumbnailName = saveFile(thumbnailFile);
                    book.setThumbnail(thumbnailName);
                }

                if (pdfFile != null && !pdfFile.isEmpty()) {
                    String pdfFileName = saveFile(pdfFile);
                    book.setPdfFile(pdfFileName);
                }

                if (courseId != null) {
                    Course course = courseRepo.findById(courseId)
                        .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với ID: " + courseId));
                    book.setCourse(course);
                }

            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi cập nhật file", e);
            }

            return bookRepo.save(book);
        });
    }
    
    public List<PageContentDto> extractPdfPagesAsImages(Long bookId) throws IOException {
        Book book = bookRepo.findById(bookId)
            .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sách với ID: " + bookId));

        String pdfFileName = book.getPdfFile();
        if (pdfFileName == null || pdfFileName.isEmpty()) {
            throw new IllegalArgumentException("Sách không có file PDF");
        }

        File pdfFile = new File(System.getProperty("user.dir"), uploadDir + "/" + pdfFileName);
        if (!pdfFile.exists()) {
            throw new IOException("File PDF không tồn tại: " + pdfFile.getAbsolutePath());
        }

        List<PageContentDto> pages = new ArrayList<>();

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PDFRenderer renderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = renderer.renderImageWithDPI(i, 150); // DPI: độ nét

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(image, "png", baos);

                String base64Image = Base64.getEncoder().encodeToString(baos.toByteArray());
                String imageDataUrl = "data:image/png;base64," + base64Image;

                pages.add(new PageContentDto(i + 1, null, List.of(imageDataUrl)));
            }
        }

        return pages;
    }


    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File uploadPath = new File(System.getProperty("user.dir"), uploadDir);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        File dest = new File(uploadPath, fileName);
        file.transferTo(dest);

        return fileName;
    }
}
