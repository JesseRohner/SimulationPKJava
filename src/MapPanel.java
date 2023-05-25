/* Lily and Farren Wang
 * CS230 Final Project
 * MapPanel.java
 * 
 * Created by: Lily
 * Contains the Map object, a queue of wild Pokemon, and functionality to listen for arrow key activity, moves 
 * the character on the screen, starts a battle, and handles game over accordingly. 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javafoundations.ArrayQueue;
import java.util.*; // For Scanner and Random

public class MapPanel extends JPanel
{
  private final int SIZE_OF_MAP_CELL = 80;  // Size of each map cell
  private final Color MAP_COLOR = new Color(115, 206, 165);  // Color of map
  private final ImageIcon GATE_IMAGE = new ImageIcon("img/gate.gif");
  private final ImageIcon GRUNT_IMAGE = new ImageIcon("img/grunt.gif");
  private final ImageIcon PLAYER_IMAGE = new ImageIcon("img/player.gif");
  private final ImageIcon BRUSH_IMAGE = new ImageIcon("img/brush.gif");
  private final String STATUS_MESSAGE = "I have to escape from this place!";
  
  private int rows;  // Number of rows in map
  private int cols;  // Number of cols in map
  private Pokemon pokemon;  // Charmander, Squirtle, or Bulbasaur
  private Map theMap;  // We separate "the map" from the GUI map label components.
  private JLabel[][] mapLabels;  // We separate the map from the "GUI map label components".
  private int playerRow;  // Player's current row
  private int playerCol;  // Player's current column
  private int gateRow;  // Gate's current row
  private int gateCol;  // Gate's current column
  private int gruntRow; // Team Rocket Grunt's current row
  private int gruntCol; // Team Rocket Grunt's current column
  private JLabel statusLabel;  // Displays all information for player
  private JLabel goldLabel; // Displays the amount of gold the player has
  private ArrayQueue<Pokemon> pokemonQueue; // Contains all the wild Pokemon available for battle
  private DirectionListener movementListener;  // Listens for keyboard events
  private MouseOverListener mouseListener;  // Listens for mouse-over (hover) events
  public JFrame battle;
  
  
  //-----------------------------------------------------------------
  //  Constructor: Sets up this panel
  //-----------------------------------------------------------------
  public MapPanel(int rows, int cols, Pokemon pokemon)
  {
    battle = new JFrame();
    // Uses a custom font found on the Internet
    Font myFont = null;
    try {
      File fontFile = new File("Pokemon GB.ttf");
      try {
        myFont = Font.createFont(Font.TRUETYPE_FONT, fontFile).deriveFont(Font.PLAIN, 22f);
      } catch (java.awt.FontFormatException e) {
        System.out.println("Error while creating font");
      }} catch (java.io.IOException e) {
        System.out.println("Error while creating font");
      }
      
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(myFont);
      
      this.rows = rows;
      this.cols = cols;
      this.pokemon = pokemon;
      playerRow = rows-2;  // Player's starting row
      playerCol = cols-2;  // Player's starting column
      gateRow = rows-2;   // Gate's row
      gateCol = 1;  // Gate's column
      gruntRow = 1; // Team Rocket Grunt's row
      gruntCol = 1; // Team Rocket Grunt's column
      
      // Gives the starter pokemon that the user selected 4 moves
      try {
        pokemon.populateMoves(); 
      } catch (IOException e) {
        System.out.println("Error while reading file");
      } 
      
      // Adds all Pokemon into queue to represent population of wild Pokemon
      try {
        pokemonQueue = new ArrayQueue<Pokemon>(); // Initializes queue
        Scanner scan = new Scanner(new File("txt/Pokemon.txt")); // Read from txt file
        while (scan.hasNextLine()) { 
          String temp1 = scan.next();
          String temp2 = scan.next();
          pokemonQueue.enqueue(new Pokemon(temp1, temp2));
        }
        scan.close();
      } catch (java.io.IOException e) {
        System.out.println("Error while reading from Pokemon file");
      }
      
      mouseListener = new MouseOverListener();  // Listen for mouse-over (hover) events
      theMap = new Map(rows, cols, pokemon.getName());  // Create the map structure
      addMapComponents();  // Add map label components to GUI
      
      statusLabel = new JLabel(STATUS_MESSAGE);
      statusLabel.setFont(myFont);
      goldLabel = new JLabel("                          Gold: " + theMap.getGold()); // Displays current amount of gold
      goldLabel.setFont(myFont);
      add(statusLabel);
      add(goldLabel);
      movementListener = new DirectionListener();
      addKeyListener(movementListener);  // Listen for keyboard events, i.e., arrow keys
      
      setPreferredSize(new Dimension(cols*SIZE_OF_MAP_CELL, rows*SIZE_OF_MAP_CELL + 80)); // Add 80 to account for status label
      setFocusable(true);
  }
  
  /**
   * Populate map label components for GUI.
   */
  private void addMapComponents() {     
    JPanel mapPanel = new JPanel();
    mapPanel.setLayout(new GridLayout(rows, cols));  // Use GridLayout for map labels
    mapLabels = new JLabel[rows][cols];
    for (int r=0; r<rows; r++) {
      for (int c=0; c<cols; c++) {
        mapLabels[r][c] = new JLabel();
        mapLabels[r][c].setPreferredSize(new Dimension(SIZE_OF_MAP_CELL, SIZE_OF_MAP_CELL));
        mapLabels[r][c].setMinimumSize(new Dimension(SIZE_OF_MAP_CELL, SIZE_OF_MAP_CELL));
        Color clr = MAP_COLOR;
        if (theMap.getCell(r,c).isBrush()) 
          mapLabels[r][c].setIcon(BRUSH_IMAGE);
        mapLabels[r][c].setBackground(clr);
        mapLabels[r][c].setOpaque(true);
        mapPanel.add(mapLabels[r][c]);  // Add label components to GUI
      }
    }
    mapLabels[playerRow][playerCol].setIcon(PLAYER_IMAGE);  // Add player image
    mapLabels[playerRow][playerCol].addMouseListener(mouseListener);  // Listen for mouse-over events
    mapLabels[gateRow][gateCol].setIcon(GATE_IMAGE);  // Add gate image
    mapLabels[gateRow][gateCol].addMouseListener(mouseListener);  // Listen for mouse-over events
    mapLabels[gruntRow][gruntCol].setIcon(GRUNT_IMAGE); // Add Grunt image
    mapLabels[gruntRow][gruntCol].addMouseListener(mouseListener);  // Listen for mouse-over events
    add(mapPanel);  // Add map panel to GUI
  }
  
  // Moves the player on the map, and checks for game over, buying key, and entering brush
  private void movePlayer(int deltaRow, int deltaCol) {
    mapLabels[playerRow][playerCol].setIcon(null);  // Remove player from old cell
    mapLabels[playerRow][playerCol].removeMouseListener(mouseListener);
    playerRow += deltaRow;  // Move player
    playerCol += deltaCol;  // Move player
    mapLabels[playerRow][playerCol].setIcon(PLAYER_IMAGE);  // Add player to new cell
    mapLabels[playerRow][playerCol].addMouseListener(mouseListener);
    handleGameOver();  // Has player reached the gate and do they have the key? Are there Pokemon left to battle?
    buyKey(); // Has the player reached Grunt and do they have enough gold to buy the key?
    startBattle(); // Has the player stepped into a brush to battle Pokemon?
    goldLabel.setText("                          Gold: " + theMap.getGold());
  }
  
  // May or may not start a wild Pokemon battle when player walks through a brush
  private void startBattle() {
    if (!pokemonQueue.isEmpty()) { // Avoids crashing if queue is empty
      if (theMap.getCell(playerRow, playerCol).isBrush()) {
        // 1 out of 5 chance that a battle will start
        if (new Random().nextInt(5) == 0){
          battle = new JFrame(); // Frame for Pokemon battle
          Battle newEncounter = new Battle(pokemon, pokemonQueue.dequeue(), theMap);
          BattleGUIPanel battlePanel =  new BattleGUIPanel(newEncounter, battle);
          battle.getContentPane().add(battlePanel);
          battle.pack();
          battle.setVisible(true);
        }
      }
    }
  }
  
  // Checks if player has enough gold to buy the key from Team Rocket Grunt
  // If the player does, subtracts 200 gold from him and gives him the key to the gate
  public void buyKey() {
    if ((playerRow==gruntRow) && (playerCol==gruntCol)) {
      if (!theMap.hasEnoughGold()) 
        statusLabel.setText("<html>You don't have enough gold!<br> Go battle some more Pokemon!</html>");
      else {
        statusLabel.setText("Here, take the key.");
        theMap.increaseGold(-200);
        goldLabel.setText("                          Gold: " + theMap.getGold());
        theMap.setHasKey();
      }
    }
  }
  
  /**
   * Check if player has reached the gate and has a key or if there are no more Pokemon to battle.
   */
  public void handleGameOver() {
    if (((playerRow==gateRow) && (playerCol==gateCol)) || pokemonQueue.isEmpty()) { // Game over.
      if (theMap.hasKey || pokemonQueue.isEmpty()) {
        removeKeyListener(movementListener);  // Disable keys so player cannot move
        mapLabels[gateRow][gateCol].removeMouseListener(mouseListener);  // No need to listen for gate mouse-over event
        mapLabels[playerRow][playerCol].removeMouseListener(mouseListener);  // No need to listen for player mouse-over event
        mapLabels[gruntRow][gruntCol].removeMouseListener(mouseListener); // No need to listen for Grunt mouse-over event
        if (theMap.hasKey)
          statusLabel.setText("<html>You have successfully escaped! <br>Exit to play again.</html>");
        // Will not end game if player has enough gold to buy the key but runs out of Pokemon to battle
        else if (!theMap.hasEnoughGold() && pokemonQueue.isEmpty()) 
          statusLabel.setText("<html>There are no more wild Pokemon <br>in your vicinity. Try again</html>");
      } 
      else 
        statusLabel.setText("You need a key to open this gate");
    }
  }
  
  
  //*****************************************************************
  //  Represents the listener for keyboard activity.
  //*****************************************************************
  private class DirectionListener implements KeyListener
  {
    //--------------------------------------------------------------
    //  Responds to the user pressing arrow keys by adjusting the
    //  player location accordingly.
    //--------------------------------------------------------------
    public void keyPressed (KeyEvent event)
    {
      if (event.getKeyCode() == KeyEvent.VK_UP) {  // Up arrow pressed
        if (playerRow-1 != 0) // Ensures the player doesn't go out of bounds
          movePlayer(-1, 0);  // Move player up
        // So the brush doesn't disappear after moving off of it
        if (theMap.getCell(playerRow+1, playerCol).isBrush()) 
          mapLabels[playerRow+1][playerCol].setIcon(BRUSH_IMAGE);
        // So the gate doesn't disappear after moving off of it
        if (playerRow+1 == gateRow && playerCol == gateCol) 
          mapLabels[playerRow+1][playerCol].setIcon(GATE_IMAGE);
      } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {  // Down arrow pressed
        if (playerRow+1 != rows-1) 
          movePlayer(+1, 0);  // Move player down 
        if (theMap.getCell(playerRow-1, playerCol).isBrush()) 
          mapLabels[playerRow-1][playerCol].setIcon(BRUSH_IMAGE); 
        // So Team Rocket Grunt doesn't disappear after moving off of her cell
        if (playerRow-1 == gruntRow && playerCol == gruntCol) 
          mapLabels[gruntRow][gruntCol].setIcon(GRUNT_IMAGE);
      } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {  // Left arrow pressed
        if (playerCol-1 != 0) // Ensures the player doesn't go out of bounds
          movePlayer(0, -1);  // Move player left
        if (theMap.getCell(playerRow, playerCol+1).isBrush()) 
          mapLabels[playerRow][playerCol+1].setIcon(BRUSH_IMAGE); 
        // So the gate doesn't disappear after moving off of it
        if (playerRow == gateRow && playerCol+1 == gateCol) 
          mapLabels[gateRow][gateCol].setIcon(GATE_IMAGE);
      } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {  // Right arrow pressed
        if (playerCol+1 != cols-1) 
          movePlayer(0, +1);  // Move player right
        if (theMap.getCell(playerRow, playerCol-1).isBrush()) 
          mapLabels[playerRow][playerCol-1].setIcon(BRUSH_IMAGE); 
        // So the gate doesn't disappear after moving off of it
        if (playerRow == gateRow && playerCol-1 == gateCol) 
          mapLabels[gateRow][gateCol].setIcon(GATE_IMAGE);
        // So Team Rocket Grunt doesn't disappear after moving off of her cell
        if (playerRow == gruntRow && playerCol-1 == gruntCol) 
          mapLabels[gruntRow][gruntCol].setIcon(GRUNT_IMAGE);
        if (playerRow == gateRow && playerCol-1 == gateCol) 
          mapLabels[playerRow][playerCol-1].setIcon(GATE_IMAGE);
      }
    }
    
    //--------------------------------------------------------------
    //  Provide empty definitions for unused event methods.
    //--------------------------------------------------------------
    public void keyTyped (KeyEvent event) {}
    public void keyReleased (KeyEvent event) {}
  }
  
  //*****************************************************************
  //  Represents the listener for mouse activity.
  //*****************************************************************
  private class MouseOverListener implements MouseListener
  {
    //--------------------------------------------------------------
    //  Responds to the user mousing over the player, Grunt, or gate.
    //--------------------------------------------------------------
    public void mouseEntered (MouseEvent event) {
      if (event.getSource().equals(mapLabels[playerRow][playerCol]))  // Mouse over player
        statusLabel.setText("This is you");
      else if (event.getSource().equals(mapLabels[gateRow][gateCol]))  // Mouse over gate
        statusLabel.setText("The gate needs a key");
      else if (event.getSource().equals(mapLabels[gruntRow][gruntCol])) // Mouse over Grunt
        statusLabel.setText("Psst! I can sell you the key for 200 gold!");
    }
    
    public void mouseExited (MouseEvent event) {
      statusLabel.setText(STATUS_MESSAGE);
    }
    
    //--------------------------------------------------------------
    //  Provide empty definitions for unused event methods.
    //--------------------------------------------------------------
    public void mouseClicked (MouseEvent event) {}
    public void mousePressed (MouseEvent event) {}
    public void mouseReleased (MouseEvent event) {}
  }
}