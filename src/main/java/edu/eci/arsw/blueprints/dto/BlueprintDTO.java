package edu.eci.arsw.blueprints.dto;

import edu.eci.arsw.blueprints.model.Point;
import java.util.List;

public class BlueprintDTO {
    private String author = null;
    private List<Point> points = null;
    private String name = null;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
