package ec.com.erp.facturacion.electronica.modelo.notacredito;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import ec.com.erp.facturacion.electronica.enumeradores.MonedaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.ObligadoContabilidadEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoIdentificacionCompradorEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuesto;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuestoNotaCredito;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "fechaEmision", "dirEstablecimiento", "tipoIdentificacionComprador",
		"razonSocialComprador", "identificacionComprador", "contribuyenteEspecial", "obligadoContabilidad", "rise",
		"codDocModificado", "numDocModificado", "fechaEmisionDocSustento", "totalSinImpuestos",
		"valorModificacion", "moneda", "totalConImpuestos", "motivo" })
@XmlSeeAlso({ TotalImpuesto.class })
public class InfoNotaCredito implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5693500887762244833L;

	@XmlElement(required = true)
	private String fechaEmision;

	private String dirEstablecimiento;

	@XmlElement
	private TipoIdentificacionCompradorEnum tipoIdentificacionComprador;

	@XmlElement(required = true)
	private String razonSocialComprador;

	private String identificacionComprador;

	private String contribuyenteEspecial;

	private ObligadoContabilidadEnum obligadoContabilidad;

	private String rise;

	@XmlElement(required = true)
	private String codDocModificado;

	@XmlElement(required = true)
	private String numDocModificado;

	@XmlElement(required = true)
	private String fechaEmisionDocSustento;

	@XmlElement(required = true)
	private BigDecimal totalSinImpuestos;

	@XmlElement(required = true)
	private BigDecimal valorModificacion;

	private MonedaEnum moneda;

	@XmlElementWrapper(name = "totalConImpuestos")
	@XmlElement(name = "totalImpuesto")
	private List<TotalImpuestoNotaCredito> totalConImpuestos;

	@XmlElement(required = true)
	private String motivo;

	public String getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(String fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public String getDirEstablecimiento() {
		return dirEstablecimiento;
	}

	public void setDirEstablecimiento(String dirEstablecimiento) {
		this.dirEstablecimiento = dirEstablecimiento;
	}

	public TipoIdentificacionCompradorEnum getTipoIdentificacionComprador() {
		return tipoIdentificacionComprador;
	}

	public void setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum tipoIdentificacionComprador) {
		this.tipoIdentificacionComprador = tipoIdentificacionComprador;
	}

	public String getRazonSocialComprador() {
		return razonSocialComprador;
	}

	public void setRazonSocialComprador(String razonSocialComprador) {
		this.razonSocialComprador = razonSocialComprador;
	}

	public String getIdentificacionComprador() {
		return identificacionComprador;
	}

	public void setIdentificacionComprador(String identificacionComprador) {
		this.identificacionComprador = identificacionComprador;
	}

	public String getContribuyenteEspecial() {
		return contribuyenteEspecial;
	}

	public void setContribuyenteEspecial(String contribuyenteEspecial) {
		this.contribuyenteEspecial = contribuyenteEspecial;
	}

	public ObligadoContabilidadEnum getObligadoContabilidad() {
		return obligadoContabilidad;
	}

	public void setObligadoContabilidad(ObligadoContabilidadEnum obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}

	public String getRise() {
		return rise;
	}

	public void setRise(String rise) {
		this.rise = rise;
	}

	public String getCodDocModificado() {
		return codDocModificado;
	}

	public void setCodDocModificado(String codDocModificado) {
		this.codDocModificado = codDocModificado;
	}

	public String getNumDocModificado() {
		return numDocModificado;
	}

	public void setNumDocModificado(String numDocModificado) {
		this.numDocModificado = numDocModificado;
	}

	public String getFechaEmisionDocSustento() {
		return fechaEmisionDocSustento;
	}

	public void setFechaEmisionDocSustento(String fechaEmisionDocSustento) {
		this.fechaEmisionDocSustento = fechaEmisionDocSustento;
	}

	public BigDecimal getTotalSinImpuestos() {
		return totalSinImpuestos;
	}

	public void setTotalSinImpuestos(BigDecimal totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}

	public BigDecimal getValorModificacion() {
		return valorModificacion;
	}

	public void setValorModificacion(BigDecimal valorModificacion) {
		this.valorModificacion = valorModificacion;
	}

	public MonedaEnum getMoneda() {
		return moneda;
	}

	public void setMoneda(MonedaEnum moneda) {
		this.moneda = moneda;
	}

	public List<TotalImpuestoNotaCredito> getTotalConImpuestos() {
		return totalConImpuestos;
	}

	public void setTotalConImpuestos(List<TotalImpuestoNotaCredito> totalConImpuestos) {
		this.totalConImpuestos = totalConImpuestos;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
}
