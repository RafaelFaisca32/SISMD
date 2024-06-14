package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class ColorStrengthConditionalBlurRecursiveAction extends RecursiveAction {

    private final Filters filters;
    private final Color[][] tmp;
    private final Color[][] tmpResult;
    private final int startRow;
    private final int endRow;

    private final int subMatrix;
    private final int redTreshold;
    private final int blueTreshold;
    private final int greenThreshold;



    public ColorStrengthConditionalBlurRecursiveAction(Filters filters, Color[][] tmp, Color[][] tmpResult, int startRow, int endRow, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        this.filters = filters;
        this.tmp = tmp;
        this.tmpResult = tmpResult;
        this.startRow = startRow;
        this.endRow = endRow;
        this.subMatrix = subMatrix;
        this.redTreshold = redTreshold;
        this.blueTreshold = blueTreshold;
        this.greenThreshold = greenThreshold;
    }

    @Override
    protected void compute() {
        if(endRow - startRow <= filters.THRESHOLD_RECURSIVE){
            filters.colorStrengthConditionalBlur2(tmp,tmpResult,subMatrix,redTreshold,blueTreshold,greenThreshold,startRow,endRow);
        } else {
            int mid = (startRow + endRow) / 2;
            ColorStrengthConditionalBlurRecursiveAction leftAction = new ColorStrengthConditionalBlurRecursiveAction(filters, tmp, tmpResult, startRow, mid, subMatrix, redTreshold, blueTreshold, greenThreshold);
            ColorStrengthConditionalBlurRecursiveAction rightAction = new ColorStrengthConditionalBlurRecursiveAction(filters, tmp, tmpResult, mid, endRow, subMatrix, redTreshold, blueTreshold, greenThreshold);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
