package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.concurrent.RecursiveAction;

public class SwirlRecursiveAction extends RecursiveAction {
    private final Filters filters;
    private final Color[][] tmp;

    private final Color[][] tmpResult;
    private final int startRow;
    private final int endRow;

    private final int x_middle;

    private final int y_middle;

    public SwirlRecursiveAction(Filters filters, Color[][] tmp,Color[][] tmpResult, int startRow, int endRow) {
        this.filters = filters;
        this.tmp = tmp;
        this.tmpResult = tmpResult;
        this.startRow = startRow;
        this.endRow = endRow;
        this.x_middle = this.tmp.length / 2;
        this.y_middle = this.tmp[0].length / 2;

    }

    @Override
    protected void compute() {
        if (endRow - startRow <= filters.THRESHOLD_RECURSIVE) {
                filters.SwirlFilter2(tmp,tmpResult,startRow,endRow,x_middle,y_middle);
        } else {
            int mid = (startRow + endRow) / 2;
            SwirlRecursiveAction leftAction = new SwirlRecursiveAction(filters, tmp,tmpResult, startRow, mid);
            SwirlRecursiveAction rightAction = new SwirlRecursiveAction(filters, tmp,tmpResult, mid, endRow);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
