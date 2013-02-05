// Board.java
package tetris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board	{
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private int [] widths;
	private int previousMaxHeight;
	private int maxHeight;
	private int [] heights;
	private int [] previousWidths;
	private int [] previousHeights;
	private boolean[][] previousGrid;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private ArrayList<Integer> clearedRowArrayList;
	boolean committed;
	
	
	// Here a few trivial methods are provided:
	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	*/
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		previousGrid = new boolean[width][height];
		grid = new boolean[width][height];
		
		// all these 4 arrays init to 0
		widths = new int[height];
		heights = new int[width];
		previousWidths = new int[height];
		previousHeights = new int[width];
		maxHeight = 0;
		previousMaxHeight = maxHeight;
		clearedRowArrayList = new ArrayList<Integer>();	
		commit();
	}
	
	
	/**
	 Returns the width of the board in blocks.
	*/
	public int getWidth() {
		return width;
	}
	
	
	/**
	 Returns the height of the board in blocks.
	*/
	public int getHeight() {
		return height;
	}
	
	
	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	*/
	public int getMaxHeight() {	 
		return maxHeight; // YOUR CODE HERE
	}
	
	
	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	*/
	public void sanityCheck() {
		if (DEBUG) {

			int[] tempWidths = new int[height];
			int[] tempHeights = new int[width];
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					
					if (grid[j][i]) {
						tempWidths[i]=tempWidths[i] + 1;
						tempHeights[j]=((i+1)>tempHeights[j])?i+1:tempHeights[j];
					}
					
				}	
			}
			//check heights
			for (int i = 0; i < tempHeights.length; i++) {
				if (tempHeights[i]!=heights[i]) {
					System.out.printf(" !!!!!!! heights off %s should be %s at %s \n", heights[i], tempHeights[i], i);
				}
			}
			//check widths
			for (int i = 0; i < tempWidths.length; i++) {
				if (tempWidths[i]!=widths[i]) {
					System.out.printf("!!!!!! widths off %s should be %s at %s \n", widths[i], tempWidths[i], i);
				}
			}
			
			
			
			//check that the maxHeight is the highest height
			for (int i = 0; i < width; i++) {
				if(maxHeight<heights[i]) throw new RuntimeException(" maxHeight or height aint workin");
				//if(maxHeight>height) throw new RuntimeException("game should be over");
			}
			//check that the widths are appropriate
			for (int i = 0; i < height; i++) {
				if((width<widths[i])){
					System.out.printf("width %s is %s", i,widths[i]);
					throw new RuntimeException(" widths are out of bounds high");
				}
				if((widths[i]<0)){
					System.out.printf("width %s is %s", i,widths[i]);
					throw new RuntimeException(" widths are low");
				}
			}
			
			if(clearedRowArrayList.size()>maxHeight) throw new RuntimeException("Clearing to many rows");
			
			
		}
	}
	
	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.
	 
	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	*/
	public int dropHeight(Piece piece, int x) {
		int theMax =0;
		for (int i = 0; i < piece.getSkirt().length; i++) {
			if (theMax<(heights[x+i]-piece.getSkirt()[i])) {
				theMax=heights[x+i]-piece.getSkirt()[i];
			}
		}
		return theMax; // YOUR CODE HERE
	}
	
	
	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	*/
	public int getColumnHeight(int x) {
		return heights[x]; // YOUR CODE HERE
	}
	
	/**
	 Returns the number of filled blocks in
	 the given row.
	*/
	public int getRowWidth(int y) {
		 return widths[y]; // YOUR CODE HERE
	}
	
	
	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	*/
	public boolean getGrid(int x, int y) {
		if (x>=width || y>=height) {
			System.out.printf("asking for way high nums man maxHeight= %s width = %s height = %s x=%s y=%s!!",maxHeight, width, height, x,y);
			return false;
		}else{
			return grid[x][y]; // YOUR CODE HERE
		}
	}
	
	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;
	
	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.
	 
	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	*/
	public int place(Piece piece, int x, int y) {
	
		// flag !committed problem
		commit();
		if (!committed) throw new RuntimeException("place commit problem");
		
		int result = PLACE_OK;
		clearedRowArrayList.removeAll(clearedRowArrayList);	
		ArrayList<Integer> changedColumnsArrayList = new ArrayList<Integer>();
		for (TPoint tp:piece.getBody()) {
			
			//TODO check if its width or width-1 same for height
			// check out of bounds x
			if (((x+tp.x)>=width)||((x+tp.x)<0)) {
				//undo();
				if (DEBUG) {
					System.out.printf("Place Out of Bounds! \n");
				}
				return PLACE_OUT_BOUNDS;
			}
			// check out of bounds y
			if (((y+tp.y)>=height)||((y+tp.y)<0)) {
				//undo();
				if (DEBUG) {
					System.out.printf("Place Out of Bounds! \n");
				}
				return PLACE_OUT_BOUNDS;
			}
			if (DEBUG) {
				System.out.printf("Not Out of Bounds! \n");
			}
			// attempt to place each point
			if (!getGrid((x+tp.x),(y+tp.y))) {
				// Success!
				grid[x+tp.x][y+tp.y]=true;
				// Update widths
				widths[y+tp.y] = widths[y+tp.y] + 1;
				// check for cleared row
				if (widths[y+tp.y]>=(width)) {
					if (DEBUG) {
						System.out.printf("Row Filled! \n");
					}
					result = PLACE_ROW_FILLED;
					clearedRowArrayList.add(y+tp.y);
				}
				
				// Update heights
				/*if ((y+tp.y)>(heights[x+tp.x])) {
					if (DEBUG) {
						System.out.printf("height[%s]:from %s to %s ",x+tp.x,heights[x+tp.x],y+tp.y);						
					}
					heights[x+tp.x] = y+tp.y;
					
				}else{
					if (DEBUG) {
						System.out.printf("height[%s]:from %s to %s ",x+tp.x,heights[x+tp.x],heights[x+tp.x]+1);						
					}
					heights[x+tp.x] = heights[x+tp.x] + 1;
				
				}
				*/
				if (DEBUG) {
					System.out.printf("height[%s]:from %s to %s ",x+tp.x,heights[x+tp.x],y+tp.y);						
				}
				
				if(heights[x+tp.x] < y+tp.y+1) heights[x+tp.x] = y+tp.y+1;
				
				if (!changedColumnsArrayList.contains(x+tp.x)) {
					changedColumnsArrayList.add(x+tp.x);
				}
			}else{
				//overlapping!
				//undo();
				if (DEBUG) {
					System.out.printf("Overlapping piece! \n");
				}
				return PLACE_BAD;
			}
		}
		

		
		//update maxheight.. only check the columns that were changed
		for (Integer integer : changedColumnsArrayList) {
			//TODO this is a sucky way to do it but lets see if it works
			if(maxHeight<(heights[integer])){
				maxHeight = heights[integer];
				if (DEBUG) {
					System.out.printf("height[%s]:%s ",integer,heights[integer]);
				}
			}
		}
		if (DEBUG) {
			System.out.printf("\n");
		}
		sanityCheck();
		return result;
	}
	
	
	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	*/
	public int clearRows() {
		int rowsCleared = 0;
		if (clearedRowArrayList.size()==0) {
			return 0;
		}
		committed = false;
		for (Integer row : clearedRowArrayList) {
			//clear the row to 0
			for (int i = 0; i < width; i++) { //iterate from left to right
				//error checking if
				if (grid[i][row]) {
					grid[i][row] = false;
					
				}else{
					System.out.print("ISSUE WITH ClearedRowArrayList \n");
				}
				
			}
			widths[row]=0;
		
			
		}
		// rebuild the grid by compressing the cleared rows
		int compress = 0;
		for (int i_y = 0; i_y < (height-clearedRowArrayList.size()); i_y++) {
			if(clearedRowArrayList.contains(i_y+compress) &&clearedRowArrayList.contains(i_y+compress+1) &&clearedRowArrayList.contains(i_y+compress+2)&&clearedRowArrayList.contains(i_y+compress+3)   ){
				//tetris!!!!!!!
				compress+=4;
			}// 3 cleared rows in a row
			else if(clearedRowArrayList.contains(i_y+compress) &&clearedRowArrayList.contains(i_y+compress+1) &&clearedRowArrayList.contains(i_y+compress+2)  ){
				compress+=3;
			}	// 2 cleared rows in a row		
			else if(clearedRowArrayList.contains(i_y+compress) &&clearedRowArrayList.contains(i_y+compress+1)){
					compress+=2;
			} // ya you know why...
			else if(clearedRowArrayList.contains(i_y+compress) ){
				compress+=1;
			}
			for (int i_x = 0; i_x < width; i_x++) {
				grid[i_x][i_y] = grid[i_x][i_y+compress];
			}
			
		}
		// zero out maxHeight and heights
		maxHeight=0;
		for (int i = 0; i < width; i++) {
			heights[i]=0;
		}
		//repopulate height and maxHeight.. zero out widths and repopulate widths.
		for (int i = 0; i < height; i++) {
			widths[i]=0;
			for (int j = 0; j < width; j++) {
				if (grid[j][i]) {
					widths[i]=widths[i] + 1;
					heights[j]=((i+1)>heights[j])?i+1:heights[j];
					if(maxHeight<(heights[j])){
						maxHeight = heights[j];
						if (DEBUG) {
							System.out.printf("height[%s]:%s ",j,heights[j]);
						}
					}
				}
			}	
		}
		
		int clearedCount = clearedRowArrayList.size();
		clearedRowArrayList.removeAll(clearedRowArrayList);
		sanityCheck();
		return clearedCount;
	}



	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	*/
	public void undo() {
		// YOUR CODE HERE
		//TODO make sure this works
		committed = true;
		for (int i = 0; i < width; i++) {
			System.arraycopy(previousGrid[i], 0, grid[i], 0, height);	
		}
		
		System.arraycopy( previousWidths, 0,widths, 0, widths.length);
		System.arraycopy( previousHeights, 0, heights, 0, heights.length);
	
		maxHeight = previousMaxHeight;
		sanityCheck();
	}
	
	
	/**
	 Puts the board in the committed state.
	*/
	public void commit() {
		//TODO what do I have to do here?
		committed = true;
		//TODO Make sure this works
		for (int i = 0; i < width; i++) {
			System.arraycopy(grid[i], 0, previousGrid[i], 0, height);	
		}
		
		System.arraycopy(widths, 0, previousWidths, 0, widths.length);
		System.arraycopy(heights, 0, previousHeights, 0, heights.length);
		previousMaxHeight = maxHeight;
		sanityCheck();
	}


	
	/*
	 Renders the board state as a big String, suitable for printing.
	 This is the sort of print-obj-state utility that can help see complex
	 state change over time.
	 (provided debugging utility) 
	 */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


