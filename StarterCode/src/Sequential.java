import filters.Filters;

import java.util.Random;

public class Sequential {

    public static void startSequential(Filters filters) {
        applyFiltersSequentially(filters);
        System.out.println("---------------------------------------------------- ");
    }

    public static void applyFiltersSequentially(Filters filters) {
        filters.BrighterFilter("sequential/brighter.jpg", filters.BRIGHTER_VALUE);

        filters.GrayscaleFilter("sequential/grayscale.jpg");

        filters.SwirlFilter("sequential/swirl.jpg");

        filters.GlassFilter("sequential/glass.jpg",filters.GLASS_MAXIMUM_DISTANCE, new Random());

        filters.BlurFilter("sequential/blur.jpg", filters.BLUR_SUBMATRIX);

        filters.colorStrengthConditionalBlur("sequential/conditionalBlur.jpg", filters.CONDITIONALBLUR_SUBMATRIX, filters.RED_TRESHOLD,filters.BLUE_TRESHOLD,filters.GREEN_TRESHOLD);

    }


}