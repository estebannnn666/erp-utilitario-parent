package ec.com.erp.firebase.model;

import java.io.Serializable;

public class Taxe implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private String id; 
	private String descriptionTax; 
	private String nameTax; 
	private String valueTax; 
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescriptionTax() {
		return descriptionTax;
	}
	public void setDescriptionTax(String descriptionTax) {
		this.descriptionTax = descriptionTax;
	}
	public String getNameTax() {
		return nameTax;
	}
	public void setNameTax(String nameTax) {
		this.nameTax = nameTax;
	}
	public String getValueTax() {
		return valueTax;
	}
	public void setValueTax(String valueTax) {
		this.valueTax = valueTax;
	}
}
