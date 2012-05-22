/* =========================================================
 * FileLogger.java
 *
 * Author:      Peter
 * Created:     14-Dec-2007, 16:14:57
 * 
 * Description
 * --------------------------------------------------------
 * General Class Description.
 * This class is a concrete implimenation of the abstract 
 * and interface log handler
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath.LogHandlers;


/**
 * Class Description.
 *  * This class is a concrete implimenation of the abstract 
 * and interface log handler
 * @version     1.0 14-Dec-2007
 * @author      Peter
 **/
public class FileLogHandler extends LogHandler  
{


        
    /** Constructor - Creates a new instance of fileIOBehaviour into the behaviour collection
     * @param tlAppend - true if logging to append to existing file
     * @param tcFileName Name of file to contain log
     * @throws Goliath.Exceptions.InvalidIOException 
     */
    public FileLogHandler(boolean tlAppend, String tcFileName) throws Goliath.Exceptions.InvalidIOException 
    {
        try 
        {
            this.addBehaviour("FileIOBehaviour", new FileIOBehaviour(tlAppend, tcFileName));
        }
        catch (Exception e) 
        {
               throw new Goliath.Exceptions.InvalidIOException(tcFileName, e);
        }
    }
}
    