//*******************************************************************
//  BSTNode.java       Java Foundations
//
//  Represents a node in a binary search tree, storing Comparable
//  elements.
//*******************************************************************

package javafoundations;

public class BSTNode<T extends Comparable<T>> extends BTNode<T>
{
   //-----------------------------------------------------------------
   //  Creates a new tree node with the specified data.
   //-----------------------------------------------------------------
   public BSTNode (T element)
   {
      super(element);
   }

   //-----------------------------------------------------------------
   //  Adds a new node containing the specified element at the
   //  appropriate place in this tree.
   //-----------------------------------------------------------------
   public void add (T item)
   {
      if (item.compareTo(element) < 0)
         if (left == null)
            left = new BSTNode (item);
         else
            ((BSTNode)left).add (item);
      else
         if (right == null)
            right = new BSTNode (item);
         else
            ((BSTNode)right).add (item);
   }

   //-----------------------------------------------------------------
   //  Returns the node in this subtree whose element matches the
   //  specified target. Returns null if the target is not found.
   //  Overrides the find method of BTNode to capitalize on the
   //  binary search tree characteristics.
   //-----------------------------------------------------------------
   public BSTNode<T> find (T target)
   {
      BSTNode<T> result = null;

      if (target.compareTo(element) == 0)
         result = this;
      else
      {
         if (target.compareTo(element) < 0)
         {
            if (left != null)
               result = ((BSTNode)left).find (target);
         }
         else
            if (right != null)
               result = ((BSTNode)right).find (target);
      }

      return result;
   }

   //-----------------------------------------------------------------
   //  Removes the specified target from this subtree. Returns a
   //  reference to the revised tree. The tree will be unchanged if
   //  the target is not found.
   //-----------------------------------------------------------------
   public BSTNode<T> remove(T target)
   {
      BSTNode<T> result = this;

      if (target.compareTo(element) == 0)
      {
         if (left == null && right == null)
            result = null;
         else if (left != null && right == null)
            result = (BSTNode)left;
         else if (left == null && right != null)
            result = (BSTNode)right;
         else
         {
            result = getSuccessor();
            result.left = left;
            result.right = right;
         }
      }
      else
         if (target.compareTo(element) < 0)
            if (left != null)
               left = ((BSTNode)left).remove(target);
         else
            if (right != null)
               right = ((BSTNode)right).remove(target);

      return result;
   }

   //-----------------------------------------------------------------
   //  Finds and returns the node containing the inorder successor of
   //  this node, and then removes the successor from its original
   //  location in the tree.
   //-----------------------------------------------------------------
   protected BSTNode<T> getSuccessor()
   {
      BSTNode<T> successor = (BSTNode)right;

      while (successor.getLeft() != null)
         successor = (BSTNode) successor.getLeft();

      ((BSTNode)right).remove (successor.getElement());

      return successor;
   }
}
