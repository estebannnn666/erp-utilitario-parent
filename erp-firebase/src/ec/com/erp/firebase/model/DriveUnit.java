package ec.com.erp.firebase.model;

import java.io.Serializable;

public class DriveUnit implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private String id; 
	private String isDefault; 
	private String unitDriveName; 
	private String unitDriveTypeCode; 
	private String unitDriveValue; 
	private String unitDriveValueCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
	public String getUnitDriveName() {
		return unitDriveName;
	}
	public void setUnitDriveName(String unitDriveName) {
		this.unitDriveName = unitDriveName;
	}
	public String getUnitDriveTypeCode() {
		return unitDriveTypeCode;
	}
	public void setUnitDriveTypeCode(String unitDriveTypeCode) {
		this.unitDriveTypeCode = unitDriveTypeCode;
	}
	public String getUnitDriveValue() {
		return unitDriveValue;
	}
	public void setUnitDriveValue(String unitDriveValue) {
		this.unitDriveValue = unitDriveValue;
	}
	public String getUnitDriveValueCode() {
		return unitDriveValueCode;
	}
	public void setUnitDriveValueCode(String unitDriveValueCode) {
		this.unitDriveValueCode = unitDriveValueCode;
	} 
}
