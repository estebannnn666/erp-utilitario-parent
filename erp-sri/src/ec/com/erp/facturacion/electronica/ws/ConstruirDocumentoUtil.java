package ec.com.erp.facturacion.electronica.ws;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ec.com.erp.cliente.common.constantes.ERPConstantes;
import ec.com.erp.cliente.mdl.dto.FacturaCabeceraDTO;
import ec.com.erp.cliente.mdl.dto.FacturaDetalleDTO;
import ec.com.erp.cliente.mdl.dto.NotaCreditoDTO;
import ec.com.erp.cliente.mdl.dto.NotaCreditoDetalleDTO;
import ec.com.erp.facturacion.electronica.enumeradores.AmbienteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoImpuestoEnum;
import ec.com.erp.facturacion.electronica.enumeradores.CodigoPorcentajeEnum;
import ec.com.erp.facturacion.electronica.enumeradores.FacturacionElectronicaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TarifaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoEmisionEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.DetAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.Detalle;
import ec.com.erp.facturacion.electronica.modelo.factura.DetalleNotaCredito;
import ec.com.erp.facturacion.electronica.modelo.factura.Impuesto;
import ec.com.erp.facturacion.electronica.modelo.factura.InfoTributaria;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuesto;
import ec.com.erp.facturacion.electronica.modelo.factura.TotalImpuestoNotaCredito;

public class ConstruirDocumentoUtil {
	
	public static InfoTributaria crearInfoTributaria(String tipoComprobante, Date fechaEmision, String rucFactElectronica, String secuenciaDocumento, String codigoEstablecimiento, String codigoPuntoEmision) {
		InfoTributaria infoTributaria = new InfoTributaria();
		infoTributaria.setAmbiente(AmbienteEnum.PRODUCCION);
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
		infoTributaria.setCodDoc(tipoComprobante);
		infoTributaria.setEstab(codigoEstablecimiento);
		infoTributaria.setPtoEmi(codigoPuntoEmision);
		infoTributaria.setSecuencial(secuenciaDocumento);
		infoTributaria.generarClaveAcceso(fechaEmision, tipoComprobante, codigoNumerico(secuenciaDocumento));
		return infoTributaria;
	}
	
	private static String codigoNumerico(String secuenciaFactura) {
		int tamCadena = secuenciaFactura.length();
		return secuenciaFactura.substring(1, tamCadena);
	}
	
	public static List<Detalle> crearDetallesFactura(FacturaCabeceraDTO facturaCabeceraDTO, Collection<FacturaDetalleDTO> facturaDetalleDTOCols) {
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
				detalle.setPrecioUnitario(formatoDecimales.format(obtenerValorUnitarioFactura(facturaCabeceraDTO, detalleFactura)));
				detalle.setPrecioTotalSinImpuesto(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue()));
				detalle.setDescuento(detalleFactura.getDescuento() == null ? "0.00" : formatoDecimales.format(detalleFactura.getDescuento().doubleValue()));
				
				List<DetAdicional> detallesAdicionales = new ArrayList<>();
				DetAdicional detAdicional = new DetAdicional();
				detallesAdicionales.add(detAdicional);
				detAdicional.setNombre(detalleFactura.getArticuloUnidadManejoDTO().getCodigoValorUnidadManejo().equals("UNI") ? detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor() : detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor()+"x"+detalleFactura.getArticuloUnidadManejoDTO().getValorUnidadManejo());
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
	
