import java.util.Timer;

import javax.swing.*;

/************************************************************************
 * PROJECT 5: Tetris Game
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: TetroShape.java : This is an interface that creates an instance 
 *                                 for all Tetrominoes shapes.
 ********************************************************************************/
public interface TetroShape{
		
	char[][] shapeA();
	boolean canMove(block grid[][],int r,int c);
	void stepDown();
	void cut(block grid[][]);
	void paste(block into[][]);
	boolean isGridFull(block grid[][]);
	
}
