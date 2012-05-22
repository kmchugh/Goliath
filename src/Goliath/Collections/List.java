/* =========================================================
 * List.java
 *
 * Author:      kmchugh
 * Created:     14-Dec-2007, 12:32:11
 * 
 * Description
 * --------------------------------------------------------
 * List class extends the java arraylist class
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath.Collections;

import java.util.ArrayList;
import java.util.Collection;

/**
 * List class extends the java arraylist class
 *
 * @version     1.0 14-Dec-2007
 * @author      kmchugh
**/
public class List<T> extends java.util.ArrayList<T>
        implements Goliath.Interfaces.Collections.IList<T>, java.util.List<T>
{
     /**
     * Creates an instance of this class 
     */
    public List()
    {
        // This makes all the lists smaller to start with, cuts down on memory footprint.
        super(0);
    }

    /**
     * Creates an instance of this class with an initial size 
     *
     * @param  tnInitialCapacity    the initial size of this list
     */
    public List(int tnInitialCapacity)
    {
        super(tnInitialCapacity);
    }

    /**
     * Creates an instance of this class using the collection to fill 
     *
     * @param  toCollection    the collection to fill with
     */
    public List(Collection<? extends T> toCollection)
    {
        super(toCollection);
    }

    public List(T[] toArray)
    {
        super((toArray != null) ? toArray.length : 0);

        if (toArray == null)
        {
            return;
        }
        
        for (int i=0; i<toArray.length; i++)
        {
            this.add(toArray[i]);
        }
    }

    /**
     * inserts the element at the specified index position, or at the end
     * of the collection if the specified index position is after EOF
     * @param tnIndex
     * @param toElement
     */
    @Override
    public void add(int tnIndex, T toElement) 
    {
        if (tnIndex >= size())
        {
            super.add(toElement);
        }
        else
        {
            super.add(tnIndex, toElement);
        }
    }
    
    
}
