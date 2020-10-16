package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.util.Collection;

public class Order implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private HeadOrder header; 
	private Collection<DetailOrder> details;
	public HeadOrder getHeader() {
		return header;
	}
	public void setHeader(HeadOrder header) {
		this.header = header;
	}
	public Collection<DetailOrder> getDetails() {
		return details;
	}
	public void setDetails(Collection<DetailOrder> details) {
		this.details = details;
	}
}
