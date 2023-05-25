/* Lily and Farren Wang
 * CS230 Final Project
 * Map.java
 * 
 * Created by: Lily
 * Uses a 2-D array of integers to represent the map that the character walks on. 
 * Will randomly generate brushes all over the map that contain wild Pokémon for the player to battle. 
 * A corner cell will contain Team Rocket Grunt who sells the key and another corner cell will 
 * contain the gate. */
import java.util.*;
import java.awt.Point;

/**
 * Represents a 2D maze of cells.
 */
public class Map {
  public final int COST_OF_KEY = 200;
  
  private int rows, cols;
  private String pokemon;
  private Cell[][] grid;
  public int gold; // Keeps track of how much gold the user has
  public boolean hasKey; // Used to see if the user has obtained the key or not
  
  public Map(int rows, int cols, String pokemon) {
    this.rows = rows;
    this.cols = cols;
    this.pokemon = pokemon;
    gold = 50; // Player starts off with 50 gold;
    hasKey = false; // Player starts off not having the key
    grid = new Cell[rows][cols];
    for (int r=0; r<rows; r++) {
      for (int c=0; c<cols; c++)
        grid[r][c] = new Cell();  // Initialize each Cell in the map
    }
    generateRandomMap(1, 1);  // Start at (1,1)
  }
  
  // Getters and setters
  public Cell getCell(int row, int col) {
    return grid[row][col];
  }
  
  public int getGold() {
    return gold;
  }
  
  public void increaseGold(int amount){
    gold += amount;
  }
  
  public void setHasKey() {
    hasKey = true;
  }
  
  public boolean getHasKey() {
    return hasKey;
  }
  
  // Checks to see if the player has enough gold to buy the key from Grunt
  public boolean hasEnoughGold() {
    return (gold >= COST_OF_KEY);
  }
  
  /**
   * Populates the entire map wuth brushes and then randomly removes approximately half of them
   */
  private void generateRandomMap(int row, int col) {
    HashMap<Point, Point> brushes = new HashMap<Point, Point>();
    for (int i = 0; i < (rows*cols)/2; i++) {
      grid[row][col].setBrush(false);
      addBrushes(brushes, row, col);
      int random1 = new Random().nextInt(rows-2)+1;
      int random2 = new Random().nextInt(cols-2)+1;
      if (grid[random1][random2].isBrush()) {
        grid[random1][random2].addToMap();
        addBrushes(brushes, random1, random2);
      }
      brushes.remove(new Point(random1, random2));
    }
  }
  
  /**
   * Adds up to four brushes for the specified (row,col) to the dictionary of brushes.
   * The "brushes" here represent passages to other parts of the map, so we only
   * add "brushes" to cells that are not part of the border row or column.
   */
  private void addBrushes(HashMap<Point, Point> brushes, int row, int col) {
    if (row > 2) brushes.put(new Point(row-1, col), new Point(row-2, col));  // Add northern brush
    if (row < rows-3) brushes.put(new Point(row+1, col), new Point(row+2, col));  // Add southern brush
    if (col > 2) brushes.put(new Point(row, col-1), new Point(row, col-2));  // Add western brush
    if (col < cols-3) brushes.put(new Point(row, col+1), new Point(row, col+2));  // Add eastern brush       
  }
}

/**
 * Represents one cell in the map.
 */
class Cell {
  
  private boolean brush;
  
  public Cell() {
    brush = true;
  }
  
  public boolean isBrush() {
    return brush;
  }
  
  public void setBrush(boolean isBrush) {
    brush = isBrush;
  }
  
  public void addToMap() {
    brush = false;
  }
}