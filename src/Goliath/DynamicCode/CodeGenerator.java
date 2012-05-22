/* =========================================================
 * CodeGenerator.java
 *
 * Author:      kmchugh
 * Created:     29-Jan-2008, 09:47:26
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

package Goliath.DynamicCode;

/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 29-Jan-2008
 * @author      kmchugh
**/
public abstract class CodeGenerator<T> extends Goliath.Object 
        implements Goliath.Interfaces.DynamicCode.ICodeGenerator<T>
{
    /** Creates a new instance of CodeGenerator */
    public CodeGenerator()
    {
    }

    @Override
    public abstract String generateCode(CodeType toCodeType, T toObject);
}
