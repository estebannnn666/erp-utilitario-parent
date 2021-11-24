package ec.com.erp.facturacion.electronica.enumeradores;

public enum TipoComprobanteEnum {

	/**
	 * Factura
	 */
	FACTURA("01"),

	/**
	 * Nota de Credito
	 */
	NOTA_DE_CREDITO("04"),

	/**
	 * Nota de Credito
	 */
	NOTA_DE_DEBITO("05"),

	/**
	 * Nota de Credito
	 */
	GUIA_DE_REMISION("06"),
	/**
	 * Nota de Credito
	 */
	COMPROBANTE_DE_RETENCION("07");

	private String codigo;

	private TipoComprobanteEnum(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}
}
