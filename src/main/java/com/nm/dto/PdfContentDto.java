package com.nm.dto;

import java.util.List;

public class PdfContentDto {
    private String text;
    private List<String> imagesBase64;
   
	public PdfContentDto(String text, List<String> imagesBase64) {
		super();
		this.text = text;
		this.imagesBase64 = imagesBase64;
	}

	public PdfContentDto() {
		super();
		// TODO Auto-generated constructor stub
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

	
	
    
    
}
