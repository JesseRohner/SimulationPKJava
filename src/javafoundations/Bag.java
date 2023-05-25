//*******************************************************************
//  Bag.java       Java Foundations
//
//  Defines the interface to a bag collection.
//*******************************************************************

package javafoundations;

public interface Bag<T> extends Iterable<T>
{
   //  Adds the specified element to the bag.
   public void add (T element);

   //  Removes and returns a random element from the bag.
   public T remove();

   //  Returns true if the bag contains no elements and false
   //  otherwise.
   public boolean isEmpty();

   //  Returns the number of elements in the bag.
   public int size();

   //  Returns a string representation of the bag.
   public String toString();
}
