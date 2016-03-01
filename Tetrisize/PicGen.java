package Tetrisize;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;

import java.text.ParseException;

public class PicGen extends JPanel {
/*
	public static int xres = 750;
	public static int yres = 270;
    public static int size = 10;
//*/
/*
    public static int xres = 500;
	public static int yres = 500;
    public static int size = 10;
//*/	
/*
	public static int xres = 850;
	public static int yres = 1000;
    public static int size = 10;
//*/
/*
	public static int xres = 1000;
	public static int yres = 800;
    public static int size = 10;
//*/
    public static int xRes;
    public static int yRes;
    public static int size = 10;
	public int[][] tiles; 
    public static String fname = null; 
    
    

	//public static int[][] test1 = toArray(loadimage("test.jpg"));
   public static void main(String[] args) {
   		/*for(int i = 0; i < test1.length; i++){
   			for(int j = 0; j < test1[0].length; j++){
   				System.out.print(test1[j][i]);
   				System.out.print(" ");
   				}
   				System.out.println();
   				}*/

      if(args.length > 0){
        fname = "completed_images/" + args[0];
      }


      JFrame frame = new JFrame();
      PicGen pic = new PicGen();
      pic.tiles = pic.parseSolution("../result.sol");
      frame.getContentPane().add(pic);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      // 21 and 56 are values that make the whole image visible on windows 10. Different values for different OS's
      frame.setSize(pic.xRes * pic.size + 21, pic.yRes * pic.size + 56);
      frame.setVisible(true);
   }
	
	public Color c(int n){ 
        //if (true) return new Color(14 * n, 14 * n, 14 * n);
        switch (n) {
            case 0:
                return new Color(36 * 0, 36 * 0, 36 * 0);
            case 1:
            case 2:
                return new Color(36 * 1, 36 * 1, 36 * 1);
            case 3:
            case 4:
            case 5:
            case 6:
                return new Color(36 * 2, 36 * 2, 36 * 2);
            case 7:
            case 8:
            case 9:
            case 10:
                return new Color(36 * 3, 36 * 3, 36 * 3);
            case 11:
            case 12:
            case 13:
            case 14:
                return new Color(36 * 4, 36 * 4, 36 * 4);
            case 15:
            case 16:
                return new Color(36 * 5, 36 * 5, 36 * 5);
            case 17:
            case 18:
                return new Color(36 * 6, 36 * 6, 36 * 6);
        }
		return null;
	}

    public int[][] parseSolution(String fileName) {
        int[][] returnList = null;
        File f = new File(fileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        BufferedReader br = null;
        try {
            /*
             * First find what the height and width are
             */ 
			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
	 		br = new BufferedReader(new InputStreamReader(bis));
	        String line;
            int width = 0;
            int height = 0;
			while ((line = br.readLine()) != null) {
	            if (line.startsWith("x")) {
                    String variable = line.substring(1, line.length() - 2);
                    String[] vals = variable.split(",");
                    int x = Integer.parseInt(vals[0]);
                    int y = Integer.parseInt(vals[1]);
                    if (x > width) width = x;
                    if (y > height) height = y;
                }
            } 
            xRes = ++width;
            yRes = ++height;
	        fis.close();
	        bis.close();
	        br.close();

            returnList = new int[width][height];
            for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                returnList[i][j] = -1;
            }

			fis = new FileInputStream(f);
			bis = new BufferedInputStream(fis);
	 		br = new BufferedReader(new InputStreamReader(bis));
			while ((line = br.readLine()) != null) {
	            if (line.startsWith("x") && line.endsWith(" 1")) {
                    String variable = line.substring(1, line.length() - 2);
                    String[] vals = variable.split(",");
                    int x = Integer.parseInt(vals[0]);
                    int y = Integer.parseInt(vals[1]);
                    int c = Integer.parseInt(vals[2]);
                    System.out.println("x" + x + "," + y + "," + c);
                    if (returnList[x][y] != -1 ) {
                        throw new ParseException("Two tetris blocks in one cell, WOOPS", 0);
                    }
                    returnList[x][y] = c;         
	            }
	        }
	        fis.close();
	        bis.close();
	        br.close();
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        } 

        return returnList;

    }

  	 

