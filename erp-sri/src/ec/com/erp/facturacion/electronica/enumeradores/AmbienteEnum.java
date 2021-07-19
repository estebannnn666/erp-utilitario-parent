package ec.com.erp.facturacion.electronica.enumeradores;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;

@XmlEnum
public enum AmbienteEnum {
	/**
	 * Ambiente de pruebas
	 */
	@XmlEnumValue("1")
	PRUEBAS("https://celcer.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl", "https://celcer.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl", "1"),

	/**
	 * Ambiente de produccion
	 */
	@XmlEnumValue("2")
	PRODUCCION("https://cel.sri.gob.ec/comprobantes-electronicos-ws/RecepcionComprobantesOffline?wsdl", "https://cel.sri.gob.ec/comprobantes-electronicos-ws/AutorizacionComprobantesOffline?wsdl", "2");

	private String urlRecepcion;
	private String urlAutorizacion;
	private String codigo;

	private AmbienteEnum(String urlRecepcion, String urlAutorizacion, String codigo) {

		this.urlRecepcion = urlRecepcion;
		this.urlAutorizacion = urlAutorizacion;
		this.codigo = codigo;
	}

	public String getUrlRecepcion() {
		return urlRecepcion;
	}

	public String getUrlAutorizacion() {
		return urlAutorizacion;
	}

	public String getCodigo() {
		return codigo;
	}
}
