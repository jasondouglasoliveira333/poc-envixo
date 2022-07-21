package br.com.jdo2.poc.envixo.view;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class ProductView {
	
	private Integer id;
	private String name;
	private String description;
	private String category;
	private String tags;
	private BigDecimal price;
	private BigDecimal promotionPrice;
	private String midiaPath;
	private String status;

	private List<ProductFileView> productFiles; 
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPromotionPrice() {
		return promotionPrice;
	}

	public void setPromotionPrice(BigDecimal promotionPrice) {
		this.promotionPrice = promotionPrice;
	}

	public String getMidiaPath() {
		return midiaPath;
	}

	public void setMidiaPath(String midiaPath) {
		this.midiaPath = midiaPath;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductFileView> getProductFiles() {
		return productFiles;
	}

	public void setProductFiles(List<ProductFileView> productFiles) {
		this.productFiles = productFiles;
	}

	
}
