package threads;

import filters.Filters;

import java.awt.*;

public class SwirlThread implements Runnable{

    private Filters filters;
    private final Color[][] image;

    private final Color[][] imageResult;
    private final int startRow;
    private final int endRow;

    private final int x_middle;

    private final int y_middle;



    @Override
    public void run() {
        filters.SwirlFilter2(image,imageResult,startRow,endRow,x_middle,y_middle);
    }

    public SwirlThread(Filters filters, Color[][] image,Color[][] imageResult, int startRow, int endRow, int x_middle,int y_middle) {
        this.filters = filters;
        this.image = image;
        this.imageResult = imageResult;
        this.startRow = startRow;
        this.endRow = endRow;
        this.x_middle = x_middle;
        this.y_middle = y_middle;
    }
}
