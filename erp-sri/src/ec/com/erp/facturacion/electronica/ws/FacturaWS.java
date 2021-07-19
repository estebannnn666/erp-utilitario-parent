package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.ws.BindingProvider;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ec.com.erp.facturacion.electronica.modelo.Factura;
import ec.com.erp.facturacion.electronica.util.FirmaXadesBesUtil;
import ec.com.erp.facturacion.electronica.util.XmlUtil;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantes;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesService;
import ec.com.erp.facturacion.electronica.ws.autorizacion.RespuestaComprobante;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantes;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesService;
import ec.com.erp.facturacion.electronica.ws.recepcion.RespuestaSolicitud;

public class FacturaWS{

	@Test
	public void ejecutarFacturacionElectronicaFactura() throws SAXParseException, CertificateException, SAXException, IOException, JAXBException, InterruptedException {
		
		try {
			FacturaUtil facturaTest = new FacturaUtil();
			Factura factura = facturaTest.crearFactura();
			ByteArrayOutputStream baosFactura = (new XmlUtil()).convertirObjetoAXml(Factura.class, factura);
			FirmaXadesBesUtil firmaXadesBesUtil = new FirmaXadesBesUtil("src/test/resources/p12/JOHANAPAMELABENAVIDESBLANCO140721193429.p12",
					obtenerPasswordDesdeArchivoDeRecursos());
			ByteArrayOutputStream baosFacturaFirmada = new ByteArrayOutputStream();
			firmaXadesBesUtil.firmarDocumento(new ByteArrayInputStream(baosFactura.toByteArray()), baosFacturaFirmada);
	
			RecepcionComprobantesService webServiceRecepcion = new RecepcionComprobantesService();
			RecepcionComprobantes port1 = webServiceRecepcion.getRecepcionComprobantesPort();
			RespuestaSolicitud respuestaSolicitud = port1.validarComprobante(baosFacturaFirmada.toByteArray());
			
			BindingProvider bindingProvider = (BindingProvider) port1;
			bindingProvider.getRequestContext().put(
			      BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
			      "http://foo:8086/HelloWhatever");
	
			if (!respuestaSolicitud.getComprobantes().getComprobante().isEmpty()) {
				for (ec.com.erp.facturacion.electronica.ws.recepcion.Mensaje mensaje : respuestaSolicitud.getComprobantes()
						.getComprobante().get(0).getMensajes().getMensaje()) {
					System.out.println(mensaje.getIdentificador() + " " + mensaje.getInformacionAdicional());
				}
			}
	
			Thread.sleep(4500);
	
			AutorizacionComprobantesService webServiceAutorizacion = new AutorizacionComprobantesService();
	
			AutorizacionComprobantes port2 = webServiceAutorizacion.getAutorizacionComprobantesPort();
			RespuestaComprobante respuestaComprobante = port2
					.autorizacionComprobante(factura.getInfoTributaria().getClaveAcceso());
			if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				for (ec.com.erp.facturacion.electronica.ws.autorizacion.Mensaje mensaje : respuestaComprobante.getAutorizaciones()
						.getAutorizacion().get(0).getMensajes().getMensaje()) {
					System.out.println(mensaje.getIdentificador() + " " + mensaje.getInformacionAdicional());
				}
			}
		}catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getStackTrace());
		}
	}
	
	public static String obtenerPasswordDesdeArchivoDeRecursos() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/test/resources/p12/password.txt"),
				Charset.forName("UTF-8"));
		return lines.get(0);
	}

}
