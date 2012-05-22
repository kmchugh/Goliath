/* ========================================================
 * ChildCountChangedEvent.java
 *
 * Author:      admin
 * Created:     Sep 2, 2011, 3:01:59 PM
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
package Goliath;

/**
 * This event should be used when the child count has changed, 
 * for example adding or removing children from a list
 *
 * @see         Related Class
 * @version     1.0 Sep 2, 2011
 * @author      admin
 **/
public class ChildCountChangedEvent<T, C> extends Event<T>
{

    // TODO: This class is in the wrong place, needs to be refactored

    private C m_oChild;
    private boolean m_lAdded;

    /**
     * Creates a new instance of ChildCountChangedEvent
     */
    public ChildCountChangedEvent(T toTarget, C toChild, boolean tlAdded)
    {
        super(toTarget);
        m_oChild = toChild;
        m_lAdded = tlAdded;
    }
    
    /**
     * Checks if the child was added
     * @return true if added
     */
    public boolean wasAdded()
    {
        return m_lAdded;
    }
    
    /**
     * Checks if the child was removed
     * @return true if removed
     */
    public boolean wasRemoved()
    {
        return !m_lAdded;
    }
    
    /**
     * The child that the add or removed happened with
     * @return the child
     */
    public C getChild()
    {
        return m_oChild;
    }
}
