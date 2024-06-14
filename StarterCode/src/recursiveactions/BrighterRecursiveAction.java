package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class BrighterRecursiveAction extends RecursiveAction {

    private final Filters filters;
    private final Color[][] tmp;
    private final int startRow;
    private final int endRow;

    private final int value;



    public BrighterRecursiveAction(Filters filters, Color[][] tmp, int startRow, int endRow, int value) {
        this.filters = filters;
        this.tmp = tmp;
        this.startRow = startRow;
        this.endRow = endRow;
        this.value = value;
    }

    @Override
    protected void compute() {
        if(endRow - startRow <= filters.THRESHOLD_RECURSIVE){
            filters.BrighterFilter2(value,tmp,startRow,endRow);
        } else {
            int mid = (startRow + endRow) / 2;
            BrighterRecursiveAction leftAction = new BrighterRecursiveAction(filters, tmp, startRow, mid, value);
            BrighterRecursiveAction rightAction = new BrighterRecursiveAction(filters, tmp, mid, endRow, value);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
