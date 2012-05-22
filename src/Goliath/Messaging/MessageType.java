/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Messaging;

/**
 *
 * @author kenmchugh
 */
public class MessageType extends Goliath.DynamicEnum
{

    public MessageType(String tcValue)
        throws Goliath.Exceptions.InvalidParameterException
    {
        super(tcValue);
    }

    private static MessageType g_oDefault;

    public static MessageType DEFAULT()
    {
        if (g_oDefault == null)
        {
            try
            {
                g_oDefault = new MessageType("DEFAULT");
            }
            catch (Goliath.Exceptions.InvalidParameterException ex)
            {}
        }
        return g_oDefault;
    }
}
