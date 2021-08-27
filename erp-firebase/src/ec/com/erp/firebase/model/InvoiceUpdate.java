package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.util.Collection;

public class InvoiceUpdate implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private HeadInvoiceUpdate header; 
	private Collection<DetailInvoice> details;
	public HeadInvoiceUpdate getHeader() {
		return header;
	}
	public void setHeader(HeadInvoiceUpdate header) {
		this.header = header;
	}
	public Collection<DetailInvoice> getDetails() {
		return details;
	}
	public void setDetails(Collection<DetailInvoice> details) {
		this.details = details;
	}
}
