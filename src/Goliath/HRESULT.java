/* =========================================================
 * HRESULT.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 13, 2007 10:30 PM
 * 
 * Description
 * --------------------------------------------------------
 * This is used for any API calls.
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/

package Goliath;

/**
 * This class is used for API Calls
 * For example:
 * <pre>
 *      HRESULT lnResult = GetWin32WindowHandle(lnWindowAddress);
 * </pre>
 *
 * @version     1.0 Nov 13, 2007
 * @author      Ken McHugh
**/
public class HRESULT
{
    private long m_nValue = 0;
    
    /** Creates a new instance of HRESULT */
    public HRESULT()
    {
    }
    
    
    /**
     * Creates a new instance of HRESULT
     * @param tnValue   the value to initialise HRESULT with 
     */
    protected HRESULT(long tnValue)
    {
        m_nValue = tnValue;
    }
    
    
    /**
     * Creates a string representation of the object
     * @return A string representation of the object
     */
    @Override
    public String toString()
    {
        return Long.toString(m_nValue);
    }
}
