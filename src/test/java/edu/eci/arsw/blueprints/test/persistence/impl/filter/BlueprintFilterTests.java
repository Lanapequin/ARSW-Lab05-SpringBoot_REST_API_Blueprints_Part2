package edu.eci.arsw.blueprints.test.persistence.impl.filter;

import java.util.Arrays;
import java.util.List;

import edu.eci.arsw.blueprints.config.AppConfig;
import edu.eci.arsw.blueprints.filter.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppConfig.class)
class BlueprintFilterTests {

    @Autowired
    @Qualifier("redundancyFilter")
    BlueprintFilter redundancyFilter;

    @Autowired
    @Qualifier("subsamplingFilter")
    BlueprintFilter subsamplingFilter;

    @Test
    void testRedundancyFilter() {
        List<Point> input = Arrays.asList(
                new Point(0, 0), new Point(0, 0), new Point(1, 1), new Point(1, 1)
        );
        List<Point> expected = Arrays.asList(
                new Point(0, 0), new Point(1, 1)
        );
        assertEquals(expected, redundancyFilter.filter(input));
    }

    @Test
    void testSubsamplingFilter() {
        List<Point> input = Arrays.asList(
                new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)
        );
        List<Point> expected = Arrays.asList(
                new Point(0, 0), new Point(2, 2)
        );
        assertEquals(expected, subsamplingFilter.filter(input));
    }
}
