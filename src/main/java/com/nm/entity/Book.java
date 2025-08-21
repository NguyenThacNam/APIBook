package com.nm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumbnail", length = 500)
    private String thumbnail;

    @Column(name = "pdf_file", length = 500)
    private String pdfFile;
    
    @Column(name = "pdf_password", length = 255)
    private String pdfPassword;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    @JsonIgnoreProperties({"books"})
    private Course course; 

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor mặc định
    public Book() {}

    // Constructor khởi tạo nhanh
    public Book(String title, String content, String thumbnail, String pdfFile, Course course  ,String pdfPassword) {
        this.title = title;
        this.content = content;
        this.thumbnail = thumbnail;
        this.pdfFile = pdfFile;
        this.course = course;
        this.pdfPassword = pdfPassword;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPdfFile() {
        return pdfFile;
    }
    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public String getPdfPassword() {
        return pdfPassword;
    }
    public void setPdfPassword(String pdfPassword) {
        this.pdfPassword = pdfPassword;
    }

}
