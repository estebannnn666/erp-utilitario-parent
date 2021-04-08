package ec.com.erp.firebase.commons.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import ec.com.erp.firebase.commons.constantes.ProviderConstant;
import ec.com.erp.firebase.model.Client;

public class ClientProvider {
	
	private static DatabaseReference database;

//	public static void main(String [] args) throws InterruptedException, ExecutionException, IOException {
//		updateSequence("client", "4");
//	}
	/**
	 * Method for get the list of clients of fire base data.
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	@SuppressWarnings("deprecation")
	public static Collection<Client> obtainClientFirebase() throws InterruptedException, ExecutionException{
		
		Collection<Client> clientsFireBase = new ArrayList<>();
		try {
			
			String token = FireBaseUtil.getTokenAccesFirebase();
			System.out.println("Token: "+token);
			
    	    String clientes = FireBaseUtil.getDataBaseResults(token, ProviderConstant.URL_GET_CLIENTS);
    	    System.out.println("success");
    	    System.out.println("Resp:"+clientes);
    	    	
    	    JsonParser parserJson = new JsonParser();  
    	    JsonElement rootNode = parserJson.parse(clientes);  
    	    if (rootNode.isJsonArray()) { 
    	    	JsonArray marks = rootNode.getAsJsonArray();
    	    	Client client;
    	    	for (int i = 0; i < marks.size(); i++) { 
    	            JsonObject value = marks.get(i).getAsJsonObject(); 
    	            client = new Client();
    	            client.setId(value.get("id").getAsInt());
    	            client.setAddress(value.get("address").getAsString());
    	            client.setBuyType(value.get("buyType").getAsString());
    	            client.setCity(value.get("city").getAsString());
    	            client.setDocument(value.get("document").getAsString());
    	            client.setEmail(value.get("email") != null ? value.get("email").getAsString() : null);
    	            client.setName(value.get("name").getAsString());
    	            client.setFirstName(value.get("firstName") != null ? value.get("firstName").getAsString() : null);
    	            client.setSecondName(value.get("secondName") != null ? value.get("secondName").getAsString() : null);
    	            client.setFirstLastName(value.get("firstLastName") != null ? value.get("firstLastName").getAsString() : null);
    	            client.setSecondLastName(value.get("secondLastName") != null ? value.get("secondLastName").getAsString() : null);
    	            client.setTelephone(value.get("telephone").getAsString());
    	            client.setType(value.get("type").getAsString());
    	            client.setUserId(value.get("userId") != null ? value.get("userId").getAsString() : null);
    	            client.setZoneValueCode(value.get("zoneValueCode") != null ? value.get("zoneValueCode").getAsString() : null);
    	            client.setZoneTypeCode(value.get("zoneTypeCode") != null ? value.get("zoneTypeCode").getAsInt() : null);
    	            clientsFireBase.add(client);
    	        } 
    	    }
		 } catch (IOException e) {
            e.printStackTrace();
	     }
		return clientsFireBase;
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
	
	/**
	 * Method for create or update to client.
	 * @param client The data of client
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public static void createUpdateClient(Collection<Client> clientCols) throws InterruptedException, ExecutionException, IOException{
		initialiceFireBase();
		for(Client client: clientCols){
			database.child("Clients").child(""+client.getId()).setValueAsync(client).get();
		}
	}
}
