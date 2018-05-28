package ec.com.erp.utilitario.commons.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Map;
import java.util.Set;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jdom.transform.JDOMResult;
import org.jdom.transform.JDOMSource;
import org.xml.sax.SAXException;

import ec.com.erp.cliente.common.exception.ERPException;

/**
 * 
 * @author Esteban Gudino
 * 2018-05-26
 */
public class TransformerUtil
{
	/**
	 * Convierte de document a string
	 * @param pDoc
	 * @return
	 */
	  public static String xmlToString(Document pDoc)
	  {
		  XMLOutputter outPutter = new XMLOutputter();
		  outPutter.setFormat(Format.getPrettyFormat());
		  return outPutter.outputString(pDoc);
	  }

	  /**
	   * Convierte de xml a String con 2 parametros
	   * @param pDoc
	   * @param format
	   * @return
	   */
	  public static String xmlToString(Document pDoc, boolean format) {
		  XMLOutputter outPutter = new XMLOutputter();
		  outPutter.setFormat(Format.getCompactFormat());
		  if (format) {
			  outPutter.setFormat(Format.getPrettyFormat());
		  }
		  return outPutter.outputString(pDoc);
	  }

	  /**
	   * Convierte de xml a String con 3 parametros
	   * @param pDoc
	   * @param codificacion
	   * @param identation
	   * @return
	   */
	  public static String xmlToString(Document pDoc, String codificacion, boolean identation)
	  {
		  XMLOutputter outPutter = new XMLOutputter();
		  Format format = Format.getCompactFormat();
	
		  if (identation) {
			  format = Format.getPrettyFormat();
		  } else {
			  format = Format.getCompactFormat();
			  format.setOmitDeclaration(true);
		  }
		  format.setEncoding(codificacion);
		  outPutter.setFormat(format);
		  return outPutter.outputString(pDoc);
	  }

	  /**
	   * Metodo generico para transformar documentos
	   * @param pXML
	   * @param pXSLT
	   * @param pParams
	   * @return
	   * @throws TransformerException
	   */
	  public static Document transformar(Document pXML, Document pXSLT, Map<String, ?> pParams) throws TransformerException
	  {
		  return transformar(pXML, pXSLT, pParams, null, Boolean.FALSE);
	  }

	  /**
	   * Metodo para transformar a bytes
	   * @param archivoXml
	   * @param plantillaXsl
	   * @return
	   * @throws TransformerException
	   */
	  public static byte[] transformarABytes(InputStream archivoXml, InputStream plantillaXsl) throws TransformerException
	  {
		  try
		  {
			  ByteArrayOutputStream out = new ByteArrayOutputStream();
	
			  TransformerFactory factory = TransformerFactory.newInstance();
			  Transformer transformer = factory.newTransformer(new StreamSource(plantillaXsl));
	
			  Source src = new StreamSource(archivoXml);
	
			  Result res = new StreamResult(out);
	
			  transformer.transform(src, res);
			  return out.toByteArray();
		  } catch (Exception e) {
			  throw new TransformerException("No se pudo realizar la transformacion del archivo xml", e);
		  }
	  }

	  /**
	   * 
	   * @param plantillaHTML
	   * @return
	   */
	  public static String obtenerPlantillaHTML(String plantillaHTML)
	  {
		  String plantilla = null;
		  try
		  {
			  if ((plantillaHTML != null) && (plantillaHTML.trim().length() > 0))
			  {
				  File plantillaFile = new File(plantillaHTML);
	
				  if (plantillaFile.exists())
				  {
					  FileInputStream fileInStr = new FileInputStream(plantillaFile);
					  int fSize = (int)plantillaFile.length();
					  byte[] bfile = new byte[fSize];
					  int c = -1;
					  int i = 0;
					  while ((c = fileInStr.read()) != -1) {
						  bfile[i] = (byte)c;
						  i++;
					  }
	
					  plantilla = new String(bfile);
	
					  fileInStr.close();
				  }
			  }
		  }
		  catch (Exception ex)
		  {
		  }
	
		  return plantilla;
	  }

