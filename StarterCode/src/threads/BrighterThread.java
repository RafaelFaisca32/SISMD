package threads;

import filters.Filters;

import java.awt.*;

public class BrighterThread implements Runnable{

    private final Filters filters;

    private final Color[][] image;

    private int value;

    private int startingPoint;

    private int endingPoint;

    @Override
    public void run() {

        filters.BrighterFilter2(value,image, startingPoint, endingPoint);

    }

    public BrighterThread(Filters filters, Color[][] image, int value, int startingPoint, int endingPoint) {
        this.filters = filters;
        this.image = image;
        this.value = value;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
    }
}
