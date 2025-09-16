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
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hcadavid
 */
class InMemoryPersistenceTest {

    @Test
    void saveNewAndLoadTest() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts0 = new Point[]{new Point(40, 40), new Point(15, 15)};
        Blueprint bp0 = new Blueprint("mack", "mypaint", pts0);
        ibpp.saveBlueprint(bp0);

        Point[] pts = new Point[]{new Point(0, 0), new Point(10, 10)};
        Blueprint bp = new Blueprint("john", "thepaint", pts);
        ibpp.saveBlueprint(bp);

        Blueprint loaded = ibpp.getBlueprint(bp.getAuthor(), bp.getName());

        assertNotNull(loaded, "Loading a previously stored blueprint returned null.");
        assertEquals(bp, loaded, "Loading a previously stored blueprint returned a different blueprint.");
    }

    @Test
    void saveExistingBpTest() throws Exception {
        InMemoryBlueprintPersistence ibpp = new InMemoryBlueprintPersistence();

        Point[] pts = new Point[]{new Point(0, 0), new Point(10, 10)};
        Blueprint bp = new Blueprint("john", "thepaint", pts);

        ibpp.saveBlueprint(bp); // primera vez, debería funcionar

        Point[] pts2 = new Point[]{new Point(10, 10), new Point(20, 20)};
        Blueprint bp2 = new Blueprint("john", "thepaint", pts2);

        // segunda vez con mismo autor y nombre → excepción
        assertThrows(BlueprintPersistenceException.class, () -> ibpp.saveBlueprint(bp2),
                "An exception was expected after saving a second blueprint with the same name and author");
    }

    @Test
    void testSaveAndLoadBlueprint() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp = new Blueprint("Alice", "House", new Point[]{new Point(10, 10), new Point(20, 20)});
        persistence.saveBlueprint(bp);

        Blueprint loaded = persistence.getBlueprint("Alice", "House");

        assertNotNull(loaded, "Blueprint should not be null");
        assertEquals(bp, loaded, "Blueprint should match saved one");
    }

    @Test
    void testSaveDuplicateBlueprint() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp1 = new Blueprint("Bob", "Villa", new Point[]{new Point(1, 1)});
        Blueprint bp2 = new Blueprint("Bob", "Villa", new Point[]{new Point(2, 2)});

        persistence.saveBlueprint(bp1);

        assertThrows(BlueprintPersistenceException.class, () -> persistence.saveBlueprint(bp2),
                "Saving a duplicate blueprint should throw an exception");
    }

    @Test
    void testGetBlueprintsByAuthor() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp1 = new Blueprint("Carol", "Design1", new Point[]{});
        Blueprint bp2 = new Blueprint("Carol", "Design2", new Point[]{});
        Blueprint bp3 = new Blueprint("Dave", "Other", new Point[]{});

        persistence.saveBlueprint(bp1);
        persistence.saveBlueprint(bp2);
        persistence.saveBlueprint(bp3);

        Set<Blueprint> carolBps = persistence.getBlueprintsByAuthor("Carol");

        assertEquals(2, carolBps.size(), "Carol should have 2 blueprints");
        assertTrue(carolBps.contains(bp1));
        assertTrue(carolBps.contains(bp2));
    }

    @Test
    void testGetBlueprintsByNonexistentAuthor() {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        assertThrows(BlueprintNotFoundException.class,
                () -> persistence.getBlueprintsByAuthor("Nonexistent"),
                "Should throw exception when author does not exist");
    }

    @Test
    void testGetBlueprintReturnsNullIfNotFound() throws Exception {
        InMemoryBlueprintPersistence persistence = new InMemoryBlueprintPersistence();

        Blueprint bp = persistence.getBlueprint("Ghost", "Phantom");

        assertNull(bp, "Should return null if blueprint doesn't exist");
    }
}
