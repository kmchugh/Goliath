/* ========================================================
 * Tree.java
 *
 * Author:      kmchugh
 * Created:     Dec 16, 2010, 7:41:06 PM
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */

package Goliath.Collections;

import Goliath.Constants.StringFormatType;
import Goliath.Environment;
import Goliath.Exceptions.InvalidIndexException;
import Goliath.Interfaces.Collections.IList;
import Goliath.Interfaces.Collections.ITree;
import java.util.Collection;
import java.util.Iterator;
import java.util.ListIterator;

/**
 *
 * @author kmchugh
 */
public class Tree<T> extends Goliath.Object
        implements ITree<T>
{
    private T m_oHead;
    private List<ITree<T>> m_oLeaves;
    private ITree<T> m_oRoot;
    private ITree<T> m_oParent;

    public Tree(T toHead)
    {
        Goliath.Utilities.checkParameterNotNull("toHead", toHead);
        m_oHead= toHead;
    }

    @Override
    public final T getHead()
    {
        return m_oHead;
    }
    
    private synchronized List<ITree<T>> getLeaves()
    {
        if (m_oLeaves == null)
        {
            m_oLeaves = new List<ITree<T>>();
        }
        return m_oLeaves;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    /**
     * Gets the tree node for the specified item
     * @param toItem the item to get the tree node for
     * @return the tree for the tree node
     */
    @Override
    public ITree<T> getTreeFor(T toItem)
    {
        // TODO: This can be optimised by adding a lookup hashtable as discussed with Peter
        if (m_oHead.equals(toItem))
        {
            return this;
        }

        if (!isLeaf())
        {
            List<ITree<T>> loLeaves = new List(getLeaves());
            for (ITree loTree : loLeaves)
            {
                ITree<T> loItem = loTree.getTreeFor(toItem);
                if (loItem != null)
                {
                    return loItem;
                }
            }
        }
        return null;
    }

    @Override
    public IList<T> getLeafItems()
    {
        List<T> loReturn = new List();
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                loReturn.addAll(loTree.getLeafItems());
            }
        }
        else
        {
            loReturn.add(m_oHead);
        }
        return loReturn;
    }

    @Override
    public boolean isRoot()
    {
        return m_oParent == null;
    }

    @Override
    public boolean isLeaf()
    {
        return m_oLeaves == null || m_oLeaves.size() == 0;
    }

    protected ITree<T> wrapNode(T toElement)
    {
        Tree<T> loTree = new Tree<T>(toElement);
        updateTree(loTree);
        return loTree;
    }

    private void updateTree(Tree<T> toTree)
    {
        toTree.m_oParent = this;
        toTree.m_oRoot = m_oRoot != null ? m_oRoot : this;
        if (!toTree.isLeaf())
        {
            for (ITree<T> loTree : toTree.getLeaves())
            {
                // TODO: Casting for now, this needs to change when we start subclassing tree
                Tree<T> loActualTree = (Tree<T>)loTree;
                updateTree(loActualTree);
            }
        }
    }

    private void cleanTree(Tree<T> toTree)
    {
        toTree.m_oParent = toTree == this ? null : toTree;
        toTree.m_oRoot = this;
        if (!toTree.isLeaf())
        {
            for (ITree<T> loTree : toTree.getLeaves())
            {
                // TODO: Casting for now, this needs to change when we start subclassing tree
                Tree<T> loActualTree = (Tree<T>)loTree;
                cleanTree(loActualTree);
            }
        }
    }

    @Override
    public final boolean add(T toElement)
    {
        return getLeaves().add(wrapNode(toElement));
    }

    @Override
    public final void add(int tnIndex, T toElement)
    {
        getLeaves().add(tnIndex, wrapNode(toElement));
    }
    
    @Override
    public final boolean add(ITree<T> toTree)
    {
        boolean llReturn = getLeaves().add(toTree);
        if (llReturn)
        {
            updateTree((Tree<T>)toTree);
        }
        return llReturn;
    }

    @Override
    public final void add(int tnIndex, ITree<T> toTree)
    {
        getLeaves().add(tnIndex, toTree);
        updateTree((Tree<T>)toTree);
    }

    @Override
    public final boolean addAll(Collection<? extends T> toElements)
    {
        boolean llReturn = true;
        for (T loElement : toElements)
        {
            llReturn = llReturn && add(loElement);
        }
        return llReturn;
    }

    @Override
    public final boolean addAll(int tnIndex, Collection<? extends T> toElements)
    {
        int lnIndex = tnIndex;
        for (T loElement : toElements)
        {
            add(lnIndex++, loElement);
        }
        return toElements != null && toElements.size() > 0;
    }

    @Override
    public final boolean addAll(IList<ITree<T>> toTrees)
    {
        boolean llReturn = true;
        for (ITree<T> loTree : toTrees)
        {
            llReturn = llReturn && add(loTree);
        }
        return llReturn;
    }

    @Override
    public final boolean addAll(int tnIndex, IList<ITree<T>> toTrees)
    {
        int lnIndex = tnIndex;
        for (ITree<T> loTree : toTrees)
        {
            add(lnIndex++, loTree);
        }
        return toTrees != null && toTrees.size() > 0;
    }

    @Override
    public final boolean remove(Object toElement)
    {
        if (!isLeaf())
        {
            for (ITree<T> loObject : getLeaves())
            {
                if (loObject.getHead().equals(toElement))
                {
                    boolean llReturn = getLeaves().remove(loObject);
                    if (llReturn)
                    {
                        cleanTree((Tree<T>)loObject);
                    }
                    return llReturn;
                }
            }
        }
        return false;
    }

    @Override
    public final T remove(int tnIndex)
    {
        if (!isLeaf())
        {
            ITree<T> loRemoved = getLeaves().remove(tnIndex);
            if (loRemoved != null)
            {
                cleanTree((Tree<T>)loRemoved);
            }
            return loRemoved != null ? loRemoved.getHead() :null;
        }
        return null;
    }

    @Override
    public final boolean remove(ITree<T> toTree)
    {
        if (!isLeaf())
        {
            boolean llReturn = getLeaves().remove(toTree);
            if (llReturn)
            {
                cleanTree((Tree<T>)toTree);
            }
        }
        return false;
    }

    @Override
    public final boolean removeAll(Collection<?> toElements)
    {
        if (!isLeaf())
        {
            boolean llReturn = true;
            for (T loElement : (Collection<T>)toElements)
            {
                llReturn = llReturn && remove(loElement);
            }
            return llReturn;
        }
        return false;
    }

    @Override
    public final boolean retainAll(Collection<?> toElements)
    {
        if (!isLeaf())
        {
            List<ITree<T>> loRemove = new List<ITree<T>>();

            for (ITree<T> loTree : getLeaves())
            {
                if (!toElements.contains(loTree.getHead()))
                {
                    loRemove.add(loTree);
                }
            }
            return removeAll(toElements);
        }
        return false;
    }

    @Override
    public final boolean removeAll(IList<ITree<T>> toTrees)
    {
        if (!isLeaf())
        {
            boolean llReturn = getLeaves().removeAll(toTrees);
            if (llReturn)
            {
                for (ITree<T> loTree : toTrees)
                {
                    cleanTree((Tree<T>)loTree);
                }
            }
        }
        return false;
    }

    @Override
    public final void clear()
    {
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                cleanTree((Tree<T>)loTree);
            }
            m_oLeaves = null;
        }
    }
    
    @Override
    public final T get(int tnIndex)
    {
        T loReturn = !isLeaf() ? getLeaves().get(tnIndex).getHead() : null;
        if (loReturn == null)
        {
            throw new InvalidIndexException("Collection does not have " + Integer.toString(tnIndex) + " elements.");
        }
        return loReturn;
    }

    @Override
    public final int indexOf(Object toObject)
    {
        if (!isLeaf())
        {
            for (int i=0; i<m_oLeaves.size();i++)
            {
                ITree<T> loTree = m_oLeaves.get(i);
                if (loTree.getHead().equals(toObject))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public final int lastIndexOf(Object toObject)
    {
        if (!isLeaf())
        {
            for (int i=m_oLeaves.size()-1; i >=0 ; i--)
            {
                ITree<T> loTree = m_oLeaves.get(i);
                if (loTree.getHead().equals(toObject))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public final int indexOf(ITree<T> toObject)
    {
        if (!isLeaf())
        {
            return m_oLeaves.indexOf(toObject);
        }
        return -1;
    }

    @Override
    public final int lastIndexOf(ITree<T> toObject)
    {
        if (!isLeaf())
        {
            return m_oLeaves.lastIndexOf(toObject);
        }
        return -1;
    }

    @Override
    public final boolean contains(Object toObject)
    {
        if (m_oHead.equals(toObject))
        {
            return true;
        }

        if (!isLeaf())
        {
            for (ITree loTree : getLeaves())
            {
                if (loTree.getHead().equals(toObject))
                {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final boolean containsAll(Collection<?> toItems)
    {
        if (!isLeaf())
        {
            boolean llReturn = true;
            for (T loObject : (Collection<T>)getLeaves())
            {
                llReturn = llReturn && contains(loObject);
                if (!llReturn)
                {
                    break;
                }
            }
            return llReturn;
        }
        return false;
    }

    @Override
    public final boolean contains(ITree<T> toTree)
    {
        // TODO: Rework this so it will also check children of children
        return (!isLeaf() && getLeaves().contains(toTree));
    }

    @Override
    public final boolean containsAll(IList<ITree<T>> toTrees)
    {
        return (!isLeaf() && getLeaves().containsAll(toTrees));
    }

    @Override
    public final T set(int tnIndex, T toElement)
    {
        T loReturn = remove(tnIndex);
        getLeaves().add(tnIndex, wrapNode(toElement));
        return loReturn;
    }

    @Override
    public final int size()
    {
        return 1 + (isLeaf() ? 0 : m_oLeaves.size());
    }

    @Override
    public final java.util.List<T> subList(int tnFromIndex, int tnToIndex)
    {
        java.util.List<T> loReturn = new List<T>();
        if (!isLeaf())
        {
            for (int i=tnFromIndex; i<=tnToIndex; i++)
            {
                loReturn.add(get(tnToIndex));
            }
        }
        return loReturn;
    }

    @Override
    public final Object[] toArray()
    {
        List<T> loList = new List<T>(size());
        loList.add(m_oHead);
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                loList.add(loTree.getHead());
            }
        }
        return loList.toArray();
    }

    @Override
    public final <K> K[] toArray(K[] taArray)
    {
        List<K> loList = new List<K>(size());
        loList.add((K)m_oHead);
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                loList.add((K)loTree.getHead());
            }
        }
        return loList.toArray(taArray);
    }

    /**
     * Builds a list from this tree including all of the child trees
     * @return a full list of all the items in this tree
     */
    @Override
    public IList<T> toFlatList()
    {
        IList<T> loReturn = new List<T>();
        loReturn.add(m_oHead);
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                loReturn.addAll(loTree.toFlatList());
            }
        }
        return loReturn;
    }



    @Override
    public final Iterator<T> iterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final ListIterator<T> listIterator(int index)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public final ITree<T> getParent()
    {
        return m_oParent;
    }
    
    @Override
    public final int getDepth()
    {
        return getParent() == null ? 0 : getParent().getDepth() + 1;
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        StringBuilder loBuilder = new StringBuilder();

        for (int i = 0; i < getDepth(); i++)
        {
            loBuilder.append(" ");
        }
        loBuilder.append(m_oHead.toString());
        loBuilder.append(Environment.NEWLINE());
        if (!isLeaf())
        {
            for (ITree<T> loTree : getLeaves())
            {
                loBuilder.append(loTree.toString());
            }
        }
        return loBuilder.toString();
    }
}
