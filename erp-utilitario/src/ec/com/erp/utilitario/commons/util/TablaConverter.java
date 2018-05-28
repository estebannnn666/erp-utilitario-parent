package ec.com.erp.utilitario.commons.util;

import java.util.Iterator;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 * Clase conversora de formatos
 * @author Administrador
 *
 */
public class TablaConverter
{
	public static final String ELE_TABLE = "table";
	public static final String ELE_TD = "td";
	public static final String ELE_TR = "tr";
	public static final String ELE_COL = "col";
	public static final String ELE_IMG = "img";
	public static final String ATT_SRC = "src";
	public static final String ATT_WIDTH = "width";
	private String uriPorDefecto;

	public TablaConverter(String uriPorDefecto)
	{
		this.uriPorDefecto = uriPorDefecto;
	}

	public TablaConverter()
	{
		this("http://localhost:9090");
	}

	public void parse(Document doc)
	{
		recorrer(doc.getRootElement(), null, 0);
	}

	private void recorrer(Element e, TablaInformacion tablaPadre, int indent)
  {
    if (e.getName().equalsIgnoreCase("table")) {
      tablaPadre = new TablaInformacion();
    }

    if (e.getName().equalsIgnoreCase("img"))
    {
      Attribute att = e.getAttribute("src");
      String value = att.getValue();
      value = this.uriPorDefecto + value;
      att.setValue(value);
    }
    else if (e.getName().equalsIgnoreCase("td")) {
      Attribute att = e.getAttribute("width");
      boolean agregado = false;
      if (att != null) {
        String anchoStr = att.getValue().trim();
        char[] anchoArray = anchoStr.toCharArray();

        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < anchoArray.length; i++) {
          char caracter = anchoArray[i];
          if (Character.isDigit(caracter)) {
            buff = buff.append(caracter);
          }
        }
        anchoStr = buff.toString();
        if ((anchoStr != null) && (!anchoStr.equals(""))) {
          int ancho = Integer.valueOf(anchoStr).intValue();
          tablaPadre.agregarColumna(ancho);
          agregado = true;
        }

      }

      if (!agregado) {
        tablaPadre.agregarColumna(0);
      }

    }
    else if (e.getName().equalsIgnoreCase("tr")) {
      tablaPadre.agregarFila();
    }

    Iterator<?> it = e.getChildren().iterator();
    while (it.hasNext())
    {
      Object o = it.next();
      if ((o instanceof Element))
        recorrer((Element)o, tablaPadre, indent);
    }
    if (e.getName().equalsIgnoreCase("table"))
    {
      int cant = tablaPadre.getCantidadColumnas();

      tablaPadre.ajustarColumnas();

      for (int i = 0; i < cant; i++) {
        Element element = new Element("col", e.getNamespace());
        element.setAttribute("width", tablaPadre.getAnchoColumna(i) + "*");

        e.addContent(element);
      }
    }
  }
}