	public static List<DetalleNotaCredito> crearDetallesNotaCredito(NotaCreditoDTO notaCreditoDTO, Collection<NotaCreditoDetalleDTO> notaCreditoDetalleDTOCols) {
		// Convertidor de decimales
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		// Agregar detalles
		List<DetalleNotaCredito> detalles = new ArrayList<>();
		notaCreditoDetalleDTOCols.stream().forEach(detalleFactura ->{
			if(detalleFactura.getCodigoArticulo() != null){
				DetalleNotaCredito detalle = new DetalleNotaCredito();
				detalle.setCodigoInterno(detalleFactura.getArticuloDTO().getCodigoBarras());
				detalle.setDescripcion(detalleFactura.getDescripcion());
				detalle.setCantidad(detalleFactura.getCantidad().toString());
				detalle.setPrecioUnitario(formatoDecimales.format(obtenerValorUnitarioNotaCredito(notaCreditoDTO, detalleFactura)));
				detalle.setPrecioTotalSinImpuesto(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue()));
				detalle.setDescuento(detalleFactura.getDescuento() == null ? "0.00" : formatoDecimales.format(detalleFactura.getDescuento().doubleValue()));
				
				List<DetAdicional> detallesAdicionales = new ArrayList<>();
				DetAdicional detAdicional = new DetAdicional();
				detallesAdicionales.add(detAdicional);
				detAdicional.setNombre(detalleFactura.getArticuloUnidadManejoDTO().getCodigoValorUnidadManejo().equals("UNI") ? detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor() : detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor()+"x"+detalleFactura.getArticuloUnidadManejoDTO().getValorUnidadManejo());
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
	
	private static BigDecimal obtenerValorUnitarioFactura(FacturaCabeceraDTO facturaCabeceraDTO, FacturaDetalleDTO facturaDetalleDTO){
		BigDecimal valorUnitario;
		if(facturaCabeceraDTO.getTipoCliente().equals(ERPConstantes.CODIGO_CATALOGO_VALOR_CLIENTE_MINORISTA)) {
			valorUnitario = BigDecimal.valueOf(facturaDetalleDTO.getArticuloDTO().getPrecioMinorista().doubleValue() * facturaDetalleDTO.getArticuloUnidadManejoDTO().getValorUnidadManejo());
		}else {
			valorUnitario = BigDecimal.valueOf(facturaDetalleDTO.getArticuloDTO().getPrecio().doubleValue() * facturaDetalleDTO.getArticuloUnidadManejoDTO().getValorUnidadManejo());
		}
		return valorUnitario;
	}
	
	private static BigDecimal obtenerValorUnitarioNotaCredito(NotaCreditoDTO notaCreditoDTO, NotaCreditoDetalleDTO notaCreditoDetalleDTO){
		BigDecimal valorUnitario;
		if(notaCreditoDTO.getTipoCliente().equals(ERPConstantes.CODIGO_CATALOGO_VALOR_CLIENTE_MINORISTA)) {
			valorUnitario = BigDecimal.valueOf(notaCreditoDetalleDTO.getArticuloDTO().getPrecioMinorista().doubleValue() * notaCreditoDetalleDTO.getArticuloUnidadManejoDTO().getValorUnidadManejo());
		}else {
			valorUnitario = BigDecimal.valueOf(notaCreditoDetalleDTO.getArticuloDTO().getPrecio().doubleValue() * notaCreditoDetalleDTO.getArticuloUnidadManejoDTO().getValorUnidadManejo());
		}
		return valorUnitario;
	}
	
	public static List<TotalImpuesto> crearTotalImpuestosFactura(FacturaCabeceraDTO facturaCabeceraDTO) {
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
	
	public static List<TotalImpuestoNotaCredito> crearTotalImpuestosNotaCredito(NotaCreditoDTO notaCreditoDTO) {
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		List<TotalImpuestoNotaCredito> totalImpuestos = new ArrayList<>();
		
		if(notaCreditoDTO.getTotalSinImpuestos().doubleValue() > 0){
			TotalImpuestoNotaCredito totalImpuestoZero = new TotalImpuestoNotaCredito();
			totalImpuestoZero.setCodigo(CodigoImpuestoEnum.IVA);
			totalImpuestoZero.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_0);
			totalImpuestoZero.setBaseImponible(formatoDecimales.format(notaCreditoDTO.getTotalSinImpuestos().doubleValue()));
			totalImpuestoZero.setValor("0.00");
			totalImpuestos.add(totalImpuestoZero);
		}
		
		TotalImpuestoNotaCredito totalImpuesto = new TotalImpuestoNotaCredito();
		totalImpuesto.setCodigo(CodigoImpuestoEnum.IVA);
		totalImpuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_12);
		totalImpuesto.setBaseImponible(formatoDecimales.format(notaCreditoDTO.getTotalImpuestos().doubleValue()));
		totalImpuesto.setValor(formatoDecimales.format(notaCreditoDTO.getTotalIva().doubleValue()));
		totalImpuestos.add(totalImpuesto);
		return totalImpuestos;
	}
	
}