	  /**
	   * 
	   * @param pString
	   * @return
	   * @throws ERPException
	   */
	  public static Document stringToXML(String pString)  throws ERPException
	  {
		  Document doc = null;
		  SAXBuilder parser = new SAXBuilder();
		  try {
			  doc = parser.build(new StringReader(pString));
		  } catch (JDOMException e) {
			  throw new ERPException("Error al tratar de convertir un string xml a Document", e);
		  } catch (IOException e) {
			  throw new ERPException("Error al tratar de convertir un string xml a Document", e);
		  }
		  return doc;
	  }

	  /**
	   * 
	   * @param pString
	   * @return
	   * @throws Exception
	   */
	  public static Document stringToXMLDocument(String pString)  throws Exception
	  {
		  Document doc = null;
		  SAXBuilder parser = new SAXBuilder();
		  try {
			  doc = parser.build(new StringReader(pString));
		  } catch (JDOMException e) {
			  throw e;
		  } catch (IOException e) {
			  throw e;
		  }
		  return doc;
	  }

	  /**
	   * 
	   * @param pXML
	   * @param pXSLT
	   * @return
	   * @throws TransformerException
	   */
	  public static Document transformar(Document pXML, Document pXSLT) throws TransformerException
	  {
		  return transformar(pXML, pXSLT, null, null, Boolean.FALSE);
	  }

	  /**
	   * Vaildar documentos
	   * @param contenidoXML
	   * @param pathXSD
	   * @throws TransformerException
	   */
	  public static void validaDocumentoXML(String contenidoXML, String pathXSD) throws TransformerException
	  {
		  try
		  {
			  SchemaFactory factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			  Schema schema = factory.newSchema(new StreamSource(pathXSD));
			  Validator validator = schema.newValidator();
			  Source s = new StreamSource(new StringReader(contenidoXML));
			  validator.validate(s);
		  } catch (SAXException ex) {
			  throw new TransformerException(ex);
		  } catch (IOException e) {
			  throw new TransformerException(e);
		  }
	  }

	  /**
	   * Metodo para transformar documentos
	   * @param pXML
	   * @param pXSLT
	   * @param pParams
	   * @param xsltCabecera
	   * @return
	   * @throws TransformerException
	   */
	  public static Document transformar(Document pXML, Document pXSLT, Map<String, ?> pParams, String xsltCabecera) throws TransformerException
	  {
		  return transformar(pXML, pXSLT, pParams, xsltCabecera, Boolean.FALSE);
	  }

	  /**
	   * Metodo para transformar documentos
	   * @param pXML
	   * @param pXSLT
	   * @param pParams
	   * @param xsltCabecera
	   * @param splitLimitIBM
	   * @return
	   * @throws TransformerException
	   */
	  public static Document transformar(Document pXML, Document pXSLT, Map<String, ?> pParams, String xsltCabecera, Boolean splitLimitIBM) throws TransformerException
	  {
		  return transformar(pXML, pXSLT, pParams, xsltCabecera, splitLimitIBM, Integer.valueOf(2000));
	  }

	  /**
	   * Metodo para transformar documentos
	   * @param pXML
	   * @param pXSLT
	   * @param pParams
	   * @param xsltCabecera
	   * @param splitLimitIBM
	   * @param splitSize
	   * @return
	   * @throws TransformerException
	   */
	  public static Document transformar(Document pXML, Document pXSLT, Map<String, ?> pParams, String xsltCabecera, Boolean splitLimitIBM, Integer splitSize) throws TransformerException
	  {
		  JDOMSource xsltSource = new JDOMSource(pXSLT);
		  TransformerFactory transFact = TransformerFactory.newInstance();

		  if ((splitLimitIBM != null) && (splitLimitIBM.booleanValue())) {
			  transFact.setAttribute("http://www.ibm.com/xmlns/prod/xltxe-j/split-limit", splitSize);
		  }
		  if (xsltCabecera != null) {
			  transFact.setURIResolver(new UrlResolver(xsltCabecera));
		  }
		  Transformer trans = transFact.newTransformer(xsltSource);
		  if ((pParams != null) && (!pParams.isEmpty())) {
			  Set<String> keys = pParams.keySet();
			  for (String key : keys) {
				  trans.setParameter(key, pParams.get(key));
			  }
		  }
		  JDOMSource docIn = new JDOMSource(pXML);
		  JDOMResult docOut = new JDOMResult();
		  trans.setOutputProperty("indent", "no");
		  trans.setOutputProperty("encoding", "UTF-8");
		  trans.transform(docIn, docOut);
		  return docOut.getDocument();
	  }
}