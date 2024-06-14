package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class BlurRecursiveAction extends RecursiveAction {
    private final Filters filters;
    private final Color[][] tmp;
    private final Color[][] tmpResult;
    private final int startRow;
    private final int endRow;

    private final int value;



    public BlurRecursiveAction(Filters filters, Color[][] tmp, Color[][] tmpResult, int startRow, int endRow, int value) {
        this.filters = filters;
        this.tmp = tmp;
        this.tmpResult = tmpResult;
        this.startRow = startRow;
        this.endRow = endRow;
        this.value = value;
    }

    @Override
    protected void compute() {
        if(endRow - startRow <= filters.THRESHOLD_RECURSIVE){
            filters.BlurFilter2(value,tmp,tmpResult,startRow,endRow);
        } else {
            int mid = (startRow + endRow) / 2;
            BlurRecursiveAction leftAction = new BlurRecursiveAction(filters, tmp, tmpResult, startRow, mid, value);
            BlurRecursiveAction rightAction = new BlurRecursiveAction(filters, tmp, tmpResult, mid, endRow, value);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
