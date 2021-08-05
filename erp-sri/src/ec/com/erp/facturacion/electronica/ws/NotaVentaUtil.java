package ec.com.erp.facturacion.electronica.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ec.com.erp.cliente.common.exception.ERPException;
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.cliente.mdl.dto.FacturaDetalleDTO;
import ec.com.erp.facturacion.electronica.enumeradores.FacturacionElectronicaEnum;
import ec.com.erp.facturacion.electronica.modelo.DetallesAdicionalesReporte;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class NotaVentaUtil {
	
	public static byte[] generarNotaVenta(FacturaCabeceraDTO facturaCabeceraDTO) throws JRException, IOException {
		try {
			Map<String, Object> params = new HashMap<>();
			JRDataSource dataSource = new JRBeanCollectionDataSource(getDetallesAdiciones(facturaCabeceraDTO.getFacturaDetalleDTOCols()));
			params = obtenerInfoCabecera(facturaCabeceraDTO);
			params.put(JRParameter.REPORT_LOCALE, Locale.US);
			JasperFillManager.fillReportToFile("C:\\ErpLibreries\\resources\\nota_venta.jasper", params, dataSource);
			String filePdf = JasperExportManager.exportReportToPdfFile("C:\\ErpLibreries\\resources\\nota_venta.jrprint");
			File file = new File(filePdf);
			return Files.readAllBytes(file.toPath());
		}catch (JRException e) {
			throw new ERPException("Error", e.getMessage()) ;
		}catch (Exception e) {
			throw new ERPException("Error", e.getMessage()) ;
		}
	}
	
	private static Map<String, Object> obtenerInfoCabecera(FacturaCabeceraDTO facturaCabeceraDTO) {
		Map<String, Object> param = new HashMap<>();
		SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
		try {
			param.put("LOGO", new FileInputStream("C:\\ErpLibreries\\imagenes\\probersa.jpeg"));
		} catch (FileNotFoundException ex) {
			try {
				param.put("LOGO", new FileInputStream("C:\\ErpLibreries\\imagenes\\logo.jpeg"));
				Logger.getLogger(FacturaWS.class.getName()).log(Level.SEVERE, null, ex);
			} catch (FileNotFoundException ex1) {
				Logger.getLogger(FacturaWS.class.getName()).log(Level.SEVERE, null, ex1);
			}
		}
		param.put("RUC", FacturacionElectronicaEnum.RUCPRINCIPA.getRuc());
		param.put("NUM_FACT", facturaCabeceraDTO.getNumeroDocumento());
		param.put("DIR_MATRIZ", FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
		param.put("NUM_AUT", facturaCabeceraDTO.getCodigoReferenciaFactura());
		param.put("FECHA_AUT", formatoFecha.format(facturaCabeceraDTO.getFechaDocumento()));
		param.put("FECHA_EMISION", formatoFecha.format(facturaCabeceraDTO.getFechaDocumento()));
		param.put("EMAIL_EMPRESA", "jpame_21@hotmail.com");
		param.put("RUC_CLIENTE", facturaCabeceraDTO.getRucDocumento());
		param.put("RAZON_SOCIAL", facturaCabeceraDTO.getNombreClienteProveedor());
		param.put("SUCURSAL", "IBARRA - ECUADOR");
		param.put("TELEFONO", facturaCabeceraDTO.getTelefono());
		param.put("DIRECCION", facturaCabeceraDTO.getDireccion());
		
		// Convertidor de decimales
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
				
		param.put("SUBTOTAL", formatoDecimales.format(facturaCabeceraDTO.getSubTotal()));
		param.put("DESCUENTO", formatoDecimales.format(facturaCabeceraDTO.getDescuento()));
		param.put("TARIFACERO", formatoDecimales.format(facturaCabeceraDTO.getTotalSinImpuestos()));
		param.put("TARIFAIVA", formatoDecimales.format(facturaCabeceraDTO.getTotalImpuestos()));
		param.put("VALORIVA", formatoDecimales.format(facturaCabeceraDTO.getTotalIva()));
		param.put("TOTAL", formatoDecimales.format(facturaCabeceraDTO.getTotalCuenta()));
		return param;
	}
	
	private static List<DetallesAdicionalesReporte> getDetallesAdiciones(Collection<FacturaDetalleDTO> facturaDetalleDTOCols) throws SQLException, ClassNotFoundException {
		// Convertidor de decimales
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		
		List<DetallesAdicionalesReporte> detallesAdiciones = new ArrayList<>();
	    for (FacturaDetalleDTO det : facturaDetalleDTOCols) {
	    	DetallesAdicionalesReporte detAd = new DetallesAdicionalesReporte();
	    	detAd.setCodigoPrincipal(det.getArticuloDTO().getCodigoBarras());
	    	detAd.setCodigoAuxiliar(det.getCodigoArticulo().toString());
	    	detAd.setDescripcion(det.getDescripcion());
	    	detAd.setCantidad(det.getCantidad().toString());
	    	detAd.setPrecioTotalSinImpuesto(formatoDecimales.format(det.getSubTotal().doubleValue()));
	    	detAd.setPrecioUnitario(det.getValorUnidad());
	    	detAd.setPrecioSinSubsidio(BigDecimal.ZERO);
	    	if (det.getDescuento() != null) {
	    		detAd.setDescuento(formatoDecimales.format(det.getDescuento()));
	    	}
	    	detAd.setDetalle1(det.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor());
	    	detallesAdiciones.add(detAd);
	    } 
	    return detallesAdiciones;
	}
}
