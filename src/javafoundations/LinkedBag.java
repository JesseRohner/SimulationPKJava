//**************************************************************
//  LinkedBag.java       Java Foundations
//
//  Represents a linked implementation of a bag collection.
//**************************************************************

package javafoundations;

import java.util.*;
import javafoundations.exceptions.EmptyCollectionException;

public class LinkedBag<T> implements Bag<T>
{
   private int count = 0;
   private LinearNode<T> contents;
   private static Random rand = new Random();

   //-----------------------------------------------------------------
   //   Creates an empty list.
   //-----------------------------------------------------------------
   public LinkedBag()
   {
      count = 0;
      contents = null;
   }

   //-----------------------------------------------------------------
   //  Adds the specified element to this bag by putting it on the
   //  front of the list.
   //-----------------------------------------------------------------
   public void add (T element)
   {
      LinearNode<T> node = new LinearNode<T>(element);
      node.setNext(contents);
      contents = node;
      count++;
   }

   //-----------------------------------------------------------------
   //  Removes a random element from this bag. Throws an
   //  EmptyCollectionException if this bag is empty.
   //-----------------------------------------------------------------
   public T remove() throws EmptyCollectionException
   {
      T result = null;

      if (count == 0)
         throw new EmptyCollectionException ("Remove operation "
            + "failed. The bag is empty.");

      int choice = rand.nextInt(count) + 1;
      if (choice == 1)
      {
         result = contents.getElement();
         contents = contents.getNext();
      }
      else
      {
			LinearNode<T> current = contents;
         for (int i=1; i < choice-1; i++)
             current = current.getNext();
         result = current.getNext().getElement();
         current.setNext(current.getNext().getNext());
      }
		count--;
      return result;
   }

   //-----------------------------------------------------------------
   //  Returns an iterator for this bag.
   //-----------------------------------------------------------------
   public Iterator<T> iterator()
   {
      ArrayIterator<T> iter = new ArrayIterator<T>();
      LinearNode<T> current = contents;

      while (current != null)
      {
         iter.add(current.getElement());
         current = current.getNext();
      }

      return iter;
   }

   //-----------------------------------------------------------------
   //  The following methods are left as programming projects.
   //-----------------------------------------------------------------
   public boolean isEmpty() 
	{
		return count == 0;
 	}
   public int size() 
	{
		return count;
	}
   public String toString() 
	{
		String result = "";
		result += "Bag contents:\n";
		LinearNode<T> current = contents;
		while(current != null)
		{
			result += current.getElement();
			current = current.getNext();
		}
				
		return result;
	}
}
