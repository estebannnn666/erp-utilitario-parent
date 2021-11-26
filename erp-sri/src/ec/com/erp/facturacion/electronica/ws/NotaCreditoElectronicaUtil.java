package ec.com.erp.facturacion.electronica.ws;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.collections4.CollectionUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import ec.com.erp.cliente.common.exception.ERPException;
import ec.com.erp.cliente.mdl.dto.NotaCreditoDTO;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoImpuestoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoPorcentajeEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoEmisionEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.CampoInfoAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.DetAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.DetalleNotaCredito;
import ec.com.erp.facturacion.electronica.modelo.factura.DetallesAdicionalesReporte;
import ec.com.erp.facturacion.electronica.modelo.factura.DocumentoAutorizacionSRI;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoTributaria;
import ec.com.erp.facturacion.electronica.modelo.factura.IvaDiferenteCeroReporte;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalComprobante;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuestoNotaCredito;
import ec.com.erp.facturacion.electronica.modelo.notacredito.InfoNotaCredito;
import ec.com.erp.facturacion.electronica.modelo.notacredito.NotaCredito;
import ec.com.erp.facturacion.electronica.util.XmlUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRXmlUtils;

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
	
	public static byte[] imprimirnNotaCreditoFormato(byte[] xmlFactura) throws JRException, IOException {
		try {
			SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
			Map<String, Object> params = new ConcurrentHashMap<>();
			InputStream isFromFirstData = new ByteArrayInputStream(xmlFactura);
			Document document = JRXmlUtils.parse(isFromFirstData);
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			StringWriter sw = new StringWriter();
			t.transform(new DOMSource(document), new StreamResult(sw));
			String xml = sw.toString();
			DocumentoAutorizacionSRI autorizacion = xmlAObjectAutorizacion(xml);
			NotaCredito notaCreditoSRI = xmlAObjectFactura(autorizacion.getComprobante());
			JRDataSource dataSource = new JRBeanCollectionDataSource(getDetallesAdiciones(notaCreditoSRI));
			params = obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(notaCreditoSRI.getInfoTributaria(), autorizacion.getNumeroAutorizacion(), formatoFecha.format(autorizacion.getFechaAutorizacion().getTime())), obtenerInfoNotaCredito(notaCreditoSRI.getInfoNotaCredito()));
			obtenerInformacionAdicional(notaCreditoSRI, params);
			obtenerTotalesFactura(params, notaCreditoSRI);
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			JasperFillManager.fillReportToFile("C:\\ErpLibreries\\resources\\nota_credito_probersa.jasper", params, dataSource);
			String filePdf = JasperExportManager.exportReportToPdfFile("C:\\ErpLibreries\\resources\\nota_credito_probersa.jrprint");
			File file = new File(filePdf);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return fileContent;
		}catch (JRException e) {
			throw new ERPException("Error", e.getMessage()) ;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	private static Map<String, Object> obtenerMapaParametrosReportes(Map<String, Object> mapa1, Map<String, Object> mapa2) {
		mapa1.putAll(mapa2);
		return mapa1;
	}
	
	private static Map<String, Object> obtenerInfoNotaCredito(InfoNotaCredito infoNotaCredito) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("LLEVA_CONTABILIDAD", infoNotaCredito.getObligadoContabilidad().name());
		param.put("RS_COMPRADOR", infoNotaCredito.getRazonSocialComprador());
		param.put("RUC_COMPRADOR", infoNotaCredito.getIdentificacionComprador());
		param.put("NUMERO_FACTURA", infoNotaCredito.getNumDocModificado());
		param.put("FECHA_EMISION", infoNotaCredito.getFechaEmision());
		param.put("FECHA_FACTURA", infoNotaCredito.getFechaEmisionDocSustento());
		param.put("MOTIVO", infoNotaCredito.getMotivo());
		return param;
	}
	
	private static Map<String, Object> obtenerParametrosInfoTriobutaria(InfoTributaria infoTributaria, String numAut, String fechaAut) throws ERPException, ClassNotFoundException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("RUC", infoTributaria.getRuc());
		param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
		param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
		param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
		try {
			param.put("LOGO", new FileInputStream("C:\\ErpLibreries\\imagenes\\probersa.jpeg"));
		} catch (FileNotFoundException ex) {
			try {
				param.put("LOGO", new FileInputStream("C:\\ErpLibreries\\imagenes\\logo.jpeg"));
				Logger.getLogger(FacturaElectronicaUtil.class.getName()).log(Level.SEVERE, null, ex);
			} catch (FileNotFoundException ex1) {
				Logger.getLogger(FacturaElectronicaUtil.class.getName()).log(Level.SEVERE, null, ex1);
			}
		}
		param.put("SUBREPORT_DIR", "C:\\ErpLibreries\\resources\\");
		param.put("SUBREPORT_PAGOS", "C:\\ErpLibreries\\resources\\");
		param.put("SUBREPORT_TOTALES", "C:\\ErpLibreries\\resources\\");
		param.put("TIPO_EMISION", obtenerTipoEmision(infoTributaria));
		param.put("NUM_AUT", numAut);
		param.put("FECHA_AUT", fechaAut);
		param.put("NUM_FACT",
				infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi() + "-" + infoTributaria.getSecuencial());
		param.put("AMBIENTE", infoTributaria.getAmbiente().toString());
		param.put("NOM_COMERCIAL", infoTributaria.getNombreComercial());
		return param;
	}
	
	private static String obtenerTipoEmision(InfoTributaria infoTributaria) {
		if (infoTributaria.getTipoEmision().compareTo(TipoEmisionEnum.INDISPONIBILIDAD) == 0) {
			return TipoEmisionEnum.INDISPONIBILIDAD.getCodigo();
		}
		if (infoTributaria.getTipoEmision().compareTo(TipoEmisionEnum.NORMAL) == 0) {
			return TipoEmisionEnum.NORMAL.getCodigo();
		}
		return null;
	}
	
	private static DocumentoAutorizacionSRI xmlAObjectAutorizacion(String valor) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { DocumentoAutorizacionSRI.class });
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (DocumentoAutorizacionSRI) unmarshaller.unmarshal(new StringReader(valor));
		} catch (JAXBException e) {
			return null;
		}
	}
	
	private static NotaCredito xmlAObjectFactura(String valor) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { NotaCredito.class });
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (NotaCredito) unmarshaller.unmarshal(new StringReader(valor));
		} catch (JAXBException e) {
			return null;
		}
	}
	
	private static List<DetallesAdicionalesReporte> getDetallesAdiciones(NotaCredito notaCreditoSRI) throws SQLException, ClassNotFoundException {
		List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<>();
	    for (DetalleNotaCredito det : notaCreditoSRI.getDetalles()) {
	    	DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	    	detAd.setCodigoPrincipal(det.getCodigoInterno());
	    	detAd.setDescripcion(det.getDescripcion());
	    	detAd.setCantidad(det.getCantidad());
	    	detAd.setPrecioTotalSinImpuesto(det.getPrecioTotalSinImpuesto());
	    	detAd.setPrecioUnitario(BigDecimal.valueOf(Double.parseDouble(det.getPrecioUnitario())));
	    	detAd.setPrecioSinSubsidio(BigDecimal.ZERO);
	    	if (det.getDescuento() != null) {
	    		detAd.setDescuento(det.getDescuento());
	    	}
	    	if (CollectionUtils.isNotEmpty(det.getDetallesAdicionales())) {
	    		for (DetAdicional detAdicional : det.getDetallesAdicionales()) {
	    			detAd.setDetalle1(detAdicional.getNombre());
	    		} 
	    	}
	    	detallesAdiciones.add(detAd);
	    } 
	    return detallesAdiciones;
	}
	
	private static void obtenerInformacionAdicional(NotaCredito notaCreditoSRI, Map<String, Object> params){
		if(notaCreditoSRI.getInfoAdicional() != null && CollectionUtils.isNotEmpty(notaCreditoSRI.getInfoAdicional().getCampoAdicional())){
			params.put("EMAIL", "N/A");
			for(CampoInfoAdicional campoAdiciona: notaCreditoSRI.getInfoAdicional().getCampoAdicional()){
				
				if(campoAdiciona.getNombre().equals("Email")){
					params.put("EMAIL", campoAdiciona.getValue().toUpperCase());
				}
			}
		}
		
	}
	
	private static void obtenerTotalesFactura(Map<String, Object> param, NotaCredito notaCreditoSRI) throws SQLException, ClassNotFoundException {
        TotalComprobante tc = getTotales(notaCreditoSRI.getInfoNotaCredito());
        // Convertidor de decimales
 		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
 	    decimalSymbols.setDecimalSeparator('.');
 		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
 		formatoDecimales.setMinimumFractionDigits(2);
        for(IvaDiferenteCeroReporte iva : tc.getIvaDistintoCero()) {
        	param.put("TARIFAIVA", formatoDecimales.format(iva.getSubtotal()));
        }
        for (IvaDiferenteCeroReporte iva : tc.getIvaDistintoCero()) {
        	param.put("VALORIVA", formatoDecimales.format(iva.getValor()));
        }
        param.put("TARIFACERO", formatoDecimales.format(tc.getSubtotal0()));
        param.put("SUBTOTAL", formatoDecimales.format(notaCreditoSRI.getInfoNotaCredito().getTotalSinImpuestos()));
        param.put("DESCUENTO", formatoDecimales.format(BigDecimal.ZERO));
        param.put("TOTAL", formatoDecimales.format(notaCreditoSRI.getInfoNotaCredito().getValorModificacion()));
    }
	
	private static TotalComprobante getTotales(InfoNotaCredito infoNotaCredito) {
		List<IvaDiferenteCeroReporte> ivaDiferenteCero = new ArrayList<IvaDiferenteCeroReporte>();

		BigDecimal totalIva = new BigDecimal(0.0D);
		BigDecimal totalIva0 = new BigDecimal(0.0D);
		BigDecimal totalExentoIVA = new BigDecimal(0.0D);
		BigDecimal totalICE = new BigDecimal(0.0D);
		BigDecimal totalIRBPNR = new BigDecimal(0.0D);
		BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
		TotalComprobante tc = new TotalComprobante();
		for (TotalImpuestoNotaCredito ti : infoNotaCredito.getTotalConImpuestos()) {
			if (ti.getCodigo().compareTo(CodigoImpuestoEnum.IVA) == 0) {
				if(CodigoPorcentajeEnum.IVA_0.compareTo(ti.getCodigoPorcentaje()) == 0){
					totalIva0 = BigDecimal.valueOf(Double.parseDouble(ti.getBaseImponible()));
				}
				if(CodigoPorcentajeEnum.IVA_12.compareTo(ti.getCodigoPorcentaje()) == 0){
			        IvaDiferenteCeroReporte iva = new IvaDiferenteCeroReporte(BigDecimal.valueOf(Double.parseDouble(ti.getBaseImponible())), "", BigDecimal.valueOf(Double.parseDouble(ti.getValor())));
			        ivaDiferenteCero.add(iva);
				}
			}
		}
		tc.setIvaDistintoCero(ivaDiferenteCero);
		tc.setSubtotal0(totalIva0);
		tc.setTotalIce(totalICE);
		tc.setSubtotal(totalIva0.add(totalIva));
		tc.setSubtotalExentoIVA(totalExentoIVA);
		tc.setTotalIRBPNR(totalIRBPNR);
		tc.setSubtotalNoSujetoIva(totalSinImpuesto);
		return tc;
	}
	
}
