package at.tugraz.recipro.recipes.boundary;

import com.airhacks.rulz.jaxrsclient.JAXRSClientProvider;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import javax.json.*;
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

    
    @Rule
    public JAXRSClientProvider allergensProvider = JAXRSClientProvider.buildWithURI("http://localhost:8080/recipro-backend/api/allergens");


    @Test
    public void createAndFindRecipeById(){
        String title = "Bananenkuchen";
        String description = "Best recipe ever.";
        double rating = 3.7; 
        int preparationTime = 120;
        int servings = 4;
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject milk = recipeBuilder
                .add("name", "Milk")
                .build();
        
        JsonObject flour = recipeBuilder
                .add("name", "Flour")
                .build();
        
        JsonObject ingredient_milk = recipeBuilder
                .add("ingredient", milk)
                .add("quantity", "2000")
                .add("unit", "MILLILITER")
                .build();
        
        JsonObject ingredient_flour = recipeBuilder
                .add("ingredient", flour)
                .add("quantity", "500")
                .add("unit", "GRAM")
                .build();
        
        JsonArrayBuilder ingredientsListBuilder = Json.createArrayBuilder();
        JsonArray ingredients = ingredientsListBuilder
                .add(ingredient_milk)
                .add(ingredient_flour)
                .build();
        
        recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", title)
                .add("preparationTime", preparationTime)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .add("ingredients", ingredients)
                .add("rating", rating)
                .add("servings", servings)
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
        assertThat(payload.getInt("preparationTime"), is(preparationTime));
        assertThat(payload.getString("description"), is(description));
        assertThat(payload.getJsonArray("recipeTypes").size(), is(2));
        assertThat(payload.getJsonArray("recipeTypes"), is(recipeTypesToCreate));
        assertThat(payload.getJsonArray("ingredients").size(), is(2));
        assertThat(payload.getJsonNumber("rating").doubleValue(), is(rating));
        assertThat(payload.getInt("servings"), is(servings));
    }
    
    @Test
    public void findAllRecipes() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Gulasch")
                .add("preparationTime", 235)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Schnell und einfach")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("findAllRecipes payload " + payload);
        
        assert(payload.size() > 0);
    }
    
    @Test
    public void filterByTitle() {
        String title = "Torte";
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Sachertorte")
                .add("preparationTime", 120)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Chocolate cake.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Erdbeertorte")
                .add("preparationTime", 180)
                .add("recipeTypes", recipeTypesToCreate)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Kaffeetorte")
                .add("preparationTime", 140)
                .add("recipeTypes", recipeTypesToCreate)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        
        response = this.provider.target()
                .queryParam("title", title.toLowerCase())
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByTitle payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getString("title").contains(title.toLowerCase()) 
                                                || ((JsonObject) x).getString("title").contains(title.toUpperCase())
                                                || ((JsonObject) x).getString("title").contains(title)));
    }
    
    @Test
    public void filterPrepTime() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("SNACK")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Butterbrot")
                .add("preparationTime", 5)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Chocolate cake.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));


        int minpreptime = 50;
        int maxpreptime = 200;
        response = this.provider.target()
                .queryParam("minpreptime", Integer.toString(minpreptime))
                .queryParam("maxpreptime", Integer.toString(maxpreptime))
                .request(MediaType.APPLICATION_JSON)
                .get();
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterPrepTime payload " + payload);
         
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getInt("preparationTime") >= minpreptime
                                            && ((JsonObject) x).getInt("preparationTime") <= maxpreptime));
    }
    
    @Test
    public void filterByType() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Schnitzel")
                .add("preparationTime", 100)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Very good.")
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Auflauf")
                .add("preparationTime", 40)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Nice.")
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        response = this.provider.target()
                .queryParam("types", "MAIN_COURSE")
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByType payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getJsonArray("recipeTypes").contains(recipeTypesToCreate.get(0))));
    }
    
    @Test
    public void filterByMinRating() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Schnitzel")
                .add("preparationTime", 100)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Very good.")
                .add("rating", 4)
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Auflauf")
                .add("preparationTime", 40)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Nice.")
                .add("rating", 2.7)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Torte")
                .add("preparationTime", 30)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Nice.")
                .add("rating", 5.0)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));        
        
        response = this.provider.target()
                .queryParam("minrating", 3)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByMinRating payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getJsonNumber("rating").doubleValue() >= 3));
        assert(payload.stream().anyMatch(x -> ((JsonObject) x).getJsonNumber("rating").doubleValue() == 5.0));
    }
    
    @Test
    public void filterByMaxRating() {
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Schnitzel")
                .add("preparationTime", 100)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Very good.")
                .add("rating", 4)
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Auflauf")
                .add("preparationTime", 40)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Nice.")
                .add("rating", 2.7)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        recipeToCreate = recipeBuilder
                .add("title", "Torte")
                .add("preparationTime", 60)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Awesome.")
                .add("rating", 0.0)
                .build();
        
        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        response = this.provider.target()
                .queryParam("maxrating", 3)
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByMaxRating payload " + payload);
        
        assert(payload.stream().allMatch(x -> ((JsonObject) x).getJsonNumber("rating").doubleValue() <= 3));
        assert(payload.stream().anyMatch(x -> ((JsonObject) x).getJsonNumber("rating").doubleValue() == 0));
    }
    
    @Test
    public void filterByAllergens() {
        Response allergenResponse = this.allergensProvider.target()
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        JsonArray allergens = allergenResponse.readEntity(JsonArray.class);
        JsonObject allergen = allergens.getJsonObject(0);
        JsonArrayBuilder allergensBuilder = Json.createArrayBuilder();
        for (int i = 1; i < allergens.size(); i++) {
            allergensBuilder.add(allergens.get(i));
        }
        allergens = allergensBuilder.build();


        String title = "Allergencake";
        String description = "Best allergens ever.";
        double rating = 4.7; 
        int preparationTime = 120;
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();
        
        JsonObjectBuilder ingredientBuilder = Json.createObjectBuilder();
        JsonObject milk = ingredientBuilder
                .add("name", "Milk")
                .add("allergens", allergens)
                .build();
        
        JsonObject flour = ingredientBuilder
                .add("name", "Flour")
                .build();
        
        JsonObject eggs = ingredientBuilder
                .add("name", "Eggs")
                .build();
        
        JsonObject ingredientMilk = ingredientBuilder
                .add("ingredient", milk)
                .add("quantity", "200")
                .add("unit", "MILLILITER")                
                .build();
        
        JsonObject ingredientFlour = ingredientBuilder
                .add("ingredient", flour)
                .add("quantity", "500")
                .add("unit", "GRAM")
                .build();
        
        JsonObject ingredientEggs = ingredientBuilder
                .add("ingredient", eggs)
                .add("quantity", "4")
                .add("unit", "NONE")
                .build();
        
        JsonArrayBuilder ingredientsListBuilder = Json.createArrayBuilder();
        JsonArray ingredients = ingredientsListBuilder
                .add(ingredientMilk)
                .add(ingredientFlour)
                .add(ingredientEggs)
                .build();
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", title)
                .add("preparationTime", preparationTime)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .add("ingredients", ingredients)
                .add("rating", rating)
                .build();
        
        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(response.getStatus(), is(201));
        
        String location = response.getHeaderString("Location");
        System.out.println("filterByAllergens location " + location);
        int id = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));
        
        response = this.provider.target()
                .queryParam("allergens", allergens.getJsonObject(0).getString("shortName"))
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        assertThat(response.getStatus(), is(200));
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterByAllergens payload " + payload);
        
        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id), is(false));

        response = this.provider.target()
                .queryParam("allergens", allergen.getString("shortName"))
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(200));

        payload = response.readEntity(JsonArray.class);
        System.out.println("filterByAllergens payload " + payload);

        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id), is(true));
    }

    @Test
    public void filterBySpecificIngredients() {
        String title1 = "IngredientcakeMilk";
        String title2 = "IngredientcakeFlour";
        String description = "Best ingredients ever.";
        double rating = 4.7;
        int preparationTime = 120;

        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("DESSERT")
                .add("SNACK")
                .build();

        JsonObjectBuilder ingredientBuilder = Json.createObjectBuilder();
        JsonObject milk = ingredientBuilder
                .add("name", "Milk")
                .build();

        JsonObject flour = ingredientBuilder
                .add("name", "Flour")
                .build();

        JsonObject eggs = ingredientBuilder
                .add("name", "Eggs")
                .build();

        JsonObject ingredientMilk = ingredientBuilder
                .add("ingredient", milk)
                .add("quantity", "200")
                .add("unit", "MILLILITER")
                .build();

        JsonObject ingredientFlour = ingredientBuilder
                .add("ingredient", flour)
                .add("quantity", "500")
                .add("unit", "GRAM")
                .build();

        JsonObject ingredientEggs = ingredientBuilder
                .add("ingredient", eggs)
                .add("quantity", "4")
                .add("unit", "NONE")
                .build();

        JsonArrayBuilder ingredientsListBuilder = Json.createArrayBuilder();
        JsonArray ingredientsJson1 = ingredientsListBuilder
                .add(ingredientMilk)
                .add(ingredientFlour)
                .build();

        JsonArray ingredientsJson2 = ingredientsListBuilder
                .add(ingredientEggs)
                .add(ingredientFlour)
                .build();

        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", title1)
                .add("preparationTime", preparationTime)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .add("ingredients", ingredientsJson1)
                .add("rating", rating)
                .build();

        Response response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));

        assertThat(response.getStatus(), is(201));

        String location = response.getHeaderString("Location");
        System.out.println("filterBySpecificIngredients location " + location);
        int id1 = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));

        recipeToCreate = recipeBuilder
                .add("title", title2)
                .add("preparationTime", preparationTime)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", description)
                .add("ingredients", ingredientsJson2)
                .add("rating", rating)
                .build();

        response = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));

        assertThat(response.getStatus(), is(201));

        location = response.getHeaderString("Location");
        System.out.println("filterBySpecificIngredients location " + location);
        int id2 = Integer.parseInt(location.substring(location.lastIndexOf('/') + 1));

        response = this.provider.target()
                .queryParam("ingredientsexclude", ingredientsJson1.getJsonObject(0).getJsonObject("ingredient").getString("name"))
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(200));

        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("filterBySpecificIngredients payload " + payload);

        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id1), is(false));
        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id2), is(true));

        response = this.provider.target()
                .queryParam("ingredientsinclude", ingredientsJson1.getJsonObject(0).getJsonObject("ingredient").getString("name"))
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertThat(response.getStatus(), is(200));

        payload = response.readEntity(JsonArray.class);
        System.out.println("filterBySpecificIngredients payload " + payload);

        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id1), is(true));
        assertThat(payload.stream().anyMatch(x -> ((JsonObject)x).getInt("id") == id2), is(false));
    }

    @Test
    public void getAllTypes() {
        Response response = this.provider.target()
                .path("types")
                .request(MediaType.APPLICATION_JSON)
                .get();
        
        JsonArray payload = response.readEntity(JsonArray.class);
        System.out.println("getAllTypes payload " + payload);
        assertThat(payload.size(), is(3));
    }
    
    @Test
    public void storeAndGetJpegImage() throws URISyntaxException {
        
        URL url = getClass().getClassLoader().getResource("test_image1.jpeg");
        assertNotNull(url);
        File image = new File(url.toURI());
        assert(image.exists());
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Taco")
                .add("preparationTime", 20)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Very taco.")
                .build();
        
        Response postRecipeResponse = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(postRecipeResponse.getStatus(), is(201));
        String recipeLocation = postRecipeResponse.getHeaderString("Location");
        
        Response postImageResponse = this.provider.target(recipeLocation)
          .path("/image")
          .request("image/jpeg")
          .post(Entity.entity(image, "image/jpeg"));
        
        assertThat(postImageResponse.getStatus(), is(201));
        String imageLocation = postImageResponse.getHeaderString("Location");
        System.out.println("storeAndGetImage location " + imageLocation);
        assertNotNull(imageLocation);
        
        Response getImageResponse = this.provider.target(imageLocation)
                .request("image/jpeg")
                .get();
        
        assertThat(getImageResponse.getStatus(), is(200));
        File payload = getImageResponse.readEntity(File.class); 
        assertThat(payload.length(), is(image.length()));
    }
    
    @Test
    public void getNonexistingImage() {
                
        int max = 5000;
        int min = 1000;
        int randomId = min + (int)(Math.random() * ((max - min) + 1));
        
        Response response = this.provider.target()
                .path(randomId + "/image")
                .request("image/jpeg")
                .get();
        
        assertThat(response.getStatus(), is(404));
    }
    
    @Test
    public void storeAndGetPngImage() throws URISyntaxException {
        
        URL url = getClass().getClassLoader().getResource("test_image2.png");
        assertNotNull(url);
        File image = new File(url.toURI());
        assert(image.exists());
        
        JsonArrayBuilder recipeTypeBuilder = Json.createArrayBuilder();
        JsonArray recipeTypesToCreate = recipeTypeBuilder
                .add("MAIN_COURSE")
                .build();       
        
        JsonObjectBuilder recipeBuilder = Json.createObjectBuilder();
        JsonObject recipeToCreate = recipeBuilder
                .add("title", "Vegetable Patty")
                .add("preparationTime", 30)
                .add("recipeTypes", recipeTypesToCreate)
                .add("description", "Kinda Patty.")
                .build();
        
        Response postRecipeResponse = this.provider.target()
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(recipeToCreate));
        
        assertThat(postRecipeResponse.getStatus(), is(201));
        String recipeLocation = postRecipeResponse.getHeaderString("Location");
        
        Response postImageResponse = this.provider.target(recipeLocation)
          .path("/image")
          .request("image/png")
          .post(Entity.entity(image, "image/png"));
        
        assertThat(postImageResponse.getStatus(), is(201));
        String imageLocation = postImageResponse.getHeaderString("Location");
        System.out.println("storeAndGetImage location " + imageLocation);
        assertNotNull(imageLocation);
        
        Response getImageResponse = this.provider.target(imageLocation)
                .request("image/png")
                .get();
        
        assertThat(getImageResponse.getStatus(), is(200));
        File payload = getImageResponse.readEntity(File.class); 
        assertThat(payload.length(), is(image.length()));
    }
}
