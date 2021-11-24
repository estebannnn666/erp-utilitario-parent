package ec.com.erp.facturacion.electronica.modelo.factura;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"campoAdicional"})
public class InformacionAdicional {
	
	@XmlElement(required = true)
    protected List<CampoInfoAdicional> campoAdicional;

	public List<CampoInfoAdicional> getCampoAdicional() {
		if (this.campoAdicional == null) {
			this.campoAdicional = new ArrayList<>();
		}
		return this.campoAdicional;
    }
}
