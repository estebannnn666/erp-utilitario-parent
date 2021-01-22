package ec.com.erp.firebase.commons.provider;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.com.erp.firebase.commons.constantes.ProviderConstant;
import ec.com.erp.firebase.model.ImageItem;
import ec.com.erp.firebase.model.Item;

public class ItemProvider {
	
	private static DatabaseReference database;

	public static void main(String [] args) throws InterruptedException, ExecutionException, IOException {
		obtainImagesItemFirebase();
	}
	
	/**
	 * Method for get the list of items of fire base data.
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Collection<Item> obtainItemFirebase() throws InterruptedException, ExecutionException{
		
		Collection<Item> itemsFireBase = new ArrayList<>();
		try {
			String token = FireBaseUtil.getTokenAccesFirebase();
    	    String items = FireBaseUtil.getDataBaseResults(token, ProviderConstant.URL_GET_ITEMS);
    	    System.out.println("success");
    	    Type listType = new TypeToken<ArrayList<Item>>(){}.getType();
    	    Gson g = new Gson();
    	    itemsFireBase = g.fromJson(items, listType);
		 } catch (IOException e) {
            e.printStackTrace();
	     }
		return itemsFireBase;
	}
	
	/**
	 * Method for get the list of image items of fire base data.
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public static Collection<ImageItem> obtainImagesItemFirebase() throws InterruptedException, ExecutionException{
		Collection<ImageItem> itemsFireBase = new ArrayList<>();
		try {
			String token = FireBaseUtil.getTokenAccesFirebase();
    	    String items = FireBaseUtil.getDataBaseResults(token, ProviderConstant.URL_GET_IMAGES);
			String typeDowload = items.substring(0, 1);
			
			if(typeDowload.equals(ProviderConstant.CHARACTER_KEYS)) {
		        // convert JSON string to Map
	    	    ObjectMapper mapper = new ObjectMapper();
	            Map<String, ImageItem> map = mapper.readValue(items, new TypeReference<Map<String, ImageItem>>() {});
	            for(Entry<String, ImageItem> valor: map.entrySet()){
	            	ImageItem imageItem = new ImageItem();
	            	imageItem.setId(valor.getValue().getId());
	            	imageItem.setBarCode(valor.getValue().getBarCode());
	            	imageItem.setImage(valor.getValue().getImage());
	            	itemsFireBase.add(imageItem);
	            }
            }else {
	    	    Type listType = new TypeToken<ArrayList<ImageItem>>(){}.getType();
	    	    Gson g = new Gson();
	    	    itemsFireBase = g.fromJson(items, listType);
            }
		 } catch (Exception e) {
            e.printStackTrace();
	     }
		return itemsFireBase;
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
	 * Method for create or update to items.
	 * @param client The data of client
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public static void createUpdateItem(Collection<Item> itemCols) throws InterruptedException, ExecutionException, IOException{
		initialiceFireBase();
		for(Item item: itemCols){
			database.child("Items").child(""+item.getDataItem().getId()).setValueAsync(item).get();
		}
	}
}
