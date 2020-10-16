package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HeadInvoice implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer idInvoice;
	private String clientDirection;
	private String clientDocument; 
	private String clientName; 
	private String clientPhone; 
	private Date dateDocument; 
	private BigDecimal discount; 
	private String numberDocument;
	private Boolean paidOut;
	private BigDecimal subTotal;
	private BigDecimal totalInvoice;
	private BigDecimal totalIva;
	private BigDecimal totalNotTax;
	private BigDecimal totalTax;
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
	public Date getDateDocument() {
		return dateDocument;
	}
	public void setDateDocument(Date dateDocument) {
		this.dateDocument = dateDocument;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
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
	public BigDecimal getSubTotal() {
		return subTotal;
	}
	public void setSubTotal(BigDecimal subTotal) {
		this.subTotal = subTotal;
	}
	public BigDecimal getTotalInvoice() {
		return totalInvoice;
	}
	public void setTotalInvoice(BigDecimal totalInvoice) {
		this.totalInvoice = totalInvoice;
	}
	public BigDecimal getTotalIva() {
		return totalIva;
	}
	public void setTotalIva(BigDecimal totalIva) {
		this.totalIva = totalIva;
	}
	public BigDecimal getTotalNotTax() {
		return totalNotTax;
	}
	public void setTotalNotTax(BigDecimal totalNotTax) {
		this.totalNotTax = totalNotTax;
	}
	public BigDecimal getTotalTax() {
		return totalTax;
	}
	public void setTotalTax(BigDecimal totalTax) {
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
