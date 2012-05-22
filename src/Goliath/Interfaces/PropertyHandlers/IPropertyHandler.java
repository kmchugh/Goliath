/* =========================================================
 * IPropertyHandler.java
 *
 * Author:      kmchugh
 * Created:     27 November 2007, 14:52
 *
 * Description
 * --------------------------------------------------------
 * General Interface Description.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 *
 * =======================================================*/

package Goliath.Interfaces.PropertyHandlers;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 27 November 2007
 * @author      kmchugh
 **/
public interface IPropertyHandler
{
    /**
     * Sets the behaviour for reading and writing settings with this class
     *
     * @param  toBehaviour the behaviour to use to write
     * @return  true if the behaviour is changed
     */
    void addBehaviour(String tcKey, Goliath.Interfaces.PropertyHandlers.IPropertyIOBehaviour toBehaviour);
    void removeBehaviour(String tcKey);
    void removeBehaviours();
    
    <K> void setProperty(String tcPath, K tcValue);
    <K> K getProperty(String tcPath, K toDefault);
    <K> K getProperty(String tcPath);
    
}
