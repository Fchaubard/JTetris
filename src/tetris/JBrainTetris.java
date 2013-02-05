package tetris;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class JBrainTetris extends JTetris {
	private JCheckBox brainMode;
	private JComponent panel;
	private DefaultBrain brain;
	private boolean countChanged;
	private int previousCount;
	private Brain.Move move;
	private int numberOfRotationsNeeded;
	private int translationNeeded;
	
	public JBrainTetris(int pixels) {
		super(pixels);
		brain = new DefaultBrain();
		previousCount = 0;
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public void tick(int verb) {
		if (currentPiece != null) {
			board.undo();	// remove the piece from its old position
		}
		if (brainMode.isSelected() && (verb == JTetris.DOWN) && board.committed) {
			if ( !(super.count == previousCount)) {// is a new piece
				
				previousCount = super.count; // update count
				Piece currentPiece = super.currentPiece;
				
				// check to see if board is in committed state
				// Override verb with DefaultBrain
				// possibly should be a copy of the old board so it does get overridden 
				move = brain.bestMove(super.board, currentPiece, board.getHeight() - 4, null);
				numberOfRotationsNeeded = 0;
				translationNeeded = 0;
				int additionalTranslation = 0;
				// be careful of null when no move is possible!
				if (move!=null) {
					// infer number of rotations needed
					while(!currentPiece.equals(move.piece)){
						numberOfRotationsNeeded+=1;
						currentPiece = currentPiece.fastRotation();
					}
					/*for (TPoint tp : move.piece.getBody()) {
						if (tp.x==0 && tp.y==0) {
							additionalTranslation+=1;
							break;
						}
					}*/
					
					// infer number of Left or Rights needed (left is negative)
					//translationNeeded = move.x - super.currentX+additionalTranslation-1;
					translationNeeded = move.x - super.currentX;
					System.out.printf("Best move: x=%s y=%s rotations=%s translations=%s \n", move.x, move.y, numberOfRotationsNeeded, translationNeeded);
				}
			}
			else{
				// keep moving the piece until it is in the correct x (column)
				if(numberOfRotationsNeeded>0){
					numberOfRotationsNeeded-=1;
					
					super.tick(JTetris.ROTATE);
				}else if(translationNeeded>0){
					
					translationNeeded-=1;
					super.tick(JTetris.RIGHT);
				}else if(translationNeeded<0){
					
					translationNeeded+=1;
					super.tick(JTetris.LEFT);
				}
			}
			
		}
		super.tick(verb);
	}


	@Override
	public JComponent createControlPanel() {
		// TODO Auto-generated method stub
		panel = super.createControlPanel();
		panel.add(new JLabel("Brain:"));
		brainMode = new JCheckBox("Brain active"); 
		panel.add(brainMode);
		
		return panel;
	}


	/**
	 Creates a frame with a JBrainTetris.
	*/
	public static void main(String[] args) {
		// Set GUI Look And Feel Boilerplate.
		// Do this incantation at the start of main() to tell Swing
		// to use the GUI LookAndFeel of the native platform. It's ok
		// to ignore the exception.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) { }
		
		JBrainTetris tetris = new JBrainTetris(16);
		JFrame frame = JTetris.createFrame(tetris);
		frame.setVisible(true);
	}

}
