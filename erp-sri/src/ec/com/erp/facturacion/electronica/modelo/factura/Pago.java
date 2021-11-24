package ec.com.erp.facturacion.electronica.modelo.factura;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.erp.facturacion.electronica.enumeradores.FormaPagoEnum;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "formaPago", "total", "plazo", "unidadTiempo" })
public class Pago {

	@XmlElement
	private FormaPagoEnum formaPago;

	@XmlElement
	private String total;

	@XmlElement
	private String plazo;

	@XmlElement
	private String unidadTiempo;

	public FormaPagoEnum getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(FormaPagoEnum formaPagoEnum) {
		this.formaPago = formaPagoEnum;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getPlazo() {
		return plazo;
	}

	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}

	public String getUnidadTiempo() {
		return unidadTiempo;
	}

	public void setUnidadTiempo(String unidadTiempo) {
		this.unidadTiempo = unidadTiempo;
	}

}
