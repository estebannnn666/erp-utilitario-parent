package ec.com.erp.firebase.model;

import java.io.Serializable;
import java.util.Collection;

public class Item implements Serializable{
	
	private static final long serialVersionUID = 7863262235394607247L;

	private DataItem dataItem; 
	private Collection<DriveUnit> driveUnit;
	private Collection<Taxe> taxes;
	public DataItem getDataItem() {
		return dataItem;
	}
	public void setDataItem(DataItem dataItem) {
		this.dataItem = dataItem;
	}
	public Collection<DriveUnit> getDriveUnit() {
		return driveUnit;
	}
	public void setDriveUnit(Collection<DriveUnit> driveUnit) {
		this.driveUnit = driveUnit;
	}
	public Collection<Taxe> getTaxes() {
		return taxes;
	}
	public void setTaxes(Collection<Taxe> taxes) {
		this.taxes = taxes;
	}
}
