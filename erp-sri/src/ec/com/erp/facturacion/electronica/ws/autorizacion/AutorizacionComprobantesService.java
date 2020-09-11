package ec.com.erp.facturacion.electronica.ws.autorizacion;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

/**
 * This class was generated by Apache CXF 3.1.4
 * 2017-10-14T21:18:33.227-05:00
 * Generated source version: 3.1.4
 * 
 */
@WebServiceClient(name = "AutorizacionComprobantesService", 
                  wsdlLocation = "META-INF/wsdl/AutorizacionComprobantes.wsdl",
                  targetNamespace = "http://ec.gob.sri.ws.autorizacion") 
public class AutorizacionComprobantesService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesService");
    public final static QName AutorizacionComprobantesPort = new QName("http://ec.gob.sri.ws.autorizacion", "AutorizacionComprobantesPort");
    static {
        URL url = null;
        try {
        	URL baseUrl = AutorizacionComprobantesService.class.getClassLoader().getResource(".");
			url = new URL(baseUrl, "META-INF/wsdl/AutorizacionComprobantes.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(AutorizacionComprobantesService.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:AutorizacionComprobantes.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public AutorizacionComprobantesService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public AutorizacionComprobantesService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public AutorizacionComprobantesService() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    public AutorizacionComprobantesService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public AutorizacionComprobantesService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public AutorizacionComprobantesService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }    




    /**
     *
     * @return
     *     returns AutorizacionComprobantes
     */
    @WebEndpoint(name = "AutorizacionComprobantesPort")
    public AutorizacionComprobantes getAutorizacionComprobantesPort() {
        return super.getPort(AutorizacionComprobantesPort, AutorizacionComprobantes.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns AutorizacionComprobantes
     */
    @WebEndpoint(name = "AutorizacionComprobantesPort")
    public AutorizacionComprobantes getAutorizacionComprobantesPort(WebServiceFeature... features) {
        return super.getPort(AutorizacionComprobantesPort, AutorizacionComprobantes.class, features);
    }

}
