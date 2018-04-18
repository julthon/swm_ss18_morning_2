/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.math.BigDecimal;
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
import org.junit.Rule;

/**
 *
 * @author Dominik
 */
public class RecipesResourceIT {
    
    @Rule
    public JAXRSClientProvider provider = JAXRSClientProvider.buildWithURI("http://localhost:8080/recipro-backend/api/recipes");
    
    /*private Client client;
    private WebTarget target;
    
    @Before
    public void initClient() {
        this.client = ClientBuilder.newClient();
        this.target = this.client.target("http://localhost:8080/recipro-backend/api/recipes");
    }
    */
    
    @Test
    public void createRecipes() {
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Erdbeerkuchen")
                .add("preparationTime", 120)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Best recipe ever.")
                .build();
        
        Response response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
        
        recipeToCreate = recipeBuilder
                .add("title", "Kuchen")
                .add("preparationTime", 140)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Best recipe ever.")
                .build();
        
        response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
        
        recipeToCreate = recipeBuilder
                .add("title", "Torte")
                .add("preparationTime", 60)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Best recipe ever.")
                .build();
        
        response = this.provider.target().request(MediaType.APPLICATION_JSON).post(Entity.json(recipeToCreate));
        assertThat(response.getStatus(), is(204));
    }
    
    
    @Test
    public void findRecipeById(){
        int id = 1;
        
        Response response = this.provider.target()
                .path(Integer.toString(id))
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));

        JsonObject payload = response.readEntity(JsonObject.class);
        System.out.println("findRecipeById payload " + payload);
        
        assertThat(payload.getInt("id"), is(1));
        assertThat(payload.getString("title"), is("Erdbeerkuchen"));
        assertThat(payload.getInt("preparationTime"), is(120));
        assertThat(payload.getString("description"), is("Best recipe ever."));
        assertThat(payload.getJsonArray("recipeTypes").size(), is(2));
        assertThat(payload.getJsonArray("recipeTypes").getJsonString(1), is("DESSERT"));
        assertThat(payload.getJsonArray("recipeTypes").getJsonString(2), is("SNACK"));


    }
    
    @Test
    public void findRecipesByTitleAndType(){
        JsonObjectBuilder findQueryBuilder = Json.createObjectBuilder();
        JsonObject findQuery = findQueryBuilder
                .add("title", "Kuchen")
                .add("type", "dessert")
                .build();
        
        Response response = this.provider.target()
                .path("find")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(findQuery));
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("findRecipesByTitleAndType payload " + payload);
        
        assertThat(payload.size(), is(2));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("title").contains("kuchen")));                


    }
    //{"title": "cake", "type": "dessert", "time_to_prepare": "45", "min_rating": "3"}
    
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
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getInt("id") > 0));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getInt("preparationTime") > 0));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("description") != null));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("title") != null));
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getJsonArray("recipeTypes") != null));

    }
}
