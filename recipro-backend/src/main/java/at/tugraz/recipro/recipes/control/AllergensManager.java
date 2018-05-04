package at.tugraz.recipro.recipes.control;

import at.tugraz.recipro.recipes.entity.Allergen;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Julian
 */
@ApplicationScoped
public class AllergensManager {
    
    @PersistenceContext
    EntityManager entityManager;

    public Allergen save(Allergen allergen) {
        return this.entityManager.merge(allergen);
    }

    public Allergen findByShortName(String shortName) {
        List<Allergen> result = this.entityManager.createNamedQuery(Allergen.FIND_BY_SHORT_NAME, Allergen.class)
                .setParameter("shortName", "%" + shortName.toLowerCase() + "%").getResultList();
        return (result.size() > 0) ? result.get(0) : null;
    }

    public List<Allergen> findAll() {
        return this.entityManager.createNamedQuery(Allergen.FIND_ALL, Allergen.class)
                .getResultList();
    }
}
