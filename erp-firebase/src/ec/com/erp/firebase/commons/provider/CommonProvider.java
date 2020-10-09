package ec.com.erp.firebase.commons.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import ec.com.erp.firebase.commons.constantes.ProviderConstant;
import ec.com.erp.firebase.model.Sequence;

public class CommonProvider {
	
	private static DatabaseReference database;
	
	/**
	 * Method for get the list of sequences
	 * @param nameSequence
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Sequence obtenerSecuencial() throws InterruptedException, ExecutionException{
		Sequence sequence = null;
	    try {
	    	String token = FireBaseUtil.getTokenAccesFirebase();
			String secuenciales = FireBaseUtil.getDataBaseResults(token, ProviderConstant.URL_GET_SEQUENCE);
			Gson g = new Gson();
			sequence = g.fromJson(secuenciales, Sequence.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return sequence; 
	}
	
	public static void updateSequence(String nameSequence, String value) throws InterruptedException, ExecutionException, IOException{
		initialiceFireBase();
		database.child("Sequence").child(nameSequence).setValueAsync(value).get();
	}
	
	
	/**
	 * Metodo para inicializar la coneccion con la base de datos de fire base
	 * @throws IOException
	 */
	public static void initialiceFireBase() throws IOException {
		// [BEGIN initialize]
		if(FirebaseApp.getApps().size() == 0){
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
