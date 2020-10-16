package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.util.Collection;

public class Invoice implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private HeadInvoice header; 
	private Collection<DetailInvoice> details;
	public HeadInvoice getHeader() {
		return header;
	}
	public void setHeader(HeadInvoice header) {
		this.header = header;
	}
	public Collection<DetailInvoice> getDetails() {
		return details;
	}
	public void setDetails(Collection<DetailInvoice> details) {
		this.details = details;
	}
}
