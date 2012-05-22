/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Exceptions;

/**
 *
 * @author Peter Stanbridge
 */
public class TestFailedException  extends Goliath.Exceptions.UncheckedException
{

        /**
    * Creates a new instance of TestFailed
    * TestFailed can be thrown whenever a unit test fails
    *
    */
    public TestFailedException()
    {
        super();
    }
    
    
    /**
    * Creates a new instance of TestFailed
    * TestFailed can be thrown whenever a unit test fails
    *
    * @param tcMessage   The error message
    */
    public TestFailedException(String tcMessage)
    {
        super(tcMessage);
    }
    

    
}
