/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.runners.MethodSorters;

/**
 *
 * @author Dominik
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RecipesResourceIT {
    
    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://localhost:8080/recipro-backend/api/recipes");
    
    /*private Client client;
    private WebTarget target;*/
    
    @Test
    public void _createRecipes() {
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .build();
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Erdbeerkuchen")
                .add("preparationTime", 120)
                .add("recipeTypes", recipeTypeBuilder.add("DESSERT").build())
                .build();
        
        Response response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
        
        recipeToCreate = recipeBuilder
                .add("title", "Kuchen")
                .add("preparationTime", 140)
                .add("recipeTypes", recipeTypeBuilder.add("SNACK").build())
                .build();
        
        response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
        
        recipeToCreate = recipeBuilder
                .add("title", "Torte")
                .add("preparationTime", 60)
                .add("recipeTypes", recipeTypeBuilder.add("MAIN_COURSE").build())
                .build();
        
        response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
    }
    
    @Test
    public void findRecipesByTitle() {
        String title = "Kuchen";
        
        Response response = this.provider.target()
                .path(title)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
         
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("findRecipesByTitle payload " + payload);
        
        assertThat(payload.size(), is(2));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("title").toLowerCase().contains(title.toLowerCase())));
    }
    
    @Test
    public void findAllRecipes() {
        Response response = this.provider.target()
                .path("all")
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("findAllRecipes payload " + payload);
        
        assertThat(payload.size(), is(3));
    }
    
    @Test
    public void filterByTitle() {
        Response response = this.provider.target()
                .path("filter")
                .queryParam("title", "Torte")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByTitle payload " + payload);
        
        assertThat(payload.size(), is(1));
        JsonObject obj = payload.getJsonObject(0);
        assertEquals(obj.getString("title"), "Torte");
    }
    
    @Test
    public void filterPrepTime() {
        Response response = this.provider.target()
                .path("filter")
                .queryParam("minpreptime", "61")
                .queryParam("maxpreptime", "139")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterPrepTime payload " + payload);
        
        assertThat(payload.size(), is(1));
    }
    
    @Test
    public void filterByType() {
        Response response = this.provider.target()
                .path("filter")
                .queryParam("types", "MAIN_COURSE")
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByType payload " + payload);
        
        assertThat(payload.size(), is(1));
        JsonObject obj = payload.getJsonObject(0);
        assertEquals(obj.getString("title"), "Torte");
    }
}
