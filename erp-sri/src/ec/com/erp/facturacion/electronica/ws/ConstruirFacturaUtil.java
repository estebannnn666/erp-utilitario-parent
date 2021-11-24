package ec.com.erp.facturacion.electronica.ws;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.facturacion.electronica.enumeradores.FacturacionElectronicaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.FormaPagoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.MonedaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.ObligadoContabilidadEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoComprobanteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoIdentificacionCompradorEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.CampoInfoAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.Factura;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoFactura;
import ec.com.erp.facturacion.electronica.modelo.factura.InformacionAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.Pago;

public class ConstruirFacturaUtil {
	
	public static Factura crearFactura(FacturaCabeceraDTO facturaCabeceraDTO, String codigoEstablecimiento, String codigoPuntoEmision) {
		Factura factura = new Factura();
		factura.setVersion("1.0.0");
		factura.setId("comprobante");
		factura.setInfoTributaria(ConstruirDocumentoUtil.crearInfoTributaria(TipoComprobanteEnum.FACTURA.getCodigo(), facturaCabeceraDTO.getFechaDocumento(), facturaCabeceraDTO.getTipoRuc(), numeroFactura(facturaCabeceraDTO.getNumeroDocumento()), codigoEstablecimiento, codigoPuntoEmision));
		factura.setInfoFactura(crearInfoFactura(facturaCabeceraDTO));
		factura.setDetalles(ConstruirDocumentoUtil.crearDetallesFactura(facturaCabeceraDTO, facturaCabeceraDTO.getFacturaDetalleDTOCols()));
		// Informacion adicionals
		InformacionAdicional info = new InformacionAdicional();
		CampoInfoAdicional regimen = new CampoInfoAdicional();
		regimen.setNombre("Regimen");
		regimen.setValue("Contribuyente Regimen Microempresas");
		info.getCampoAdicional().add(regimen);
		if(facturaCabeceraDTO.getVendedorDTO() != null){
			CampoInfoAdicional vendedor = new CampoInfoAdicional();
			vendedor.setNombre("Vendedor");
			vendedor.setValue(facturaCabeceraDTO.getVendedorDTO().getPersonaDTO().getNombreCompleto());
			info.getCampoAdicional().add(vendedor);
		}
		if(facturaCabeceraDTO.getCiudad() != null && facturaCabeceraDTO.getCiudad().trim() != ""){
			CampoInfoAdicional ciudadSector = new CampoInfoAdicional();
			ciudadSector.setNombre("Ciudad");
			ciudadSector.setValue(facturaCabeceraDTO.getCiudad());
			info.getCampoAdicional().add(ciudadSector);
		}
		
		factura.setInfoAdicional(info);
		return factura;
	}


	private static InfoFactura crearInfoFactura(FacturaCabeceraDTO facturaCabeceraDTO) {
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		InfoFactura infoFactura = new InfoFactura();
		infoFactura.setFechaEmision((new SimpleDateFormat("dd/MM/YYYY")).format(facturaCabeceraDTO.getFechaDocumento()));
		infoFactura.setDirEstablecimiento(FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
		infoFactura.setObligadoContabilidad(ObligadoContabilidadEnum.NO);
		if(facturaCabeceraDTO.getRucDocumento().length() == 13){
			infoFactura.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.RUC);
		}
		if(facturaCabeceraDTO.getRucDocumento().length() == 10){
			infoFactura.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.CEDULA);
		}
		if(facturaCabeceraDTO.getRucDocumento().equals("9999999999")){
			infoFactura.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.CONSUMIDOR_FINAL);
		}		
		infoFactura.setRazonSocialComprador(facturaCabeceraDTO.getNombreClienteProveedor());
		infoFactura.setIdentificacionComprador(facturaCabeceraDTO.getRucDocumento());
		infoFactura.setDireccionComprador(facturaCabeceraDTO.getDireccion());
		infoFactura.setTotalSinImpuestos(formatoDecimales.format(facturaCabeceraDTO.getSubTotal().doubleValue()));
		infoFactura.setTotalDescuento(formatoDecimales.format(facturaCabeceraDTO.getDescuento().doubleValue()));
		infoFactura.setTotalConImpuestos(ConstruirDocumentoUtil.crearTotalImpuestosFactura(facturaCabeceraDTO));
		infoFactura.setImporteTotal(formatoDecimales.format(facturaCabeceraDTO.getTotalCuenta().doubleValue()));
		infoFactura.setMoneda(MonedaEnum.DOLAR);
		infoFactura.setPropina("0.00");
		infoFactura.setPagos(crearPagos(formatoDecimales.format(facturaCabeceraDTO.getTotalCuenta().doubleValue())));
		return infoFactura;
	}
	
	private static List<Pago> crearPagos(String totalPago) {
		List<Pago> pagos = new ArrayList<>();
		Pago pago = new Pago();
		pago.setFormaPago(FormaPagoEnum.SIN_UTILIZACION_DEL_SISTEMA_FINANCIERO);
		pago.setTotal(totalPago);
		pagos.add(pago);
		return pagos;
	}

	public static String numeroFactura(String secuenciaFactura) {
		String cadena = "000000000";
		int totalCadena = cadena.length();
		int numeroCaracteres = secuenciaFactura.length();
		int carRemplazar = totalCadena - numeroCaracteres;
		String res = cadena.substring(0, carRemplazar);
		return res+""+secuenciaFactura;
	}
	
}
