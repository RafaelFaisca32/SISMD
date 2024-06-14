package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Utils {

  Utils() {}
 

   /**
   * Loads image from filename into a Color (pixels decribed with rgb values) matrix.
   * 
   * @param filename the name of the imge in the filesystem.
   * @return Color matrix.
   */
  public static Color[][] loadImage(String filename) {
    BufferedImage buffImg = loadImageFile(filename);
    Color[][] colorImg = convertTo2DFromBuffered(buffImg);
    return colorImg;
  }

  /**
   * Converts image from a Color matrix to a .jpg file.
   * 
   * @param image the matrix of Color objects.
   * @param filename to the image.
   */
  public static void writeImage(Color[][] image,String filename){
    File outputfile = new File(filename);
		var bufferedImage = Utils.matrixToBuffered(image);
		try {
          ImageIO.write(bufferedImage, "jpg", outputfile);
        } catch (IOException e) {
          System.out.println("Could not write image "+filename+" !");
          e.printStackTrace();
          System.exit(1);
        }
  }


  /**
   * Loads in a BufferedImage from the specified path to be processed.
   * @param filename The path to the file to read.
   * @return a BufferedImage if able to be read, NULL otherwise.
   */
  private static BufferedImage loadImageFile(String filename) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File(filename));
    } catch (IOException e) {
      System.out.println("Could not load image "+filename+" !");
      e.printStackTrace();
      System.exit(1);
    }
    return img;
  }

  
  /**
   *  Copy a Color matrix to another Color matrix. 
   *  Useful if one does not want to modify the original image.
   * 
   * @param image the source matrix
   * @return a copy of the image
   */
  
  public static Color[][] copyImage(Color[][] image) {
    Color[][] copy = new Color[image.length][image[0].length];
    for (int i = 0; i < image.length; i++) {
      for (int j = 0; j < image[i].length; j++) {
        copy[i][j] = image[i][j];
      }
    }
    return copy;
  }
  
  
  /**
   * Converts a matrix of Colors into a BufferedImage to 
   *  write on the filesystem.
   * 
   * @param image the matrix of Colors
   * @return the image ready for writing to filesystem
   */
  private static BufferedImage matrixToBuffered(Color[][] image) {
    int width = image.length;
    int height = image[0].length;
    BufferedImage bImg = new BufferedImage(width, height, 1);

    for (int x = 0; x < width; x++) {
      for(int y = 0; y < height; y++) {
        bImg.setRGB(x,  y, image[x][y].getRGB());
      }
    }
    return bImg;
  }

  /**
   * Converts a file loaded into a BufferedImage to a 
   * matrix of Colors
   * 
   * @param image the BufferedImage to convert
   * @return the matrix of Colors
   */

  private static Color[][] convertTo2DFromBuffered(BufferedImage image) {
    int width = image.getWidth();
    int height = image.getHeight();
    Color[][] result = new Color[width][height];

    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        // Get the integer RGB, and separate it into individual components.
        // (BufferedImage saves RGB as a single integer value).
        int pixel = image.getRGB(x, y);
        //int alpha = (pixel >> 24) & 0xFF;
        int red = (pixel >> 16) & 0xFF;
        int green = (pixel >> 8) & 0xFF;
        int blue = pixel & 0xFF;
        result[x][y] = new Color(red, green, blue);
      }
    }
    return result;
  }

  public static double dFormula(int x_middle, int y_middle, int i , int j){
    return Math.sqrt(Math.pow(i - x_middle, 2) + Math.pow(j - y_middle, 2));
  }

  public static double formulaTheta (double d){
    return (Math.PI / 256) * d;
  }

  public static Color[][] createSubmatrix(Color[][] matrix, int x, int y, int z) {
    int startX = Math.max(0, x - z / 2); // Start index for x
    int startY = Math.max(0, y - z / 2); // Start index for y
    int endX = Math.min(matrix.length - 1, x + z / 2); // End index for x
    int endY = Math.min(matrix[0].length - 1, y + z / 2); // End index for y

    // Adjust the end indices if z is even to ensure a proper center
    if (z % 2 == 0) {
      endX = Math.min(matrix.length - 1, x + z / 2 - 1);
      endY = Math.min(matrix[0].length - 1, y + z / 2 - 1);
    }

    // Create the new submatrix
    Color[][] submatrix = new Color[endX - startX + 1][endY - startY + 1];

    // Fill the submatrix with corresponding values from the original matrix
    for (int i = startX; i <= endX; i++) {
      for (int j = startY; j <= endY; j++) {
        if (matrix[i][j] != null) {
          submatrix[i - startX][j - startY] = matrix[i][j];
        }
      }
    }

    return submatrix;
  }

  public static void blurPixel(int i, int j, int subMatrix, Color[][] tmp, Color[][] tmpResult){
    int red = 0;
    int blue = 0;
    int green = 0;
    Color[][] submatrix = createSubmatrix(tmp, i, j, subMatrix);
    int totalElements = 0;

    for (int x = 0; x < submatrix.length - 1; x++) {
      for (int z = 0; z < submatrix[x].length - 1; z++) {
        if (submatrix[x][z] != null) {
          totalElements++;
          red = red + submatrix[x][z].getRed();
          blue = blue + submatrix[x][z].getBlue();
          green = green + submatrix[x][z].getGreen();
        }
      }
    }

    red = red / totalElements;
    blue = blue / totalElements;
    green = green / totalElements;

    if (red > 255) {
      red = 255;
    }

    if (green > 255) {
      green = 255;
    }
    if (blue > 255) {
      blue = 255;
    }

    tmpResult[i][j] = new Color(red, green, blue);
  }


}