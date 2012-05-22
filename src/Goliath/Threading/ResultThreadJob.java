/* ========================================================
 * ResultThreadJob.java
 *
 * Author:      kmchugh
 * Created:     Feb 3, 2011, 7:23:34 PM
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

package Goliath.Threading;

import Goliath.Arguments.Arguments;


        
/**
 * Class Description.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0 Feb 3, 2011
 * @author      kmchugh
**/
public abstract class ResultThreadJob<A extends Arguments, T extends Object> extends ThreadJob<A>
{
    private T m_oResult;

    public ResultThreadJob(A toCommandArgs)
    {
        super(toCommandArgs);
    }

    public void setResult(T toResult)
    {
        m_oResult = toResult;
    }

    public T getResult()
    {
        return m_oResult;
    }
}