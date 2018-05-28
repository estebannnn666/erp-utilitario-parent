package ec.com.erp.utilitario.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;

import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.DOMOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.w3c.tidy.Configuration;
import org.w3c.tidy.Tidy;
import org.xml.sax.InputSource;

/**
 * Clase para realizar conversiones a
 * PDF teniendo como entrada un HTML
 * 
 * @author Esteban Gudino
 * 26/05/2018
 */
public class HtmlPdf {

	// configure fopFactory as desired
	private FopFactory fopFactory = FopFactory.newInstance();

	/** xsl-fo namespace URI */
	protected static String foNS = "http://www.w3.org/1999/XSL/Format";

	private Document docFopXSLTTransformacion;

	long tTotal;

	long thtmlToXhtml;

	long txhtmlToFop;

	long tfopToPdf;

	
	/**
	 * 
	 * Crea un transformador a partir de un archivo. Cada vez que se construya
	 * el HtmlAPdf con este constructor se cargara el xslt de transforamcion
	 * especificado
	 * 
	 * @param xsltFile
	 *            Ruta del archivo que contiene el xslt
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public HtmlPdf(String xsltFile) throws FileNotFoundException, JDOMException, IOException {
		SAXBuilder parser = new SAXBuilder();
		docFopXSLTTransformacion = parser.build(new FileInputStream(xsltFile));
	}

	/**
	 * 
	 * Crea un transformador a partir de un archivo. Cada vez que se construya
	 * el HtmlAPdf con este constructor se cargara el xslt de transforamcion
	 * especificado
	 * 
	 * @param xsltFile
	 *            Ruta del archivo que contiene el xslt
	 * @throws FileNotFoundException
	 * @throws JDOMException
	 * @throws IOException
	 */
	public HtmlPdf() throws FileNotFoundException, JDOMException, IOException {
		this(HtmlPdf.class.getResourceAsStream("/ec/com/smx/framework/common/fo/xhtml2fo.xsl"));
	}

	/**
	 * Crea un HtmlAPdf a partir de un inputstream. La primera vez que instancia
	 * un HtmlApdf sucesivas instancias utilizar�n el mismo documento para
	 * evitar el proceso de carga
	 * 
	 * @param inputStream
	 *            InputStream al XSLT
	 * @throws JDOMException
	 * @throws IOException
	 */
	public HtmlPdf(InputStream inputStream) throws JDOMException, IOException {
		// el xslt de tranformacion se parsea la primera vez, las siguientes
		// veces se utiliza el mismo
		// documento
		if (docFopXSLTTransformacion == null) {
			SAXBuilder parser = new SAXBuilder();
			docFopXSLTTransformacion = parser.build(inputStream);
		}
	}

	/**
	 * Transforma un html a xhtml. Se utiliza la libreria Tidy que permite
	 * cerrar tags mal declarados. Ademas se incluye la cabecera adecuada del
	 * xhtml y los namespaces
	 * 
	 * @param html
	 *            html que se convertira a xhtml
	 * @return Un Documento que contiene el xhtml
	 * @throws IOException
	 * @throws JDOMException
	 */
	@SuppressWarnings("unused")
	public Document html2Xhtml(String html, String url) throws IOException, JDOMException {
		thtmlToXhtml = System.currentTimeMillis();
		Tidy t = new Tidy();
		t.setXHTML(true);
		t.setTidyMark(false);
		t.setShowWarnings(false);
		t.setCharEncoding(Configuration.LATIN1);
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		org.w3c.dom.Document d = t.parseDOM(new ByteArrayInputStream(html.getBytes()), byteArray);

		String cabecera = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

		String xml = cabecera + new String(byteArray.toByteArray());

		// arma el JDOM Document
		StringReader reader = new StringReader(xml);
		SAXBuilder parser = new SAXBuilder(false);
		parser.setValidation(false);

		parser.setDTDHandler(null);
		parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		Document doc = parser.build(new InputSource(reader));

		// agrega los col
		TablaConverter tablaParser = new TablaConverter(url);
		tablaParser.parse(doc);
		thtmlToXhtml = System.currentTimeMillis() - thtmlToXhtml;
		return doc;
	}

	/**
	 * Transforma un xhtml a un documento FOP. Para hacerlo utiliza el xslt con
	 * el que se construy� este objeto mezcl�ndolo con el xml de par�metro
	 * 
	 * @param pXML
	 *            Xhtml que se converita a FOP
	 * @return Documento con archivo FOP
	 * @throws TransformerException
	 */
	@SuppressWarnings("rawtypes")
	public Document transformar(Document pXML, HashMap<String, String> parametros) throws TransformerException {
		txhtmlToFop = System.currentTimeMillis();

		pXML.setDocType(null);
		JDOMSource xsltSource = new JDOMSource(docFopXSLTTransformacion);
		TransformerFactory transFact = TransformerFactory.newInstance();
		Transformer trans = transFact.newTransformer(xsltSource);

		JDOMSource docIn = new JDOMSource(pXML);
		JDOMResult docOut = new JDOMResult();
		trans.setParameter("page-number-print-in-footer", false);
		if (parametros != null) {
			Set set = parametros.entrySet();
			Iterator iter = set.iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				trans.setParameter(entry.getKey().toString(), entry.getValue());
			}
		}
		trans.transform(docIn, docOut);
		txhtmlToFop = System.currentTimeMillis() - txhtmlToFop;
		return docOut.getDocument();
	}

	/**
	 * Converts a DOM Document to a PDF file using FOP.
	 * 
	 * @param xslfoDoc
	 *            the DOM Document
	 * @param pdf
	 *            the target PDF file
	 */
	public byte[] convertDOM2PDFBytes(org.w3c.dom.Document xslfoDoc) {
		try {
			FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
			// configure foUserAgent as desired

			// Setup output
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			// OutputStream out = new java.io.FileOutputStream(pdf);
			// out = new java.io.BufferedOutputStream(out);

			try {
				// Construct fop with desired output format and output stream
				Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

				// Setup Identity Transformer
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer(); // identity
																	// transformer

				// Setup input for XSLT transformation
				Source src = new DOMSource(xslfoDoc);

				// Resulting SAX events (the generated FO) must be piped through
				// to FOP
				Result res = new SAXResult(fop.getDefaultHandler());

				// Start XSLT transformation and FOP processing
				transformer.transform(src, res);
				return out.toByteArray();
			} finally {
				out.close();
			}

		} catch (Exception e) {
		}
		return new byte[0];
	}

	/**
	 * Main method.
	 * 
	 * @param args
	 *            command-line arguments
	 * @throws JDOMException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public byte[] convertir(String html, String cabecera, String documentoID, HashMap<String, String> parametros, String url) throws IOException, JDOMException, TransformerException {
		Document docXhtml = html2Xhtml(html, url);
		Document docFop = transformar(docXhtml, parametros);
		DOMOutputter output = new DOMOutputter();
		org.w3c.dom.Document docFopDom = output.output(docFop);
		byte contenido[] = convertDOM2PDFBytes(docFopDom);
		return contenido;
	}
}

