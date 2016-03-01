package Tetrisize;

import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.BufferedInputStream;
import java.util.ArrayList;

/**
 *  Authors: 
 * Eli Rosenberg
 * Conrad Schloer
 */

public class GenerateLP {

    // Size of square board
    static int xRes = 0; //85; 
    static int yRes = 0;//100;
    // Number of tetris pieces
    static int numPieces = 19;

    
    public static void main(String[] args) {
        /*int[][] rList = parseSolution("../result.sol", n, n); 
        for (int i = 0; i < n; i++)
        for (int j = 0; j < n; j++) {
            if (rList[i][j] != -1) {
                System.out.println(i + "," + j + "," + rList[i][j]);
            }
        }
        if (true) return;*/ 
	    PrintWriter writer = null;
        try {
	        File outputFile = new File("../output.lp");
	        try {
	            writer = new PrintWriter(outputFile);
	        } catch (IOException e) {
	            System.out.println("Unable to print output, output file not found (" + outputFile.getAbsolutePath() + ")");
	            System.out.println(e.getMessage());
	            return;
	        }


            // Constant grey scale values for each tetris piece
            int[] tetrisDarknessValues = new int[numPieces];
            for (int k = 0; k < numPieces; k++) {
                switch (k) {
                    case 0:
                        tetrisDarknessValues[k] = 0;
                        break;
                    case 1:
                    case 2:
                        tetrisDarknessValues[k] = 1;
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        tetrisDarknessValues[k] = 2;
                        break;
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                        tetrisDarknessValues[k] = 3;
                        break;
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                        tetrisDarknessValues[k] = 4;
                        break;
                    case 15:
                    case 16:
                        tetrisDarknessValues[k] = 5;
                        break;
                    case 17:
                    case 18:
                        tetrisDarknessValues[k] = 6;
                        break; 
                }
                //tetrisDarknessValues[k] = k;
            }
	
	        int[][] imageDarknessValues = null; 
            if (args.length > 0) {
                imageDarknessValues = ImageManip.toArray(ImageManip.loadImage(args[0]), 7); 
            }
            xRes = imageDarknessValues.length;
            yRes = imageDarknessValues[0].length;


	        writer.println("\\ Input File for Gurobi LP solver");

            // Create the objective function 
	        writer.println("Minimize");
            String objective = "";
            for (int i = 0; i < xRes; i++) {
                for (int j = 0; j < yRes; j++) {
                    objective += " + z" + i + j;
                    
                }
            }
            writer.println(objective);

            // Create the constraints
	        writer.println("Subject To");

            /** 
             * Constraints for z, which is difference between
             * desired darkness val and actual darkness val
            **/

	        writer.println("\\ Constraints for the z's to be absolute value");
            for (int i = 0; i < xRes; i++) {
                System.out.println("On row " + i + " for absolute value");
                for (int j = 0; j < yRes; j++) {
                    String inequality = " <=";
                    String lhs1Pos = "";
                    String lhs1Neg = "";
                    
                    lhs1Pos += sumTouchingTiles(i, j, tetrisDarknessValues, true, true);
                    lhs1Neg += sumTouchingTiles(i, j, tetrisDarknessValues, true, false);
                    String lhs2 = " z" + i + j; 

                    String rhs = "";
                    rhs += " " + imageDarknessValues[i][j];

                    writer.println(lhs1Pos + " -" + lhs2 + inequality + rhs);
                    writer.println(lhs1Neg + " -" + lhs2 + inequality + " -" + rhs); 
                }
            }

            /** 
             * Constraints for each tile i, j, to ensure
             * exactly 1 of the ?? potential tiles covering it up
             * will actually be used
            **/

	        writer.println("\\ Constraints to ensure no overlap or empty cells");
            for (int i = 0; i < xRes; i++) {
                System.out.println("On row " + i + " for no overlap");
                for (int j = 0; j < yRes; j++) {
                    String rhs = " 1";
                    String inequality = " =";
                    String lhs = sumTouchingTiles(i, j, null, false, true);

                    writer.println(lhs + inequality + rhs);
                }
            }

            /** 
             * Constraints for each tile i, j, to ensure
             * no pieces go off the edge
            **/

	        writer.println("\\ Constraints to ensure no tetris pieces go off the bottom edge");
            String rhs = " 0";
            String inequality = " =";
            String lhs = sumEdgeTilesBottom();
            writer.println(lhs + inequality + rhs);

	        writer.println("\\ Constraints to ensure no tetris pieces go off the right edge");
            rhs = " 0";
            inequality = " =";
            lhs = sumEdgeTilesRight();
            writer.println(lhs + inequality + rhs);

	        writer.println("\\ Constraints to ensure no tetris pieces go off the top edge");
            rhs = " 0";
            inequality = " =";
            lhs = sumEdgeTilesTop();
            writer.println(lhs + inequality + rhs);

            writer.println("\\ Ensure all variables are binary");
            writer.println("Binary");
            String rString = "";
            for (int i = 0; i < xRes; i++) {
                System.out.println("Doing binary for row " + i);
            for (int j = 0; j < yRes; j++)
            for (int c = 0; c < numPieces; c++) {
                writer.print(" x" + i + "," + j + "," + c);
            }
            }
            //writer.println(rString);
            System.out.println("Done");

        } finally {
            if (writer != null) writer.close();
        }
        

    }

