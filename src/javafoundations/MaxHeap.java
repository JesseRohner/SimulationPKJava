//*******************************************************************
//  MaxHeap.java       Java Foundations
//
//  Defines the interface to a max heap.
//*******************************************************************

package javafoundations;

public interface MaxHeap<T extends Comparable<T>> extends BinaryTree<T>
{
   //  Adds the specified object to the heap.
   public void add (T obj);

   //  Returns a reference to the element with the highest value in
   //  the heap.
   public T getMax ();

   //  Removes and returns the element with the highest value in the
   //  heap.
   public T removeMax ();
}
