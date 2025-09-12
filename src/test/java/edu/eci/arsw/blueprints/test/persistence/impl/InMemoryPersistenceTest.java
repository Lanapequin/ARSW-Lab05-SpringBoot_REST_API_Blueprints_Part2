/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.test.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;

import java.util.Set;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author hcadavid
 */
public class InMemoryPersistenceTest {
    
    @Test
    public void saveNewAndLoadTest() throws BlueprintPersistenceException, BlueprintNotFoundException{
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();

        Point[] pts0=new Point[]{new Point(40, 40),new Point(15, 15)};
        Blueprint bp0=new Blueprint("mack", "mypaint",pts0);
        
        ibpp.saveBlueprint(bp0);
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        ibpp.saveBlueprint(bp);
        
        assertNotNull("Loading a previously stored blueprint returned null.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()));
        
        assertEquals("Loading a previously stored blueprint returned a different blueprint.",ibpp.getBlueprint(bp.getAuthor(), bp.getName()), bp);
        
    }


    @Test
    public void saveExistingBpTest() {
        InMemoryBlueprintPersistence ibpp=new InMemoryBlueprintPersistence();
        
        Point[] pts=new Point[]{new Point(0, 0),new Point(10, 10)};
        Blueprint bp=new Blueprint("john", "thepaint",pts);
        
        try {
            ibpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException ex) {
            fail("Blueprint persistence failed inserting the first blueprint.");
        }
        
        Point[] pts2=new Point[]{new Point(10, 10),new Point(20, 20)};
        Blueprint bp2=new Blueprint("john", "thepaint",pts2);

        try{
            ibpp.saveBlueprint(bp2);
            fail("An exception was expected after saving a second blueprint with the same name and autor");
        }
        catch (BlueprintPersistenceException ex){
            
        }
    }

    @Test
    public void testSaveAndLoadBlueprint() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp = new Blueprint("Alice", "House", new Point[]{new Point(10, 10), new Point(20, 20)});
        persistence.saveBlueprint(bp);

        Blueprint loaded = persistence.getBlueprint("Alice", "House");
        assertNotNull("Blueprint should not be null", loaded);
        assertEquals("Blueprint should match saved one", bp, loaded);
    }

    @Test(expected = BlueprintPersistenceException.class)
    public void testSaveDuplicateBlueprint() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp1 = new Blueprint("Bob", "Villa", new Point[]{new Point(1, 1)});
        Blueprint bp2 = new Blueprint("Bob", "Villa", new Point[]{new Point(2, 2)});

        persistence.saveBlueprint(bp1);
        persistence.saveBlueprint(bp2); // This should throw
    }

    @Test
    public void testGetBlueprintsByAuthor() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp1 = new Blueprint("Carol", "Design1", new Point[]{});
        Blueprint bp2 = new Blueprint("Carol", "Design2", new Point[]{});
        Blueprint bp3 = new Blueprint("Dave", "Other", new Point[]{});

        persistence.saveBlueprint(bp1);
        persistence.saveBlueprint(bp2);
        persistence.saveBlueprint(bp3);

        Set<Blueprint> carolBps = persistence.getBlueprintsByAuthor("Carol");

        assertEquals("Carol should have 2 blueprints", 2, carolBps.size());
        assertTrue(carolBps.contains(bp1));
        assertTrue(carolBps.contains(bp2));
    }

    @Test(expected = BlueprintNotFoundException.class)
    public void testGetBlueprintsByNonexistentAuthor() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();
        persistence.getBlueprintsByAuthor("Nonexistent"); // Should throw
    }

    @Test
    public void testGetBlueprintReturnsNullIfNotFound() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();
        Blueprint bp = persistence.getBlueprint("Ghost", "Phantom");
        assertNull("Should return null if blueprint doesn't exist", bp);
    }
    
}
