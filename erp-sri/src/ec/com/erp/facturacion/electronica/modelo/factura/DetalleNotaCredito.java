package ec.com.erp.facturacion.electronica.modelo.factura;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "codigoInterno", "descripcion", "cantidad", "precioUnitario", "descuento",
		"precioTotalSinImpuesto", "detallesAdicionales", "impuestos" })
@XmlSeeAlso({ Impuesto.class })

public class DetalleNotaCredito implements Serializable {

	private static final long serialVersionUID = 1442890771223985798L;

	@XmlElement
	private String codigoInterno;

	@XmlElement
	private String descripcion;

	@XmlElement
	private String cantidad;

	@XmlElement
	private String precioUnitario;

	@XmlElement
	private String descuento;

	@XmlElement
	private String precioTotalSinImpuesto;

	@XmlElementWrapper(name = "impuestos")
	@XmlElement(name = "impuesto")
	private List<Impuesto> impuestos;

	@XmlElementWrapper(name = "detallesAdicionales")
	@XmlElement(name = "detAdicional")
	private List<DetAdicional> detallesAdicionales;

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCantidad() {
		return cantidad;
	}

	public void setCantidad(String cantidad) {
		this.cantidad = cantidad;
	}

	public String getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(String precioUnitario) {
		this.precioUnitario = precioUnitario;
	}

	public String getDescuento() {
		return descuento;
	}

	public void setDescuento(String descuento) {
		this.descuento = descuento;
	}

	public String getPrecioTotalSinImpuesto() {
		return precioTotalSinImpuesto;
	}

	public void setPrecioTotalSinImpuesto(String precioTotalSinImpuesto) {
		this.precioTotalSinImpuesto = precioTotalSinImpuesto;
	}

	public List<Impuesto> getImpuestos() {
		return impuestos;
	}

	public void setImpuestos(List<Impuesto> impuestos) {
		this.impuestos = impuestos;
	}

	public List<DetAdicional> getDetallesAdicionales() {
		return detallesAdicionales;
	}

	public void setDetallesAdicionales(List<DetAdicional> detallesAdicionales) {
		this.detallesAdicionales = detallesAdicionales;
	}
}
