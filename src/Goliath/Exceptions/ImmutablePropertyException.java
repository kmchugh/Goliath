package Goliath.Exceptions;

/**
 * An immutable property exception occurs when a property is modifed that is
 * meant to be immutable
 * @author admin
 */
public class ImmutablePropertyException  extends Goliath.Exceptions.UncheckedException
{

    /**
     * Creates a new instance of the Immutable property exception
     * @param tcProperty the property that a change was attempted with
     */
    public ImmutablePropertyException(String tcProperty)
    {
        super("The property " + tcProperty + " is immutable");
    }
    
    /**
     * Creates a new instance of the Immutable property exception
     * @param tcProperty the property that a change was attempted with
     * @param tlLogError if true, then the error is automatically logged
     */
    public ImmutablePropertyException(String tcProperty, boolean tlLogError)
    {
        super("The property " + tcProperty + " is immutable", tlLogError);
    }
}