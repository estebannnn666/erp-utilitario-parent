package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class DetailInvoice implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer idItem; 
	private String barCodeItem; 
	private String description; 
	private BigDecimal discount; 
	private Boolean existsTax; 
	private String numberDocument; 
	private Integer quantity; 
	private BigDecimal subTotal; 
	private BigDecimal unitValue; 
	private String valueCatalogDriverUnit; 
	private Integer valueDriverUnit;
	public Integer getIdItem() {
		return idItem;
	}
	public void setIdItem(Integer idItem) {
		this.idItem = idItem;
	}
	public String getBarCodeItem() {
		return barCodeItem;
	}
	public void setBarCodeItem(String barCodeItem) {
		this.barCodeItem = barCodeItem;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public Boolean getExistsTax() {
		return existsTax;
	}
	public void setExistsTax(Boolean existsTax) {
		this.existsTax = existsTax;
	}
	public String getNumberDocument() {
		return numberDocument;
	}
	public void setNumberDocument(String numberDocument) {
		this.numberDocument = numberDocument;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getUnitValue() {
		return unitValue;
	}
	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
	}
	public String getValueCatalogDriverUnit() {
		return valueCatalogDriverUnit;
	}
	public void setValueCatalogDriverUnit(String valueCatalogDriverUnit) {
		this.valueCatalogDriverUnit = valueCatalogDriverUnit;
	}
	public Integer getValueDriverUnit() {
		return valueDriverUnit;
	}
	public void setValueDriverUnit(Integer valueDriverUnit) {
		this.valueDriverUnit = valueDriverUnit;
	}
}
