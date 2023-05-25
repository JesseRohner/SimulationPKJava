//*******************************************************************
//  LinkedMaxHeap.java        Java Foundations
//
//  Implements a max heap using a linked representation.
//*******************************************************************

package javafoundations;

import javafoundations.exceptions.*;

public class LinkedMaxHeap<T extends Comparable<T>>
   extends LinkedBinaryTree<T> implements MaxHeap<T>
{
   private HeapNode<T> last;

   //-----------------------------------------------------------------
   //  Creates a max heap with the specified element as its root.
   //-----------------------------------------------------------------
   public LinkedMaxHeap ()
   {
      super();
      last = null;
   }

   //-----------------------------------------------------------------
   //  Creates a max heap with the specified element as its root.
   //-----------------------------------------------------------------
   public LinkedMaxHeap (T element)
   {
      root = new HeapNode<T>(element);
      last = (HeapNode<T>)root;
   }

   //-----------------------------------------------------------------
   //  Adds the specified element to this heap by adding it as a leaf
   //  then reestablishing the heap relationships.
   //-----------------------------------------------------------------
   public void add (T element)
   {
      HeapNode<T> node = new HeapNode<T>(element);
      HeapNode<T> newParent = null;

      if (root == null)
         root = node;
      else
      {
         newParent = ((HeapNode<T>)root).getParentAdd(last);

         if (newParent.left == null)
            newParent.setLeft(node);
         else
            newParent.setRight(node);
      }

      node.setParent(newParent);
      last = node;

      ((HeapNode<T>)root).heapifyAdd(last);
   }

   //-----------------------------------------------------------------
   //  Remove the element with the largest value in this heap and
   //  returns a reference to it.
   //-----------------------------------------------------------------
   public T removeMax()
   {
      if (root == null)
         throw new EmptyCollectionException ("Remove max operation " +
            "failed. Tree is empty.");

      T maxElement = root.getElement();

      if (root.count() == 1)
         root = last = null;
      else
      {
         HeapNode<T> newLast = ((HeapNode<T>)root).getNewLastNode(last);
         if (last.parent.left == last)
            last.parent.left = null;
         else
            last.parent.right = null;

         root.setElement(last.getElement());
         last = newLast;
         ((HeapNode<T>)root).heapifyRemove((HeapNode<T>)root);
      }

      return maxElement;
   }

	@Override
	public T getMax() {
		// TODO Auto-generated method stub
		return null;
	}

   //-----------------------------------------------------------------
   //  The following method is left as a programming project.
   //-----------------------------------------------------------------
   // public T getMax() { }
}
