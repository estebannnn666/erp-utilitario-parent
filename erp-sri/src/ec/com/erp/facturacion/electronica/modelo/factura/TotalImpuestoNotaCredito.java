package ec.com.erp.facturacion.electronica.modelo.factura;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ec.com.erp.facturacion.electronica.enumeradores.CodigoImpuestoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoPorcentajeEnum;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "codigo", "codigoPorcentaje", "descuentoAdicional", "baseImponible", "valor" })
public class TotalImpuestoNotaCredito implements Serializable {

	private static final long serialVersionUID = -138623113562583170L;

	@XmlElement
	private CodigoImpuestoEnum codigo;

	@XmlElement
	private CodigoPorcentajeEnum codigoPorcentaje;

	@XmlElement
	private String baseImponible;

	@XmlElement
	private String valor;

	@XmlElement
	private String descuentoAdicional;

	public CodigoImpuestoEnum getCodigo() {
		return codigo;
	}

	public void setCodigo(CodigoImpuestoEnum codigoImpuestoEnum) {
		this.codigo = codigoImpuestoEnum;
	}

	public CodigoPorcentajeEnum getCodigoPorcentaje() {
		return codigoPorcentaje;
	}

	public void setCodigoPorcentaje(CodigoPorcentajeEnum codigoPorcentaje) {
		this.codigoPorcentaje = codigoPorcentaje;
	}

	public String getBaseImponible() {
		return baseImponible;
	}

	public void setBaseImponible(String baseImponible) {
		this.baseImponible = baseImponible;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public void setDescuentoAdicional(String descuentoAdicional) {
		this.descuentoAdicional = descuentoAdicional;
	}

}