/* Lily and Farren Wang
 * CS230 Final Project
 * PokemonStartup.java
 *
 * Created by: Lily
 * Creates and displays the main program frame. 
 * This program is optimized for PC. */
import javax.swing.JFrame; 

public class PokemonStartup {
  
   public static void main (String[] args)
   {
      JFrame frame = new JFrame ("Pokemon Game Startup Window");
      frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

      frame.getContentPane().add(new PokemonStartupPanel());

      frame.pack();
      frame.setVisible(true);
   }
}