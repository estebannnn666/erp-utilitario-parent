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

import ec.com.erp.facturacion.electronica.enumeradores.MonedaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.ObligadoContabilidadEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoIdentificacionCompradorEnum;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "fechaEmision", "dirEstablecimiento", "obligadoContabilidad", "tipoIdentificacionComprador",
		"razonSocialComprador", "identificacionComprador", "direccionComprador", "totalSinImpuestos", "totalDescuento", "totalConImpuestos",
		"propina", "importeTotal", "moneda", "pagos" })
@XmlSeeAlso({ TotalImpuesto.class })
public class InfoFactura implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5693500887762244833L;

	@XmlElement
	private String fechaEmision;

	@XmlElement
	private String dirEstablecimiento;

	@XmlElement
	private ObligadoContabilidadEnum obligadoContabilidad;

	@XmlElement
	private TipoIdentificacionCompradorEnum tipoIdentificacionComprador;

	@XmlElement
	private String razonSocialComprador;

	@XmlElement
	private String identificacionComprador;
	
	@XmlElement
	private String direccionComprador;

	@XmlElement
	private String totalSinImpuestos;

	@XmlElement
	private String totalDescuento;

	@XmlElement
	private String propina;

	@XmlElement
	private String importeTotal;

	@XmlElement
	private MonedaEnum moneda;

	@XmlElementWrapper(name = "totalConImpuestos")
	@XmlElement(name = "totalImpuesto")
	private List<TotalImpuesto> totalConImpuestos;

	@XmlElementWrapper(name = "pagos")
	@XmlElement(name = "pago")
	private List<Pago> pagos;

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

	public ObligadoContabilidadEnum getObligadoContabilidad() {
		return obligadoContabilidad;
	}

	public void setObligadoContabilidad(ObligadoContabilidadEnum obligadoContabilidad) {
		this.obligadoContabilidad = obligadoContabilidad;
	}

	public TipoIdentificacionCompradorEnum getTipoIdentificacionComprador() {
		return tipoIdentificacionComprador;
	}

	public void setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum cedula) {
		this.tipoIdentificacionComprador = cedula;
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

	public String getTotalSinImpuestos() {
		return totalSinImpuestos;
	}

	public void setTotalSinImpuestos(String totalSinImpuestos) {
		this.totalSinImpuestos = totalSinImpuestos;
	}

	public String getTotalDescuento() {
		return totalDescuento;
	}

	public void setTotalDescuento(String totalDescuento) {
		this.totalDescuento = totalDescuento;
	}

	public List<TotalImpuesto> getTotalConImpuestos() {
		return totalConImpuestos;
	}

	public void setTotalConImpuestos(List<TotalImpuesto> totalConImpuestos) {
		this.totalConImpuestos = totalConImpuestos;
	}

	public String getPropina() {
		return propina;
	}

	public void setPropina(String propina) {
		this.propina = propina;
	}

	public String getImporteTotal() {
		return importeTotal;
	}

	public void setImporteTotal(String importeTotal) {
		this.importeTotal = importeTotal;
	}

	public MonedaEnum getMoneda() {
		return moneda;
	}

	public void setMoneda(MonedaEnum moneda) {
		this.moneda = moneda;
	}

	public List<Pago> getPagos() {
		return pagos;
	}

	public void setPagos(List<Pago> pagos) {
		this.pagos = pagos;
	}

	public String getDireccionComprador() {
		return direccionComprador;
	}

	public void setDireccionComprador(String direccionComprador) {
		this.direccionComprador = direccionComprador;
	}
}