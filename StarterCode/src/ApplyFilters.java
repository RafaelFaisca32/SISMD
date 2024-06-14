import filters.Filters;

import java.util.Scanner;

public class ApplyFilters {

    public static void main(String[] args) throws InterruptedException {


        Filters filters = ApplyFilters.initializeFilters();

        // Sequential approach
        Sequential.startSequential(filters);

        // Multithreaded implementation using ExecutorService
        Multithreaded.startMultithread(filters);

        // Threadpool-based implementation using CompletableFuture
        Threadpool.startThreadpool(filters);

        // Garbage Collector tuning
        // Perform any necessary configuration or tuning of the garbage collector here

    }

    public static Filters initializeFilters() {
        Scanner input = new Scanner(System.in);
        System.out.println("Insert the name of the file path you would like to use:");
        String filePath = input.nextLine();
        input.close();
        return new Filters(filePath);
    }

}


