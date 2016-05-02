/********************************************************************************
 * PROJECT 5: Tetris Game
 * PATNERS : HENVY PATEL & JANKI PATEL
 * DESCRIPTION: PlayingArea.java : This class sets up gui for the game.
 * 				It shows the game board to the users and scores information.
 ********************************************************************************/

import java.awt.event.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.Timer;

import org.omg.CORBA.portable.InputStream;

public class PlayingArea extends JFrame implements ActionListener{
	
	// establishing the grid layout
	private int row = 20;
	private int col = 10;
	private int score = 0;
	private int numLineCleared = 0;
	private int gameLevel = 1;
	private JMenu tetris;
	private JMenuBar menuBar;
	private JMenuItem about, help, quit; //sub menus in tetris
	
	//declare panel variables
	private JPanel labelPanel;
	private JPanel menuPanel;
	private JPanel infoPanel = new JPanel();
	private Container container;
	private JLabel scoreLabel = new JLabel("Score");
	private JLabel updateScore = new JLabel();
	private JLabel lineInfo = new JLabel("Lines Cleared:");
	private JLabel lineCleared = new JLabel();
	private JLabel levelInfo = new JLabel("Level");
	private JLabel showLevel = new JLabel();
	private JLabel timeInfo =  new JLabel("Time Elapsed");
	private JLabel timeLabel = new JLabel();
	private JLabel tetrisLabel = new JLabel("Next Tetris");
	private Timer timeClock; //use to show seconds elapsed for the game
	private Integer count = 1;
	private JButton startButton = new JButton("START");
	Boolean gameStarted = false;
	
	private String names[] = 
		  {"resources/darkgray20.jpg","blacksquare.jpg"};
	private block grid[][];
	// Array to store all the pieces in an array
	private String shapes_array[] = {"T","L","J","O","S","Z","I"};
	
	/************INFO FALLING PIECES INFO WILL BE ADDED HERE*****************/
	private int next_piece_grid[][] = new int[4][4];
	private Timer timer;
	private TetroShape cur_piece;
	private TetroShape next_piece;
	
	//object of PlayinArea 
	private static PlayingArea instance = new PlayingArea();
	
	//get object 
	public static PlayingArea getInstance(){
		return instance;
	}
	
	//playingArea constructor
	PlayingArea()
	{
		super("Project-5 Tetris");
		//call the gui method
		gui();
	}
	
	//method to set up GUI
	private void gui()
	{
		//Set up menuBar
		menuPanel = new JPanel(); //set up menu panel
		//Add menu items
		menuBar = new JMenuBar();
		tetris = new JMenu("MENUBAR OPTIONS");
		menuBar.add(tetris);
		//add submenu items to the Tetris menu
		about = new JMenuItem("About");
		tetris.add(about);
		about.addActionListener(this);
		help = new JMenuItem("Help");
		tetris.add(help);
		help.addActionListener(this);
		quit = new JMenuItem("Quit");
		tetris.add(quit);
		quit.addActionListener(this);
		
		//add menu to the panel
		menuPanel.add(menuBar);
		//add start/reset button
		startButton.addActionListener(this);
		menuPanel.add(startButton);
		//add time label
		menuPanel.add(timeInfo);
		timeLabel.setText(Integer.toString(0));
		menuPanel.add(timeLabel);
	
		infoPanel.add(tetrisLabel);
		//call score update method
		scoreUpdate();
		//call line cleared method
		lineCleared();
		//call method to show game level
		gameLevel();
		
		//set up container for layout
		container = getContentPane();
		container.setLayout(new BorderLayout());
		
		//Set up panel for label grid
		labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(row, col, 2, 2)); //width, height, x_space, y_scpace
		labelPanel.setSize(row, col);
		//add label panel
		container.add(labelPanel, BorderLayout.CENTER);
		//add menu panel
		container.add(menuPanel, BorderLayout.PAGE_START);

		//set up container for info panel
		Container c = getContentPane();
		infoPanel.setLayout(new GridLayout(20, 20, 5, 5));
		infoPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		c.add(infoPanel, BorderLayout.EAST);
		
		//set up grid of labels
		grid = new block[row][col];
		for(int i = 0; i < row; i++){
			for(int j = 0; j < col; j++){
				 ImageIcon imageIcon = new ImageIcon(names[0]);
			     grid[i][j] = new block("x");
				labelPanel.add(grid[i][j]);
			}
		}
		//Get tetromino shape on the board
		Tetromino newTetromino = new Tetromino();
		
