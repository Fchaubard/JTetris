package tetris;

import static org.junit.Assert.*;

import org.junit.*;

public class BoardTest {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, l1, l2, square, stick;

	// This shows how to build things in setUp() to re-use
	// across tests.
	
	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	@Before
	public void setUp() throws Exception {
		b = new Board(6, 12);
		
		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();
		
		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();
		
		
	}
	
	// Check the basic width/height/max after the one placement
	@Test
	public void testSample1() {
		b.place(pyr1, 0, 0);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}
	
	// Place sRotated into the board, then check some measures
	@Test
	public void testSample2() {
		b.place(pyr1, 0, 0);
		b.commit();
		
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}
	
	@Test
	public void checkClearRow(){
		int result;
		result = b.place(new Piece(Piece.STICK_STR), 0, 0);
		for (int i = 1; i < b.getWidth(); i++) {
			assertEquals(Board.PLACE_OK, result);
			assertEquals(0, b.getColumnHeight(i));
			result = b.place(new Piece(Piece.STICK_STR), i, 0);
			
			assertEquals(4, b.getColumnHeight(i));
			assertEquals(4, b.getMaxHeight());
		}
		assertEquals(Board.PLACE_ROW_FILLED, result);
		b.clearRows();
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(0, b.getMaxHeight());
	}
	
	
	@Test
	public void check3FailureModes(){
		int result;
		
		//check out of bounds cases left and right
		assertEquals(0, b.getColumnHeight(0));
		result = b.place(new Piece(Piece.STICK_STR), -1, 0);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		
		assertEquals(0, b.getColumnHeight(0));
		result = b.place((new Piece(Piece.STICK_STR).computeNextRotation()), b.getWidth(), 0);
		assertEquals(Board.PLACE_OUT_BOUNDS, result);
		b.undo();
		
		//check colliding parts cases 
		assertEquals(0, b.getColumnHeight(0));
		result = b.place(new Piece(Piece.STICK_STR), 0, 0);
		assertEquals(Board.PLACE_OK, result);
		if (result==Board.PLACE_OK) {
			b.commit();	
		}
		//place one down and overlap it
		result = b.place(new Piece(Piece.STICK_STR), 0, 0);
		assertEquals(Board.PLACE_BAD, result);
		b.undo();
		// try placing one on in the middle of the other one
		result = b.place(new Piece(Piece.STICK_STR), 0, 2);
		assertEquals(Board.PLACE_BAD, result);
		b.undo();
		
		assertEquals(4, b.getColumnHeight(0));
		assertEquals(4, b.getMaxHeight());
		// this one should work
		result = b.place(new Piece(Piece.L1_STR), 0,4);
		assertEquals(Board.PLACE_OK, result);
		b.commit();
		assertEquals(7, b.getColumnHeight(0));
		assertEquals(5, b.getColumnHeight(1));
		assertEquals(7, b.getMaxHeight());
		
	}
	
	// Make  more tests, by putting together longer series of 
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.
	
	
}
