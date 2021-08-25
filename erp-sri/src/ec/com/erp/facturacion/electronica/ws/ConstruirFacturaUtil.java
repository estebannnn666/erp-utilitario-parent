package ec.com.erp.facturacion.electronica.ws;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ec.com.erp.cliente.common.constantes.ERPConstantes;
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.cliente.mdl.dto.FacturaDetalleDTO;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoImpuestoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoPorcentajeEnum;
import ec.com.erp.facturacion.electronica.enumeradores.FacturacionElectronicaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.FormaPagoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.MonedaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.ObligadoContabilidadEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TarifaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoComprobanteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoEmisionEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoIdentificacionCompradorEnum;
import ec.com.erp.facturacion.electronica.modelo.DetAdicional;
import ec.com.erp.facturacion.electronica.modelo.Detalle;
import ec.com.erp.facturacion.electronica.modelo.Factura;
import ec.com.erp.facturacion.electronica.modelo.Impuesto;
import ec.com.erp.facturacion.electronica.modelo.InfoFactura;
import ec.com.erp.facturacion.electronica.modelo.InfoTributaria;
import ec.com.erp.facturacion.electronica.modelo.Pago;
import ec.com.erp.facturacion.electronica.modelo.TotalImpuesto;

public class ConstruirFacturaUtil {
	
	public static Factura crearFactura(FacturaCabeceraDTO facturaCabeceraDTO) {
		Factura factura = new Factura();
		factura.setVersion("1.0.0");
		factura.setId("comprobante");
		factura.setInfoTributaria(crearInfoTributaria(facturaCabeceraDTO.getFechaDocumento(), facturaCabeceraDTO.getTipoRuc(), facturaCabeceraDTO.getNumeroDocumento()));
		factura.setInfoFactura(crearInfoFactura(facturaCabeceraDTO));
		factura.setDetalles(crearDetalles(facturaCabeceraDTO.getFacturaDetalleDTOCols()));
		return factura;
	}

