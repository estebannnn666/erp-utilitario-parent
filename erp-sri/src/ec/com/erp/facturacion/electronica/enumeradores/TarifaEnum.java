package ec.com.erp.facturacion.electronica.enumeradores;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum TarifaEnum {
	/**
	 * Impuesto Redimible a las Botellas Plasticas no Retornables.
	 */
	@XmlEnumValue("0.02")
	IRBPNR,
	/**
	 * Impuesto Redimible a las Botellas Plasticas no Retornables.
	 */
	@XmlEnumValue("0")
	IVA_0,
	
	/**
	 * Impuesto iva 12.
	 */
	@XmlEnumValue("12")
	IVA_12,
	
	/**
	 * Impuesto iva 14.
	 */
	@XmlEnumValue("14")
	IVA_14;
}
