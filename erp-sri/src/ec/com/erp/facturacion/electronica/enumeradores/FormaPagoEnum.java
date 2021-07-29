package ec.com.erp.facturacion.electronica.enumeradores;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum FormaPagoEnum {
	/**
	 * Sin utilizacion del sistema de financiero
	 */
	@XmlEnumValue("01")
	SIN_UTILIZACION_DEL_SISTEMA_FINANCIERO("01","SIN UTILIZACION DEL SISTEMA FINANCIERO"),

	/**
	 * Compensacion de deudas
	 * 
	 */
	@XmlEnumValue("15")
	COMPENSACION_DE_DEUDAS("15","COMPENSACION DE DEUDAS"),
	/**
	 * Tarjeta de debito
	 */
	@XmlEnumValue("16")
	TARJETA_DE_DEBITO("16","TARJETA DE DEBITO"),
	/**
	 * Dinero electronico
	 */
	@XmlEnumValue("17")
	DINERO_ELECTRONICO("17","DINERO ELECTRONICO"),
	/**
	 * Impuesto a la Salida de Divisas
	 */
	@XmlEnumValue("18")
	TARJETA_PREPAGO("18","TARJETA PREPAGO"),
	/**
	 * Tarjetas de credito
	 */
	@XmlEnumValue("19")
	TARJETA_DE_CREDITO("19","TARJETA DE CREDITO"),
	/**
	 * Otros con utilizacion del sistema Financiero
	 */
	@XmlEnumValue("20")
	OTROS_CON_UTILIZACION_DEL_SISTEMA_FINANCIERO("20","OTROS CON UTILIZACION DEL SISTEMA FINANCIERO"),
	/**
	 * Endoso de Titulos
	 */
	@XmlEnumValue("21")
	ENDOSO_DE_TITULOS("21","ENDOSO DE TITULOS");
	
	private String codigo;
	private String descripcion;

	private FormaPagoEnum(String codigo, String descripcion) {

		this.codigo = codigo;
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}
}
