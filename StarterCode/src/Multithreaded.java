import filters.Filters;
import threads.*;
import utils.Utils;

import java.awt.*;

public class Multithreaded {

    public static void startMultithread(Filters filters) throws InterruptedException {

        startBrighterThread(filters, "multithread/brighter.jpg", filters.BRIGHTER_VALUE);

        startGrayscaleThread(filters,"multithread/grayscale.jpg");

        startSwirlThread(filters, "multithread/swirl.jpg");

        startGlassThread(filters, "multithread/glass.jpg", filters.GLASS_MAXIMUM_DISTANCE);

        startBlurThread(filters, "multithread/blur.jpg", filters.BLUR_SUBMATRIX);

        startColorStrengthConditionalBlur(filters, "multithread/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD, filters.BLUE_TRESHOLD, filters.GREEN_TRESHOLD);
        System.out.println("---------------------------------------------------- ");
    }

    private static void startBrighterThread(Filters filters, String outputFile, int value) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            threads[i] = new Thread(new BrighterThread(filters,tmp,value,startRow,endRow)) ;
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Brighter MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startGrayscaleThread(Filters filters, String outputFile) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            threads[i] = new Thread(new GrayscaleThread(filters,tmp, startRow, endRow));
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Grayscale MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startSwirlThread(Filters filters, String outputFile) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int x_matrix = tmp.length / 2;
        int y_matrix = tmp[0].length / 2;
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            threads[i] = new Thread(new SwirlThread(filters, tmp, tmpResult, startRow, endRow,x_matrix,y_matrix)) ;
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Swirl MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startGlassThread(Filters filters, String outputFile, int maximumDistance) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            threads[i] = new Thread(new GlassThread(filters, tmp, startRow, endRow, maximumDistance));
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Glass MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startBlurThread(Filters filters, String outputFile, int value) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads -1) ? tmp.length : (i+1) * rowsPerThread;
            threads[i] = new Thread(new BlurThread(filters, tmp, tmpResult, value, startRow, endRow));
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Blur MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmpResult, outputFile);
    }

    private static void startColorStrengthConditionalBlur(Filters filters, String outputFile, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) throws InterruptedException {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[numberOfThreads];
        int rowsPerThread = tmp.length / numberOfThreads;
        for(int i = 0; i< numberOfThreads; i++){
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads -1) ? tmp.length : (i+1) * rowsPerThread;
            threads[i] = new Thread(new ColorStrengthConditionalBlurThread(filters, tmp, tmpResult, subMatrix, redTreshold, blueTreshold, greenThreshold, startRow, endRow));
            threads[i].start();
        }

        for(Thread thread : threads){
            thread.join();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Color Strength Conditional MultiThreaded Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmpResult, outputFile);
    }
}
