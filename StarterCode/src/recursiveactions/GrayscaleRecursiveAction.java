package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class GrayscaleRecursiveAction extends RecursiveAction {
    private final Filters filters;
    private final Color[][] tmp;
    private final int startRow;
    private final int endRow;


    public GrayscaleRecursiveAction(Filters filters, Color[][] tmp, int startRow, int endRow) {
        this.filters = filters;
        this.tmp = tmp;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    protected void compute() {
        if (endRow - startRow <= filters.THRESHOLD_RECURSIVE) {
            filters.GrayscaleFilter2(tmp,startRow,endRow);
        } else {
            int mid = (startRow + endRow) / 2;
            GrayscaleRecursiveAction leftAction = new GrayscaleRecursiveAction(filters, tmp, startRow, mid);
            GrayscaleRecursiveAction rightAction = new GrayscaleRecursiveAction(filters, tmp, mid, endRow);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
