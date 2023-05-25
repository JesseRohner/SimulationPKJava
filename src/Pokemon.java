/* Primarily implemented by Farren Wang
 * CS230 Final Project
 * Pokemon Object class
 * May 7, 2015
 * 
 * This class allows for the creation of 
 * Pokemon objects with their name, types, 
 * moves, damages, and HP.
 */

import java.io.*;
import java.util.Scanner;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.ImageIcon;

public class Pokemon{
  
  private String name;
  private String type;
  private Hashtable<String,Integer> movesAndDamages;
  private String[] ownMoves;
  private int HP;
  
  /* constructor takes string name and 
   * string type to create new Pokemon object
   */
  
  public Pokemon (String name, String type){
    this.name = name;  
    this.type = type;
    this.HP = 100; //starting HP, not final
    ownMoves = new String[4];
    movesAndDamages = new Hashtable<String,Integer>();
  }
  
  //constructor for a generic pokemon
  public Pokemon (){
    this.name = "MissingNo";  //this is funny
    this.type = "???";
    this.HP = 100; //starting HP, not final
    ownMoves = new String[4];
    movesAndDamages = new Hashtable<String,Integer>();
  }
  
  /* reads from a text file of all moves of the pokemon's type, then
   * adds the moves to an ArrayList. Then the method shuffles the 
   * ArrayList adds the first four moves to the Pokemon. (Each Pokemon
   * learns four moves of their type)
   */
  public void populateMoves() throws FileNotFoundException {
    ArrayList<String> allMoves = new ArrayList<String>(); //really just all of one type
    
    try{
      Scanner scan = new Scanner(new File("txt/" + type + ".txt"));
      Scanner scanDamages = new Scanner (new File("txt/Damages.txt"));
      
      while (scan.hasNext() && scanDamages.hasNext()) {
        String move = scan.next();
        allMoves.add(move);
        String damageString = scanDamages.next();
        Integer damage = Integer.parseInt(damageString);
        movesAndDamages.put(move,damage);
      }
      scanDamages.close();
      scan.close();
      
      //assigns 4 random moves as the pokemon's own moves
      Collections.shuffle(allMoves);
      for (int i=0; i<4; i++) {
        ownMoves[i] = allMoves.get(i);
      }
    }
    catch (RuntimeException e) {
      throw e;
    } 
  }
  
  //generic getter methods
  
  //returns the name of the Pokemon
  public String getName(){
    return this.name;
  }
  
  //returns the type of the Pokemon;
  public String getType(){
    return this.type;
  }
  
  /* returns the moves known by the pokemon 
   * as a String array
   */
  public String[] getMoves(){
    return this.ownMoves;
  }
  
  //returns Pokemon's remaining health
  public int getHP(){
    return this.HP;
  }
  
  //other methods
  
  /* returns image associated with the pokemon
   * by reading in the image from a file using the 
   * pokemon's name
   */
  public ImageIcon getImage(){
    ImageIcon img = new ImageIcon();
    try {
      img = new ImageIcon("img/" + name +".gif");
    } 
    catch (Exception e) {
      System.out.println("File not found!");
    }
    return img;
  }
  
  /* takes in a String move as a parameter and returns
   * the damage done by that specific move
   */
  public int getDamage(String move){
    return (int) movesAndDamages.get(move);
  }
  
  /* takes an int and changes the health points
   * of the pokemon object
   * 
   * allows for taking damage/healing, though
   * HP cannot increase beyond 100 or fall below 0
   * 
   */
  public void changeHP(int change){
    this.HP += change;
    if (this.HP>100){
      this.HP=100;
    }
    else if (this.HP<0){
      this.HP=0;
    }
  }
  
  
  //this is testing code
  public static void main (String[] args){
    //creates a meowth
    Pokemon meowth = new Pokemon("Meowth", "Normal");
    try{
      //gives the meowth moves
      meowth.populateMoves();
      String[] moves = meowth.getMoves();
      //gets the damage that meowth's first move does
      System.out.println(meowth.getDamage(moves[1]));
      System.out.println(meowth.getType());
    }
    catch (Exception e){
      System.out.println(e);
    }
    
    try {
        Scanner scan = new Scanner(new File("txt/Pokemon.txt"));
        while (scan.hasNextLine()) { 
          String temp1 = scan.next();
          String temp2 = scan.next();
          Pokemon newPoke = new Pokemon(temp1, temp2);
          System.out.println(newPoke.getName() + newPoke.getType());

        }
        scan.close();
      } catch (java.io.IOException e) {
        System.out.println("Error while reading from Pokemon file");
      }
    
    
  }
}