	//generates a color array of tetris tiles from a 2D array of upper-left corner values
	public Color[][] fillgrid() {	
		
		//Grid is a 2d array of colors 
		Color[][] grid = new Color[xRes * size][yRes * size];
		
		//Make background Red
		Color red = new Color(200,0,0);
		for (int x = 0; x < tiles.length; x++) {
    		for (int y = 0; y < tiles[x].length; y++) {
    			grid[x][y] = red;
    	}}
		
		//Place Tiles on grid
        System.out.println("tiles length: " + tiles.length);
	 	for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[x].length; y++) {
                switch (tiles[x][y]) {
                
                case 0: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0 || i== ((2* size)-1) || j==0 || j== ((2*size)-1)){
                                    grid[x * size + i][y * size + j] = c(1);
                                }
                                else{
                                    grid[x * size + i][y * size + j] = c(0);
                                    }
                            }
                        }
                         break;
              
               case 1:  
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 4 * size; j++){
                                if(i == 0 || i == ((size)-1) || j == 0 || j == ((4 * size)-1)){
                                    grid[x * size + i][y * size + j] = c(0);
                                }
                                else{
                                    grid[x * size + i][y * size + j] = c(1);
                                }
                            }
                        }
                        break;
                case 2: 
                        for (int i = 0; i < 4* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0 || i== ((4*size)-1) || j==0 || j== ((size)-1)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(2);
                                }
                            }
                        }
                        break;
                case 3: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0 || j==0 ||(i==(2*size-1)|| (j==(size-1) && i<=size))){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(3);
                                }
                            }
                         }
                                
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i == 0 || (j==(2*size-1)) || i==(size-1) ){
                                    grid[size+x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][size+y*size+j] = c(3);
                                    }
                            }
                        }
                        break;
                        
                case 4: 
                        for (int i = 0; i < 2 * size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0 || j==0||j==(size-1)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(4);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==(size-1) || (i==0 && j<=size) || j==0 || j == (2*size-1)){
                                    grid[2*size+x*size+i][y*size+j - size] = c(0);
                                }
                                else{
                                     grid[2*size+x*size+i][y*size+j - size] = c(4);
                                }
                            }
                        }
                        break;
                        
                case 5: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0 || j==(size-1) || i==(2*size-1) || (j==0 && i>=(size-1))){
                                    grid[x*size+i][2*size+y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][2*size+y*size+j] = c(5);
                                }
                            }
                        }
                        
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0 || j==0 || i==(size-1)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(5);
                                }
                            }
                        }
                        break;
                        
                case 6: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(j==0||j==(size-1) ||i==(2*size)-1){
                                        grid[size+x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j] = c(6);
                                }
                                    
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0||j==0||j==(2*size)-1||(i==(size-1) && j>=size)){
                                        grid[x*size+i][y*size+j] = c(0);
                                    }
                                    else{
                                        grid[x*size+i][y*size+j] = c(6);
                                    }
                            }
                        }
                        break;
                case 7: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||i==(2*size)-1||j==0||(j==(size-1)&&i>=size)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(7);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0||j==(2*size)-1||i==(size-1)){
                                    grid[x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][size+y*size+j] = c(7);
                                }
                            }
                        }
                        break;
                case 8:
                        for (int i = 0; i < 2 * size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||j==0||j==(size-1)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(8);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if((i==0 && j>=size) || j==(2*size-1)||i==(size-1)||j==0){
                                    grid[2*size+x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[2*size+x*size+i][y*size+j] = c(8);

                                }
                            }
                        }
                        break;
                case 9: 
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||i==(2*size-1)||j==(size-1)||(j==0&&i<=size)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(9);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0||i==(size-1)||j==0){
                                    grid[size+x*size+i][y*size+j-2*size] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j-2*size] = c(9);
                                }
                            }
                        }
                        break;
                case 10:
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                                if(j==0||i==(2*size)-1||j==(size-1)){
                                    grid[size+x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][size+y*size+j] = c(10);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0||j==0||j==(2*size-1)||(i==(size-1) && j<=size)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(10);
                                }
                            }
                        }
                        break;
                        
                case 11: 
                        for (int i = 0; i < 3* size; i++){
                            for (int j = 0; j < size; j++){
                                if(j==(size-1)||i==0||i==(3*size)-1||(j==0 && i<= size) || (j==0 && i>= (2*size-1))){
                                    grid[x*size+i][y*size+j] = c(0);
                                    }
                                else{
                                    grid[x*size+i][y*size+j] = c(11);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||j==0||i==(size-1)){
                                    grid[size+x*size+i][y*size+j-size] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j-size] = c(11);
                                }
                            }
                        }
                        break;
                case 12:
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < size; j++){
                                if(j==0||j==(size-1)||i==(size-1)){
                                    grid[size+x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][size+y*size+j] = c(12);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 3 * size; j++){
                                if(i==0||j==0||j==(3*size)-1|| (i==size-1 && j<=size) || (i==size-1 && j>=(2*size))){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(12);
                                }
                            }
                        }
                        break;
                case 13: 
                        for (int i = 0; i < 3* size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||j==0||i==(3*size)-1||j==(size)-1 && i<=size||j==(size)-1 && i>=2*size-1){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(13);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||i==(size-1)||j==(size-1)){
                                    grid[size+x*size+i][y*size+j+size] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j+size] = c(13);
                                }
                            }
                        }
                        break;
                case 14:
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||j==0||j==(size-1)){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(14);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 3 * size; j++){
                                if(i==0 && j <= size||i==0 && j>=(2*size)-1|| j==0 || j== (3*size-1)||i==(size-1)){
                                    grid[size+x*size+i][y*size+j-size] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j-size] = c(14);
                                }
                            }
                        }
                        break;
                case 15: 
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                            if(i==0||j==0||j==(2*size)-1||i==(size-1) && j>=size-1){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(15);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                            if(i==(size-1)||j==0||j==(2*size)-1||i==0 && j<=size){
                                    grid[size+x*size+i][y*size+j-size] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][y*size+j-size] = c(15);
                                }
                            }
                        }
                        break;
                case 16: 
                        for (int i = 0; i < 2 *size; i++){
                            for (int j = 0; j < size; j++){
                            if(i==0||j==0||i==(2*size)-1||j==(size-1) && i<=size){
                                grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                grid[x*size+i][y*size+j] = c(16);
                                }
                            }
                        }
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                            if(i==0||i==(2*size)-1||j==(size-1)||j==0 && i>=size-1){
                                    grid[size+x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][size+y*size+j] = c(16);
                                }
                            }
                        }
                        break;
                case 17: 
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                                if(i==0||j==0||j==(2*size)-1||i==(size-1)&&j<=size){
                                grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                grid[x*size+i][y*size+j] = c(17);
                                }
                            }
                        }
                        for (int i = 0; i < size; i++){
                            for (int j = 0; j < 2* size; j++){
                            if(i==0&&j>=size||j==0||j==(2*size)-1||i==size-1){
                                    grid[size+x*size+i][size+y*size+j] = c(0);
                                }
                                else{
                                    grid[size+x*size+i][size+y*size+j] = c(17);
                                }
                            }
                        }
                        break;
                case 18: 
                        for (int i = 0; i < 2 *size; i++){
                            for (int j = 0; j < size; j++){
                                if(i==0||i==(2*size)-1|| j==(size-1) || j==0&&i<=size){
                                    grid[x*size+i][y*size+j] = c(0);
                                }
                                else{
                                    grid[x*size+i][y*size+j] = c(18);
                                }
                            }
                        }
                        for (int i = 0; i < 2* size; i++){
                            for (int j = 0; j < size; j++){
                            if(i==0||i==(2*size)-1||j==0||j==(size-1)&&i>=size-1){
                                grid[size+x*size+i][y*size+j-size] = c(0);
                                }
                                
                            else{
                                grid[size+x*size+i][y*size+j-size] = c(18);
                            }
                            }
                        }
                        break;
         }
                }
            }
        	
        	return grid;
    	}

   private Image createImage(){
      BufferedImage bufferedImage = new BufferedImage(xRes * size, yRes * size, BufferedImage.TYPE_INT_RGB);
      Color[][] grid = fillgrid();
      for (int x = 0; x < grid.length; x++) {
    	for (int y = 0; y < grid[x].length; y++) {
            if (grid[x][y] == null) continue;
        	bufferedImage.setRGB(x, y, grid[x][y].getRGB());
        	}
        } 
      return bufferedImage;
   }
   
   	public void paint(Graphics g) {
    	  Image img = createImage();
          if (fname != null) {
	          try {
	            ImageIO.write((BufferedImage) img, "png", new File(fname + ".png"));
	          } catch (IOException e) {
	            System.out.println("Could not save file properly: " + e.getMessage());
	            System.exit(0);
	          }
          }
      	  g.drawImage(img, 0, 0, this);
  	 }
   
}
