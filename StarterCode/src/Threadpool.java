import filters.Filters;
import recursiveactions.*;
import threads.*;
import utils.Utils;

import java.awt.*;
import java.util.Random;
import java.util.concurrent.*;

public class Threadpool {

    public static void startThreadpool(Filters filters) {
        applyFiltersThreadPoolExecutor(filters);
        System.out.println("---------------------------------------------------- ");
        applyFiltersThreadPoolExecutorPartitions(filters);
        System.out.println("---------------------------------------------------- ");
        applyFiltersThreadPoolForkJoinPool(filters);
        System.out.println("---------------------------------------------------- ");
        applyFiltersThreadPoolCompletableFuturePartitions(filters);
        System.out.println("---------------------------------------------------- ");
    }

    public static void applyFiltersThreadPoolCompletableFuturePartitions(Filters filters) {

        startBrighterCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/brighter.jpg", filters.BRIGHTER_VALUE);

        startGrayscaleCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/grayscale.jpg");

        startSwirlCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/swirl.jpg");

        startGlassCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/glass.jpg", filters.GLASS_MAXIMUM_DISTANCE);

        startBlurCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/blur.jpg", filters.BLUR_SUBMATRIX);

        startColorStrengthConditionalBlurCompletableFuturePartition(filters, "threadpoolpoolcompletablepartitions/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD, filters.BLUE_TRESHOLD, filters.GREEN_TRESHOLD);

    }


    public static void applyFiltersThreadPoolExecutor(Filters filters) {

        startBrighterExecutorThread(filters, "threadpoolexecutor/brighter.jpg", filters.BRIGHTER_VALUE);

        startGrayscaleExecutorThread(filters, "threadpoolexecutor/grayscale.jpg");

        startSwirlExecutorThread(filters, "threadpoolexecutor/swirl.jpg");

        startGlassExecutorThread(filters, "threadpoolexecutor/glass.jpg", filters.GLASS_MAXIMUM_DISTANCE);

        startBlurExecutorThread(filters, "threadpoolexecutor/blur.jpg", filters.BLUR_SUBMATRIX);

        startColorStrengthConditionalBlurExecutorThread(filters, "threadpoolexecutor/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD, filters.BLUE_TRESHOLD, filters.GREEN_TRESHOLD);
    }

    public static void applyFiltersThreadPoolExecutorPartitions(Filters filters) {

        startBrighterExecutorThreadPartition(filters, "threadpoolexecutorpartition/brighter.jpg", filters.BRIGHTER_VALUE);

        startGrayscaleExecutorThreadPartition(filters, "threadpoolexecutorpartition/grayscale.jpg");

        startSwirlExecutorThreadPartition(filters, "threadpoolexecutorpartition/swirl.jpg");

        startGlassExecutorThreadPartition(filters, "threadpoolexecutorpartition/glass.jpg", filters.GLASS_MAXIMUM_DISTANCE);

        startBlurExecutorThreadPartition(filters, "threadpoolexecutorpartition/blur.jpg", filters.BLUR_SUBMATRIX);

        startColorStrengthConditionalBlurExecutorThreadPartition(filters, "threadpoolexecutorpartition/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD, filters.BLUE_TRESHOLD, filters.GREEN_TRESHOLD);

    }


    public static void applyFiltersThreadPoolForkJoinPool(Filters filters) {
        startBrighterForkJoin(filters, "threadpoolforkjoin/brighter.jpg", filters.BRIGHTER_VALUE);

        startGrayscaleForkJoin(filters, "threadpoolforkjoin/grayscale.jpg");

        startSwirlForkJoin(filters, "threadpoolforkjoin/swirl.jpg");

        startGlassForkJoin(filters, "threadpoolforkjoin/glass.jpg", filters.GLASS_MAXIMUM_DISTANCE);

        startBlurForkJoin(filters, "threadpoolforkjoin/blur.jpg", filters.BLUR_SUBMATRIX);

        startColorStrengthConditionalBlurForkJoin(filters, "threadpoolforkjoin/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD, filters.BLUE_TRESHOLD, filters.GREEN_TRESHOLD);
    }

    private static void startBrighterExecutorThreadPartition(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        System.out.println("Number of threads in partition:"+ numberOfThreads);
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;


        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new BrighterThread(filters, tmp, value, startRow, endRow));
            executor.execute(task);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Brighter ThreadPool Executor based with " + filters.NPARTITIONS + " partitions Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startGrayscaleExecutorThreadPartition(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;


        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new GrayscaleThread(filters, tmp, startRow, endRow));
            executor.execute(task);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Grayscale ThreadPool Executor based with " + filters.NPARTITIONS + " partitions Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startSwirlExecutorThreadPartition(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int x_matrix = tmp.length / 2;
        int y_matrix = tmp[0].length / 2;
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;


        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new SwirlThread(filters, tmp,tmpResult, startRow, endRow, x_matrix, y_matrix));
            executor.execute(task);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Swirl ThreadPool Executor based with " + filters.NPARTITIONS + " partitions Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startGlassExecutorThreadPartition(Filters filters, String outputFile, int maximumDistance) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;

        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new GlassThread(filters, tmp, startRow, endRow, maximumDistance));
            executor.execute(task);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Glass ThreadPool Executor based with " + filters.NPARTITIONS + " partitionsExecution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startBlurExecutorThreadPartition(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;


        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new BlurThread(filters, tmp, tmpResult, value, startRow, endRow));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Blur ThreadPool Executor based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");

        Utils.writeImage(tmpResult, outputFile);

    }

    private static void startColorStrengthConditionalBlurExecutorThreadPartition(Filters filters, String outputFile, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int sizePartition = tmp.length / filters.NPARTITIONS;
        float aux = (float) tmp.length / filters.NPARTITIONS;
        float rest = aux - (long) sizePartition;

        for (int i = 0; i < filters.NPARTITIONS; i++) {
            int startRow = i * sizePartition;
            int endRow = (i + 1) * sizePartition;

            if (i == filters.NPARTITIONS - 1) {
                endRow = (int) (endRow + (i + 1) * rest);
            }
            Runnable task = new Thread(new ColorStrengthConditionalBlurThread(filters, tmp, tmpResult, subMatrix, redTreshold, blueTreshold, greenThreshold, startRow, endRow));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Color Strength Conditional ThreadPool Executor based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmpResult, outputFile);

    }

    private static void startBrighterExecutorThread(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new BrighterThread(filters, tmp, value, startRow, endRow));
            executor.execute(task);
        }
        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Brighter ThreadPool Executor based Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startGrayscaleExecutorThread(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new GrayscaleThread(filters, tmp, startRow, endRow));
            executor.execute(task);
        }

