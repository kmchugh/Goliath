package Goliath.Watchers;


/**
 *
 * @param <T> the type of object being watched
 * @author home_stanbridge
 */
public abstract class ObjectWatcher<T> extends Watcher
{
    private T m_oWatchObject;

    
    public ObjectWatcher(T toWatchObject)
    {
        super();
        m_oWatchObject = toWatchObject;
    }


    /**
     * gets a reference to the object being watched
     * @return the object being watched
     */
    protected T getObject()
    {
        return m_oWatchObject;
    }
}
