package at.tugraz.recipro.ws;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Recipe;
import at.tugraz.recipro.helper.ResourceAccessHelper;
import at.tugraz.recipro.recipro.R;

public abstract class WSConnection {
    public static String backend_url = ResourceAccessHelper.getStringFromId(R.string.connect_url);
    public static String recipe_path = ResourceAccessHelper.getStringFromId(R.string.connect_path_recipes);
    public static String image_path = ResourceAccessHelper.getStringFromId(R.string.connect_path_image);

    public static List<Recipe> sendQuery(Map<String, String> queryParams) throws RestClientException {
        Uri.Builder uriBuilder = Uri.parse(backend_url)
                .buildUpon()
                .appendPath(recipe_path);

        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            uriBuilder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        String uri = uriBuilder.build().toString();

        Log.d("RECIPES", "REQUEST URL: " + uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new GsonHttpMessageConverter());

        ResponseEntity<Recipe[]> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Recipe[].class);
        Log.d("RECIPES", "RESPONSE STATUS CODE: " + response.getStatusCode());
        Log.d("WSCONNECTION", "Returned: " + response.getBody().length);

        return Arrays.asList(response.getBody());
    }

    public static boolean postImage(long recipeId, byte[] image, ImageType imageType) {
        String uri = Uri.parse(backend_url)
                .buildUpon()
                .appendPath(recipe_path)
                .appendPath(Long.toString(recipeId))
                .appendPath(image_path)
                .build()
                .toString();

        Log.d("RECIPES", "POST IMAGE URL: " + uri);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(imageType == ImageType.JPEG ? MediaType.IMAGE_JPEG : MediaType.IMAGE_PNG);
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(image, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        ResponseEntity<Void> response = restTemplate.exchange(uri, HttpMethod.POST, entity, Void.class);

        Log.d("RECIPES", "RESPONSE STATUS CODE: " + response.getStatusCode());
        Log.d("RECIPES", "CREATED URL: " + response.getHeaders().getFirst("location"));

        if (response.getStatusCode() == HttpStatus.CREATED)
            return true;

        return false;
    }

    public static Bitmap getImage(long recipeId) {
        String uri = Uri.parse(backend_url)
                .buildUpon()
                .appendPath(recipe_path)
                .appendPath(Long.toString(recipeId))
                .appendPath(image_path)
                .build()
                .toString();

        Log.d("RECIPES", "GET IMAGE URL: " + uri);

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());

        try {
            ResponseEntity<Resource> response = restTemplate.exchange(uri, HttpMethod.GET, entity, Resource.class);
            Log.d("RECIPES", "RESPONSE STATUS CODE: " + response.getStatusCode());

            InputStream responseInputStream;
            responseInputStream = response.getBody().getInputStream();

            return BitmapFactory.decodeStream(responseInputStream);

        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
                Log.d("RECIPES", "IMAGE NOT FOUND");
                return null;
            }
            throw ex;

        } catch (IOException ex) {
            Log.d("RECIPES", "COULD NOT DECODE IMAGE");
            return null;
        }
    }
}
