package threads;

import filters.Filters;

import java.awt.*;
import java.util.Random;

public class GlassThread implements Runnable{

    private final Filters filters;
    private final Color[][] image;
    private final int startingPoint;
    private final int endingPoint;

    private final Random random;
    private final int maximumDistance;

    public GlassThread(Filters filters,Color[][] image,  int startingPoint, int endingPoint, int maximumDistance) {
        this.filters = filters;
        this.image = image;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
        this.random = new Random();
        this.maximumDistance = maximumDistance;
    }

    @Override
    public void run() {
        filters.GlassFilter2(image,startingPoint,endingPoint,maximumDistance,random);
    }

    public void justExecute() { filters.GlassFilter2(image,startingPoint,endingPoint,maximumDistance,random); }
}
