package ec.com.erp.facturacion.electronica.ws;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
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
	
	public static Factura crearFactura(String rucFactElectronica, String secuenciaFactura, FacturaCabeceraDTO facturaCabeceraDTO) {
		Factura factura = new Factura();
		factura.setVersion("2.1.0");
		factura.setId("comprobante");
		factura.setInfoTributaria(crearInfoTributaria(rucFactElectronica, secuenciaFactura));
		factura.setInfoFactura(crearInfoFactura(facturaCabeceraDTO));
		factura.setDetalles(crearDetalles(facturaCabeceraDTO.getFacturaDetalleDTOCols()));
		return factura;
	}

	private static List<Detalle> crearDetalles(Collection<FacturaDetalleDTO> facturaDetalleDTOCols) {
		// Convertidor de decimales
		DecimalFormat formatoDecimales = new DecimalFormat("#.##");
		formatoDecimales.setMinimumFractionDigits(2);
		// Agregar detalles
		List<Detalle> detalles = new ArrayList<>();
		facturaDetalleDTOCols.stream().forEach(detalleFactura ->{
			Detalle detalle = new Detalle();
			detalle.setCodigoPrincipal(detalleFactura.getCodigoBarras());
			detalle.setCodigoAuxiliar(detalleFactura.getCodigoArticulo().toString());
			detalle.setDescripcion(detalleFactura.getDescripcion());
			detalle.setCantidad(detalleFactura.getCantidad().toString());
			detalle.setPrecioUnitario(formatoDecimales.format(detalleFactura.getValorUnidad().doubleValue()));
			detalle.setPrecioTotalSinImpuesto(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue()));
			detalle.setDescuento("0.00");
			

			List<DetAdicional> detallesAdicionales = new ArrayList<DetAdicional>();
			DetAdicional detAdicional = new DetAdicional();
			detallesAdicionales.add(detAdicional);
			detAdicional.setNombre(detalleFactura.getArticuloUnidadManejoDTO().getTipoUnidadManejoCatalogoValorDTO().getNombreCatalogoValor());
			detAdicional.setValor(detalleFactura.getArticuloUnidadManejoDTO().getValorUnidadManejo().toString());
			detalle.setDetallesAdicionales(detallesAdicionales);

			if(detalleFactura.getArticuloDTO().getTieneImpuesto()) {
				List<Impuesto> impuestos = new ArrayList<>();
				Impuesto impuesto = new Impuesto();
				impuestos.add(impuesto);
				impuesto.setCodigo(CodigoImpuestoEnum.IVA);
				impuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_12);
				impuesto.setTarifa(TarifaEnum.IVA_0);
				impuesto.setBaseImponible(formatoDecimales.format(detalleFactura.getSubTotal()));
				impuesto.setValor(formatoDecimales.format(detalleFactura.getSubTotal().doubleValue() * 0.12));
				detalle.setImpuestos(impuestos);
			}
			detalles.add(detalle);
		});
		

		return detalles;
	}

	private static InfoFactura crearInfoFactura(FacturaCabeceraDTO facturaCabeceraDTO) {
		DecimalFormat formatoDecimales = new DecimalFormat("#.##");
		formatoDecimales.setMinimumFractionDigits(2);
		InfoFactura infoFactura = new InfoFactura();
		infoFactura.setFechaEmision((new SimpleDateFormat("dd/MM/YYYY")).format(facturaCabeceraDTO.getFechaDocumento()));
		infoFactura.setDirEstablecimiento(FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
		infoFactura.setObligadoContabilidad(ObligadoContabilidadEnum.NO);
		infoFactura.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.RUC);
		infoFactura.setRazonSocialComprador(facturaCabeceraDTO.getNombreClienteProveedor());
		infoFactura.setIdentificacionComprador(facturaCabeceraDTO.getRucDocumento());
		infoFactura.setTotalSinImpuestos(formatoDecimales.format(facturaCabeceraDTO.getTotalSinImpuestos().doubleValue()));
		infoFactura.setTotalDescuento(formatoDecimales.format(facturaCabeceraDTO.getDescuento().doubleValue()));
		infoFactura.setTotalConImpuestos(crearTotalImpuestos(facturaCabeceraDTO));
		infoFactura.setImporteTotal(formatoDecimales.format(facturaCabeceraDTO.getTotalCuenta().doubleValue()));
		infoFactura.setMoneda(MonedaEnum.DOLAR);
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
		DecimalFormat formatoDecimales = new DecimalFormat("#.##");
		formatoDecimales.setMinimumFractionDigits(2);
		List<TotalImpuesto> totalImpuestos = new ArrayList<>();
		TotalImpuesto totalImpuesto = new TotalImpuesto();
		totalImpuesto.setCodigo(CodigoImpuestoEnum.IVA);
		totalImpuesto.setCodigoPorcentaje(CodigoPorcentajeEnum.IVA_12);
		totalImpuesto.setTarifa(TarifaEnum.IVA_0);
		totalImpuesto.setBaseImponible(formatoDecimales.format(facturaCabeceraDTO.getTotalImpuestos().doubleValue()));
		totalImpuesto.setValor(formatoDecimales.format(facturaCabeceraDTO.getTotalIva().doubleValue()));
		totalImpuestos.add(totalImpuesto);
		return totalImpuestos;
	}
	
	private static InfoTributaria crearInfoTributaria(String rucFactElectronica, String secuenciaFactura) {
		InfoTributaria infoTributaria = new InfoTributaria();
		infoTributaria.setAmbiente(AmbienteEnum.PRUEBAS);
		infoTributaria.setTipoEmision(TipoEmisionEnum.NORMAL);
		if(rucFactElectronica.equals(ERPConstantes.RUC_PRINCIPAL)) {
			infoTributaria.setRazonSocial(FacturacionElectronicaEnum.RUCPRINCIPA.getRazonSocial());
			infoTributaria.setNombreComercial(FacturacionElectronicaEnum.RUCPRINCIPA.getNombreComercial());
			infoTributaria.setDirMatriz(FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
			infoTributaria.setRuc(FacturacionElectronicaEnum.RUCPRINCIPA.getRuc());
		}
		if(rucFactElectronica.equals(ERPConstantes.RUC_SECUNDARIO)) {
			infoTributaria.setRazonSocial(FacturacionElectronicaEnum.RUCSECUNDARIO.getRazonSocial());
			infoTributaria.setNombreComercial(FacturacionElectronicaEnum.RUCSECUNDARIO.getNombreComercial());
			infoTributaria.setDirMatriz(FacturacionElectronicaEnum.RUCSECUNDARIO.getDireccion());
			infoTributaria.setRuc(FacturacionElectronicaEnum.RUCSECUNDARIO.getRuc());
		}
		infoTributaria.setCodDoc("01");
		infoTributaria.setEstab("001");
		infoTributaria.setPtoEmi("001");
		infoTributaria.setSecuencial(numeroFactura(secuenciaFactura));
		Calendar cal = Calendar.getInstance();
		infoTributaria.generarClaveAcceso(cal.getTime(), TipoComprobanteEnum.FACTURA, codigoNumerico(secuenciaFactura));
		return infoTributaria;
	}
	
	private static String numeroFactura(String secuenciaFactura) {
		String cadena = "000000000";
		int totalCadena = cadena.length();
		int numeroCaracteres = secuenciaFactura.length();
		int carRemplazar = totalCadena - numeroCaracteres;
		String res = cadena.substring(0, carRemplazar);
		return res+""+secuenciaFactura;
	}
	
	private static String codigoNumerico(String secuenciaFactura) {
		String cadena = "00000000";
		int totalCadena = cadena.length();
		int numeroCaracteres = secuenciaFactura.length();
		int carRemplazar = totalCadena - numeroCaracteres;
		String res = cadena.substring(0, carRemplazar);
		return res+""+secuenciaFactura;
	}
}