package Goliath.Security;

import Goliath.SecurityEntity;
import Goliath.Constants.StringFormatType;
import Goliath.Interfaces.Security.ISecurityManager;

/**
 * The Group is an Adapter around the actual group object, this object allows
 * the application to interact with the concrete Group object without knowing
 * what the Concrete group object actually is
 * @author kenmchugh
 */
public class Group extends SecurityEntity
{
    /**
     * Creates a new instance of the group wrapper
     * @param toManager the security manager this group belongs to
     * @param tcID the ID of this group
     */
    public Group(ISecurityManager toManager, String tcID)
    {
        super(toManager, tcID);
    }

    /**
     * Gets the Description of this Group Object
     * @return the description
     */
    public String getDescription()
    {
        return getSecurityManager().getDescription(this);
    }

    /**
     * Gets the description of this group
     * @param tcDescription the description of the group
     */
    public void setDescription(String tcDescription)
    {
        getSecurityManager().setDescription(this, tcDescription);
    }

    /**
     * Explicit formatting of the Group object
     * @param toFormat disregarded in this call
     * @return the string representation of the group
     */
    @Override
    protected String formatString(StringFormatType toFormat)
    {
        return getName() + "[" + getGUID() + "]";
    }


}
