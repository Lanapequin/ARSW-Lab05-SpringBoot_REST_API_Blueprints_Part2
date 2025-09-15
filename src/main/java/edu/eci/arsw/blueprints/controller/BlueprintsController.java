package edu.eci.arsw.blueprints.controller;

import edu.eci.arsw.blueprints.dto.BlueprintDTO;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/blueprints")
public class BlueprintsController {
    private final BlueprintsServices blueprintsServices;
    private static final Logger logger = Logger.getLogger(BlueprintsController.class.getName());

    public BlueprintsController(BlueprintsServices blueprintsServices) {
        this.blueprintsServices = blueprintsServices;
    }

    @GetMapping
    public ResponseEntity<Set<Blueprint>> getAllBlueprints() {
        try {
            Set<Blueprint> data = blueprintsServices.getAllBlueprints();
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            logger.log(Level.SEVERE, "Error fetching all blueprints", ex);
            return new ResponseEntity<>(Set.of(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}")
    public ResponseEntity<Set<Blueprint>> getBlueprintsByAuthor(@PathVariable("author") String author) {
        try {
            Set<Blueprint> data = blueprintsServices.getBlueprintsByAuthor(author);
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (BlueprintNotFoundException ex) {
            logger.log(Level.SEVERE, "Error fetching blueprints by author", ex);
            return new ResponseEntity<>(Set.of(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{author}/{bpname}")
    public ResponseEntity<ControllerResponse<Blueprint>> getBlueprintByAuthorAndName(
            @PathVariable("author") String author,
            @PathVariable("bpname") String blueprintName) {
        try {
            Blueprint blueprint = blueprintsServices.getBlueprint(author, blueprintName);
            ControllerResponse<Blueprint> response = new ControllerResponse<>(
                    blueprint,
                    "Blueprint retrieved successfully",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            logger.log(Level.WARNING, String.format(
                    "Failed to retrieve blueprint: author='%s', name='%s'. Reason: %s",
                    author, blueprintName, e.getMessage()
            ));
            ControllerResponse<Blueprint> response = new ControllerResponse<>(
                    null,
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping
    public ResponseEntity<Void> createBlueprint(@RequestBody Blueprint bp) {
        try {
            blueprintsServices.addNewBlueprint(bp);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error creating blueprint", ex);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/{author}/{bpname}")
    public ResponseEntity<ControllerResponse<Blueprint>> putBlueprintByAuthorAndName(
            @PathVariable("author") String author,
            @PathVariable("bpname") String blueprintName,
            @RequestBody BlueprintDTO bp) {
        try {
            Blueprint blueprint = blueprintsServices.updateBlueprint(author, blueprintName, bp);
            ControllerResponse<Blueprint> response = new ControllerResponse<>(
                    blueprint,
                    "Blueprint updated successfully",
                    HttpStatus.OK.value()
            );
            return ResponseEntity.ok(response);
        } catch (BlueprintNotFoundException e) {
            logger.log(Level.WARNING, String.format(
                    "Failed to retrieve blueprint: author='%s', name='%s'. Reason: %s",
                    author, blueprintName, e.getMessage()
            ));
            ControllerResponse<Blueprint> response = new ControllerResponse<>(
                    null,
                    e.getMessage(),
                    HttpStatus.NOT_FOUND.value()
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (BlueprintPersistenceException e) {
            logger.log(Level.WARNING, String.format(
                    "Failed to update blueprint: author='%s', name='%s'. Reason: %s",
                    author, blueprintName, e.getMessage()
            ));
            ControllerResponse<Blueprint> response = new ControllerResponse<>(
                    null,
                    e.getMessage(),
                    HttpStatus.CONFLICT.value()
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}
