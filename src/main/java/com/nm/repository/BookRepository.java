package com.nm.repository;

import org.springframework.stereotype.Repository;

import com.nm.entity.Book;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    // Tùy chọn: thêm các phương thức query tùy chỉnh nếu cần
    List<Book> findByTitleContaining(String keyword);

    List<Book> findByContentContaining(String keyword);
    
    List<Book> findByTitleContainingIgnoreCase(String keyword);

}

