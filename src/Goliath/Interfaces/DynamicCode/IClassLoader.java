/* =========================================================
 * IClassLoader.java
 *
 * Author:      kmchugh
 * Created:     29-May-2008, 09:19:41
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

package Goliath.Interfaces.DynamicCode;

import Goliath.Exceptions.InvalidPathException;
import java.net.URL;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 29-May-2008
 * @author      kmchugh
**/
public interface IClassLoader 
{
    void addPath(String tcPath) throws Goliath.Exceptions.InvalidPathException;
    void addFile(java.io.File toFile) throws InvalidPathException;
    void addURL(URL toURL);
    Class<?> loadClass(String name) throws ClassNotFoundException;
}
