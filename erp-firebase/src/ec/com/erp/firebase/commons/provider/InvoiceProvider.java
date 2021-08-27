package ec.com.erp.firebase.commons.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.com.erp.firebase.commons.constantes.ProviderConstant;
import ec.com.erp.firebase.model.HeadInvoiceUpdate;
import ec.com.erp.firebase.model.Invoice;
import ec.com.erp.firebase.model.InvoiceUpdate;

public class InvoiceProvider {
	
	private static DatabaseReference database;

	public static void main(String [] args) throws InterruptedException, ExecutionException, IOException {
		Collection<Invoice> facturasFireBase = obtainInvoicesFirebase();
		Collection<InvoiceUpdate> facturasActualizar = new ArrayList<>();
		facturasFireBase.stream().forEach(facturaApp -> {
			if(facturaApp.getHeader().getNumberDocument().equals("001-01040")){
				InvoiceUpdate invoiceUpdate = crearFacturaActualizar(facturaApp, "2020-12-28");
				facturaApp.getHeader().setPaidOut(Boolean.TRUE);
				facturasActualizar.add(invoiceUpdate);
			}
		});
		// Update invoices
		InvoiceProvider.updateInvoiceAux(facturasActualizar);
	}
	
	public static InvoiceUpdate crearFacturaActualizar(Invoice invoice, String dateDocument){
		InvoiceUpdate invoiceUpdate = new InvoiceUpdate();
		invoiceUpdate.setDetails(invoice.getDetails());
		invoiceUpdate.setHeader(new HeadInvoiceUpdate());
		invoiceUpdate.getHeader().setIdInvoice(invoice.getHeader().getIdInvoice());
		invoiceUpdate.getHeader().setClientDirection(invoice.getHeader().getClientDirection());
		invoiceUpdate.getHeader().setClientDocument(invoice.getHeader().getClientDocument()); 
		invoiceUpdate.getHeader().setClientName(invoice.getHeader().getClientName()); 
		invoiceUpdate.getHeader().setClientPhone(invoice.getHeader().getClientPhone()); 
		invoiceUpdate.getHeader().setDateDocument(dateDocument); 
		invoiceUpdate.getHeader().setDiscount(invoice.getHeader().getDiscount()); 
		invoiceUpdate.getHeader().setNumberDocument(invoice.getHeader().getNumberDocument());
		invoiceUpdate.getHeader().setPaidOut(invoice.getHeader().getPaidOut());
		invoiceUpdate.getHeader().setSubTotal(invoice.getHeader().getSubTotal());
		invoiceUpdate.getHeader().setTotalInvoice(invoice.getHeader().getTotalInvoice());
		invoiceUpdate.getHeader().setTotalIva(invoice.getHeader().getTotalIva());
		invoiceUpdate.getHeader().setTotalNotTax(invoice.getHeader().getTotalNotTax());
		invoiceUpdate.getHeader().setTotalTax(invoice.getHeader().getTotalTax());
		invoiceUpdate.getHeader().setTypeDocumentCode(invoice.getHeader().getTypeDocumentCode());
		invoiceUpdate.getHeader().setUserId(invoice.getHeader().getUserId());
		invoiceUpdate.getHeader().setSeller(invoice.getHeader().getSeller());
		invoiceUpdate.getHeader().setValueDocumentCode(invoice.getHeader().getValueDocumentCode());
		return invoiceUpdate;
	}
	
	/**
	 * Method for get the list of invoices of fire base data.
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Collection<Invoice> obtainInvoicesFirebase() throws InterruptedException, ExecutionException{
		
		Collection<Invoice> invoicesFireBase = new ArrayList<>();
		try {
			
			String token = FireBaseUtil.getTokenAccesFirebase();
			System.out.println("Token: "+token);
			
    	    String invoices = FireBaseUtil.getDataBaseResults(token, ProviderConstant.URL_GET_INVOICES);
    	    System.out.println("success");
    	    System.out.println("Resp:"+invoices);
    	    
    	    Type listType = new TypeToken<ArrayList<Invoice>>(){}.getType();
    	    Gson g = new Gson();
    	    invoicesFireBase = g.fromJson(invoices, listType);
    	    	
		 } catch (IOException e) {
            e.printStackTrace();
	     }
		return invoicesFireBase;
	}
	
	/**
	 * Method for update to invoices.
	 * @param invoiceCols The data of invoices
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public static void updateInvoice(Collection<Invoice> invoiceCols) throws InterruptedException, ExecutionException, IOException{
		initialiceFireBase();
		for(Invoice invoice: invoiceCols){
			database.child("Invoices").child(""+invoice.getHeader().getIdInvoice()).setValueAsync(invoice).get();
		}
	}
	
	/**
	 * Method for update to invoices.
	 * @param invoiceCols The data of invoices
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public static void updateInvoiceAux(Collection<InvoiceUpdate> invoiceCols) throws InterruptedException, ExecutionException, IOException{
		initialiceFireBase();
		for(InvoiceUpdate invoice: invoiceCols){
			database.child("Invoices").child(""+invoice.getHeader().getIdInvoice()).setValueAsync(invoice).get();
		}
	}
	
	/**
	 * Metodo para inicializar la coneccion con la base de datos de fire base
	 * @throws IOException
	 */
	public static void initialiceFireBase() throws IOException {
		// [BEGIN initialize]
		if(FirebaseApp.getApps().size() == 0) {
			FileInputStream serviceFire = new FileInputStream(ProviderConstant.PATH_FILE_AUTH);
	        FirebaseOptions options = new FirebaseOptions.Builder()
	                .setCredentials(GoogleCredentials.fromStream(serviceFire))
	                .setDatabaseUrl(ProviderConstant.URL_DATA_BASE)
	                .build();
	        FirebaseApp.initializeApp(options);
	        // [END initialize]
		}
		
		if(database == null) {
			 // Shared Database reference
	        database = FirebaseDatabase.getInstance().getReference();
		}
	}
}
