/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Exceptions;

/**
 *
 * @author kenmchugh
 */
public class FileAlreadyExistsException  extends Goliath.Exceptions.Exception
{

    /**
    * Creates a new instance of InvalidIndexException
    * InvalidIndexException should be thrown whenever retrieval of an
    * object from a collection is attempted where the index is out of range
    *
     * @param toFile The file that wasn't found
    */
    public FileAlreadyExistsException(java.io.File toFile)
    {
        super(toFile.getAbsolutePath() + " already exists");
    }
}