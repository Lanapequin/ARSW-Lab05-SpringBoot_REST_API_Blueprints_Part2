package edu.eci.arsw.blueprints.filter;

import edu.eci.arsw.blueprints.model.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("subsamplingFilter")
public class SubsamplingFilter implements BlueprintFilter {
    @Override
    public List<Point> filter(List<Point> points) {
        List<Point> filtered = new ArrayList<>();
        for (int i = 0; i < points.size(); i += 2) {
            filtered.add(points.get(i));
        }
        return filtered;
    }
}
