package ec.com.erp.firebase.commons.constantes;

public class ProviderConstant {
	
	private static final ProviderConstant INSTANCIA = new ProviderConstant();
	
	public static ProviderConstant getInstancia(){
		return INSTANCIA;
	}
	
	public final static String PATH_FILE_AUTH = "C:\\ErpLibreries\\mobileinvoice-55cf6-firebase-adminsdk-r515l-480cc09379.json";
	public final static String URL_DATA_BASE = "https://mobileinvoice-55cf6.firebaseio.com/";
	public final static String URL_GET_CLIENTS = "https://mobileinvoice-55cf6.firebaseio.com/Clients.json";
	public final static String URL_GET_SEQUENCE = "https://mobileinvoice-55cf6.firebaseio.com/Sequence.json";
	public final static String URL_GET_ITEMS = "https://mobileinvoice-55cf6.firebaseio.com/Items.json";
	public final static String URL_GET_INVOICES = "https://mobileinvoice-55cf6.firebaseio.com/Invoices.json";
	public final static String URL_GET_ORDERS = "https://mobileinvoice-55cf6.firebaseio.com/Orders.json";
}
