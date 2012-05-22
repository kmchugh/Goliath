/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Goliath.Interfaces.Messaging;

import Goliath.Collections.List;
import Goliath.Messaging.MessageType;

/**
 *
 * @author kenmchugh
 */
public interface IMessage
{
    MessageType getMessageType();
    String getName();
    String getDescription();
    List<Throwable> getErrors();

}
