/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Collections;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;

import java.util.Collection;

/**
 *
 * @author home_stanbridge
 * from tree provided by Google Inc. Licensed under the Apache License, Version 2.0
 * (NOTE) cannot find the source for this source code now
 * original author ycoppel@google.com (Yohann Coppel)
 * home_stanbridge added methods addTree
 *
 * A single tree class containing self recursive children of type T.
 * This is to be contrasted with Position Three - a more sophisticated tree that
 * takes Position objects of type T as nodes - but PositionTree is not yet complete
 *
 * NOTE: The nodes of the tree must be unique - no duplicates within the tree although an external node (one without children) may be
 * duplicate (i.e. the same object in more than one place) provided access to it is via a sub-tree that makes it unique.
 * This is because this tree is a random access tree - one can get to any node with the getSimpleTree, getSuccessors, toList etc. 
 *
 * PStanbridge - Added methods to add a tree to a leaf of the tree - this allows the build
 * of sub trees to then be added to another tree. Google version only allowed leaves to be added. 
 */
public class SimpleTree<T>
{
  // head of tree
  private T m_oHead;
  // leaves of the tree
  private List<SimpleTree<T>> m_oLeafs = new List<SimpleTree<T>>();
  // tree parent
  private SimpleTree<T> m_oParent = null;
  // tree structure in hash table
  private HashTable<T, SimpleTree<T>> m_oLocate = new HashTable<T, SimpleTree<T>>();
  // tostring() indent
  private static final int m_nIndent = 2;

    public SimpleTree(T toHead)
    {
        //set the head of the tree and add to the head of the hashtable
        m_oHead = toHead;
        m_oLocate.put(toHead, this);
    }

    public boolean containsLeaf(T toClass)
    {
        return m_oLocate.containsKey(toClass);
    }

   /**
    * Return the indexing hashtable of the tree (contains each node in the tree
    * @return - hash table containing the indexes of each node leaf and their tree values of the whole tree. 
    */
    public HashTable<T, SimpleTree<T>> returnLocate()
    {
       return m_oLocate;
   }
   
   /**
    * return a list of leaves of this node of the tree
    * @return - list of leaves of this node
    */
   public List<SimpleTree<T>> returnList()
    {
       return m_oLeafs;
   }
   
    /**
     * Add a leaf to a node in the tree
     * @param root - the node to add this node to
     * @param leaf - the node being added
     */
    public void addLeaf(T toRoot, T toLeaf)
    {
        if (m_oLocate.containsKey(toRoot))
        {
            m_oLocate.get(toRoot).addLeaf(toLeaf);
        }
        else
        {
            addLeaf(toRoot).addLeaf(toLeaf);
        }
    }

    /**
     * Add a leaf to this node
     * @param toLeaf - the node being added
     * @return - the new tree being added to this node
     */
    public SimpleTree<T> addLeaf(T toLeaf)
    {
        SimpleTree<T> loTree = new SimpleTree<T>(toLeaf);
        m_oLeafs.add(loTree);
        loTree.m_oParent = this; // "this" is the parent of the node being added
     //   loTree.setParent(this);
        loTree.m_oLocate = this.m_oLocate;
     //   loTree.setLocate(this.m_oLocate);
        m_oLocate.put(toLeaf, loTree);
        return loTree;
    }
    // Added by PStanbridge
    // ToDo - fix or remove this method
    /**
     * NOTE - not used in loading class tree - BUT this method currently does not work correctly - being corrected or removed
     * Add a tree to a root within the tree
     * @param toRoot - root leaf to add tree to
     * @param toTree - tree being added to the leaf
     */
    public void addTree(T toRoot, SimpleTree<T> toTree)
  {
      // If the root you are adding to is already in the tree, then add the tree to the root
      if (m_oLocate.containsKey(toRoot))
      {
          m_oLocate.get(toRoot).addTree(toTree);
      }
      // Else if the root is not in the tree, then add the root leaf as a new tree and add the tree to it
      else
      {
          addLeaf(toRoot).addTree(toTree);
      }

  }
   // Added by PStanbridge
    // ToDo - fix or remove this method
   /**
    * NOTE - not used in loading class tree - BUT this method currently does not work correctly - being corrected or removed
    * Add a tree to the leaf of the current tree
    * @param toTree - tree to add to the current tree leaf
    * @return - the tree being added (currently not used)
    */
    public SimpleTree<T> addTree(SimpleTree<T> toTree)
    {
        // add the tree as a new leaf to the current tree
        m_oLeafs.add(toTree);
        // this is the parent of the tree being added
        toTree.m_oParent = this;
        toTree.m_oLocate = this.m_oLocate;
        // add added tree to the hashtable
        m_oLocate.put(m_oHead, toTree);
        return toTree;
    }

