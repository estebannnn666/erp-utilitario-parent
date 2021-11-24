package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.bind.JAXBException;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ec.com.erp.cliente.common.exception.ERPException;
import ec.com.erp.cliente.mdl.dto.NotaCreditoDTO;
import ec.com.erp.facturacion.electronica.modelo.notacredito.NotaCredito;
import ec.com.erp.facturacion.electronica.util.XmlUtil;

public class NotaCreditoElectronicaUtil {
	
	private NotaCreditoElectronicaUtil(){
	}
	
	public static Map<String, Object> generarNotaCreditoElectronica(NotaCreditoDTO notaCreditoDTO, String codigoEstablecimiento, String codigoPuntoEmision) throws SAXParseException, CertificateException, SAXException, IOException, JAXBException, InterruptedException {
		try {
			Map<String, Object> datosFactura = new ConcurrentHashMap<>();
			NotaCredito notaCredito = ConstruirNotaCreditoUtil.crearNotaCredito(notaCreditoDTO, codigoEstablecimiento, codigoPuntoEmision);
			ByteArrayOutputStream baosFactura = (new XmlUtil()).convertirObjetoAXml(NotaCredito.class, notaCredito);
			// Enviar documento al sri
			DocumentoElectronicoUtil.enviarDocumentoElectronico(baosFactura, notaCreditoDTO.getTipoRuc());
			// Generar autorizacion del sri
			DocumentoElectronicoUtil.autorizarDocumentoElectronico(notaCredito.getInfoTributaria().getClaveAcceso(), datosFactura);
			datosFactura.put("NROFACTURA", notaCredito.getInfoTributaria().getEstab().concat("-").concat(notaCredito.getInfoTributaria().getPtoEmi()).concat("-").concat(notaCredito.getInfoTributaria().getSecuencial()));
			return datosFactura;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
}