    public static String sumTouchingTiles(int i, int j, int[] tetrisDarknessValues, boolean useDarkness, boolean isPositive) {
        String rString = "";
        String operation = "";
        if (isPositive) {
            operation = " + ";
        } else {
            operation = " - ";
        }

        // #0 Check all tetris pieces from x = i, y = j that could impact this tile
        int x = i;
        int y = j;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #1 Check all tetris pieces from x = i - 1, y = j that could touch this tile
        x = i - 1;
        y = j;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                0, 2, 3, 4, 6, 7, 8, 9, 11, 13, 14, 15, 16, 18
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #2 Check all tetris pieces from x = i - 2, y = j that could touch this tile
        x = i - 2;
        y = j;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                2, 4, 6, 8, 11, 13
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #3 Check all tetris pieces from x = i - 3, y = j that could touch this tile
        x = i - 3;
        y = j;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                2
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #4 Check all tetris pieces from x = i - 2, y = j + 1 that could touch this tile
        x = i - 2;
        y = j + 1;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                4, 18
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #5 Check all tetris pieces from x = i - 1, y = j + 1 that could touch this tile
        x = i - 1;
        y = j + 1;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                9, 11, 14, 15, 18
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #6 Check all tetris pieces from x = i - 1, y = j + 2 that could touch this tile
        x = i - 1;
        y = j + 2;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                9
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #7 Check all tetris pieces from x = i - 2, y = j - 1 that could touch this tile
        x = i - 2;
        y = j - 1;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                8, 10, 16
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #8 Check all tetris pieces from x = i - 1, y = j - 1 that could touch this tile
        x = i - 1;
        y = j - 1;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                0, 3, 10, 12, 13, 14, 16, 17
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #9 Check all tetris pieces from x = i - 1, y = j - 2 that could touch this tile
        x = i - 1;
        y = j - 2;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                3, 5, 17
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #10 Check all tetris pieces from x = i, y = j - 3 that could touch this tile
        x = i;
        y = j - 3;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                1
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #11 Check all tetris pieces from x = i, y = j - 2 that could touch this tile
        x = i;
        y = j - 2;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                1, 5, 7, 12
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        // #12 Check all tetris pieces from x = i, y = j - 1 that could touch this tile
        x = i;
        y = j - 1;
        if (x >= 0 && y >= 0 && x < xRes && y < yRes) { 
            int[] touchingPieces = {
                0, 1, 5, 6, 7, 10, 12, 15, 17
            };
            for (int k : touchingPieces) { 
                if (useDarkness) {
                    rString += operation + tetrisDarknessValues[k] + " x" + x + "," + y + "," + k;  
                } else {
                    rString += operation + "x" + x + "," + y + "," + k;  
                }
            }
        }

        return rString;
    }

    /**
     * A sum of all of the tiles that might start on this cell that would go off the bottom edge
     */
    public static String sumEdgeTilesBottom() {
        System.out.println("Summing edge tiles on bottom");
        String rString = "";
        for (int i = 0; i < xRes; i++) {
            int x = i;

            // All of the overlaps  from y=n-3 (anything of downward vertical length 4+) 
            int y = yRes - 3;
            int[] overlappingPieces = {
                1
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            }

            // All of the overlaps  from y=n-2 (anything of downward vertical length 3+) 
            y = yRes - 2;
            overlappingPieces = new int[] {
                1, 3, 5, 7, 12, 17
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            }
            
            // All of the overlaps  from y=n-1 (anything of downward vertical length 2+) 
            y = yRes - 1;
            overlappingPieces = new int[] {
                0, 1, 3, 5, 6, 7, 8, 10, 12, 13, 14, 15, 16, 17
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            } 
        } 
        return rString;
    }

    /**
     * A sum of all of the tiles that might start on this cell that would go off the right edge
     */
    public static String sumEdgeTilesRight() {
        System.out.println("Summing edge tiles on right");
        String rString = "";
        for (int j = 0; j < yRes; j++) {
            int y = j;

            // All of the overlaps  from x=n-3 (anything of horizontal length 4+) 
            int x = xRes - 3;
            int[] overlappingPieces = {
                2
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            }

            // All of the overlaps  from x=n-2 (anything of horizontal length 3+) 
            x = xRes - 2;
            overlappingPieces = new int[] {
                2, 4, 6, 8, 10, 11, 13, 16, 18
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            }
            
            // All of the overlaps  from x=n-1 (anything of horizontal length 2+) 
            x = xRes - 1;
            overlappingPieces = new int[] {
                0, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            } 
        }
        return rString;
    }

    /**
     * A sum of all of the tiles that might start on this cell that would go off the top edge
     */
    public static String sumEdgeTilesTop() {
        System.out.println("Summing edge tiles on top");
        String rString = "";
        for (int i = 0; i < xRes; i++) {
            int x = i;

            // All of the overlaps  from y=1 (anything of upward vertical length 3+) 
            int y = 1;
            int[] overlappingPieces = {
                9
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            }
            
            // All of the overlaps  from y=0 (anything of upward vertical length 2+) 
            y = 0;
            overlappingPieces = new int[] {
                4, 9, 11, 14, 15, 18
            };
            for (int k : overlappingPieces) {
                rString += " + x" + x + "," + y + "," + k;  
            } 
        }

        return rString;
    }


}
