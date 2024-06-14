package filters;

import utils.Utils;

import java.awt.Color;
import java.util.Random;


/**
 * Creating image filters for grayscale, brighter, swirl,
 * glass and blur effect
 * 
 * @author Jorge Coelho
 * @contact jmn@isep.ipp.pt
 * @version 1.0
 */
public class Filters {
    public final int BRIGHTER_VALUE = Integer.parseInt(System.getProperty("BRIGHTER_VALUE", "128"));
    public final int GLASS_MAXIMUM_DISTANCE = Integer.parseInt(System.getProperty("GLASS_MAXIMUM_DISTANCE", "10"));
    public final int BLUR_SUBMATRIX = Integer.parseInt(System.getProperty("BLUR_SUBMATRIX", "5"));
    public final int CONDITIONALBLUR_SUBMATRIX = Integer.parseInt(System.getProperty("CONDITIONALBLUR_SUBMATRIX", "5"));
    public final int RED_TRESHOLD = Integer.parseInt(System.getProperty("RED_TRESHOLD", "100"));
    public final int BLUE_TRESHOLD = Integer.parseInt(System.getProperty("BLUE_TRESHOLD", "150"));
    public final int GREEN_TRESHOLD = Integer.parseInt(System.getProperty("GREEN_TRESHOLD", "100"));
    public final int NPARTITIONS = Integer.parseInt(System.getProperty("NPARTITIONS", "16"));
    public final int THRESHOLD_RECURSIVE = Integer.parseInt(System.getProperty("THRESHOLD_RECURSIVE", "100"));



    String file;
    public Color[][] image;

    // Constructor with filename for source image
    public Filters(String filename) {
        this.file = filename;
        image = Utils.loadImage(filename);
    }


    // Brighter filter works by adding value to each of the red, green and blue of each pixel
    // up to the maximum of 255
    public void BrighterFilter(String outputFile, int value)  {
        Color[][] tmp = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // fetches values of each pixel
                Color pixel = tmp[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                // takes average of color values
                int bright = value;
                if (r + bright > 255)
                    r = 255;
                else
                    r = r + bright;
                if (g + bright > 255)
                    g = 255;
                else
                    g = g + bright;
                if (b + bright > 255)
                    b = 255;
                else
                    b = b + bright;

                // outputs average into picuture to make grayscale
                tmp[i][j] = new Color(r, g, b);

            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Brighter Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmp, outputFile);
    }

