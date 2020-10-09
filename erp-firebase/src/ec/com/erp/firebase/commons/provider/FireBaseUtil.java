package ec.com.erp.firebase.commons.provider;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import com.google.auth.oauth2.GoogleCredentials;

import ec.com.erp.firebase.commons.constantes.ProviderConstant;

public class FireBaseUtil {
	
	/**
	 * Method for get token authentication in fire base
	 * @return
	 */
	public static String getTokenAccesFirebase() {
		String token = "";
		try {
			// Load the service account key JSON file
			FileInputStream serviceAccount = new FileInputStream(ProviderConstant.PATH_FILE_AUTH);
	
			// Authenticate a Google credential with the service account
			GoogleCredentials googleCred = GoogleCredentials.fromStream(serviceAccount);
	
			// Add the required scopes to the Google credential
			Collection<String> links = new ArrayList<>();
			links.add("https://www.googleapis.com/auth/firebase.database");
			links.add("https://www.googleapis.com/auth/userinfo.email");
			GoogleCredentials scoped = googleCred.createScoped(links);
			// Use the Google credential to generate an access token
			token = scoped.refreshAccessToken().getTokenValue();
		} catch (IOException e) {
            e.printStackTrace();
	    }
		return token;
	}
	
	/**
	 * Method for get head with authorization
	 * @param token
	 * @param urlDataBase
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection getCommonConnection(String token, String urlDataBase) throws IOException {
	    URL url = new URL(urlDataBase);
	    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
	    httpURLConnection.setRequestProperty("Authorization", "Bearer " + token);
	    httpURLConnection.setRequestProperty("Content-Type", "application/json; UTF-8");
	    return httpURLConnection;
	}
	
	/**
	 * Method for get the result ws in string
	 * @param token
	 * @param urlDataBase
	 * @return
	 * @throws IOException
	 */
	public static String getDataBaseResults(String token, String urlDataBase) throws IOException {
		HttpURLConnection connection = getCommonConnection(token, urlDataBase);
		connection.setRequestMethod("GET");
	    int responseCode = connection.getResponseCode();
	    
	    
	    // handle error response code it occurs
	    InputStream inputStream;
	    if (200 <= responseCode && responseCode <= 299) {
	        inputStream = connection.getInputStream();
	    } else {
	        inputStream = connection.getErrorStream();
	    }

	    BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));

	    StringBuilder response = new StringBuilder();
	    String currentLine;

	    while ((currentLine = in.readLine()) != null) 
	        response.append(currentLine);

	    in.close();

	    return response.toString();
	}
}
