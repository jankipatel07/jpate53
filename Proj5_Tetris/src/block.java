import javax.swing.JLabel;
/********************************************************************************
 * PROJECT 4 : Implementation of the game : TETRIS
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: block.java : this extends the Jlabel and will be used to store the
 *                           blocks in the playing area , and also to update the
 *                           pieces based on the type of the letter. 
 *              
 ********************************************************************************/
public class block extends JLabel {

	String lettertype;
	
	public block(String s){
		lettertype = s;
	}
	
	public String getletter(){
		return lettertype;
	}
	
	public void changeletter(String b){
		lettertype = b;
	}
}
