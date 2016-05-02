import java.awt.Point;

/********************************************************************************
 * PROJECT 4 : Implementation of the game : TETRIS
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: Tetromino : This is the basic class of the program and this will be 
 * 							be essential part of the making the seven shapes of the
 * 							tetriminoes.This class will return the the respective based on 
 * 							shape requested
 *              
 ********************************************************************************/


public class Tetromino {
	 protected Point position = new Point(3, -4); // -4 to start above top row

	//get xpos
	public int getXpos() {
		return position.x; 
	}
	
	//get ypos
	public int getYpos() { 
		System.out.println("y in gety:" + position.y);
		return position.y; 
	}
	
	//set x
	public void setX(int xparam){
		position.x = xparam; 
	}
	
	//set y
	public void setY(int yparam){
		position.y = yparam; 
	}
	
	// returns the shape
	public TetroShape get_Shape (String shapeName){
		if(shapeName == null){
			return null;
		}
		else if(shapeName.equalsIgnoreCase("T")){
	         return new Tetro_T();
		}
		else if(shapeName.equalsIgnoreCase("J")){
			
	         return new Tetro_J();
		}
		else if(shapeName.equalsIgnoreCase("O")){
		
	         return new Tetro_0();
		}
		else if(shapeName.equalsIgnoreCase("I")){
			
	         return new Tetro_I();
		}
		else if(shapeName.equalsIgnoreCase("L")){
	         return new Tetro_L();
		}
		else if(shapeName.equalsIgnoreCase("S")){
	         return new Tetro_S();
		}
		else if(shapeName.equalsIgnoreCase("Z")){
	         return new Tetro_Z();
		}
		// did not find the shape 
		return null;
	}
}

