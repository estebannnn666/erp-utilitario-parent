package ec.com.erp.facturacion.electronica.modelo;

public class CampoAdicional {

	private String nombre;
	private String valor;

	public CampoAdicional() {
	}

	public CampoAdicional(String nombre, String valor) {
		this.nombre = nombre;
		this.valor = valor;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getValor() {
		return this.valor;
	}
}