/* =========================================================
 * Application.java
 *
 * Author:      Ken McHugh
 * Created:     Nov 27, 2008, 1:34 PM
 * 
 * Description
 * --------------------------------------------------------
 * This class is the abstract unit testing class that all unit
 * testing classes will inherit.
 *
 * Unit tests should be contained tests that do not rely on
 * the results or execution of any other code, this means that
 * test could be executed in a different order and have the
 * same results.
 *
 * In order for test to be run, the setting in the application settings
 * class must be set to true, and the test must inherit from this class
 * 
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * 
 * =======================================================*/
package Goliath;

import Goliath.Applications.Application;
import Goliath.Arguments.Arguments;
import Goliath.Commands.Command;
import Goliath.Exceptions.TestFailedException;
import Goliath.Interfaces.IDelegate;

public abstract class Test extends Command<Arguments, java.lang.Object>
{
    private String m_cTestResult;
    private StringBuffer m_cErrorMessage;

    @Override
    public Object doExecute() throws Throwable
    {
        m_cTestResult = "";
        m_cErrorMessage = new StringBuffer(" ");

        run();

        return null;
    }



    /**
     * Calls onRun, which is the method inheritting applications will write
     * Returns a boolean for succeed or fail test
     * @return boolean for succeed or fail of test
     */
    private boolean run()
    {
        try
        {
            // execute onRun which will be overridden in concrete test classes
           if (onRun())
           {
               log("Unit Test " + getName() + " Passed");
               m_cTestResult = "Passed";
               return true;
           }
           else
           {
               log("Unit Test " + getName() + " Failed");
               m_cTestResult = "Failed";
               return false;
           }

        // Catch anything thrown from the implementing onRun    
        }
        catch (Throwable loFail)
        {
            // log error for the thrown error
            m_cTestResult = "Failed";
            log(loFail.toString());
            return false;
        }
    }

    /**
     * Method to allow test class programmers to explicitly fail a test
     * 
     */
    protected void failTest()
    {
        throw new TestFailedException();
    }

    /**
     * Method to allow test class programmers to explicitly fail a test
     * @param tcString - message string to pass to exception handler
     */
    protected void failTest(String tcString)
    {
        throw new TestFailedException(tcString);
    }

    /**
     * Method to allow test class programmers to explicitly compare equality
     * between two objects (of the same type!)
     * @param toObject1 - object to be tested
     * @param toObject2 - other object to be tested
     */
    protected <K> void assertEquals(K toObject1, K toObject2)
    {
        if (!toObject1.equals(toObject2))
        {
            m_cErrorMessage.append("\nFailed AssertEquals in Unit Test [" + getName() + "]: [" + toObject1.toString() + "] is not equal to [" + toObject2.toString() + "]");
            throw new TestFailedException("Failed AssertEquals in Unit Test [" + getName() + "]: [" + toObject1.toString() + "] is not equal to [" + toObject2.toString() + "]");
        }
    }

    /**
     * Method to allow test class programmers to explicitly compare equality
     * between two objects (of the same type!)
     * @param toObject1 - object to be tested
     * @param toObject2 - other object to be tested
     */
    protected <K> void assertNotEquals(K toObject1, K toObject2)
    {
        if (toObject1.equals(toObject2))
        {
            m_cErrorMessage.append("\nFailed AssertNotEquals in Unit Test [" + getName() + "]: [" + toObject1.toString() + "] is equal to [" + toObject2.toString() + "]");
            throw new TestFailedException("Failed AssertNotEquals in Unit Test [" + getName() + "]: [" + toObject1.toString() + "] is equal to [" + toObject2.toString() + "]");
        }
    }

    /**
     * Name of class being run - the test class
     * Returns string name of the class being run. 
     * @return String name of class being run
     */
    @Override
    public final String getName()
    {
        return this.getClass().getName();
    }

   
    /**
     * Method to be overridden to contain the test code. 
     * Will return boolean succeed for failed
     * @return boolean indicating whether test succeeded or failed
     */
   protected abstract boolean onRun();

   protected void log(String tcMessage)
   {
       Application.getInstance().log(tcMessage);
       System.out.print(tcMessage + Environment.NEWLINE());
   }

   /**
    * Runs and logs the specified sub test, all the subtest functions should return a boolean value
    * @param tcSubTest the name of the sub test
    * @param toTestFunction the test function
    * @return true if the test succeeds
    */
   protected boolean runSubTest(String tcSubTest, String tcTestFunction)
   {
       boolean llReturn = true;

       IDelegate<Boolean> loTestFunction = Delegate.<Boolean>build(this, tcTestFunction);

       log(" --> " + new Date(getStartTime()).toString() + " - Starting sub test - " + tcSubTest);

       try
       {
            llReturn = loTestFunction.invoke(new java.lang.Object[0]);
       }
       catch (Throwable ex)
       {

           log(ex.toString());
           m_cErrorMessage.append("\n"+ex.toString());
           llReturn = false;
       }

       log(" --> " + new Date(getEndTime()).toString() + " - Finished sub test (" + (llReturn ? "Successful" : "Failure") + ") - " + tcSubTest);

       return llReturn;
   }

   /**
    * Get the Test Result
    * @return "Passed" / "Failed"
    */
   public String getTestResult()
   {
       return m_cTestResult;
   }

   /**
    * Get the Error Message
    * @return Error Message
    */
   public String getErrorMessage()
   {
       return m_cErrorMessage.toString();
   }
}
