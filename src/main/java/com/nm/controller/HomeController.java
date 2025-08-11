package com.nm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home"; // Trang chủ
    }

    @GetMapping("/add-book")
    public String showAddBookPage() {
        return "add-book"; // Giao diện thêm sách
    }
}

