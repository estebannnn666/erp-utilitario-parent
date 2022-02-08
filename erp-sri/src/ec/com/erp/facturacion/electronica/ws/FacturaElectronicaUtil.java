package ec.com.erp.facturacion.electronica.ws;

import java.io.BufferedInputStream;
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
import java.math.RoundingMode;
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
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoImpuestoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoPorcentajeEnum;
import ec.com.erp.facturacion.electronica.enumeradores.FormaPagoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoEmisionEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.CampoInfoAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.DetAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.Detalle;
import ec.com.erp.facturacion.electronica.modelo.factura.DetallesAdicionalesReporte;
import ec.com.erp.facturacion.electronica.modelo.factura.DocumentoAutorizacionSRI;
import ec.com.erp.facturacion.electronica.modelo.factura.Factura;
import ec.com.erp.facturacion.electronica.modelo.factura.FormasPago;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoFactura;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoTributaria;
import ec.com.erp.facturacion.electronica.modelo.factura.IvaDiferenteCeroReporte;
import ec.com.erp.facturacion.electronica.modelo.factura.Pago;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalComprobante;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuesto;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalesComprobante;
import ec.com.erp.facturacion.electronica.util.XmlUtil;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRXmlUtils;

public class FacturaElectronicaUtil {
	
	private FacturaElectronicaUtil(){
		
	}
	
