package threads;

import filters.Filters;

import java.awt.*;

public class ColorStrengthConditionalBlurThread implements Runnable{

    private final Filters filters;
    private final Color[][] image;
    private final Color[][] imageResult;
    private final int subMatrix;
    private final int redThreshold;
    private final int blueThreshold;
    private final int greenThreshold;
    private final int startingPoint;
    private final int endingPoint;

    public ColorStrengthConditionalBlurThread(Filters filters, Color[][] image, Color[][] imageResult, int subMatrix, int redThreshold, int blueThreshold, int greenThreshold, int startingPoint, int endingPoint) {
        this.filters = filters;
        this.image = image;
        this.imageResult = imageResult;
        this.subMatrix = subMatrix;
        this.redThreshold = redThreshold;
        this.blueThreshold = blueThreshold;
        this.greenThreshold = greenThreshold;
        this.startingPoint = startingPoint;
        this.endingPoint = endingPoint;
    }

    @Override
    public void run() {
        filters.colorStrengthConditionalBlur2(image,imageResult,subMatrix,redThreshold,blueThreshold,greenThreshold,startingPoint,endingPoint);
    }
}
