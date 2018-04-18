/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.tugraz.recipro.recipes.entity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Dominik
 */
public class RecipeIT {
    
    private EntityManager em;
    private EntityTransaction tx;
    
    @Before
    public void RecipeIT() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("it");
        this.em = emf.createEntityManager();
        this.tx = this.em.getTransaction();
    }
    
    @Test
    public void verifyMappings() {
        this.tx.begin();
        this.em.merge(new Recipe("kuchen", 42));
        this.tx.commit();
    }
    
}
