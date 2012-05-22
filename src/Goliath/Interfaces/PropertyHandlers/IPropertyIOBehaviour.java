/* =========================================================
 * IPropertyIOBehaviour.java
 *
 * Author:      kmchugh
 * Created:     27 November 2007, 14:53
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
public interface IPropertyIOBehaviour
{
    <K> void setProperty(String tcPath, K toValue);
    <K> K getProperty(String tcPath, K toDefault);
    <K> K getProperty(String tcPath);
}
