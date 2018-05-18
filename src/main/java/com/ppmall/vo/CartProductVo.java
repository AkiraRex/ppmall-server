package com.ppmall.vo;

public class CartProductVo {
	private int id;
	private int userId;
	private int productId;
	private int quantity;
	private String productName;
	private String productSubtitle;
	private String productMainImage;
	private double productPrice;
	private int pproductStatus;
	private double productTotalPrice;
	private int productStock;
	private int productChecked;
	private String limitQuantity;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductSubtitle() {
		return productSubtitle;
	}
	public void setProductSubtitle(String productSubtitle) {
		this.productSubtitle = productSubtitle;
	}
	public String getProductMainImage() {
		return productMainImage;
	}
	public void setProductMainImage(String productMainImage) {
		this.productMainImage = productMainImage;
	}
	public double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	public int getPproductStatus() {
		return pproductStatus;
	}
	public void setPproductStatus(int pproductStatus) {
		this.pproductStatus = pproductStatus;
	}
	public double getProductTotalPrice() {
		return productTotalPrice;
	}
	public void setProductTotalPrice(double productTotalPrice) {
		this.productTotalPrice = productTotalPrice;
	}
	public int getProductStock() {
		return productStock;
	}
	public void setProductStock(int productStock) {
		this.productStock = productStock;
	}
	public int getProductChecked() {
		return productChecked;
	}
	public void setProductChecked(int productChecked) {
		this.productChecked = productChecked;
	}
	public String getLimitQuantity() {
		return limitQuantity;
	}
	public void setLimitQuantity(String limitQuantity) {
		this.limitQuantity = limitQuantity;
	}
}
