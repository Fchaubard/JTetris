// Piece.java
package tetris;

import java.lang.reflect.Array;
import java.util.*;


/**
 An immutable representation of a tetris piece in a particular rotation.
 Each piece is defined by the blocks that make up its body.
 
 Typical client code looks like...
 <pre>
 Piece pyra = new Piece(PYRAMID_STR);		// Create piece from string
 int width = pyra.getWidth();			// 3
 Piece pyra2 = pyramid.computeNextRotation(); // get rotation, slow way
 
 Piece[] pieces = Piece.getPieces();	// the array of root pieces
 Piece stick = pieces[STICK];
 int width = stick.getWidth();		// get its width
 Piece stick2 = stick.fastRotation();	// get the next rotation, fast way
 </pre>
*/
public class Piece {
	// Starter code specs out a few basic things, leaving
	// the algorithms to be done.
	private TPoint[] body;
	private int[] skirt;
	private int width;
	private int height;
	private Piece next; // "next" rotation
	static private Piece[] pieces;	// singleton static array of first rotations

	/**
	 Defines a new piece given a TPoint[] array of its body.
	 Makes its own copy of the array and the TPoints inside it.
	*/
	public Piece(TPoint[] points) {
		List<TPoint> newPoints = new ArrayList<TPoint>(Arrays.asList(points));
		Collections.sort(newPoints, new Comparator<TPoint>() {
			 
			@Override
		    public int compare(TPoint a, TPoint b) {
			      if(a.x>b.x){
			    	  return -1;
			      }else if(a.x<b.x){
			    	  return 1;
			      }
			      
			      if(a.y>b.y){
			    	  return -1;
			      }
		    	  else if(a.y<b.y){
			    	  return 1;
			      }else{
			    	  return 0;
			      }
			    }
		    });
		this.body = new TPoint[points.length];
		for(int i=0;i<points.length; i++){
			body[i] = new TPoint(newPoints.get(i));
		}
		this.getSkirt();
		this.getWidth();
		
		// find the height
		int max = 0;
		for(int i=0; i<body.length; i++){
			if(max<body[i].y){
				max = body[i].y;
			}
		}
		height = max+1;
		
		width = skirt.length;
		
		//next
	}
	
	/**
	 * Alternate constructor, takes a String with the x,y body points
	 * all separated by spaces, such as "0 0  1 0  2 0	1 1".
	 * (provided)
	 */
	public Piece(String points) {
		this(parsePoints(points));
	}

	/**
	 Returns the width of the piece measured in blocks.
	*/
	public int getWidth() {
		return width;
	}

	/**
	 Returns the height of the piece measured in blocks.
	*/
	public int getHeight() {
		
		return height;
	}

	/**
	 Returns a pointer to the piece's body. The caller
	 should not modify this array.
	*/
	public TPoint[] getBody() {
		return body;
	}

	/**
	 Returns a pointer to the piece's skirt. For each x value
	 across the piece, the skirt gives the lowest y value in the body.
	 This is useful for computing where the piece will land.
	 The caller should not modify this array.
	*/
	public int[] getSkirt() {
		HashMap<Integer,Integer> skirtMap = new HashMap<Integer,Integer>();
		
		for(int i=0; i<body.length; i++){
			if(skirtMap.containsKey(body[i].x)){
				if(body[i].y<skirtMap.get(body[i].x)){
					skirtMap.remove(body[i].x);
					skirtMap.put(body[i].x, body[i].y);
				}
			}else{
				skirtMap.put(body[i].x, body[i].y);
			}
			
		}
		
		skirt = new int[skirtMap.size()];
		for(int i=0; i<skirtMap.size(); i++){
			skirt[i] = skirtMap.get(i);
		}
		return skirt;
	}

	
	/**
	 Returns a new piece that is 90 degrees counter-clockwise
	 rotated from the receiver.
	 */
	public Piece computeNextRotation() {
		
		TPoint[] newPoints = new TPoint[body.length];
		//flip about x=y
		newPoints = invertPoints(body);
		
		//flip about x=c
		newPoints = mirrorPointsOverY(newPoints);
		
		Piece newPiece = new Piece(newPoints);
		return newPiece;
	}

	private TPoint[] mirrorPointsOverY(TPoint[] newPoints) {
		int max_x=0;
		int min_x=0;

		List<TPoint> newerPoints = new ArrayList<TPoint>();

		for(TPoint tp : newPoints){
			max_x=(max_x<tp.x)? tp.x : max_x;
			min_x=(min_x>tp.x)? tp.x : min_x;
		}
		
		for(int i=0;i<newPoints.length; i++){
			// add the (rangeOfBase - 1) to get it back to the origin
			newerPoints.add(new TPoint( ((max_x-min_x) - newPoints[i].x ), newPoints[i].y));
			
		}
		
		TPoint[] array = newerPoints.toArray(new TPoint[0]);
		return array;
	}




	private TPoint[] invertPoints(TPoint[] newPoints) {
		List<TPoint> newerPoints = new ArrayList<TPoint>();
		
		for(int i=0;i<newPoints.length; i++){
			newerPoints.add(new TPoint(newPoints[i].y, newPoints[i].x));
		}
		
		
		TPoint[] array = newerPoints.toArray(new TPoint[0]);
		return array;
	}




	/**
	 Returns a pre-computed piece that is 90 degrees counter-clockwise
	 rotated from the receiver.	 Fast because the piece is pre-computed.
	 This only works on pieces set up by makeFastRotations(), and otherwise
	 just returns null.
	*/	
	public Piece fastRotation() {
		return next;
	}
	


