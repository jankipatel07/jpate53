import javax.swing.ImageIcon;
import javax.swing.JLabel;

/************************************************************************
 * PROJECT 5: Tetris Game
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: Tetro_L.java : Class which will implement TetroShape ---> for 'L' 
 ********************************************************************************/
public class Tetro_L extends Tetromino implements TetroShape {
  
	 private char shapeArea[][] = {
						  			{'x', 'l', 'x', 'x'},
						  	        {'x', 'l', 'x', 'x'},
						  	        {'x', 'l', 'l', 'x'},
						  	        {'x', 'x', 'x', 'x'}};

	 public char[][] shapeA() {
		    return shapeArea;
		 }
	    
	 public boolean isGridFull(block grid[][]) {
			for(int i=0; i<4; i++) {
				if(position.y + i >= 0)
					return true; 
				//everything from here down is on grid
				System.out.println("position.y:- " + position.y);
				// this row is above grid so look for non-empty squares
				for(int j=0; j<4; j++)
					if(grid[i][j].getletter().charAt(0) != 'x'){
						return false;
					}
			}
			System.err.println("TetrisPiece.isGridFull internal error");
			return false;
		}
	    
	    public boolean canMove(block grid[][],int r,int c) {
			for(int i=0; i<4; i++) {
				for(int j=0; j<4; j++) {
					int to_x = j + position.x;
					int to_y = i + position.y;
					if(shapeArea[i][j] == 'l') { // piece contains this square?
						if(0 > to_x || to_x >= c || to_y >= r) // square off bottom?
						{
							return false;
						}
						
						if(to_y >= 0 && grid[to_y][to_x].getletter().charAt(0) != 'x') ////// ----------------> EMPTY
							return false;
					}
				}
			}
			return true;
		}
		
		public void stepDown() {
			System.out.println("pos.y before incr:" + position.y + "y is " );
			position.y++;
			System.out.println("pos.y after incre: "+ position.y);
		}
		ImageIcon imageIcon1 = new ImageIcon("resources/darkgray.jpg");
		public void cut(block grid[][]) {
			System.out.println("I am inside cut()");
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
				{	
					if(shapeArea[i][j] == 'l' && position.y+i>=0)
					{
						grid[position.y + i][position.x + j].changeletter("x");
						grid[position.y + i][position.x + j].setIcon(imageIcon1);  //--------------------> EMPTY
					}
				}
		}
		
		/**
		 * Paste the color info of this piece into the given grid
		 */
		ImageIcon imageIcon = new ImageIcon("resources/red.jpg");
		public void paste(block into[][]) {
			for(int i=0; i<4; i++)
				for(int j=0; j<4; j++)
					if(shapeArea[i][j] == 'l' && position.y+i>=0){
						System.out.println("inside if of paste();" + shapeArea[i][j]);
						into[position.y + i][position.x + j].setIcon( imageIcon );
						into[position.y + i][position.x + j].changeletter("l");
						
	
					}
		}
}

