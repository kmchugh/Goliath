/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Collections;


/**
 *
 * @author home_stanbridge
 */
public interface ITree<T>
        extends IList<T>
{
    T getHead();

    boolean add(ITree<T> toTree);
    void add(int tnIndex, ITree<T> toTree);
    boolean addAll(IList<ITree<T>> toTrees);
    boolean addAll(int tnIndex, IList<ITree<T>> toTrees);

    boolean remove(ITree<T> toTree);
    boolean removeAll(IList<ITree<T>> toTrees);

    boolean isLeaf();
    boolean isRoot();

    ITree<T> getParent();

    int indexOf(ITree<T> toObject);
    int lastIndexOf(ITree<T> toObject);

    boolean contains(ITree<T> toTree);
    boolean containsAll(IList<ITree<T>> toTrees);

    int getDepth();

    IList<T> getLeafItems();

    IList<T> toFlatList();

    /**
     * Gets the tree node for the specified item
     * @param toItem the item to get the tree node for
     * @return the tree for the tree node
     */
    ITree<T> getTreeFor(T toItem);
    
}
