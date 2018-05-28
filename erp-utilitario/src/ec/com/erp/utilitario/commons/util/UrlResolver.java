package ec.com.erp.utilitario.commons.util;

import javax.xml.transform.Source;
import javax.xml.transform.URIResolver;

import org.jdom.Document;
import org.jdom.transform.JDOMSource;

import ec.com.erp.cliente.common.exception.ERPException;

/**
 * Clase implementacion para resolver urls
 * @author Esteban Gudino
 *
 */
class UrlResolver implements URIResolver
{
	String xsltref;

	/**
	 * Contructor sobrecargado con plantilla xslt
	 * @param xslt
	 */
	public UrlResolver(String xslt)
	{
		this.xsltref = xslt;
	}
	
	/**
	 * Metedo que resulve la URL
	 * @param href
	 * @param base
	 * @return
	 */
	public Source resolve(String href, String base) {
		JDOMSource xsltSource = null;
		try {
			Document xsltDoc = TransformerUtil.stringToXML(this.xsltref);
			xsltSource = new JDOMSource(xsltDoc);
		}
		catch (ERPException e) {
		}
		return xsltSource;
	}
}