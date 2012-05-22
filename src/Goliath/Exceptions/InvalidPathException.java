/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Exceptions;

/**
 *
 * @author home_stanbridge
 */
public class InvalidPathException extends Goliath.Exceptions.Exception
{
    public InvalidPathException(String tcMessage)
    {
        super(tcMessage);
    }

    public InvalidPathException(String tcMessage, Throwable toCause)
    {
        super(tcMessage, toCause);
    }

    public InvalidPathException(Throwable toCause)
    {
        super(toCause);
    }
}