	/**
	 Returns true if two pieces are the same --
	 their bodies contain the same points.
	 Interestingly, this is not the same as having exactly the
	 same body arrays, since the points may not be
	 in the same order in the bodies. Used internally to detect
	 if two rotations are effectively the same.
	*/
	public boolean equals(Object obj) {
		// standard equals() technique 1
		if (obj == this) return true;
		
		// standard equals() technique 2
		// (null will be false)
		if (!(obj instanceof Piece)) return false;
		Piece other = (Piece)obj;
		
		for(int i=0;i<other.getBody().length;i++){
			if(!this.getBody()[i].equals(other.getBody()[i])){
				return false;
			}
		}
		
		
		return true;
	}


	// String constants for the standard 7 tetris pieces
	public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
	public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
	public static final String L2_STR		= "0 0	1 0  1 1  1 2";
	public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
	public static final String S2_STR		= "0 1	1 1  1 0  2 0";
	public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
	public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";
	
	public static final String STICK_STR_90	= "0 0	1 0  2 0  3 0";
	
	public static final String L1_STR_90		= "0 0  1 0  2 0  2 1";
	public static final String L1_STR_180		= "1 0  1 1  1 2  0 2";
	public static final String L1_STR_270		= "0 0  0 1  1 1  2 1";
	
	public static final String L2_STR_90		= "0 1  1 1  2 0  2 1";
	public static final String L2_STR_180		= "0 0  0 1  0 2  1 2";
	public static final String L2_STR_270		= "0 0  0 1  1 0  2 0";
	
	public static final String S1_STR_90		= "0 1	1 1	 1 0  0 2";
	
	public static final String S2_STR_90		= "0 0	0 1  1 1  1 2";
	
	public static final String SQUARE_STR_90	= "0 0  0 1  1 0  1 1";
	
	public static final String PYRAMID_STR_90	= "1 0  0 1  1 1  1 2";
	public static final String PYRAMID_STR_180	= "1 0  0 1  1 1  2 1";
	public static final String PYRAMID_STR_270	= "0 0  0 1  1 1  0 2";
	
	// Indexes for the standard 7 pieces in the pieces array
	public static final int STICK = 0;
	public static final int L1	  = 1;
	public static final int L2	  = 2;
	public static final int S1	  = 3;
	public static final int S2	  = 4;
	public static final int SQUARE	= 5;
	public static final int PYRAMID = 6;
	
	/**
	 Returns an array containing the first rotation of
	 each of the 7 standard tetris pieces in the order
	 STICK, L1, L2, S1, S2, SQUARE, PYRAMID.
	 The next (counterclockwise) rotation can be obtained
	 from each piece with the {@link #fastRotation()} message.
	 In this way, the client can iterate through all the rotations
	 until eventually getting back to the first rotation.
	 (provided code)
	*/
	public static Piece[] getPieces() {
		// lazy evaluation -- create static array if needed
		if (Piece.pieces==null) {
			// use makeFastRotations() to compute all the rotations for each piece
			Piece.pieces = new Piece[] {
				makeFastRotations(new Piece(STICK_STR)),
				makeFastRotations(new Piece(L1_STR)),
				makeFastRotations(new Piece(L2_STR)),
				makeFastRotations(new Piece(S1_STR)),
				makeFastRotations(new Piece(S2_STR)),
				makeFastRotations(new Piece(SQUARE_STR)),
				makeFastRotations(new Piece(PYRAMID_STR)),
			};
		}
		
		
		return Piece.pieces;
	}
	


	/**
	 Given the "first" root rotation of a piece, computes all
	 the other rotations and links them all together
	 in a circular list. The list loops back to the root as soon
	 as possible. Returns the root piece. fastRotation() relies on the
	 pointer structure setup here.
	*/
	/*
	 Implementation: uses computeNextRotation()
	 and Piece.equals() to detect when the rotations have gotten us back
	 to the first piece.
	*/

	private static Piece makeFastRotations(Piece root) {
		
		ArrayList<Piece> nextRotations = new ArrayList<Piece>();
		nextRotations.add(root);
		
		while(!root.equals(nextRotations.get(nextRotations.size()-1).computeNextRotation())){
			nextRotations.add(nextRotations.get(nextRotations.size()-1).computeNextRotation());
			nextRotations.get(nextRotations.size()-2).next = nextRotations.get(nextRotations.size()-1);
		}
		// this next line will close the Array List to be circular
		nextRotations.get(nextRotations.size()-1).next = nextRotations.get(0);
		
		return nextRotations.get(0); // YOUR CODE HERE
	}
	
	

	/**
	 Given a string of x,y pairs ("0 0	0 1 0 2 1 0"), parses
	 the points into a TPoint[] array.
	 (Provided code)
	*/
	private static TPoint[] parsePoints(String string) {
		List<TPoint> points = new ArrayList<TPoint>();
		StringTokenizer tok = new StringTokenizer(string);
		try {
			while(tok.hasMoreTokens()) {
				int x = Integer.parseInt(tok.nextToken());
				int y = Integer.parseInt(tok.nextToken());
				
				points.add(new TPoint(x, y));
			}
		}
		catch (NumberFormatException e) {
			throw new RuntimeException("Could not parse x,y string:" + string);
		}
		
		// Make an array out of the collection
		TPoint[] array = points.toArray(new TPoint[0]);
		return array;
	}

	

}
