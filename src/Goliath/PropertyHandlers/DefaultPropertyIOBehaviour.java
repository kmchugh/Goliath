/* =========================================================
 * DefaultPropertyIOBehaviour.java
 *
 * Author:      kmchugh
 * Created:     27-Feb-2008, 01:46:08
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
 * =======================================================*/

package Goliath.PropertyHandlers;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 27-Feb-2008
 * @author      kmchugh
**/
public abstract class DefaultPropertyIOBehaviour extends Goliath.Object
        implements Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour
{
    /** Creates a new instance of DefaultPropertyIOBehaviour */
    public DefaultPropertyIOBehaviour()
    {
    }
    
    public abstract <K> void setProperty(Goliath.Interfaces.IProperty<K> toProperty);
    public abstract <K> void setProperty(String tcPath, Goliath.Interfaces.IProperty<K> toProperty);
    public abstract <K> Goliath.Interfaces.IProperty<K> getProperty(Goliath.Interfaces.IProperty<K> toProperty);
    public abstract <K> Goliath.Interfaces.IProperty<K> getProperty(String tcPath, Goliath.Interfaces.IProperty<K> toProperty);
}
