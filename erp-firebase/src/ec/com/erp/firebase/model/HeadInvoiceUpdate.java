package ec.com.erp.firebase.model;

import java.io.Serializable;

public class HeadInvoiceUpdate implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer idInvoice;
	private String clientDirection;
	private String clientDocument; 
	private String clientName; 
	private String clientPhone; 
	private String dateDocument; 
	private Double discount; 
	private String numberDocument;
	private Boolean paidOut;
	private Double subTotal;
	private Double totalInvoice;
	private Double totalIva;
	private Double totalNotTax;
	private Double totalTax;
	private String typeDocumentCode;
	private String userId;
	private String seller;
	private String valueDocumentCode;
	public Integer getIdInvoice() {
		return idInvoice;
	}
	public void setIdInvoice(Integer idInvoice) {
		this.idInvoice = idInvoice;
	}
	public String getClientDirection() {
		return clientDirection;
	}
	public void setClientDirection(String clientDirection) {
		this.clientDirection = clientDirection;
	}
	public String getClientDocument() {
		return clientDocument;
	}
	public void setClientDocument(String clientDocument) {
		this.clientDocument = clientDocument;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getClientPhone() {
		return clientPhone;
	}
	public void setClientPhone(String clientPhone) {
		this.clientPhone = clientPhone;
	}
	public String getDateDocument() {
		return dateDocument;
	}
	public void setDateDocument(String dateDocument) {
		this.dateDocument = dateDocument;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public String getNumberDocument() {
		return numberDocument;
	}
	public void setNumberDocument(String numberDocument) {
		this.numberDocument = numberDocument;
	}
	public Boolean getPaidOut() {
		return paidOut;
	}
	public void setPaidOut(Boolean paidOut) {
		this.paidOut = paidOut;
	}
	public Double getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}
	public Double getTotalInvoice() {
		return totalInvoice;
	}
	public void setTotalInvoice(Double totalInvoice) {
		this.totalInvoice = totalInvoice;
	}
	public Double getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(Double totalIva) {
		this.totalIva = totalIva;
	}
	public Double getTotalNotTax() {
		return totalNotTax;
	}
	public void setTotalNotTax(Double totalNotTax) {
		this.totalNotTax = totalNotTax;
	}
	public Double getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(Double totalTax) {
		this.totalTax = totalTax;
	}
	public String getTypeDocumentCode() {
		return typeDocumentCode;
	}
	public void setTypeDocumentCode(String typeDocumentCode) {
		this.typeDocumentCode = typeDocumentCode;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getValueDocumentCode() {
		return valueDocumentCode;
	}
	public void setValueDocumentCode(String valueDocumentCode) {
		this.valueDocumentCode = valueDocumentCode;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
}
