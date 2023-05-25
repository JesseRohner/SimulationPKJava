//********************************************************************
//  BinarySearchTree.java       Java Foundations
//
//  Defines the interface to a binary search tree.
//********************************************************************

package javafoundations;

public interface BinarySearchTree<T extends Comparable<T>> extends BinaryTree<T>
{
   //  Adds the specified element to the tree.
   public void add (T element);

   //  Finds and returns the element in the tree matching the
   //  specified target. Overrides the find method of BinaryTree.
   public T find (T target);

   //  Returns the minimum value in the binary search tree.
   public T findMin();

   //  Returns the maximum value in the binary search tree.
   public T findMax();

   //  Removes and returns the specified element from the tree.
   public T remove (T target);
}
