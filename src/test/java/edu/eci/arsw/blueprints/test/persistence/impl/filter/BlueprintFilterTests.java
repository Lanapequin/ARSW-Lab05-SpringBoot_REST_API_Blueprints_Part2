package edu.eci.arsw.blueprints.test.persistence.impl.filter;

import java.util.Arrays;
import java.util.List;

import edu.eci.arsw.blueprints.config.AppConfig;
import edu.eci.arsw.blueprints.filter.BlueprintFilter;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.Test;
import org.junit.Assert;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class BlueprintFilterTests {
    @Autowired
    @Qualifier("redundancyFilter")
    BlueprintFilter redundancyFilter;

    @Autowired
    @Qualifier("subsamplingFilter")
    BlueprintFilter subsamplingFilter;

    @Test
    public void testRedundancyFilter() {
        List<Point> input = Arrays.asList(
                new Point(0, 0), new Point(0, 0), new Point(1, 1), new Point(1, 1)
        );
        List<Point> expected = Arrays.asList(
                new Point(0, 0), new Point(1, 1)
        );
        Assert.assertEquals(expected, redundancyFilter.filter(input));
    }

    @Test
    public void testSubsamplingFilter() {
        List<Point> input = Arrays.asList(
                new Point(0, 0), new Point(1, 1), new Point(2, 2), new Point(3, 3)
        );
        List<Point> expected = Arrays.asList(
                new Point(0, 0), new Point(2, 2)
        );
        Assert.assertEquals(expected, subsamplingFilter.filter(input));
    }
}
