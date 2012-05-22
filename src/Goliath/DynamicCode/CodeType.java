/* ========================================================
 * CodeType.java
 *
 * Author:      admin
 * Created:     Jul 19, 2011, 12:22:27 PM
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
package Goliath.DynamicCode;

import Goliath.DynamicEnum;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Jul 19, 2011
 * @author      admin
 **/
public abstract class CodeType<T> extends DynamicEnum
{
    /**
     * Creates a new code type, this is not publically creatable
     * @param tcType the unique code type
     */
    protected CodeType(String tcType)
    {
        super(tcType);
    }
    
    /**
     * Generates the code for the specified object
     * @param toObject the object to generate the code for
     * @return the code in string format
     */
    public abstract String generateCode(T toObject);
}