    /**
     * set the parent value for tree
     * @param toTree - the tree to set as parent
     */

    private void setParent(SimpleTree toTree)
    {
        m_oParent = toTree;
    }
    /**
     * set the locate hash table for tree
     * @param toLocate the tree to set the locate to
     */
    private void setLocate(HashTable<T, SimpleTree<T>> toLocate)
    {
        m_oLocate = toLocate;
    }

    /**
     * set a new parent node
     * @param toParentRoot - the new parent node
     * @return - the tree containing the new parent node
     */
    public SimpleTree<T> setAsParent(T toParentRoot)
    {
        SimpleTree<T> toTree = new SimpleTree<T>(toParentRoot);
        toTree.m_oLeafs.add(this);
        this.m_oParent = toTree;
        toTree.m_oLocate = this.m_oLocate;
        toTree.m_oLocate.put(m_oHead, this);
        toTree.m_oLocate.put(toParentRoot, toTree);
        return toTree;
    }
    /**
     * return the leaf of this tree
     * @return - leaf of this tree - will be the root leaf of the tree
     */
  public T getHead() {
    return m_oHead;
  }

  /**
   * Return the tree from a given  leaf
   * @param toElement - the leaf element to obtain tree
   * @return - tree from the given element leaf
   */
  public SimpleTree<T> getSimpleTree(T toElement) {
    return m_oLocate.get(toElement);
  }

  /**
   * get the parent of this tree
   * @return - the parent of this tree
   */
  public SimpleTree<T> getParent() {
    return m_oParent;
  }

  /**
 * Return a collection of the tree leaves from a given "root" position
 * @param toRoot - position to return leaves from
 * @return - collection of leaves from the given root position
 */
  public Collection<T> getSuccessors(T toRoot) {
    Collection<T> loSuccessors = new List<T>();
    SimpleTree<T> loTree = getSimpleTree(toRoot);
    if (null != loTree) {
      for (SimpleTree<T> leaf : loTree.m_oLeafs) {
        loSuccessors.add(leaf.m_oHead);
      }
    }
    return loSuccessors;
  }
  /**
   * return immediate leaves of this tree root
   * @return - collection of immediate leaves of this tree root
   */
  public Collection<SimpleTree<T>> getSubSimpleTrees() {
    return m_oLeafs;
  }
/**
 * Return a collection of tree leaves from the passed in root of a passed in tree
 * @param <T> - type type of tree leaf
 * @param toOf - leaf to act as root leaf in returned list
 * @param toIn - tree to return the collection of leaves from the passed in node
 * @return
 */
  public static <T> Collection<T> getSuccessors(T toOf, Collection<SimpleTree<T>> toIn) {
    for (SimpleTree<T> tree : toIn) {
      if (tree.m_oLocate.containsKey(toOf)) {
        return tree.getSuccessors(toOf);
      }
    }
    return new List<T>();
  }

  /**
   * return string representation of the tree
   * @return - string representation of the tree
   */
  @Override
  public String toString() {
    return printTree(0);
  }

  /**
   * Returns a list of classes that are accessible from the current leaf
   * @return - list of classes that recursively inherit the current leaf class
   */
  public List<T> toList()
    {
      return listTree(new List<T>());
    }

  /**
   * Returns a list of classes that are accessible from the passed in leaf
   * @param toLeaf - the leaf selected to return the classes accessible from
   * @return a list of classes that recursively inherit the passed in leaf class or null if the passed leaf is not in the tree
   */
  public  List<T>  toList(T toLeaf)
    {
      if (containsLeaf(toLeaf))
      {
            return getSimpleTree(toLeaf).toList();
      }
      return null;
    }


    /**
     * Return a string containing the leaf names in the tree
     * @param tnIncrement - set increment size for indent
     * @return - string representation of the tree
     */
    private List<T> listTree(List<T> toTreeList)
    {

        toTreeList.add(m_oHead);

        for (SimpleTree<T> child : m_oLeafs)
        {
            child.listTree(toTreeList);
        }
        return toTreeList;
    }

    /**
     * Return a string containing the leaf names in the tree
     * @param tnIncrement - set increment size for indent
     * @return - string representation of the tree
     */
    private String printTree(int tnIncrement)
    {
        String lcTreeString = "";
        String lcIncrement = "";
        for (int i = 0; i < tnIncrement; ++i)
        {
            lcIncrement = lcIncrement + " ";
        }
        lcTreeString = lcIncrement + m_oHead.toString();
        for (SimpleTree<T> child : m_oLeafs)
        {
            lcTreeString += "\n" + child.printTree(tnIncrement + m_nIndent);
        }
        return lcTreeString;
    }

}
