package Tetrisize;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Color;

public class ImageManip {

    public static int[][] toArray(BufferedImage img, int numGrayValues) {
        int xRes = img.getWidth();
        int yRes = img.getHeight();
        int[][] pixels = new int[xRes][yRes];
        float range = 255.0f / numGrayValues;
        for (int i = 0; i < xRes; i++)
        for (int j = 0; j < yRes; j++) {
            Color c = new Color(img.getRGB(i, j));
            //System.out.println(c.getGreen() + ", " + c.getRed());
            // Since it's gray scale, any color will work
            int val = c.getGreen();
            pixels[i][j] = (int) Math.floor(val / range);
        }
        return pixels;
    }

    public static BufferedImage loadImage(String filename) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.out.println("No such file exists: " + filename);
            System.exit(0);
        }
        makeGray(img);
        return img;
    }

    public static void makeGray(BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++)
        for (int y = 0; y < img.getHeight(); y++) {
            int rgb = img.getRGB(x, y);
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = (rgb & 0xFF);

            int grayLevel = (r + g + b) / 3;
            int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
            img.setRGB(x, y, gray);
        }
    }

}
