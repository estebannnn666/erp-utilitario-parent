package ec.com.erp.facturacion.electronica.modelo.notacredito;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import ec.com.erp.facturacion.electronica.modelo.factura.CampoAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.Detalle;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoTributaria;
import ec.com.erp.facturacion.electronica.modelo.factura.InformacionAdicional;


@XmlRootElement(name = "notaCredito")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "infoTributaria", "infoNotaCredito", "detalles", "infoAdicional" })
@XmlSeeAlso({ InfoTributaria.class, InfoNotaCredito.class, Detalle.class, CampoAdicional.class })

public class NotaCredito implements Serializable {

	private static final long serialVersionUID = -4921604319752459192L;

	@XmlElement(required = true)
	private InfoTributaria infoTributaria;

	@XmlElement(required = true)
	private InfoNotaCredito infoNotaCredito;

	@XmlElementWrapper(name = "detalles")
	@XmlElement(name = "detalle")
	private List<Detalle> detalles;

	private InformacionAdicional infoAdicional;
	
	@XmlAttribute(required = true)
	private String id;
	
	@XmlAttribute(required = true)
	private String version;

	public InfoTributaria getInfoTributaria() {
		return infoTributaria;
	}

	public void setInfoTributaria(InfoTributaria infoTributaria) {
		this.infoTributaria = infoTributaria;
	}

	public InfoNotaCredito getInfoNotaCredito() {
		return infoNotaCredito;
	}

	public void setInfoNotaCredito(InfoNotaCredito infoNotaCredito) {
		this.infoNotaCredito = infoNotaCredito;
	}

	public List<Detalle> getDetalles() {
		return detalles;
	}

	public void setDetalles(List<Detalle> detalles) {
		this.detalles = detalles;
	}

	public InformacionAdicional getInfoAdicional() {
		return infoAdicional;
	}

	public void setInfoAdicional(InformacionAdicional infoAdicional) {
		this.infoAdicional = infoAdicional;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}