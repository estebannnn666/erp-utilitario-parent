package ec.com.erp.facturacion.electronica.enumeradores;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum FacturacionElectronicaEnum {
	/**
	 * Ambiente de pruebas
	 */
	@XmlEnumValue("1001594132001")
	RUCPRINCIPA("1001594132001","BENAVIDES VALENZUELA EDUARDO HOMERO","PROBERSA","EL OLIVO ALTO SN y SN(A UNA CUADRA DE LA SEDE SOCIAL DEL BARRIO)"),

	/**
	 * Ambiente de produccion
	 */
	@XmlEnumValue("1003635263001")
	RUCSECUNDARIO("1003635263001","BENAVIDES BLANCO JOHANA PAMELA","PROBERSA","EL OLIVO ALTO SN y SN(A UNA CUADRA DE LA SEDE SOCIAL DEL BARRIO)");

	private String ruc;
	private String razonSocial;
	private String nombreComercial;
	private String direccion;

	private FacturacionElectronicaEnum(String ruc, String razonSocial, String nombreComercial, String direccion) {
		this.ruc = ruc;
		this.razonSocial = razonSocial;
		this.nombreComercial = nombreComercial;
		this.direccion = direccion;
	}

	public String getRuc() {
		return ruc;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public String getNombreComercial() {
		return nombreComercial;
	}
	
	public String getDireccion() {
		return direccion;
	}
}
