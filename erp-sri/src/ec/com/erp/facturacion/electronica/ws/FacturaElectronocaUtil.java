package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ec.com.erp.cliente.common.constantes.ERPConstantes;
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.modelo.Factura;
import ec.com.erp.facturacion.electronica.util.FirmaXadesBesUtil;
import ec.com.erp.facturacion.electronica.util.XmlUtil;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.autorizacion.AutorizacionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.autorizacion.RespuestaComprobante;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOffline;
import ec.com.erp.facturacion.electronica.ws.recepcion.RecepcionComprobantesOfflineService;
import ec.com.erp.facturacion.electronica.ws.recepcion.RespuestaSolicitud;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
//import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.query.JRXPathQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRXmlUtils;

public class FacturaElectronocaUtil {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static byte[] getReporte(ByteArrayOutputStream baosFactura) throws JRException {
		Map params = new HashMap();
//		InputStream isFromFirstData = new ByteArrayInputStream(baosFactura.toByteArray());
//		Document document = JRXmlUtils.parse(isFromFirstData);
		Document document = JRXmlUtils.parse(JRLoader.getLocationInputStream("src/main/java/ec/facturacion/electronica/ride/Factura_V_2_1_0.xml"));
		params.put(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT, document);
		params.put(JRXPathQueryExecuterFactory.XML_DATE_PATTERN, "yyyy-MM-dd");
		params.put(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN, "#,##0.##");
		params.put(JRXPathQueryExecuterFactory.XML_LOCALE, Locale.ENGLISH);
		params.put(JRParameter.REPORT_LOCALE, Locale.US);
//		JasperFillManager.fillReportToFile("src/main/java/ec/facturacion/electronica/ride/Factura_V_2_1_0.jasper", params);
		File jasper = new File("src/main/java/ec/facturacion/electronica/ride/Factura_V_2_1_0.jasper");
        byte[] bytes = JasperRunManager.runReportToPdf(jasper.getPath(), params);
        return bytes;
	}
	
	public static byte[] ejecutarFacturacionElectronicaFactura(String rucFactElectronica, String secuenciaFactura, FacturaCabeceraDTO facturaCabeceraDTO) throws SAXParseException, CertificateException, SAXException, IOException, JAXBException, InterruptedException {
		try {
			Factura factura = ConstruirFacturaUtil.crearFactura(rucFactElectronica, secuenciaFactura, facturaCabeceraDTO);
			ByteArrayOutputStream baosFactura = (new XmlUtil()).convertirObjetoAXml(Factura.class, factura);
			FirmaXadesBesUtil firmaXadesBesUtil;
			if(rucFactElectronica.equals(ERPConstantes.RUC_PRINCIPAL)) {
				firmaXadesBesUtil = new FirmaXadesBesUtil("src/ec/com/erp/facturacion/electronica/resources/EDUARDOHOMEROBENAVIDESVALENZUELA140721193429.p12",
						obtenerPasswordDesdeArchivoFacturaPrincipal());
			}else {
				firmaXadesBesUtil = new FirmaXadesBesUtil("src/ec/com/erp/facturacion/electronica/resources/JOHANAPAMELABENAVIDESBLANCO140721193429.p12",
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
					System.out.println(mensaje.getIdentificador() + " " + mensaje.getInformacionAdicional());
				}
			}
	
			Thread.sleep(4500);
	
			URL wsdLocationAut = new URL(AmbienteEnum.PRUEBAS.getUrlAutorizacion());
			QName serviceNameAut = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesOfflineService");
			AutorizacionComprobantesOfflineService webServiceAutorizacion = new AutorizacionComprobantesOfflineService(wsdLocationAut, serviceNameAut);
			AutorizacionComprobantesOffline port2 = webServiceAutorizacion.getAutorizacionComprobantesOfflinePort();
			RespuestaComprobante respuestaComprobante = port2.autorizacionComprobante(factura.getInfoTributaria().getClaveAcceso());
			if (!respuestaComprobante.getAutorizaciones().getAutorizacion().isEmpty()) {
				for (ec.com.erp.facturacion.electronica.ws.autorizacion.Mensaje mensaje : respuestaComprobante.getAutorizaciones()
						.getAutorizacion().get(0).getMensajes().getMensaje()) {
					System.out.println(mensaje.getIdentificador() + " " + mensaje.getInformacionAdicional());
				}
			}
			return baosFactura.toByteArray();
		}catch (Exception e) {
			e.getStackTrace();
			System.out.println(e.getStackTrace());
		}
		return null;
	}
	
	private static String obtenerPasswordDesdeArchivoFacturaPrincipal() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/ec/com/erp/facturacion/electronica/resources/passwordPrincipal.txt"),
				Charset.forName("UTF-8"));
		return lines.get(0);
	}
	
	private static String obtenerPasswordDesdeArchivoFacturaSecundaria() throws IOException {
		List<String> lines = Files.readAllLines(Paths.get("src/ec/com/erp/facturacion/electronica/resources/passwordSecundaria.txt"),
				Charset.forName("UTF-8"));
		return lines.get(0);
	}
	
	
}
