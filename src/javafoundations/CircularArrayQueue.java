//********************************************************************
//  CircularArrayQueue.java       Java Foundations
//
//  Represents an array implementation of a queue in which neither
//  end of the queue is fixed in the array. The variables storing the
//  indexes of the front and rear of the queue continually increment
//  as elements are removed and added, cycling back to 0 when they
//  reach the end of the array.
//********************************************************************

package javafoundations;

import javafoundations.exceptions.*;

public class CircularArrayQueue<T> implements Queue<T>
{
   private final int DEFAULT_CAPACITY = 10;
   private int front, rear, count;
   private T[] queue;

   //-----------------------------------------------------------------
   //  Creates an empty queue using the default capacity.
   //-----------------------------------------------------------------
   public CircularArrayQueue()
   {
      front = rear = count = 0;
      queue = (T[]) (new Object[DEFAULT_CAPACITY]);
   }

   //-----------------------------------------------------------------
   //  Adds the specified element to the rear of this queue, expanding
   //  the capacity of the queue array if necessary.
   //-----------------------------------------------------------------
   public void enqueue (T element)
   {
      if (count == queue.length)
         expandCapacity();

      queue[rear] = element;
      rear = (rear+1) % queue.length;
      count++;
   }

   //-----------------------------------------------------------------
   //  Creates a new array to store the contents of this queue with
   //  twice the capacity of the old one.
   //-----------------------------------------------------------------
   public void expandCapacity()
   {
      T[] larger = (T[])(new Object[queue.length*2]);

      for (int index=0; index < count; index++)
         larger[index] = queue[(front+index) % queue.length];

      front = 0;
      rear = count;
      queue = larger;
   }

	@Override
	public T dequeue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T first() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

   //-----------------------------------------------------------------
   //  The following methods are left as Programming Projects.
   //-----------------------------------------------------------------
   // public T dequeue () throws EmptyCollectionException { }
   // public T first () throws EmptyCollectionException { }
   // public int size() { }
   // public boolean isEmpty() { }
   // public String toString() { }
}