	private static List<Detalle> crearDetalles(Collection<FacturaDetalleDTO> facturaDetalleDTOCols) {
		// Convertidor de decimales
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		// Agregar detalles
		List<Detalle> detalles = new ArrayList<>();
		facturaDetalleDTOCols.stream().forEach(detalleFactura ->{
			if(detalleFactura.getCodigoArticulo() != null){
				Detalle detalle = new Detalle();
				detalle.setCodigoPrincipal(detalleFactura.getArticuloDTO().getCodigoBarras());
				detalle.setCodigoAuxiliar(detalleFactura.getCodigoArticulo().toString());
				detalle.setDescripcion(detalleFactura.getDescripcion());
				detalle.setCantidad(detalleFactura.getCantidad().toString());
				detalle.setPrecioUnitario(formatoDecimales.format(detalleFactura.getValorUnidad().doubleValue() * detalleFactura.getArticuloUnidadManejoDTO().getValorUnidadManejo()));
				detalle.setPrecioTotalSinImpuesto(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue()));
				detalle.setDescuento(detalleFactura.getDescuento() == null ? "0.00" : formatoDecimales.format(detalleFactura.getDescuento().doubleValue()));
				
				List<DetAdicional> detallesAdicionales = new ArrayList<>();
				DetAdicional detAdicional = new DetAdicional();
				detallesAdicionales.add(detAdicional);
				detAdicional.setNombre(detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor());
				detAdicional.setValor(detalleFactura.getArticuloUnidadManejoDTO().getValorUnidadManejo().toString());
				detalle.setDetallesAdicionales(detallesAdicionales);
	
				List<Impuesto> impuestos = new ArrayList<>();
				Impuesto impuesto = new Impuesto();
				impuesto.setCodigo(CodigoImpuestoEnum.IVA);
				impuesto.setBaseImponible(formatoDecimales.format(detalleFactura.getSubTotal()));
				if(detalleFactura.getArticuloDTO().getTieneImpuesto()) {
					impuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_12);
					impuesto.setTarifa(TarifaEnum.IVA_12);
					impuesto.setValor(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue() * 0.12));
				}else{
					impuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_0);
					impuesto.setTarifa(TarifaEnum.IVA_0);
					impuesto.setValor("0.00");
				}
				impuestos.add(impuesto);
				detalle.setImpuestos(impuestos);
				detalles.add(detalle);
			}
		});
		return detalles;
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
		infoFactura.setTotalConImpuestos(crearTotalImpuestos(facturaCabeceraDTO));
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

	private static List<TotalImpuesto> crearTotalImpuestos(FacturaCabeceraDTO facturaCabeceraDTO) {
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		List<TotalImpuesto> totalImpuestos = new ArrayList<>();
		
		TotalImpuesto totalImpuestoZero = new TotalImpuesto();
		totalImpuestoZero.setCodigo(CodigoImpuestoEnum.IVA);
		totalImpuestoZero.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_0);
		totalImpuestoZero.setTarifa(TarifaEnum.IVA_0);
		totalImpuestoZero.setBaseImponible(formatoDecimales.format(facturaCabeceraDTO.getTotalSinImpuestos().doubleValue()));
		totalImpuestoZero.setValor("0.00");
		totalImpuestos.add(totalImpuestoZero);
		
		TotalImpuesto totalImpuesto = new TotalImpuesto();
		totalImpuesto.setCodigo(CodigoImpuestoEnum.IVA);
		totalImpuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_12);
		totalImpuesto.setTarifa(TarifaEnum.IVA_12);
		totalImpuesto.setBaseImponible(formatoDecimales.format(facturaCabeceraDTO.getTotalImpuestos().doubleValue()));
		totalImpuesto.setValor(formatoDecimales.format(facturaCabeceraDTO.getTotalIva().doubleValue()));
		totalImpuestos.add(totalImpuesto);
		return totalImpuestos;
	}
	
	private static InfoTributaria crearInfoTributaria(Date fechaEmision, String rucFactElectronica, String secuenciaFactura) {
		InfoTributaria infoTributaria = new InfoTributaria();
		infoTributaria.setAmbiente(AmbienteEnum.PRUEBAS);
		infoTributaria.setTipoEmision(TipoEmisionEnum.NORMAL);
		if(rucFactElectronica.equals(ERPConstantes.TIPO_RUC_UNO)) {
			infoTributaria.setRazonSocial(FacturacionElectronicaEnum.RUCPRINCIPA.getRazonSocial());
			infoTributaria.setNombreComercial(FacturacionElectronicaEnum.RUCPRINCIPA.getNombreComercial());
			infoTributaria.setDirMatriz(FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
			infoTributaria.setRuc(FacturacionElectronicaEnum.RUCPRINCIPA.getRuc());
		}
		if(rucFactElectronica.equals(ERPConstantes.TIPO_RUC_DOS)) {
			infoTributaria.setRazonSocial(FacturacionElectronicaEnum.RUCSECUNDARIO.getRazonSocial());
			infoTributaria.setNombreComercial(FacturacionElectronicaEnum.RUCSECUNDARIO.getNombreComercial());
			infoTributaria.setDirMatriz(FacturacionElectronicaEnum.RUCSECUNDARIO.getDireccion());
			infoTributaria.setRuc(FacturacionElectronicaEnum.RUCSECUNDARIO.getRuc());
		}
		infoTributaria.setCodDoc("01");
		infoTributaria.setEstab("001");
		infoTributaria.setPtoEmi("001");
		infoTributaria.setSecuencial(numeroFactura(secuenciaFactura));
		infoTributaria.generarClaveAcceso(fechaEmision, TipoComprobanteEnum.FACTURA, codigoNumerico(secuenciaFactura));
		return infoTributaria;
	}
	
	public static String numeroFactura(String secuenciaFactura) {
		String cadena = "000000000";
		int totalCadena = cadena.length();
		int numeroCaracteres = secuenciaFactura.length();
		int carRemplazar = totalCadena - numeroCaracteres;
		String res = cadena.substring(0, carRemplazar);
		return res+""+secuenciaFactura;
	}
	
	private static String codigoNumerico(String secuenciaFactura) {
		int tamCadena = secuenciaFactura.length();
		return secuenciaFactura.substring(1, tamCadena);
	}
}
