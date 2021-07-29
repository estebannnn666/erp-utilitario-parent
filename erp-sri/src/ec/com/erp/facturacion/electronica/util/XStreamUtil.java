package ec.com.erp.facturacion.electronica.util;

import java.io.Writer;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

import ec.com.erp.facturacion.electronica.ws.autorizacion.Autorizacion;
import ec.com.erp.facturacion.electronica.ws.autorizacion.Mensaje;
import ec.com.erp.facturacion.electronica.ws.autorizacion.RespuestaComprobante;

public class XStreamUtil {

	public static XStream getRespuestaXStream() {
		XStream xstream = new XStream(new XppDriver() {
			public HierarchicalStreamWriter createWriter(Writer out) {
				return new PrettyPrintWriter(out) {
					protected void writeText(QuickWriter writer, String text) {
						writer.write(text);
					}
				};
			}
		});

		xstream.alias("respuesta", RespuestaComprobante.class);
		xstream.alias("autorizacion", Autorizacion.class);
		xstream.alias("fechaAutorizacion", XMLGregorianCalendarImpl.class);
		xstream.alias("mensaje", Mensaje.class);
		xstream.registerConverter(new RespuestaDateConverter());
		return xstream;
	}

}
