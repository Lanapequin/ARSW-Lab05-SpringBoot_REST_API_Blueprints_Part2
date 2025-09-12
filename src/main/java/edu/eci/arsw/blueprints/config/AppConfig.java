package edu.eci.arsw.blueprints.config;

import edu.eci.arsw.blueprints.filter.BlueprintFilter;
import edu.eci.arsw.blueprints.filter.RedundancyFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
/**
 * @author LePeanutButter
 * @author Lanapequin
 */

@Configuration
@ComponentScan(basePackages = "edu.eci.arsw.blueprints")
public class AppConfig {

    @Bean
    public BlueprintFilter blueprintFilter() {
        return new RedundancyFilter();
    }
}

