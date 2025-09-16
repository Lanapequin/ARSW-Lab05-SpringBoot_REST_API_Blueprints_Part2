/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.services;

import edu.eci.arsw.blueprints.dto.BlueprintDTO;
import edu.eci.arsw.blueprints.filter.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcadavid
 * @author LePeanutButter
 * @author Lanapequin
 */
@Service
public class BlueprintsServices {
    private final BlueprintsPersistence bpp;
    private final BlueprintFilter filter;
    private final Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    public BlueprintsServices(BlueprintsPersistence bpp, @Qualifier("redundancyFilter") BlueprintFilter filter) {
        this.bpp = bpp;
        this.filter = filter;
    }
    
    public void addNewBlueprint(Blueprint bp){
        try {
            bpp.saveBlueprint(bp);
        } catch (BlueprintPersistenceException e) {
            logger.log(Level.SEVERE, String.format("Failed to save blueprint: %s", bp), e);
        }
    }
    
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        return bpp.getAllBlueprints();
    }
    
    /**
     * 
     * @param author blueprint's author
     * @param name blueprint's name
     * @return the blueprint of the given name created by the given author
     * @throws BlueprintNotFoundException if there is no such blueprint
     */
    public Blueprint getBlueprint(String author,String name) throws BlueprintNotFoundException{
        return bpp.getBlueprint(author, name);
    }

    /**
     * Applies the configured filter to a given blueprint and returns a new blueprint
     * with the filtered set of points.
     *
     * <p>This method delegates the filtering logic to the injected {@link BlueprintFilter}
     * implementation, which can either remove redundant points or perform subsampling,
     * depending on the filter configured in the application context.</p>
     *
     * @param bp the original {@link Blueprint} to be filtered. Must not be null.
     * @return a new {@link Blueprint} instance containing the same author and name,
     *         but with a filtered list of {@link Point}s.
     */
    public Blueprint getFilteredBlueprint(Blueprint bp) {
        List<Point> filteredPoints = filter.filter(bp.getPoints());
        return new Blueprint(bp.getAuthor(), bp.getName(), filteredPoints.toArray(new Point[0]));
    }
    
    /**
     * 
     * @param author blueprint's author
     * @return all the blueprints of the given author
     * @throws BlueprintNotFoundException if the given author doesn't exist
     */
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException{
        return bpp.getBlueprintsByAuthor(author);
    }

    public void deleteBlueprint(String author, String name) throws BlueprintNotFoundException {
        bpp.deleteBlueprint(author, name);
    }

    public Blueprint updateBlueprint(String author, String name, BlueprintDTO bp) throws BlueprintNotFoundException, BlueprintPersistenceException {
        return bpp.updateBlueprint(author, name, bp);
    }

}
