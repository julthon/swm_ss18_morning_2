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
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
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
    public void createAndFindRecipeById(){
        
        String title = "Bananenkuchen";
        String description = "Best recipe ever.";
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", title)
                .add("preparationTime", 120)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        String location = response.getHeaderString("Location");
        System.out.println("findRecipeById location " + location);
        
        response = this.provider
                .target(location)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));

        JsonObject payload = response.readEntity(JsonObject.class);
        System.out.println("findRecipeById payload " + payload);

        int id = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));
        
        assertThat(payload.getInt("id"), is(id));
        assertThat(payload.getString("title"), is(title));
        assertThat(payload.getInt("preparationTime"), is(120));
        assertThat(payload.getString("description"), is(description));
        assertThat(payload.getJsonArray("recipeTypes").size(), is(2));
        assertThat(payload.getJsonArray("recipeTypes"), is(recipeTypesToCreate));
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
