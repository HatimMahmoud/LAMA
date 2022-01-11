package lama.logic;

import java.util.Comparator;

public class Paar implements Comparable<Paar>, Comparator<Paar> {

    private String name;
    private Integer points;

    Paar(String name, int points){
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int compareTo(Paar o) {
        return points.compareTo(o.getPoints());
    }

    @Override
    public int compare(Paar o1, Paar o2) {
        return (o2.getPoints()-o1.getPoints());
    }
}
