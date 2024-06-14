package threads;

import filters.Filters;

import java.awt.*;

public class GrayscaleThread implements Runnable{

    private final Filters filter;
    private final Color[][] image;
    private final int startRow;
    private final int endRow;


    public GrayscaleThread(Filters filter, Color[][] image, int startRow, int endRow) {
        this.filter = filter;
        this.image = image;
        this.startRow = startRow;
        this.endRow = endRow;
    }

    @Override
    public void run() {
        filter.GrayscaleFilter2(image,startRow,endRow);
    }
}
