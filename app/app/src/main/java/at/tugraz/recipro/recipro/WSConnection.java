package at.tugraz.recipro.recipro;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.helper.ResourceAccessHelper;

class WSConnection {
    public static String backend_url = ResourceAccessHelper.getStringFromId(R.string.connect_url);
    public static String backend_path = ResourceAccessHelper.getStringFromId(R.string.connect_path_recipes);

    public static List<Recipe> sendQuery(Map<String, String> queryParams) throws RestClientException {
        String url = backend_url + backend_path;

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        for(Map.Entry<String, String> entry : queryParams.entrySet()){
            uriBuilder = uriBuilder
                    .queryParam(entry.getKey(), entry.getValue());
        }

        Log.d("RECIPES", "REQUEST URL: " + uriBuilder.build().toUriString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        ResponseEntity<Recipe[]> response = restTemplate.exchange(uriBuilder.build().toUriString(), HttpMethod.GET, entity, Recipe[].class);
        Log.d("RECIPES", "RESPONSE STATUS CODE: " + response.getStatusCode());
        Log.d("WSCONNECTION", "Returned: " + response.getBody().length);

        return Arrays.asList(response.getBody());
    }

    static public void postImage(int recipeId)
    {
        String urlString = backend_url + backend_path + "/" + recipeId + "/image";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(urlString);
        ResponseEntity<Recipe[]> response = restTemplate.exchange(uriBuilder.build().toUriString(), HttpMethod.POST, entity, Recipe[].class);
    }


    public Bitmap getImage(int recipeId)
    {
        String urlString = backend_url + backend_path + "/" + recipeId + "/image";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap imageBitmap = BitmapFactory.decodeStream(input);
            return imageBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
