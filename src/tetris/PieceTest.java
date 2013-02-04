package tetris;

import static org.junit.Assert.*;
import java.util.*;

import org.junit.*;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest {
	// You can create data to be used in the your
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4; //pyr
	private Piece s1, s1Rotated; //s1
	private Piece s2, s2Rotated; //s2
	private Piece l1, l1_90,l1_180,l1_270; //l1
	private Piece l2, l2_90,l2_180,l2_270; //l2
	private Piece stick, stickRotated; //stick
	private Piece square, squareRotated; //square
	
	public static final String STICK_STR	= "0 0	0 1	 0 2  0 3";
	public static final int[] STICK_SKIRT_ARRAY	= {0};
	public static final int[] STICK_SKIRT_ARRAY_90	= {0,0,0,0};
	
	public static final String L1_STR		= "0 0	0 1	 0 2  1 0";
	public static final int[] L1_SKIRT_ARRAY	= {0,0};
	public static final int[] L1_SKIRT_ARRAY_90	= {0,0,0};
	public static final int[] L1_SKIRT_ARRAY_180	= {2,0};
	public static final int[] L1_SKIRT_ARRAY_270	= {0,1,1};
	
	public static final String L2_STR		= "0 0	1 0  1 1  1 2";
	public static final int[] L2_SKIRT_ARRAY	= {0,0};
	public static final int[] L2_SKIRT_ARRAY_90	= {1,1,0};
	public static final int[] L2_SKIRT_ARRAY_180	= {0,2};
	public static final int[] L2_SKIRT_ARRAY_270	= {0,0,0};
	
	
	public static final String S1_STR		= "0 0	1 0	 1 1  2 1";
	public static final int[] S1_SKIRT_ARRAY	= {0,0,1};
	public static final int[] S1_SKIRT_ARRAY_90	= {1,0};
	
	public static final String S2_STR		= "0 1	1 1  1 0  2 0";
	public static final int[] S2_SKIRT_ARRAY	= {1,0,0};
	public static final int[] S2_SKIRT_ARRAY_90	= {0,1};
	
	public static final String SQUARE_STR	= "0 0  0 1  1 0  1 1";
	public static final int[] SQUARE_SKIRT_ARRAY	= {0,0};
	
	public static final String PYRAMID_STR	= "0 0  1 0  1 1  2 0";
	public static final int[] PYR_SKIRT_ARRAY	= {0,0,0};
	public static final int[] PYR_SKIRT_ARRAY_90	= {1,0};
	public static final int[] PYR_SKIRT_ARRAY_180	= {1,0,1};
	public static final int[] PYR_SKIRT_ARRAY_270	= {0,1};

	
	@Before
	public void setUp() throws Exception {
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s1 = new Piece(Piece.S1_STR);
		s1Rotated = s1.computeNextRotation();

		s2 = new Piece(Piece.S2_STR);
		s2Rotated = s2.computeNextRotation();
		
		l1 = new Piece(Piece.L1_STR);
		l1_90 = l1.computeNextRotation();
		l1_180 = l1_90.computeNextRotation();
		l1_270 = l1_180.computeNextRotation();
		
		l2 = new Piece(Piece.L2_STR);
		l2_90 = l2.computeNextRotation();
		l2_180 = l2_90.computeNextRotation();
		l2_270 = l2_180.computeNextRotation();
		
		stick = new Piece(Piece.STICK_STR);
		stickRotated = stick.computeNextRotation();
		
		square = new Piece(Piece.SQUARE_STR);
		squareRotated = square.computeNextRotation();
		
	}
	
	// Here are some sample tests to get you started
	
	@Test // this test will check the parsing as well as the getBody as well as the computeNextRotation()
	public void testOrientations() {
		// Check for pyr piece
		for(int i=0; i<pyr1.getBody().length; i++){
			assertTrue(new Piece(Piece.PYRAMID_STR).getBody()[i].equals( pyr1.getBody()[i]));
			assertTrue(new Piece(Piece.PYRAMID_STR_90).getBody()[i].equals( pyr2.getBody()[i]));
			assertTrue(new Piece(Piece.PYRAMID_STR_180).getBody()[i].equals( pyr3.getBody()[i]));
			assertTrue(new Piece(Piece.PYRAMID_STR_270).getBody()[i].equals( pyr4.getBody()[i]));
			assertTrue(pyr1.getBody()[i].equals( pyr4.computeNextRotation().getBody()[i]));
			// check for l1
			assertTrue(new Piece(Piece.L1_STR).getBody()[i].equals( l1.getBody()[i]));
			assertTrue(new Piece(Piece.L1_STR_90).getBody()[i].equals( l1_90.getBody()[i]));
			assertTrue(new Piece(Piece.L1_STR_180).getBody()[i].equals( l1_180.getBody()[i]));
			assertTrue(new Piece(Piece.L1_STR_270).getBody()[i].equals( l1_270.getBody()[i]));
			assertTrue(l1.getBody()[i].equals( l1_270.computeNextRotation().getBody()[i]));
			
			// check for l2
			assertTrue(new Piece(Piece.L2_STR).getBody()[i].equals( l2.getBody()[i]));
			assertTrue(new Piece(Piece.L2_STR_90).getBody()[i].equals( l2_90.getBody()[i]));
			assertTrue(new Piece(Piece.L2_STR_180).getBody()[i].equals( l2_180.getBody()[i]));
			assertTrue(new Piece(Piece.L2_STR_270).getBody()[i].equals( l2_270.getBody()[i]));
			assertTrue(l2.getBody()[i].equals( l2_270.computeNextRotation().getBody()[i]));
			
			// check for s1
			assertTrue(new Piece(Piece.S1_STR).getBody()[i].equals( s1.getBody()[i]));
			assertTrue(new Piece(Piece.S1_STR_90).getBody()[i].equals( s1Rotated.getBody()[i]));
			assertTrue(s1.getBody()[i].equals( s1Rotated.computeNextRotation().getBody()[i]));
			
			// check for s2
			assertTrue(new Piece(Piece.S2_STR).getBody()[i].equals( s2.getBody()[i]));
			assertTrue(new Piece(Piece.S2_STR_90).getBody()[i].equals( s2Rotated.getBody()[i]));
			assertTrue(s2.getBody()[i].equals( s2Rotated.computeNextRotation().getBody()[i]));
		}		
	}
	
	
	@Test
	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());
		
		// Check size of l1 piece
		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());
		
		// Check size of l1 piece
		assertEquals(3, l1_90.getWidth());
		assertEquals(2, l1_90.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, l2.getWidth());
		assertEquals(3, l2.getHeight());

		assertEquals(3, l2_90.getWidth());
		assertEquals(2, l2_90.getHeight());
		
		// Check size of pyr piece
		assertEquals(3, s1.getWidth());
		assertEquals(2, s1.getHeight());
		
		assertEquals(2, s1Rotated.getWidth());
		assertEquals(3, s1Rotated.getHeight());
		
		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(3, s2.getWidth());
		assertEquals(2, s2.getHeight());
		
		assertEquals(2, s2Rotated.getWidth());
		assertEquals(3, s2Rotated.getHeight());
		
		assertEquals(2, square.getWidth());
		assertEquals(2, square.getHeight());
		Piece square_rotated = square.computeNextRotation();
		assertEquals(2, square_rotated.getWidth());
		assertEquals(2, square_rotated.getHeight());
		
		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());
		Piece l_rotated = l.computeNextRotation();
		assertEquals(4, l_rotated.getWidth());
		assertEquals(1, l_rotated.getHeight());
		
	}
	
	@Test 
	public void testMakeFastRotations(){
		Piece [] pieces = new Piece[Piece.getPieces().length];
		pieces = Piece.getPieces();
		// for stick
		assertTrue(pieces[0].fastRotation().equals(stick.computeNextRotation()));
		assertTrue(pieces[0].fastRotation().fastRotation().equals(stick.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[0].fastRotation().fastRotation().fastRotation().equals(stick.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[0].fastRotation().fastRotation().fastRotation().fastRotation().equals(stick.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

		// for L1
		assertTrue(pieces[1].fastRotation().equals(l1.computeNextRotation()));
		assertTrue(pieces[1].fastRotation().fastRotation().equals(l1.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[1].fastRotation().fastRotation().fastRotation().equals(l1.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[1].fastRotation().fastRotation().fastRotation().fastRotation().equals(l1.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

		// for L2
		assertTrue(pieces[2].fastRotation().equals(l2.computeNextRotation()));
		assertTrue(pieces[2].fastRotation().fastRotation().equals(l2.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[2].fastRotation().fastRotation().fastRotation().equals(l2.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[2].fastRotation().fastRotation().fastRotation().fastRotation().equals(l2.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		
		
		// for S1
		assertTrue(pieces[3].fastRotation().equals(s1.computeNextRotation()));
		assertTrue(pieces[3].fastRotation().fastRotation().equals(s1.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[3].fastRotation().fastRotation().fastRotation().equals(s1.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[3].fastRotation().fastRotation().fastRotation().fastRotation().equals(s1.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

		// for S2
		assertTrue(pieces[4].fastRotation().equals(s2.computeNextRotation()));
		assertTrue(pieces[4].fastRotation().fastRotation().equals(s2.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[4].fastRotation().fastRotation().fastRotation().equals(s2.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[4].fastRotation().fastRotation().fastRotation().fastRotation().equals(s2.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

		// for sq
		assertTrue(pieces[5].fastRotation().equals(square.computeNextRotation()));
		assertTrue(pieces[5].fastRotation().fastRotation().equals(square.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[5].fastRotation().fastRotation().fastRotation().equals(square.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[5].fastRotation().fastRotation().fastRotation().fastRotation().equals(square.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

		// for pyr
		assertTrue(pieces[6].fastRotation().equals(pyr1.computeNextRotation()));
		assertTrue(pieces[6].fastRotation().fastRotation().equals(pyr1.computeNextRotation().computeNextRotation()));
		assertTrue(pieces[6].fastRotation().fastRotation().fastRotation().equals(pyr1.computeNextRotation().computeNextRotation().computeNextRotation()));
		assertTrue(pieces[6].fastRotation().fastRotation().fastRotation().fastRotation().equals(pyr1.computeNextRotation().computeNextRotation().computeNextRotation().computeNextRotation()));		

	}
	
	// Test the skirt returned by a few pieces
	@Test
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		
		//pyr
		assertTrue(Arrays.equals(PYR_SKIRT_ARRAY, pyr1.getSkirt()));
		assertTrue(Arrays.equals(PYR_SKIRT_ARRAY_90, pyr2.getSkirt()));
		assertTrue(Arrays.equals(PYR_SKIRT_ARRAY_180, pyr3.getSkirt()));
		assertTrue(Arrays.equals(PYR_SKIRT_ARRAY_270, pyr4.getSkirt()));
		
		//s1
		assertTrue(Arrays.equals(S1_SKIRT_ARRAY, s1.getSkirt()));
		assertTrue(Arrays.equals(S1_SKIRT_ARRAY_90, s1Rotated.getSkirt()));
		
		//s2
		assertTrue(Arrays.equals(S2_SKIRT_ARRAY, s2.getSkirt()));
		assertTrue(Arrays.equals(S2_SKIRT_ARRAY_90, s2Rotated.getSkirt()));
		
		//stick
		assertTrue(Arrays.equals(STICK_SKIRT_ARRAY, stick.getSkirt()));
		assertTrue(Arrays.equals(STICK_SKIRT_ARRAY_90, stickRotated.getSkirt()));
		
		//sq
		assertTrue(Arrays.equals(SQUARE_SKIRT_ARRAY, square.getSkirt()));
		
		//l1
		assertTrue(Arrays.equals(L1_SKIRT_ARRAY, l1.getSkirt()));
		assertTrue(Arrays.equals(L1_SKIRT_ARRAY_90, l1_90.getSkirt()));
		assertTrue(Arrays.equals(L1_SKIRT_ARRAY_180, l1_180.getSkirt()));
		assertTrue(Arrays.equals(L1_SKIRT_ARRAY_270, l1_270.getSkirt()));
		
		//l2
		assertTrue(Arrays.equals(L2_SKIRT_ARRAY, l2.getSkirt()));
		assertTrue(Arrays.equals(L2_SKIRT_ARRAY_90, l2_90.getSkirt()));
		assertTrue(Arrays.equals(L2_SKIRT_ARRAY_180, l2_180.getSkirt()));
		assertTrue(Arrays.equals(L2_SKIRT_ARRAY_270, l2_270.getSkirt()));
		
	}
	
	
}
