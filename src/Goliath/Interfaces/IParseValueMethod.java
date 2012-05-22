/* ========================================================
 * IParseValueMethod.java
 *
 * Author:      admin
 * Created:     Jul 21, 2011, 1:07:15 PM
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
package Goliath.Interfaces;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jul 21, 2011
 * @author      admin
 **/
public interface IParseValueMethod<T, R>
{
    R parseFromValue(T toValue);
    T parseToValue(R toValue);
}
