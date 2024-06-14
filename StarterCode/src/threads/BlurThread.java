package threads;

import filters.Filters;

import java.awt.*;

public class BlurThread implements Runnable{

    private final Filters filters;
    private final Color[][] image;
    private final Color[][] imageResult;
    private final int value;
    private final int startingPoint;
    private final int endingPoint;

    public BlurThread(Filters filters, Color[][] image, Color[][] imageResult, int value, int startingPoint, int endingPoint) {
        this.filters = filters;
        this.image = image;
        this.imageResult = imageResult;
        this.value = value;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
    }

    @Override
    public void run() {
        filters.BlurFilter2(value,image,imageResult,startingPoint, endingPoint);
    }
}
