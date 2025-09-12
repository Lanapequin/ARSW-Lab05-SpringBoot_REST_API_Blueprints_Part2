package edu.eci.arsw.blueprints;

import edu.eci.arsw.blueprints.config.AppConfig;
import edu.eci.arsw.blueprints.model.*;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Set;

/**
 * @author LePeanutButter
 * @author Lanapequin
 */

public class Main {
    static Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        BlueprintsServices services = ctx.getBean(BlueprintsServices.class);

        String firstAuthor = "Alice";

        try {
            Blueprint bp1 = new Blueprint(firstAuthor, "PlanA", new Point[]{new Point(10, 10), new Point(20, 20)});
            Blueprint bp2 = new Blueprint(firstAuthor, "PlanB", new Point[]{new Point(15, 15), new Point(25, 25)});
            Blueprint bp3 = new Blueprint("Bob", "PlanX", new Point[]{new Point(5, 5), new Point(8, 8)});

            services.addNewBlueprint(bp1);
            services.addNewBlueprint(bp2);
            services.addNewBlueprint(bp3);

            logger.log(Level.INFO, "Blueprint Alice, PlanA: {0}", services.getBlueprint(firstAuthor, "PlanA"));

            Set<Blueprint> aliceBlueprints = services.getBlueprintsByAuthor(firstAuthor);
            logger.info("Blueprints by Alice:");
            aliceBlueprints.forEach(bp -> logger.info(bp.toString()));

            Set<Blueprint> allBlueprints = services.getAllBlueprints();
            logger.info("All blueprints in the system:");
            allBlueprints.forEach(bp -> logger.info(bp.toString()));

            services.deleteBlueprint(firstAuthor, "PlanB");

            logger.info("Blueprints by Alice after deleting PlanB:");
            aliceBlueprints = services.getBlueprintsByAuthor(firstAuthor);
            aliceBlueprints.forEach(bp -> logger.info(bp.toString()));

        } catch (BlueprintNotFoundException e) {
            logger.log(Level.WARNING, "Blueprint not found", e);
        }
    }
}

