/* =========================================================
 * ICodeGenerator.java
 *
 * Author:      kmchugh
 * Created:     29-Jan-2008, 09:48:20
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

package Goliath.Interfaces.DynamicCode;

import Goliath.DynamicCode.CodeType;

/**
 * Interface Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 29-Jan-2008
 * @author      kmchugh
**/
public interface ICodeGenerator<T>
{
    /**
     * Generates code from the specified type using the object
     * specified as the code base
     * @param toCodeType the type of code to generate
     * @param toObject the object to use as the base
     * @return the code as generated
     */
    String generateCode(CodeType toCodeType, T toObject);
}
