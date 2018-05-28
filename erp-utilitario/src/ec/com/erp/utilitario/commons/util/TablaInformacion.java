package ec.com.erp.utilitario.commons.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Clase informacion para clase converter
 * @author Esteban Gudino
 *
 */
public class TablaInformacion
{
	private List<Integer> columnas;
	private int columnaActual;
	private int row;

	public TablaInformacion()
	{
		this.columnas = new ArrayList<>();
	}

	public void agregarColumna(int ancho)
	{
		if (this.columnaActual < this.columnas.size()) {
			int ultimoAncho = ((Integer)this.columnas.get(this.columnaActual)).intValue();
			if (ancho > ultimoAncho)
			{
				this.columnas.set(this.columnaActual, Integer.valueOf(ancho));
			}
		}
		else
		{
			this.columnas.add(Integer.valueOf(ancho));
		}

		this.columnaActual += 1;
	}

	public void agregarFila()
	{
		this.row += 1;
		this.columnaActual = 0;
	}

	public int getCantidadColumnas()
	{
		return this.columnas.size();
	}

	public int getAnchoColumna(int col) {
		return ((Integer)this.columnas.get(col)).intValue();
	}

	public int getRow() {
		return this.row;
	}

	public void ajustarColumnas()
	{
		int sumaTotal = 0;
		Iterator<Integer> it = this.columnas.iterator();
		while (it.hasNext()) {
			Integer ancho = (Integer)it.next();
			sumaTotal += ancho.intValue();
		}

		if (this.columnas.size() > 0)
		{
			int faltante = 100 - sumaTotal;
			int cadaCol = Math.abs(faltante / this.columnas.size());
			int sobrante = faltante % this.columnas.size();
			for (int i = 0; (i < this.columnas.size()) && (cadaCol > 0); i++) {
				int ancho = ((Integer)this.columnas.get(i)).intValue();
				ancho += cadaCol;
				this.columnas.set(i, Integer.valueOf(ancho));
			}

			for (int i = 0; i < sobrante; i++) {
				int ancho = ((Integer)this.columnas.get(i)).intValue() + 1;
				this.columnas.set(i, Integer.valueOf(ancho));
			}
		}
	}
}