    public void BrighterFilter2(int value, Color[][] imageBeingProcessed, int startRow, int endRow) {
        // Runs through portion of the matrix specified by startRow and endRow
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {

                // fetches values of each pixel
                Color pixel = imageBeingProcessed[i][j];
                int r = pixel.getRed();
                int g = pixel.getGreen();
                int b = pixel.getBlue();

                // calculates new color values
                int bright = value;
                if (r + bright > 255)
                    r = 255;
                else
                    r += bright;
                if (g + bright > 255)
                    g = 255;
                else
                    g += bright;
                if (b + bright > 255)
                    b = 255;
                else
                    b += bright;

                // outputs new color into picture
                imageBeingProcessed[i][j] = new Color(r, g, b);
            }
        }
    }



    public void GrayscaleFilter(String outputFile)  {
        Color[][] tmp = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // applies the grayscale formula A = (r+g+b+) / 3
                int grayscaleValue = (tmp[i][j].getRed()+tmp[i][j].getGreen()+tmp[i][j].getBlue())/3;

                // replace each of the r,g and b by A
                tmp[i][j] = new Color(grayscaleValue, grayscaleValue, grayscaleValue);

            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Grayscale Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmp, outputFile);
    }

    public void GrayscaleFilter2(Color[][] imageBeingProcessed, int startRow, int endRow)  {

        // Runs through entire matrix
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {

                // applies the grayscale formula A = (r+g+b+) / 3
                int grayscaleValue = (imageBeingProcessed[i][j].getRed()+imageBeingProcessed[i][j].getGreen()+imageBeingProcessed[i][j].getBlue())/3;

                // replace each of the r,g and b by A
                imageBeingProcessed[i][j] = new Color(grayscaleValue, grayscaleValue, grayscaleValue);

            }
        }
    }

    public void SwirlFilter(String outputFile)  {
        Color[][] tmp = Utils.copyImage(image);
        Color[][] tmpResult = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();

        int x_middle = tmp.length / 2;
        int y_middle = tmp[0].length / 2;
        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                double theta = Utils.formulaTheta(Utils.dFormula(x_middle,y_middle,i,j));
                int newX = (int) (((i - x_middle) * Math.cos(theta)) - ((j - y_middle) * Math.sin(theta)) + x_middle);
                int newY = (int) (((i - x_middle) * Math.sin(theta)) + ((j - y_middle) * Math.cos(theta)) + y_middle);

                // we need to be careful about the matrix length
                if(newX>= tmp.length){
                    newX = tmp.length -1;
                }
                if(newY >= tmp[i].length) {
                    newY = tmp[i].length - 1;
                }
                if(newX< 0){
                    newX = 0;
                }
                if(newY < 0){
                    newY = 0;
                }

                tmp[i][j] = tmpResult[newX][newY];

            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Swirl Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmp, outputFile);
    }

    public void SwirlFilter2(Color[][] imageBeingProcessed,  Color[][] tmpResult, int startRow, int endRow, int x_middle, int y_middle)  {
        // Runs through entire matrix
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {
                double theta = Utils.formulaTheta(Utils.dFormula(x_middle,y_middle,i,j));
                int newX = (int) (((i - x_middle) * Math.cos(theta)) - ((j - y_middle) * Math.sin(theta)) + x_middle);
                int newY = (int) (((i - x_middle) * Math.sin(theta)) + ((j - y_middle) * Math.cos(theta)) + y_middle);

                // we need to be careful about the matrix length
                if(newX>= imageBeingProcessed.length){
                    newX = imageBeingProcessed.length -1;
                }
                if(newY >= imageBeingProcessed[i].length) {
                    newY = imageBeingProcessed[i].length - 1;
                }
                if(newX< 0){
                    newX = 0;
                }
                if(newY < 0){
                    newY = 0;
                }

                imageBeingProcessed[i][j] = tmpResult[newX][newY];

            }
        }
    }


    public void GlassFilter(String outputFile, int maximumDistance, Random random) {
        Color[][] tmp = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {

                // Generate random offsets within maximumDistance range
                int xOffset = random.nextInt(2 * maximumDistance + 1) - maximumDistance;
                int yOffset = random.nextInt(2 * maximumDistance + 1) - maximumDistance;

                // Calculate new coordinates
                int x = i + xOffset;
                int y = j + yOffset;

                // Check if the new coordinates are within the bounds of the image
                if (x >= 0 && x < tmp.length && y >= 0 && y < tmp[x].length) {
                    Color currentPixel = tmp[i][j];
                    Color neighbourPixel = tmp[x][y];
                    // Swapping the 2 pixels
                    tmp[x][y] = currentPixel;
                    tmp[i][j] = neighbourPixel;
                }


            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Glass Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmp, outputFile);
    }

    public void GlassFilter2(Color[][] imageBeingProcessed, int startRow, int endRow, int maximumDistance, Random random) {
        // Runs through entire matrix
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {
                // Generate random offsets within maximumDistance range
                int xOffset = random.nextInt(2 * maximumDistance + 1) - maximumDistance;
                int yOffset = random.nextInt(2 * maximumDistance + 1) - maximumDistance;

                // Calculate new coordinates
                int x = i + xOffset;
                int y = j + yOffset;

                // Check if the new coordinates are within the bounds of the image
                if (x >= 0 && x < imageBeingProcessed.length && y >= 0 && y < imageBeingProcessed[x].length) {
                    Color currentPixel = imageBeingProcessed[i][j];
                    Color neighbourPixel = imageBeingProcessed[x][y];
                    // Swapping the 2 pixels
                    imageBeingProcessed[x][y] = currentPixel;
                    imageBeingProcessed[i][j] = neighbourPixel;
                }
            }
        }
    }


    public void BlurFilter(String outputFile , int subMatrix)  {
        Color[][] tmp = Utils.copyImage(image);
        Color[][] tmpResult = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();
        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                Utils.blurPixel(i,j,subMatrix,tmp,tmpResult);
            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Blur Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmpResult, outputFile);
    }

    public void BlurFilter2(int subMatrix , Color[][] imageBeingProcessed, Color[][] finalImage, int startRow, int endRow)  {
        // Runs through entire matrix
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {
                Utils.blurPixel(i,j,subMatrix,imageBeingProcessed,finalImage);
            }
        }
    }

    public void colorStrengthConditionalBlur(String outputFile , int subMatrix, int redTreshold, int blueTreshold, int greenThreshold) {
        Color[][] tmp = Utils.copyImage(image);
        Color[][] tmpResult = Utils.copyImage(image);
        long startTime = System.currentTimeMillis();

        // Runs through entire matrix
        for (int i = 0; i < tmp.length; i++) {
            for (int j = 0; j < tmp[i].length; j++) {
                if(tmp[i][j].getRed()>redTreshold && tmp[i][j].getBlue()>blueTreshold && tmp[i][j].getGreen()>greenThreshold) {
                    Utils.blurPixel(i,j,subMatrix,tmp,tmpResult);
                }

            }
        }
        long endTime = System.currentTimeMillis();
        long totalTime = endTime-startTime;
        System.out.println("Color Strength Conditional Blur Sequential Execution Total Time: " + (totalTime) + "ms");
        Utils.writeImage(tmpResult, outputFile);
    }

    public void colorStrengthConditionalBlur2(Color[][] imageBeingProcessed, Color[][] finalImage, int subMatrix, int redTreshold, int blueTreshold, int greenThreshold, int startRow, int endRow) {
        // Runs through entire matrix
        for (int i = startRow; i < endRow; i++) {
            for (int j = 0; j < imageBeingProcessed[i].length; j++) {
                if(imageBeingProcessed[i][j].getRed()>redTreshold && imageBeingProcessed[i][j].getBlue()>blueTreshold && imageBeingProcessed[i][j].getGreen()>greenThreshold) {
                    Utils.blurPixel(i,j,subMatrix,imageBeingProcessed,finalImage);
                }

            }
        }
    }


}
