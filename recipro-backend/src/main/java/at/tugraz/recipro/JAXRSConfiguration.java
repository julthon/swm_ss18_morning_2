package at.tugraz.recipro;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * /register
 * /login
 * /recipes/create
 * /recipes/find (Post)
 *   {"title": "cake", "type": "dessert", "time_to_prepare": "45", "min_rating": "3"}
 * /recipes/delete
 * /recipes/update
 */
@ApplicationPath("api")
public class JAXRSConfiguration extends Application {

}