        executor.shutdown();


        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Grayscale ThreadPool Executor based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startSwirlExecutorThread(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int x_matrix = tmp.length / 2;
        int y_matrix = tmp[0].length / 2;
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new SwirlThread(filters, tmp, tmpResult, startRow, endRow, x_matrix, y_matrix));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Swirl ThreadPool Executor based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startGlassExecutorThread(Filters filters, String outputFile, int maximumDistance) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new GlassThread(filters, tmp, startRow, endRow, maximumDistance));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Glass ThreadPool Executor based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);

    }

    private static void startBlurExecutorThread(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new BlurThread(filters, tmp, tmpResult, value, startRow, endRow));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Blur ThreadPool Executor based Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmpResult, outputFile);

    }

    private static void startColorStrengthConditionalBlurExecutorThread(Filters filters, String outputFile, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();

        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
        int rowsPerThread = tmp.length / numberOfThreads;
        for (int i = 0; i < numberOfThreads; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == numberOfThreads - 1) ? tmp.length : (i + 1) * rowsPerThread;
            Runnable task = new Thread(new ColorStrengthConditionalBlurThread(filters, tmp, tmpResult, subMatrix, redTreshold, blueTreshold, greenThreshold, startRow, endRow));
            executor.execute(task);
        }

        executor.shutdown();

        try {
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            executor.shutdownNow();
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Color Strength Conditional ThreadPool Executor based Execution Time: " + totalTime + " ms");
        Utils.writeImage(tmpResult, outputFile);

    }

    private static void startSwirlForkJoin(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new SwirlRecursiveAction(filters, tmp, tmpResult,0, tmp.length));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Swirl ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startBrighterForkJoin(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new BrighterRecursiveAction(filters, tmp, 0, tmp.length, value));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Brighter ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startGrayscaleForkJoin(Filters filters, String outputFile) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new GrayscaleRecursiveAction(filters, tmp, 0, tmp.length));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Grayscale ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startGlassForkJoin(Filters filters, String outputFile, int maximumDistance) {
        Color[][] tmp = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new GlassRecursiveAction(filters, tmp, 0, tmp.length, maximumDistance));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Glass ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmp, outputFile);
    }

    private static void startBlurForkJoin(Filters filters, String outputFile, int value) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new BlurRecursiveAction(filters, tmp, tmpResult, 0, tmp.length, value));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Blur ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmpResult, outputFile);
    }

    private static void startColorStrengthConditionalBlurForkJoin(Filters filters, String outputFile, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        Color[][] tmp = Utils.copyImage(filters.image);
        Color[][] tmpResult = Utils.copyImage(filters.image);
        long startTime = System.currentTimeMillis();
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
        forkJoinPool.invoke(new ColorStrengthConditionalBlurRecursiveAction(filters, tmp, tmpResult, 0, tmp.length, subMatrix, redTreshold, blueTreshold, greenThreshold));
        forkJoinPool.shutdown();
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Color Strength Conditional ThreadPool Fork Join based Execution Time: " + (totalTime) + " ms");
        Utils.writeImage(tmpResult, outputFile);
    }

    private static void startBrighterCompletableFuturePartition(Filters filters, String outputFile, int value) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();
            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                int finalStartRow = startRow;
                int finalEndRow = endRow;
                CompletableFuture.runAsync(() -> filters.BrighterFilter2(value, tmp, finalStartRow, finalEndRow));
                startRow = endRow;
            }

            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Brighter ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitions Execution Time: " + totalTime + " ms");
            Utils.writeImage(tmp, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private static void startGrayscaleCompletableFuturePartition(Filters filters, String outputFile) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();

            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                int finalStartRow = startRow;
                int finalEndRow = endRow;
                CompletableFuture.runAsync(() -> filters.GrayscaleFilter2(tmp, finalStartRow, finalEndRow));
                startRow = endRow;
            }
            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Grayscale ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");

            Utils.writeImage(tmp, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startColorStrengthConditionalBlurCompletableFuturePartition(Filters filters, String outputFile, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            Color[][] tmpResult = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();

            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                int finalStartRow = startRow;
                int finalEndRow = endRow;
                CompletableFuture.runAsync(() -> filters.colorStrengthConditionalBlur2(tmp, tmpResult, subMatrix, redTreshold, blueTreshold, greenThreshold, finalStartRow, finalEndRow));
                startRow = endRow;
            }
            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Color Strength Conditional ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");
            Utils.writeImage(tmpResult, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startBlurCompletableFuturePartition(Filters filters, String outputFile, int value) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            Color[][] tmpResult = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();

            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                int finalStartRow = startRow;
                int finalEndRow = endRow;
                CompletableFuture.runAsync(() -> filters.BlurFilter2(value, tmp, tmpResult, finalStartRow, finalEndRow));
                startRow = endRow;
            }
            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Blur ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");
            Utils.writeImage(tmpResult, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startGlassCompletableFuturePartition(Filters filters, String outputFile, int maximumDistance) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();

            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                GlassThread glassThread = new GlassThread(filters,tmp,startRow,endRow,maximumDistance);
                CompletableFuture.runAsync(glassThread::justExecute);
                startRow = endRow;
            }
            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Glass ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitionsExecution Time: " + totalTime + " ms");
            Utils.writeImage(tmp, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void startSwirlCompletableFuturePartition(Filters filters, String outputFile) {
        try {
            Color[][] tmp = Utils.copyImage(filters.image);
            Color[][] tmpResult = Utils.copyImage(filters.image);
            long startTime = System.currentTimeMillis();

            int x_matrix = tmp.length / 2;
            int y_matrix = tmp[0].length / 2;
            int sizePartition = tmp.length / filters.NPARTITIONS;
            int rest = tmp.length / filters.NPARTITIONS;
            int startRow = 0;

            for (int i = 0; i < filters.NPARTITIONS; i++) {
                int endRow = startRow + sizePartition;

                if (i == filters.NPARTITIONS - 1) {
                    endRow += rest;
                }

                int finalStartRow = startRow;
                int finalEndRow = endRow;
                CompletableFuture.runAsync(() -> filters.SwirlFilter2(tmp,tmpResult, finalStartRow, finalEndRow, x_matrix, y_matrix));
                startRow = endRow;
            }
            ForkJoinPool.commonPool().awaitTermination(30, TimeUnit.SECONDS);
            long endTime = System.currentTimeMillis();
            long totalTime = endTime-startTime;
            System.out.println("Swirl ThreadPool CompletableFuture Partitions based with " + filters.NPARTITIONS + " partitions Execution Time: " + (totalTime) + " ms");

            Utils.writeImage(tmp, outputFile);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
