package at.tugraz.recipro.recipro;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import at.tugraz.recipro.data.Recipe;

class WSConnection {

    private static final String BACKEND_URL = "http://10.0.2.2:8080/recipro-backend/api";

    public static List<Recipe> sendQuery(Map<String, String> queryParams) {
        String url = BACKEND_URL + "/recipes";

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
}
