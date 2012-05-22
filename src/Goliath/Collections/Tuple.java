/* ========================================================
 * FragmentValues.java
 *
 * Author:      admin
 * Created:     Sep 26, 2011, 1:22:43 AM
 *
 * Description
 * --------------------------------------------------------
 * The tuple class represents a mathematical tuple
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * ===================================================== */
package Goliath.Collections;

import Goliath.Constants.StringFormatType;

/**
 * Represents a mathematical Tuple
 *
 * @version     1.0 Sep 26, 2011
 * @author      admin
 **/
public class Tuple<S, T> extends Goliath.Object
{
    private S m_oHead;
    private List<T> m_oTail;

    public Tuple(S toHead, T toTail)
    {
        m_oHead = toHead;
        m_oTail = new List<T>(1);
        m_oTail.add(toTail);;
    }

    public Tuple(S toHead, List<T> toTail)
    {
        m_oHead = toHead;
        m_oTail = toTail;
    }

    public S getHead()
    {
        return m_oHead;
    }

    public List<T> getTail()
    {
        return m_oTail;
    }

    public java.lang.Object get(int tnIndex)
            throws IndexOutOfBoundsException
    {
        if (tnIndex < 0 || tnIndex > m_oTail.size())
        {
            throw new IndexOutOfBoundsException();
        }

        if (tnIndex == 0)
        {
            return m_oHead;
        }

        return m_oTail.get(tnIndex-1);
    }

    public T getFromTail(int tnIndex)
    {
        return m_oTail.get(tnIndex);
    }

    @Override
    protected String formatString(StringFormatType toFormat)
    {
        if (toFormat == StringFormatType.DEFAULT())
        {
            StringBuilder loBuilder = new StringBuilder("<");
            loBuilder.append(m_oHead.toString());

            for (T loObject : m_oTail)
            {
                loBuilder.append(", ");
                loBuilder.append(loObject.toString());
            }

            loBuilder.append(">");
            return loBuilder.toString();
        }
        return super.formatString(toFormat);
    }






}
