package ec.com.erp.facturacion.electronica.ws;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import ec.com.erp.cliente.mdl.dto.NotaCreditoDTO;
import ec.com.erp.facturacion.electronica.enumeradores.FacturacionElectronicaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.MonedaEnum;
import ec.com.erp.facturacion.electronica.enumeradores.ObligadoContabilidadEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoComprobanteEnum;
import ec.com.erp.facturacion.electronica.enumeradores.TipoIdentificacionCompradorEnum;
import ec.com.erp.facturacion.electronica.modelo.factura.CampoInfoAdicional;
import ec.com.erp.facturacion.electronica.modelo.factura.InformacionAdicional;
import ec.com.erp.facturacion.electronica.modelo.notacredito.InfoNotaCredito;
import ec.com.erp.facturacion.electronica.modelo.notacredito.NotaCredito;

public class ConstruirNotaCreditoUtil {
	
	public static NotaCredito crearNotaCredito(NotaCreditoDTO notaCreditoDTO, String codigoEstablecimiento, String codigoPuntoEmision) {
		NotaCredito notaCredito = new NotaCredito();
		notaCredito.setVersion("1.0.0");
		notaCredito.setId("comprobante");
		notaCredito.setInfoTributaria(ConstruirDocumentoUtil.crearInfoTributaria(TipoComprobanteEnum.NOTA_DE_CREDITO.getCodigo(), notaCreditoDTO.getFechaDocumento(), notaCreditoDTO.getTipoRuc(), numeroNotaCredito(notaCreditoDTO.getNumeroDocumento()), codigoEstablecimiento, codigoPuntoEmision));
		notaCredito.setInfoNotaCredito(crearInfoNotaCredito(notaCreditoDTO));
		notaCredito.setDetalles(ConstruirDocumentoUtil.crearDetallesNotaCredito(notaCreditoDTO, notaCreditoDTO.getNotaCreditoDetalleDTOCols()));
		// Informacion adicionals
		InformacionAdicional info = new InformacionAdicional();
		CampoInfoAdicional regimen = new CampoInfoAdicional();
		regimen.setNombre("Regimen");
		regimen.setValue("Contribuyente Regimen Microempresas");
		info.getCampoAdicional().add(regimen);
		
		CampoInfoAdicional vendedor = new CampoInfoAdicional();
		vendedor.setNombre("Email");
		vendedor.setValue(notaCreditoDTO.getEmail() != null && !notaCreditoDTO.getEmail().trim().equals("") ? notaCreditoDTO.getEmail() : "No tiene correo");
		info.getCampoAdicional().add(vendedor);
		
		notaCredito.setInfoAdicional(info);
		return notaCredito;
	}


	private static InfoNotaCredito crearInfoNotaCredito(NotaCreditoDTO notaCreditoDTO) {
		DecimalFormatSymbols decimalSymbols = DecimalFormatSymbols.getInstance();
	    decimalSymbols.setDecimalSeparator('.');
		DecimalFormat formatoDecimales = new DecimalFormat("#.##", decimalSymbols);
		formatoDecimales.setMinimumFractionDigits(2);
		InfoNotaCredito infoNotaCredito = new InfoNotaCredito();
		infoNotaCredito.setFechaEmision((new SimpleDateFormat("dd/MM/yyyy")).format(new Date()));
		infoNotaCredito.setDirEstablecimiento(FacturacionElectronicaEnum.RUCPRINCIPA.getDireccion());
		if(notaCreditoDTO.getRucCliente().length() == 13){
			infoNotaCredito.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.RUC);
		}
		if(notaCreditoDTO.getRucCliente().length() == 10){
			infoNotaCredito.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.CEDULA);
		}
		if(notaCreditoDTO.getRucCliente().equals("9999999999")){
			infoNotaCredito.setTipoIdentificacionComprador(TipoIdentificacionCompradorEnum.CONSUMIDOR_FINAL);
		}		
		infoNotaCredito.setRazonSocialComprador(notaCreditoDTO.getRazonSocial());
		infoNotaCredito.setIdentificacionComprador(notaCreditoDTO.getRucCliente());
		infoNotaCredito.setObligadoContabilidad(ObligadoContabilidadEnum.SI);
		infoNotaCredito.setCodDocModificado(TipoComprobanteEnum.FACTURA.getCodigo());
		infoNotaCredito.setNumDocModificado(notaCreditoDTO.getNumeroComprobante());
		infoNotaCredito.setTotalConImpuestos(ConstruirDocumentoUtil.crearTotalImpuestosNotaCredito(notaCreditoDTO));
		infoNotaCredito.setFechaEmisionDocSustento((new SimpleDateFormat("dd/MM/yyyy")).format(notaCreditoDTO.getFechaEmisionFactura()));
		infoNotaCredito.setTotalSinImpuestos(notaCreditoDTO.getSubTotal().setScale(2, RoundingMode.HALF_UP));
		infoNotaCredito.setValorModificacion(notaCreditoDTO.getTotalCuenta().setScale(2, RoundingMode.HALF_UP));
		infoNotaCredito.setMoneda(MonedaEnum.DOLAR);
		infoNotaCredito.setMotivo(notaCreditoDTO.getMotivo());
		return infoNotaCredito;
	}
	
	private static String numeroNotaCredito(String secuenciaFactura) {
		String cadena = "000000000";
		int totalCadena = cadena.length();
		int numeroCaracteres = secuenciaFactura.length();
		int carRemplazar = totalCadena - numeroCaracteres;
		String res = cadena.substring(0, carRemplazar);
		return res+""+secuenciaFactura;
	}
}