	public static byte[] imprimirRideFactura(byte[] xmlFactura) throws IOException {
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
			Factura facturaSRI = xmlAObjectFactura(autorizacion.getComprobante());
			JRDataSource dataSource = new JRBeanCollectionDataSource(getDetallesAdiciones(facturaSRI));
			params = obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(facturaSRI.getInfoTributaria(), autorizacion.getNumeroAutorizacion(), formatoFecha.format(autorizacion.getFechaAutorizacion().getTime())), obtenerInfoFactura(facturaSRI.getInfoFactura()));
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			JasperFillManager.fillReportToFile("C:\\ErpLibreries\\resources\\factura.jasper", params, dataSource);
			String filePdf = JasperExportManager.exportReportToPdfFile("C:\\ErpLibreries\\resources\\factura.jrprint");
			File file = new File(filePdf);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return fileContent;
		}catch (JRException e) {
			throw new ERPException("Error", e.getMessage()) ;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	public static byte[] imprimirFacturaFormato(byte[] xmlFactura) throws JRException, IOException {
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
			Factura facturaSRI = xmlAObjectFactura(autorizacion.getComprobante());
			JRDataSource dataSource = new JRBeanCollectionDataSource(getDetallesAdiciones(facturaSRI));
			params = obtenerMapaParametrosReportes(obtenerParametrosInfoTriobutaria(facturaSRI.getInfoTributaria(), autorizacion.getNumeroAutorizacion(), formatoFecha.format(autorizacion.getFechaAutorizacion().getTime())), obtenerInfoFactura(facturaSRI.getInfoFactura()));
			obtenerRegimenMicroEmpresa(facturaSRI, params);
			obtenerTotalesFactura(params, facturaSRI);
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			JasperFillManager.fillReportToFile("C:\\ErpLibreries\\resources\\factura_probersa.jasper", params, dataSource);
			String filePdf = JasperExportManager.exportReportToPdfFile("C:\\ErpLibreries\\resources\\factura_probersa.jrprint");
			File file = new File(filePdf);
			byte[] fileContent = Files.readAllBytes(file.toPath());
			return fileContent;
		}catch (JRException e) {
			throw new ERPException("Error", e.getMessage()) ;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	public static Map<String, Object> ejecutarFacturacionElectronicaFactura(FacturaCabeceraDTO facturaCabeceraDTO, String codigoEstablecimiento, String codigoPuntoEmision) throws SAXParseException, CertificateException, SAXException, IOException, JAXBException, InterruptedException {
		try {
			Map<String, Object> datosFactura = new ConcurrentHashMap<>();
			Factura factura = ConstruirFacturaUtil.crearFactura(facturaCabeceraDTO, codigoEstablecimiento, codigoPuntoEmision);
			ByteArrayOutputStream baosFactura = (new XmlUtil()).convertirObjetoAXml(Factura.class, factura);
			// Enviar documento al sri
			DocumentoElectronicoUtil.enviarDocumentoElectronico(baosFactura, facturaCabeceraDTO.getTipoRuc());
			// Generar autorizacion del sri
			DocumentoElectronicoUtil.autorizarDocumentoElectronico(factura.getInfoTributaria().getClaveAcceso(), datosFactura);
			datosFactura.put("NROFACTURA", factura.getInfoTributaria().getEstab().concat("-").concat(factura.getInfoTributaria().getPtoEmi()).concat("-").concat(factura.getInfoTributaria().getSecuencial()));
			return datosFactura;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	private static Factura xmlAObjectFactura(String valor) {
		try {
			JAXBContext context = JAXBContext.newInstance(new Class[] { Factura.class });
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (Factura) unmarshaller.unmarshal(new StringReader(valor));
		} catch (JAXBException e) {
			return null;
		}
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

	private static Map<String, Object> obtenerMapaParametrosReportes(Map<String, Object> mapa1, Map<String, Object> mapa2) {
		mapa1.putAll(mapa2);
		return mapa1;
	}

	private static Map<String, Object> obtenerParametrosInfoTriobutaria(InfoTributaria infoTributaria, String numAut, String fechaAut) throws ERPException, ClassNotFoundException {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("RUC", infoTributaria.getRuc());
		param.put("CLAVE_ACC", infoTributaria.getClaveAcceso());
		param.put("RAZON_SOCIAL", infoTributaria.getRazonSocial());
		param.put("DIR_MATRIZ", infoTributaria.getDirMatriz());
		param.put("NEGOCIABLE", null);
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
		param.put("MARCA_AGUA", obtenerMarcaAgua(infoTributaria.getAmbiente().getCodigo()));
		param.put("NUM_FACT",
				infoTributaria.getEstab() + "-" + infoTributaria.getPtoEmi() + "-" + infoTributaria.getSecuencial());
		param.put("AMBIENTE", infoTributaria.getAmbiente().toString());
		param.put("NOM_COMERCIAL", infoTributaria.getNombreComercial());
		param.put("AGENTE_RETENCION", infoTributaria.getAgenteRetencion());
		return param;
	}
	
	private static void obtenerRegimenMicroEmpresa(Factura facturaSRI, Map<String, Object> params){
		if(facturaSRI.getInfoAdicional() != null && CollectionUtils.isNotEmpty(facturaSRI.getInfoAdicional().getCampoAdicional())){
			params.put("REGIMEN_MICROEMPRESAS", "N/A");
			params.put("VENDEDOR", "N/A");
			params.put("CIUDAD", "N/A");
			params.put("TELEFONO", "N/A");
			for(CampoInfoAdicional campoAdiciona: facturaSRI.getInfoAdicional().getCampoAdicional()){
				if(campoAdiciona.getNombre().equals("Telefono")){
					params.put("TELEFONO", campoAdiciona.getValue().toUpperCase());
				}
				if(campoAdiciona.getNombre().equals("Regimen")){
					params.put("REGIMEN_MICROEMPRESAS", campoAdiciona.getValue().toUpperCase());
				}
				if(campoAdiciona.getNombre().equals("Vendedor")){
					params.put("VENDEDOR", campoAdiciona.getValue().toUpperCase());
				}
				if(campoAdiciona.getNombre().equals("Ciudad")){
					params.put("CIUDAD", campoAdiciona.getValue().toUpperCase());
				}
			}
		}
		
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

	private static InputStream obtenerMarcaAgua(String ambiente) {
		try {
			if (ambiente.equals(AmbienteEnum.PRODUCCION.getCodigo())) {
				return new BufferedInputStream(new FileInputStream("C:\\ErpLibreries\\imagenes\\produccion.jpeg"));
			}

			return new BufferedInputStream(new FileInputStream("C:\\ErpLibreries\\imagenes\\pruebas.jpeg"));

		} catch (FileNotFoundException fe) {
			Logger.getLogger(FacturaElectronicaUtil.class.getName()).log(Level.SEVERE, null, fe);
			return null;
		}
	}

	private static Map<String, Object> obtenerInfoFactura(InfoFactura infoFactura) {
		BigDecimal totalSinSubsidio = BigDecimal.ZERO;
		BigDecimal totalSubsidio = BigDecimal.ZERO;
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("DIR_SUCURSAL", infoFactura.getDirEstablecimiento());
		param.put("CONT_ESPECIAL", "N/D");
		param.put("LLEVA_CONTABILIDAD", infoFactura.getObligadoContabilidad().name());
		param.put("RS_COMPRADOR", infoFactura.getRazonSocialComprador());
		param.put("RUC_COMPRADOR", infoFactura.getIdentificacionComprador());
		param.put("DIRECCION_CLIENTE", infoFactura.getDireccionComprador());
		param.put("FECHA_EMISION", infoFactura.getFechaEmision());
		param.put("GUIA", "N/D");
		param.put("TOTAL_SIN_SUBSIDIO", totalSinSubsidio.setScale(2, RoundingMode.UP));
		param.put("AHORRO_POR_SUBSIDIO", totalSubsidio.setScale(2, RoundingMode.UP));
		return param;
	}

	private static TotalComprobante getTotales(InfoFactura infoFactura) {
		List<IvaDiferenteCeroReporte> ivaDiferenteCero = new ArrayList<IvaDiferenteCeroReporte>();

		BigDecimal totalIva = new BigDecimal(0.0D);
		BigDecimal totalIva0 = new BigDecimal(0.0D);
		BigDecimal totalExentoIVA = new BigDecimal(0.0D);
		BigDecimal totalICE = new BigDecimal(0.0D);
		BigDecimal totalIRBPNR = new BigDecimal(0.0D);
		BigDecimal totalSinImpuesto = new BigDecimal(0.0D);
		TotalComprobante tc = new TotalComprobante();
		for (TotalImpuesto ti : infoFactura.getTotalConImpuestos()) {
			if (ti.getCodigo().compareTo(CodigoImpuestoEnum.IVA) == 0) {
				if(CodigoPorcentajeEnum.IVA_0.compareTo(ti.getCodigoPorcentaje()) == 0){
					totalIva0 = BigDecimal.valueOf(Double.parseDouble(ti.getBaseImponible()));
				}
				if(CodigoPorcentajeEnum.IVA_12.compareTo(ti.getCodigoPorcentaje()) == 0){
			        IvaDiferenteCeroReporte iva = new IvaDiferenteCeroReporte(BigDecimal.valueOf(Double.parseDouble(ti.getBaseImponible())), ti.getTarifa().name(), BigDecimal.valueOf(Double.parseDouble(ti.getValor())));
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
	
	
	
	private static List<DetallesAdicionalesReporte> getDetallesAdiciones(Factura facturaSRI) throws SQLException, ClassNotFoundException {
		List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<>();
	    for (Detalle det : facturaSRI.getDetalles()) {
	    	DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	    	detAd.setCodigoPrincipal(det.getCodigoPrincipal());
	    	detAd.setCodigoAuxiliar(det.getCodigoAuxiliar());
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
//	    	detAd.setInfoAdicional(getInfoAdicional(facturaSRI));
	    	detAd.setFormasPago(getFormasPago(facturaSRI));
	    	detAd.setTotalesComprobante(getTotalesComprobante(facturaSRI));
	    	detallesAdiciones.add(detAd);
	    } 
	    return detallesAdiciones;
	}
	
//	private static List<InformacionAdicional> getInfoAdicional(Factura facturaSRI) {
//		List<InformacionAdicional> infoAdicional = new ArrayList<>();
//		if (CollectionUtils.isNotEmpty(facturaSRI.getInfoAdicional())) {
//			for (CampoAdicional ca : facturaSRI.getInfoAdicional()) {
//				infoAdicional.add(new InformacionAdicional(ca.getValor(), ca.getNombre()));
//		    }
//		}
//	    return infoAdicional;
//	}

	private static List<FormasPago> getFormasPago(Factura facturaSRI) {
        List<FormasPago> formasPago = new ArrayList<>();
    	if (CollectionUtils.isNotEmpty(facturaSRI.getInfoFactura().getPagos())) { 
	        for (Pago pago : facturaSRI.getInfoFactura().getPagos()) {
	        	formasPago.add(new FormasPago(obtenerDetalleFormaPago(pago.getFormaPago()), pago.getTotal()));
	        }
	    }
    	return formasPago;
    }
		  
	private static String obtenerDetalleFormaPago(FormaPagoEnum formaPagoEnum) {
    	return formaPagoEnum.getCodigo().concat(" - ").concat(formaPagoEnum.getDescripcion());
    }
    
	private static List<TotalesComprobante> getTotalesComprobante(Factura facturaSRI) throws SQLException, ClassNotFoundException {
        List<TotalesComprobante> totalesComprobante = new ArrayList<>();
        BigDecimal importeTotal = BigDecimal.ZERO.setScale(2);
        BigDecimal compensaciones = BigDecimal.ZERO.setScale(2);
        TotalComprobante tc = getTotales(facturaSRI.getInfoFactura());
        for(IvaDiferenteCeroReporte iva : tc.getIvaDistintoCero()) {
        	totalesComprobante.add(new TotalesComprobante("SUBTOTAL " + iva.getTarifa() + "%", iva.getSubtotal(), false));
        }
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL IVA 0%", tc.getSubtotal0(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL NO OBJETO IVA", tc.getSubtotalNoSujetoIva(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL EXENTO IVA", tc.getSubtotalExentoIVA(), false));
        totalesComprobante.add(new TotalesComprobante("SUBTOTAL SIN IMPUESTOS", BigDecimal.valueOf(Double.parseDouble(facturaSRI.getInfoFactura().getTotalSinImpuestos())), false));
        totalesComprobante.add(new TotalesComprobante("DESCUENTO", BigDecimal.valueOf(Double.parseDouble(facturaSRI.getInfoFactura().getTotalDescuento())), false));
        totalesComprobante.add(new TotalesComprobante("ICE", tc.getTotalIce(), false));
        for (IvaDiferenteCeroReporte iva : tc.getIvaDistintoCero()) {
        	totalesComprobante.add(new TotalesComprobante(iva.getTarifa() + "%", iva.getValor(), false));
        }
        totalesComprobante.add(new TotalesComprobante("IRBPNR", tc.getTotalIRBPNR(), false));
        totalesComprobante.add(new TotalesComprobante("PROPINA", BigDecimal.ZERO, false));
        if (!compensaciones.equals(BigDecimal.ZERO.setScale(2))) {
        	totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", importeTotal, false));
        	totalesComprobante.add(new TotalesComprobante("VALOR A PAGAR", BigDecimal.valueOf(Double.parseDouble(facturaSRI.getInfoFactura().getImporteTotal())), false));
        } else {
        	totalesComprobante.add(new TotalesComprobante("VALOR TOTAL", BigDecimal.valueOf(Double.parseDouble(facturaSRI.getInfoFactura().getImporteTotal())), false));
        } 
        return totalesComprobante;
    }
	
	private static void obtenerTotalesFactura(Map<String, Object> param, Factura facturaSRI) throws SQLException, ClassNotFoundException {
        TotalComprobante tc = getTotales(facturaSRI.getInfoFactura());
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
        param.put("SUBTOTAL", facturaSRI.getInfoFactura().getTotalSinImpuestos());
        param.put("DESCUENTO", facturaSRI.getInfoFactura().getTotalDescuento());
        param.put("TOTAL", facturaSRI.getInfoFactura().getImporteTotal());
    }
}
