/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.dto.BlueprintDTO;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 *
 * @author hcadavid
 * @author LePeanutButter
 * @author Lanapequin
 */

@Repository
public class InMemoryBlueprintPersistence implements BlueprintsPersistence{

    private final Map<Tuple<String,String>,Blueprint> blueprints=new HashMap<>();

    public InMemoryBlueprintPersistence() {
        //load stub data
        Point[] pts=new Point[]{new Point(140, 140),new Point(115, 115)};
        Blueprint bp=new Blueprint("_authorname_", "_bpname_ ",pts);

        Point[] pts1 = new Point[]{new Point(150, 150), new Point(110, 110)};
        Blueprint bp1 = new Blueprint("Alice", "Casa", pts1);

        Point[] pts2 = new Point[]{new Point(50, 60), new Point(70, 80)};
        Blueprint bp2 = new Blueprint("Alice", "Parque", pts2);

        Point[] pts3 = new Point[]{new Point(10, 10), new Point(20, 20)};
        Blueprint bp3 = new Blueprint("Bob", "Puente", pts3);

        blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        blueprints.put(new Tuple<>(bp1.getAuthor(), bp1.getName()), bp1);
        blueprints.put(new Tuple<>(bp2.getAuthor(), bp2.getName()), bp2);
        blueprints.put(new Tuple<>(bp3.getAuthor(), bp3.getName()), bp3);
    }    
    
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(),bp.getName()))){
            throw new BlueprintPersistenceException("The given blueprint already exists: "+bp);
        }
        else{
            blueprints.put(new Tuple<>(bp.getAuthor(),bp.getName()), bp);
        }        
    }

    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        boolean authorExists = blueprints.keySet().stream()
                .anyMatch(key -> key.getElem1().equals(author));

        if (!authorExists) {
            throw new BlueprintNotFoundException("Author '" + author + "' does not exist.");
        }

        Tuple<String, String> key = new Tuple<>(author, bprintname);
        Blueprint blueprint = blueprints.get(key);

        if (blueprint == null) {
            throw new BlueprintNotFoundException(
                    String.format("Blueprint '%s' by author '%s' was not found.", bprintname, author)
            );
        }

        return blueprint;
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> result = new HashSet<>();
        for (Map.Entry<Tuple<String, String>, Blueprint> entry : blueprints.entrySet()) {
            if (entry.getKey().getElem1().equals(author)) {
                result.add(entry.getValue());
            }
        }
        if (result.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints found for author: " + author);
        }
        return result;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() throws BlueprintNotFoundException {
        if (blueprints.isEmpty()) {
            throw new BlueprintNotFoundException("No blueprints available");
        }
        return new HashSet<>(blueprints.values());
    }

    @Override
    public void deleteBlueprint(String author, String name) throws BlueprintNotFoundException {
        Tuple<String, String> key = new Tuple<>(author, name);
        if (!blueprints.containsKey(key)) {
            throw new BlueprintNotFoundException("Blueprint not found for author: " + author + ", name: " + name);
        }
        blueprints.remove(key);
    }

    @Override
    public Blueprint updateBlueprint(String author, String bprintname, BlueprintDTO bp) throws BlueprintNotFoundException, BlueprintPersistenceException {
        Tuple<String, String> oldKey = new Tuple<>(author, bprintname);
        boolean authorExists = blueprints.keySet().stream()
                .anyMatch(key -> key.getElem1().equals(author));

        if (!authorExists) {
            throw new BlueprintNotFoundException("Author '" + author + "' does not exist.");
        }

        Blueprint blueprint = blueprints.get(oldKey);

        if (blueprint == null) {
            throw new BlueprintNotFoundException(
                    String.format("Blueprint '%s' by author '%s' was not found.", bprintname, author)
            );
        }

        String newAuthor = bp.getAuthor() != null ? bp.getAuthor() : blueprint.getAuthor();
        String newName = bp.getName() != null ? bp.getName() : blueprint.getName();
        List<Point> newPoints = bp.getPoints() != null ? bp.getPoints() : blueprint.getPoints();
        Tuple<String, String> newKey = new Tuple<>(newAuthor, newName);

        if (!newKey.equals(oldKey) && blueprints.containsKey(newKey)) {
            throw new BlueprintPersistenceException(
                    String.format("Cannot update: a blueprint with author='%s' and name='%s' already exists.",
                            newAuthor, newName)
            );
        }

        blueprint.setAuthor(newAuthor);
        blueprint.setName(newName);
        blueprint.setPoints(newPoints);

        if (!newKey.equals(oldKey)) {
            blueprints.remove(oldKey);
        }

        blueprints.put(newKey, blueprint);
        return blueprint;
    }
}
