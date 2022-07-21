package br.com.jdo2.poc.envixo.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Product {
	
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private String description;
	private String category;
	private String tags;
	private BigDecimal price;
	private BigDecimal promotionPrice;
	private String status;
	
	@OneToMany(mappedBy="product")
	private List<ProductFile> productFiles; 

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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<ProductFile> getProductFiles() {
		return productFiles;
	}

	public void setProductFiles(List<ProductFile> productFiles) {
		this.productFiles = productFiles;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", description=" + description + ", category=" + category
				+ ", tags=" + tags + ", price=" + price + ", promotionPrice=" + promotionPrice + ", status=" + status
				+ ", productFiles=" + productFiles + "]";
	}

	
	

}
