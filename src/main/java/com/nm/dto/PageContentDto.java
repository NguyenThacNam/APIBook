package com.nm.dto;

import java.util.List;

public class PageContentDto {
    private int pageNumber;
    private String text;
    private List<String> imagesBase64;
	public PageContentDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PageContentDto(int pageNumber, String text, List<String> imagesBase64) {
		super();
		this.pageNumber = pageNumber;
		this.text = text;
		this.imagesBase64 = imagesBase64;
	}
	public  int getPageNumber() {
		return pageNumber;
	}
	public  void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public  String getText() {
		return text;
	}
	public  void setText(String text) {
		this.text = text;
	}
	public  List<String> getImagesBase64() {
		return imagesBase64;
	}
	public  void setImagesBase64(List<String> imagesBase64) {
		this.imagesBase64 = imagesBase64;
	}

    // Constructors, getters, setters
    
}
