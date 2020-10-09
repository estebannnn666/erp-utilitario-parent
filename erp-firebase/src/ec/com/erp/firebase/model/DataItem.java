package ec.com.erp.firebase.model;

import java.io.Serializable;

public class DataItem implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer id; 
	private String barCode; 
	private String commissionPercentage; 
	private String cost; 
	private String nameItem; 
	private String priceRetail; 
	private String priceWholesaler; 
	private String stock;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getCommissionPercentage() {
		return commissionPercentage;
	}
	public void setCommissionPercentage(String commissionPercentage) {
		this.commissionPercentage = commissionPercentage;
	}
	public String getCost() {
		return cost;
	}
	public void setCost(String cost) {
		this.cost = cost;
	}
	public String getNameItem() {
		return nameItem;
	}
	public void setNameItem(String nameItem) {
		this.nameItem = nameItem;
	}
	public String getPriceRetail() {
		return priceRetail;
	}
	public void setPriceRetail(String priceRetail) {
		this.priceRetail = priceRetail;
	}
	public String getPriceWholesaler() {
		return priceWholesaler;
	}
	public void setPriceWholesaler(String priceWholesaler) {
		this.priceWholesaler = priceWholesaler;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	} 
	
	
}
