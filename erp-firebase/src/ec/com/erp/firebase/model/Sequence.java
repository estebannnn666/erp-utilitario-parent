package ec.com.erp.firebase.model;

import java.io.Serializable;

public class Sequence implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;
	
	private String client; 
	private String item; 
	private String order; 
	private String invoice;
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getInvoice() {
		return invoice;
	}
	public void setInvoice(String invoice) {
		this.invoice = invoice;
	} 
}
