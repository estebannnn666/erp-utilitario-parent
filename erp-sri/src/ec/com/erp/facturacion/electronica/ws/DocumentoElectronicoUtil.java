package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.thoughtworks.xstream.XStream;

import ec.com.erp.cliente.common.constantes.ERPConstantes;
import ec.com.erp.cliente.common.exception.ERPException;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.util.FirmaXadesBesUtil;
import ec.com.erp.facturacion.electronica.util.XStreamUtil;
import ec.com.erp.facturacion.electronica.ws.autorizacion.Autorizacion;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.autorizacion.Mensaje;
import ec.com.erp.facturacion.electronica.ws.autorizacion.RespuestaComprobante;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.recepcion.RespuestaSolicitud;

public class DocumentoElectronicoUtil {
	
	private DocumentoElectronicoUtil() {
	}
	
	/**
	 * Enviar documento electronico al sri
	 * @param baosFactura
	 * @param facturaCabeceraDTO
	 * @throws SAXParseException
	 * @throws CertificateException
	 * @throws SAXException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InterruptedException
	 */
	public static void enviarDocumentoElectronico(ByteArrayOutputStream baosFactura, String tipoRuc) throws CertificateException, IOException, InterruptedException {
		try {
			FirmaXadesBesUtil firmaXadesBesUtil;
			if(tipoRuc.equals(ERPConstantes.TIPO_RUC_UNO)) {
				firmaXadesBesUtil = new FirmaXadesBesUtil("C:\\ErpLibreries\\facturacion\\EDUARDOHOMEROBENAVIDESVALENZUELA160821155846.p12",
						obtenerPasswordDesdeArchivoFacturaPrincipal());
			}else {
				firmaXadesBesUtil = new FirmaXadesBesUtil("C:\\ErpLibreries\\facturacion\\JOHANAPAMELABENAVIDESBLANCO140721193429.p12",
						obtenerPasswordDesdeArchivoFacturaSecundaria());
			}
			ByteArrayOutputStream baosFacturaFirmada = new ByteArrayOutputStream();
			firmaXadesBesUtil.firmarDocumento(new ByteArrayInputStream(baosFactura.toByteArray()), baosFacturaFirmada);
	
			URL wsdlLocation = new URL(AmbienteEnum.PRUEBAS.getUrlRecepcion());
	        QName serviceName = new QName("http://ec.gob.sri.ws.recepcion", "RecepcionComprobantesOfflineService");
			RecepcionComprobantesOfflineService webServiceRecepcion = new RecepcionComprobantesOfflineService(wsdlLocation, serviceName);
			RecepcionComprobantesOffline port1 = webServiceRecepcion.getRecepcionComprobantesOfflinePort();
			RespuestaSolicitud respuestaSolicitud = port1.validarComprobante(baosFacturaFirmada.toByteArray());
			
			BindingProvider bindingProvider = (BindingProvider) port1;
			bindingProvider.getRequestContext().put(
			      BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
			      "http://foo:8086/HelloWhatever");
	
			if (!respuestaSolicitud.getComprobantes().getComprobante().isEmpty()) {
				for (ec.com.erp.facturacion.electronica.ws.recepcion.Mensaje mensaje : respuestaSolicitud.getComprobantes()
						.getComprobante().get(0).getMensajes().getMensaje()) {
					if(!mensaje.getIdentificador().equals("43")){
						throw new ERPException("Error", "Error al generar factura electronica: "+mensaje.getIdentificador() + " - " + mensaje.getMensaje()+" - "+mensaje.getInformacionAdicional()) ;
					}
				}
			}
			Thread.sleep(4500);
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}

	/**
	 * Obtener autorizacion del Sri para el documenti enviado
	 * @param claveAcceso
	 * @param datosFactura
	 */
	public static void autorizarDocumentoElectronico(String claveAcceso, Map<String, Object> datosFactura) throws CertificateException, IOException, InterruptedException {
		try {
			URL wsdLocationAut = new URL(AmbienteEnum.PRUEBAS.getUrlAutorizacion());
			QName serviceNameAut = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
			AutorizacionComprobantesOfflineService webServiceAutorizacion = new AutorizacionComprobantesOfflineService(wsdLocationAut, serviceNameAut);
			AutorizacionComprobantesOffline port2 = webServiceAutorizacion.getAutorizacionComprobantesOfflinePort();
			RespuestaComprobante respuestaComprobante = port2.autorizacionComprobante(claveAcceso);
			if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				for (Mensaje mensaje : respuestaComprobante.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje()) {
					throw new ERPException("Error", "Error al generar factura electronica: "+mensaje.getIdentificador() + " - " + mensaje.getMensaje() +" - "+mensaje.getInformacionAdicional()) ;
				}
			}
			datosFactura.put("XMLDOCUMENT", obtenerRepuestaAutorizacionXML(respuestaComprobante.getAutorizaciones().getAutorizacion().get(0)));
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	private static byte[] obtenerRepuestaAutorizacionXML(Autorizacion autorizacion) throws ERPException {
		try {
			XStream xstream = XStreamUtil.getRespuestaXStream();
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
			setXMLCDATA(autorizacion);
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			xstream.toXML(autorizacion, writer);
			return outputStream.toByteArray();
		} catch (IOException ex) {
			throw new ERPException("Se produjo un error al convetir el archivo al formato XML", ex);
		}
	}

	private static void setXMLCDATA(Autorizacion autorizacion) {
		autorizacion.setComprobante("<![CDATA[" + autorizacion.getComprobante() + "]]>");
	}
	 
	private static String obtenerPasswordDesdeArchivoFacturaPrincipal() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("C:\\ErpLibreries\\facturacion\\passwordPrincipal.txt"),
				Charset.forName("UTF-8"));
		return lines.get(0);
	}
	
	private static String obtenerPasswordDesdeArchivoFacturaSecundaria() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("C:\\ErpLibreries\\facturacion\\passwordSecundaria.txt"),
				Charset.forName("UTF-8"));
		return lines.get(0);
	}
}
