//********************************************************************
//  ArrayBag.java       Java Foundations
//
//  Represents an array implementation of bag collection.
//********************************************************************

package javafoundations;

import java.util.*;
import javafoundations.exceptions.EmptyCollectionException;

public class ArrayBag<T> implements Bag<T>
{
   private final int DEFAULT_CAPACITY = 10;
   private int count;
   private T[] contents;
   private static Random rand = new Random();

   //-----------------------------------------------------------------
   //  Creates an empty bag using the default capacity.
   //-----------------------------------------------------------------
   public ArrayBag()
   {
      count = 0;
      contents = (T[]) (new Object[DEFAULT_CAPACITY]);
   }

   //-----------------------------------------------------------------
   //  Adds the specified element to this bag by storing the element
   //  at the end of the list, expanding the array capacity if needed.
   //-----------------------------------------------------------------
   public void add (T element)
   {
      if (count == contents.length)
         expandCapacity();

      contents[count] = element;
      count++;
   }

   //-----------------------------------------------------------------
   //  Removes a random element from this bag, shifting the last
   //  element into its place to keep the list contiguous. Throws
   //  EmptyCollectionException if this bag is empty.
   //-----------------------------------------------------------------
   public T remove() throws EmptyCollectionException
   {
      if (count == 0)
         throw new EmptyCollectionException ("Remove operation " +
            "failed. The bag is empty.");

      int index = rand.nextInt(count);
      T result = contents[index];

      count--;
      contents[index] = contents[count];
      contents[count] = null;

      return result;
   }

   //-----------------------------------------------------------------
   //  Returns true if this bag contains no elements, and false
   //  otherwise.
   //-----------------------------------------------------------------
   public boolean isEmpty()
   {
      return (count == 0);
   }

   //-----------------------------------------------------------------
   //  Returns the number of elements in this bag.
   //-----------------------------------------------------------------
   public int size()
   {
      return count;
   }

   //-----------------------------------------------------------------
   //  Returns an iterator for this bag.
   //-----------------------------------------------------------------
   public Iterator<T> iterator()
   {
      ArrayIterator<T> iter = new ArrayIterator<T>();
      for (int index = 0; index < count; index++)
         iter.add(contents[index]);

      return iter;
   }

   //-----------------------------------------------------------------
   //  Returns a string representation of this bag.
   //-----------------------------------------------------------------
   public String toString()
   {
      String result = "Bag Contents:\n";

      for (int index=0; index < count; index++)
         result += contents[index] + "\n";

      return result;
   }

   //-----------------------------------------------------------------
   //  Creates a new array to store the contents of this bag with
   //  twice the capacity of the old one.
   //-----------------------------------------------------------------
   private void expandCapacity()
   {
      T[] larger = (T []) (new Object[contents.length*2]);

      int location = 0;
      for (T element : contents)
         larger[location++] = element;

      contents = larger;
   }
}

