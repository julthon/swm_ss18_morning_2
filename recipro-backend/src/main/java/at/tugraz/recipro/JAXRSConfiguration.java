package at.tugraz.recipro;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/*
 * /register
 * /login
 * /recipes/create
 * /recipes/find (Post)
 *   {"title": "cake", "type": "dessert", "time_to_prepare": "45", "min_rating": "3"}
 * /recipes/{title}
 *   [{"preparationTime": 10, "recipeTypes": ["DESSERT"], "title": "{title}"}]
 * /recipes/filter?minpreptime=1&maxpreptime=999 (Get)
 *   [{"preparationTime": 10, "recipeTypes": ["DESSERT"], "title": "Kuchen"}]
 * /recipes/delete
 * /recipes/update
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

}
