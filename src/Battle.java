/* Primarily implemented by Farren Wang
 * CS230 Final Project
 * May 2015
 * 
 * This class contains the functionality of the battle,
 * including using moves and taking damage, as well as checking
 * whether the battle is over.
 * 
 * This class is able to change the gold amount in a map class.
 */

public class Battle{
  
  private Pokemon yours, wild;
  private Map location;
  
  //constructs a battle using two pokemon and a Map
  public Battle(Pokemon yours, Pokemon wild, Map location){
    this.yours = yours;
    this.wild = wild;
    this.location = location;
    
    //gives the wild Pokemon moves
    try{
      wild.populateMoves();
    }
    catch(Exception e){
      System.out.println(e);
    }
  }
  
  //returns the player's pokemon
  public Pokemon getYours(){
    return this.yours;
  }
  
  //returns the wild pokemon
  public Pokemon getWild(){
    return this.wild;
  }
  
  //returns the gold the player has
  public int getGold(){
    return location.getGold();
  }
  
  //changes the gold of the player
  public void increaseGold(int income){
    location.increaseGold(income);
  }
  
  //other methods
  
  /* healing with gold allows a player
   * to heal their pokemon for 50HP with 
   * 10 gold, it's like using a potion
   */
  public void healWithGold(){
    location.increaseGold(-10);
    yours.changeHP(50);
  }
  
  /* wild pokemon deals damage, returns the name of the 
   * attack used. The wild pokemon's move is chosen randomly 
   * and the damage is retrieved from the wild Pokemon object.
   * The damage is then subtracted from your pokemon's health.
   */
  
  //wild pokemon randomly chooses a move and attacks
  public String wildPokemonAttacks(){
    String[] wildMoves = wild.getMoves();
    //random int for index of move to be used
    int randomNum = (int)(Math.random()*3);
    int damage = wild.getDamage(wildMoves[randomNum]);
    yours.changeHP(-damage);
    return wildMoves[randomNum];
  }
  
  /* takes in a string representation of the move
   * the player has decided to use. Then gets the damage
   * of the move and subtracts it from the health of 
   * the opponent Pokemon.
   */
  
  public void yoursAttacks(String move){
    int damage = yours.getDamage(move);
    wild.changeHP(-damage);
  }
  
  // if your pokemon's HP reaches 0, it has fainted
  public boolean yoursFainted(){
    return yours.getHP()<=0;
  }
  
  // if the wild pokemon's HP reaches 0, it has fainted
  public boolean wildFainted(){
    return wild.getHP()<=0;
  }
  
  /* checks if battle is over, it is over when either one 
   * of the pokemon faint
   */
  public boolean isBattleOver(){
    return wildFainted()==true || yoursFainted()==true;
  }
  
  //you win if your pokemon has not fainted by the wild one has
  public boolean youWin(){
    return wildFainted()==true && yoursFainted()==false;
  }
  
  //tie if both pokemon have fainted
  public boolean isTie(){
    return wildFainted()==true && yoursFainted()==true;
  }
}