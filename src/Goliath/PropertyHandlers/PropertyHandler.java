package Goliath.PropertyHandlers;

import Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour;

public abstract class PropertyHandler extends Goliath.Object
        implements Goliath.Interfaces.PropertyHandlers.IPropertyHandler
{
    // Storage for active behaviours
    private Goliath.Collections.HashTable<String, IPropertyIOBehaviour> m_oBehaviours = new Goliath.Collections.HashTable<String, IPropertyIOBehaviour>();

    
    public PropertyHandler() 
    {
    }

    /**
     * Write an error log entry to log
     *
     * @param  tcMessage Message being logged
     */
    @Override
    public <K> void setProperty(String tcPath, K toValue)
    {
        for(IPropertyIOBehaviour m_oBehaviour : m_oBehaviours.values()) 
        {
            synchronized(m_oBehaviour)
            {
                m_oBehaviour.setProperty(tcPath, toValue);
            }
        }
    }
    
    @Override
    public <K> K getProperty(String tcPath, K toDefault)
    {
        for(IPropertyIOBehaviour m_oBehaviour : m_oBehaviours.values()) 
        {
            synchronized(m_oBehaviour)
            {
                //TODO: Fix this so that it will only read the property from one handler
                // Maybe use a hasProperty()
                toDefault = m_oBehaviour.getProperty(tcPath, toDefault);
            }
        }
        return toDefault;
    }

    @Override
    public <K> K getProperty(String tcPath)
    {
        for(IPropertyIOBehaviour m_oBehaviour : m_oBehaviours.values())
        {
            synchronized(m_oBehaviour)
            {
                //TODO: Fix this so that it will only read the property from one handler
                // Maybe use a hasProperty()
                return m_oBehaviour.<K>getProperty(tcPath);
            }
        }
        return null;
    }
   
    
    	

     /**
     * Add behaviour to the behaviour collection
     *
     * @param  toBehaviour Behaviour to be added
     * @param  tcKey Key to hash table entry for behaviour being added
     */
    @Override
     public void addBehaviour(String tcKey, IPropertyIOBehaviour toBehaviour) 
     {
        if (tcKey != null & toBehaviour != null) 
        {
            m_oBehaviours.put(tcKey, toBehaviour);
        }
     }
    
     /**
     * Remove behaviour from the behaviour collection
     *
     * @param  tcKey Key to hash table entry for behaviour being removed
     */
    @Override
     public void removeBehaviour(String tcKey) 
     {
        if (tcKey != null) 
        {
           m_oBehaviours.remove(tcKey); 
        }
     }
    
    /**
     * Remove all behaviours associated with this log handler, closing them first
     */
    @Override
    public void removeBehaviours() 
    {
        m_oBehaviours.clear();
    } 
}
