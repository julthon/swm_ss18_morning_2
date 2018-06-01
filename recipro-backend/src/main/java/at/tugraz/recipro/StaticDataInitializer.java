package at.tugraz.recipro;

import at.tugraz.recipro.recipes.entity.Allergen;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author julian
 */
@Startup
@Singleton
public class StaticDataInitializer {
    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void init() {
        initAllergens();
    }
    
    private void initAllergens() {
        Allergen allergens[] = {new Allergen("P", "Peanuts", "Peanuts are actually a legume and grow " +
                                        "underground, which is why it's sometimes called a " +
                                        "groundnut. Peanuts are often used as an ingredient " +
                                        "in biscuits, cakes, curries, desserts, sauces (such as " +
                                        "satay sauce), as well as in groundnut oil and " +
                                        "peanut flour."),
                                new Allergen("N", "Nuts", "Not to be mistaken with peanuts (which are actually " +
                                        "a legume & grow underground), this ingredient " +
                                        "refers to nuts which grow on trees, like cashew nuts, " +
                                        "almonds and hazelnuts. You can find nuts in breads, " +
                                        "biscuits, crackers, desserts, nut powders (often used " +
                                        "in Asian curries), stir-fried dishes, ice cream, " +
                                        "marzipan (almond paste), nut oils and sauces."),
                                new Allergen("Mi", "Milk", "Milk is a common ingredient in butter, cheese, " +
                                        "cream, milk powders and yoghurt. It can also be " +
                                        "found in foods brushed or glazed with milk, and " +
                                        "in powdered soups and sauces. It's often split into " +
                                        "casein in curds and BLG in whey."),
                                new Allergen("So", "Soya", "Often found in bean curd, edamame beans, miso " +
                                        "paste, textured soya protein, soya flour or tofu, soya " +
                                        "is a staple ingredient in oriental food. It can also be " +
                                        "found in desserts, ice cream, meat products, sauces " +
                                        "and vegetarian products."),
                                new Allergen("Mu", "Mustard", "Liquid mustard, mustard powder and mustard seeds " +
                                        "fall into this category. This ingredient can also be " +
                                        "found in breads, curries, marinades, meat products, " +
                                        "salad dressings, sauces and soups."),
                                new Allergen("L", "Lupin", "Yes, lupin is a flower, but it's also found in flour! " +
                                        "Lupin flour seeds can be used in some types of " +
                                        "bread, pastries and even pasta."),
                                new Allergen("E", "Eggs", "Eggs are often found in cakes, some meat products, " +
                                        "mayonnaise, mousses, pasta, quiche, sauces and " +
                                        "pastries or foods brushed or glazed with egg."),
                                new Allergen("F", "Fish", "You will find this is in some fish sauces, pizzas, " +
                                        "relishes, salad dressings, stock cubes and " +
                                        "Worcestershire sauce."),
                                new Allergen("Cr", "Crustaceans", "Crab, lobster, prawns, and scampi are crustaceans. " +
                                        "shrimp paste, often used in Thai and south-east " +
                                        "Asian curries or salads, is an ingredient to lookout for."),
                                new Allergen("Mo", "Molluscs", "These include mussels, land snails, squid and " +
                                        "whelks, but can also be commonly found in oyster " +
                                        "sauce or as an ingredient in fish stews."),
                                new Allergen("G", "Cerials containing gluten", "Wheat (such as spelt and Khorasen wheat/Kamut), " +
                                        "rye, barley and oats is often found in foods " +
                                        "containing flour such as some types of baking " +
                                        "powder, batter, bread crumbs, bread, cakes, pasta, " +
                                        "couscous, meat products, pasta, pastry, sauces, " +
                                        "soups and fried foods which are dusted with flour."),
                                new Allergen("Se", "Sesame", "These seeds can often be found in bread (sprinkled " +
                                        "on hamburger buns for example), breadsticks, " +
                                        "houmous, sesame oil and tahini. They are " +
                                        "sometimes toasted and used in salads."),
                                new Allergen("C", "Celery" , "This includes celery stalks, leaves, seeds and root" +
                                        "called celeriac. You can find celery in celery salt, " +
                                        "salads, some meat products, soups and stock cubes."),
                                new Allergen("Su", "Sulphur dioxide", "This is an ingredient often used in dried fruit such as\n" +
                                        "raisins, dried apricots and prunes. You might also " +
                                        "find it in meat products, soft drinks, vegetables as " +
                                        "well as in wine and beer. If you have asthma, you " +
                                        "have a higher risk of developing a reaction to sulphur dioxide.")};
        Arrays.stream(allergens).forEach((Allergen a) -> em.persist(a));
    }
}
