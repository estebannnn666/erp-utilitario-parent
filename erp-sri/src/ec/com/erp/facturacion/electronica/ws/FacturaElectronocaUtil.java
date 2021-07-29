package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.thoughtworks.xstream.XStream;

import ec.com.erp.cliente.common.constantes.ERPConstantes;
import ec.com.erp.cliente.common.exception.ERPException;
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.modelo.Factura;
import ec.com.erp.facturacion.electronica.util.FirmaXadesBesUtil;
import ec.com.erp.facturacion.electronica.util.XStreamUtil;
import ec.com.erp.facturacion.electronica.util.XmlUtil;
import ec.com.erp.facturacion.electronica.ws.autorizacion.Autorizacion;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.autorizacion.Mensaje;
import ec.com.erp.facturacion.electronica.ws.autorizacion.RespuestaComprobante;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.recepcion.RespuestaSolicitud;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRXmlUtils;

public class FacturaElectronocaUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] getReporte(byte[] xmlFactura) throws JRException, IOException {
		try {
			Map params = new HashMap();
			InputStream isFromFirstData = new ByteArrayInputStream(xmlFactura);
			Document document = JRXmlUtils.parse(isFromFirstData);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			StringWriter sw = new StringWriter();
			t.transform(new DOMSource(document), new StreamResult(sw));
			String xml = sw.toString();
			System.err.println("xml : " + xml);
//			Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream("C:\\ErpLibreries\\RIDE\\Factura_V_2_1_0.xml"));
			params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
			params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
			params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
			params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			JasperFillManager.fillReportToFile("C:\\ErpLibreries\\RIDE\\Factura_V_2_1_0.jasper", params);
			String filePdf = JasperExportManager.exportReportToPdfFile("C:\\ErpLibreries\\RIDE\\Factura_V_2_1_0.jrprint");
			File file = new File(filePdf);
			return Files.readAllBytes(file.toPath());
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	public static byte[] ejecutarFacturacionElectronicaFactura(String rucFactElectronica, String secuenciaFactura, FacturaCabeceraDTO facturaCabeceraDTO) throws SAXParseException, CertificateException, SAXException, IOException, JAXBException, InterruptedException {
		try {
			Factura factura = ConstruirFacturaUtil.crearFactura(rucFactElectronica, secuenciaFactura, facturaCabeceraDTO);
			ByteArrayOutputStream baosFactura = (new XmlUtil()).convertirObjetoAXml(Factura.class, factura);
			FirmaXadesBesUtil firmaXadesBesUtil;
			if(rucFactElectronica.equals(ERPConstantes.TIPO_RUC_UNO)) {
				firmaXadesBesUtil = new FirmaXadesBesUtil("C:\\ErpLibreries\\facturacion\\EDUARDOHOMEROBENAVIDESVALENZUELA140721193429.p12",
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
					throw new ERPException("Error", "Error al generar factura electronica: "+mensaje.getIdentificador() + " - " + mensaje.getMensaje()) ;
				}
			}
	
			Thread.sleep(4500);
	
			URL wsdLocationAut = new URL(AmbienteEnum.PRUEBAS.getUrlAutorizacion());
			QName serviceNameAut = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
			AutorizacionComprobantesOfflineService webServiceAutorizacion = new AutorizacionComprobantesOfflineService(wsdLocationAut, serviceNameAut);
			AutorizacionComprobantesOffline port2 = webServiceAutorizacion.getAutorizacionComprobantesOfflinePort();
			RespuestaComprobante respuestaComprobante = port2.autorizacionComprobante(factura.getInfoTributaria().getClaveAcceso());
			if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				for (Mensaje mensaje : respuestaComprobante.getAutorizaciones().getAutorizacion().get(0).getMensajes().getMensaje()) {
					throw new ERPException("Error", "Error al generar factura electronica: "+mensaje.getIdentificador() + " - " + mensaje.getMensaje()) ;
				}
			}
			return obtenerRepuestaAutorizacionXML(respuestaComprobante.getAutorizaciones().getAutorizacion().get(0));
//			return baosFactura.toByteArray();
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
	
	public static Factura xmlAObjectFactura(String valor) {
	    try {
	      JAXBContext context = JAXBContext.newInstance(new Class[] { Factura.class });
	      Unmarshaller unmarshaller = context.createUnmarshaller();
	      return (Factura)unmarshaller.unmarshal(new StringReader(valor));
	    }
	    catch (JAXBException e) {
	      return null;
	    }
	}
}
