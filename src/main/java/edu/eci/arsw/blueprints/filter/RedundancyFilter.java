package edu.eci.arsw.blueprints.filter;

import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("redundancyFilter")
public class RedundancyFilter implements BlueprintFilter {
    @Override
    public List<Point> filter(List<Point> points) {
        List<Point> filtered = new ArrayList<>();
        Point prev = null;
        for (Point p : points) {
            if (!p.equals(prev)) {
                filtered.add(p);
                prev = p;
            }
        }
        return filtered;
    }
}
