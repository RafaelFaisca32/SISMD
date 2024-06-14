package recursiveactions;

import filters.Filters;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

public class GlassRecursiveAction extends RecursiveAction {
    private final Filters filters;
    private final Color[][] tmp;
    private final int startRow;
    private final int endRow;
    private final Random random;
    private final int maximumDistance;

    public GlassRecursiveAction(Filters filters, Color[][] tmp, int startRow, int endRow, int maximumDistance) {
        this.filters = filters;
        this.tmp = tmp;
        this.startRow = startRow;
        this.endRow = endRow;
        this.maximumDistance = maximumDistance;
        this.random = new Random();
    }

    @Override
    protected void compute() {
        if(endRow - startRow <= filters.THRESHOLD_RECURSIVE){
            filters.GlassFilter2(tmp,startRow,endRow,maximumDistance,random);
        } else {
            int mid = (startRow + endRow) / 2;
            GlassRecursiveAction leftAction = new GlassRecursiveAction(filters, tmp, startRow, mid, maximumDistance);
            GlassRecursiveAction rightAction = new GlassRecursiveAction(filters, tmp, mid, endRow, maximumDistance);
            leftAction.fork();
            rightAction.fork();
            leftAction.join();
            rightAction.join();
        }
    }
}
