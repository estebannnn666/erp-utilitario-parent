package ec.com.erp.firebase.model;

import java.io.Serializable;

public class ImageItem implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private Integer id; 
	private String barCode;
	private String image;
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
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
}
