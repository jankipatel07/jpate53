
 /********************************************************************************
 * PROJECT 5: Tetris Game
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: Tetris.java : This class contains main method. 
 * PlayingArea object is created in the main method.
 ********************************************************************************/

import java.awt.event.*;

public class Tetris {
	public static void main(String [] args)
	{
		
		// creates a new instance of game
		PlayingArea game = PlayingArea.getInstance();
		game.addWindowListener(
		         new WindowAdapter() {
		            public void windowClosing( WindowEvent e )
		            {
		               System.exit( 0 );
		            }
		         }
		      );
	}
}

