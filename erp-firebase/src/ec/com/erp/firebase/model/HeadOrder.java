package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class HeadOrder implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer idOrder;
	private String clientDirection;
	private String clientDocument; 
	private String clientName; 
	private String clientPhone; 
	private Date deliveryDate; 
	private Date orderDate;
	private BigDecimal discount; 
	private String statusOrder;
	private BigDecimal subTotal;
	private BigDecimal totalInvoice;
	private BigDecimal totalIva;
	private BigDecimal totalNotTax;
	private BigDecimal totalTax;
	private String userId;
	private String seller;
	
	public Integer getIdOrder() {
		return idOrder;
	}
	public void setIdOrder(Integer idOrder) {
		this.idOrder = idOrder;
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
	public Date getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public BigDecimal getDiscount() {
		return discount;
	}
	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}
	public String getStatusOrder() {
		return statusOrder;
	}
	public void setStatusOrder(String statusOrder) {
		this.statusOrder = statusOrder;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSeller() {
		return seller;
	}
	public void setSeller(String seller) {
		this.seller = seller;
	}
}
