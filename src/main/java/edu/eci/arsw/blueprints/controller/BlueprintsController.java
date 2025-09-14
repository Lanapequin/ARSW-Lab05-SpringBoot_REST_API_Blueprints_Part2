package edu.eci.arsw.blueprints.controller;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintsController {
    @Autowired
    private BlueprintsServices blueprintsServices;

    private static final Logger logger = Logger.getLogger(BlueprintsController.class.getName());

    @GetMapping
    public ResponseEntity<?> getAllBlueprints() {
        try {
            Set<Blueprint> data = blueprintsServices.getAllBlueprints();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            logger.log(Level.SEVERE, "Error fetching all blueprints", ex);
            return new ResponseEntity<>("No blueprints found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<?> getBlueprintsByAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> data = blueprintsServices.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            logger.log(Level.SEVERE, "Error fetching blueprints by author", ex);
            return new ResponseEntity<>("Author not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<?> createBlueprint(@RequestBody Blueprint bp) {
        try {
            blueprintsServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error creating blueprint", ex);
            return new ResponseEntity<>("Blueprint already exists", HttpStatus.FORBIDDEN);
        }
    }
}