		//generate random shapes
		Random rand = new Random();
		String shape = "";
		int size = shapes_array.length;
		for(int i = 0; i < size; i++) {
			shape = shapes_array[rand.nextInt(size)];
		}
		cur_piece = newTetromino.get_Shape(shape);
		next_piece = newTetromino.get_Shape(shape);
		
		//Display the gui
		pack();
		setVisible(true);
	}

	//method to update score
	public void scoreUpdate()
	{
		//add title label to the panel
		infoPanel.add(scoreLabel);
		updateScore.setText(Integer.toString(score));
		infoPanel.add(updateScore);
	}
	public void scoreUpdate(int i)
	{
		//add title label to the panel
		score++;
		updateScore.setText(Integer.toString(score));
		infoPanel.add(updateScore);
	}
	public boolean canGoDown( block grid[][], int r , int c) {
		synchronized(timer) {
			cur_piece.cut(grid);
			int f =  ((Tetromino) cur_piece).getYpos();
			((Tetromino) cur_piece).setY(f++);
			cur_piece.canMove(grid,r,c);
			int f1 =  ((Tetromino) cur_piece).getYpos();
			((Tetromino) cur_piece).setY(f--);
			cur_piece.paste(grid);
			return true;
		}
	}
	/**method to drop the objects****/
	public void start() {
		//System.out.println("I am in start");
		ActionListener listen = new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				synchronized(timer) {
					System.out.println("before canstepdown();");
					if(canGoDown(grid,row,col)) {
							cur_piece.cut(grid);
							cur_piece.stepDown();
							cur_piece.paste(grid);
					}
					else { 
						// it hit something
						
						if( ! cur_piece.isGridFull(grid)){
							//gameOver();
							JOptionPane.showMessageDialog(null,"GAME OVER", "", JOptionPane.INFORMATION_MESSAGE);
						}
							
						else {
							
							JOptionPane.showMessageDialog(null,"reached else", "", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		};
		timer = new Timer(1000, listen);
		timer.start();
	}		
	
	// to reset the game
	public void reset()
	{
		timer.stop();
		start();
		
	}
	
	//method that updates the number of lines cleared
	public void lineCleared()
	{
		//Add title label to the panel
		infoPanel.add(lineInfo);
		lineCleared.setText(Integer.toString(numLineCleared));
		infoPanel.add(lineCleared);
	}

	//method to show game level
	public void gameLevel()
	{
		infoPanel.add(levelInfo);
		showLevel.setText(Integer.toString(gameLevel));
		infoPanel.add(showLevel);
	}
	
	//handle action performed
	public void actionPerformed(ActionEvent e) {
		String str = e.getActionCommand();
		
		//Handle Start/Reset Button
		if(e.getSource().equals(startButton))
		{
			//start button is clicked, so start the game
			start();
			if(!gameStarted)
			{
				//game started, so start the timer to show time elapsed since game started
				ActionListener a = new ActionListener()
				{
						public void actionPerformed(ActionEvent e) {
							timeLabel.setText(count.toString());
							count += 1;
						}
				};
				timeClock = new Timer(1000, a);
				timeClock.start();
				//update the label to show time
				timeLabel.setText(Integer.toString(count));
				menuPanel.add(timeLabel);
				
				//Change button to handle reset method
				startButton.setText("RESET");
				gameStarted = true;
			}
			else //reset button pressed, so reset the game
			{
				//stop timer
				timeClock.stop();
				timer.stop();
				reset();
				//start timer because game was restarted
				timeClock.start();
				count = 1;
			}
		}
		
		//Check which menu item is clicked
		if(str.equals("About"))
		{
			aboutDetails();
		}
		else if(str.equals("Help"))
		{
			helpMessage();
		}
		else if(str.equals("Quit"))
		{
			endGame();
		}
	}
	
	//Handle Abour menu
	private void aboutDetails()
	{
		 JOptionPane.showMessageDialog(null, "This is project 5, Tetris game, which is implemented for Software Design course (CS-342) Class at UIC.\n"
                 +"The team members for this project are Henvy Patel (hpate37) and Janki Patel (jpate53)\n", "About Tetris Game", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//Handle Help menu
	private void helpMessage()
	{
		 JOptionPane.showMessageDialog(null, "Instructions to play game: \n Move left - keyboard left key \n Move right - keyboard right key \n "
		 		+ "Change shape - keyboard page up \n" + "Move down Fast -  keyboard page down \n"
                 +"\n", "Help", JOptionPane.INFORMATION_MESSAGE);
	}
	
	//end the current game
	private void endGame()
	{
		System.exit(0);
	}
}