package edu.eci.arsw.blueprints.filter;

import edu.eci.arsw.blueprints.model.Point;

import java.util.List;

public interface BlueprintFilter {
    List<Point> filter(List<Point> points);
}
