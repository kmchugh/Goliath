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
public interface IConsumer
{
    void notify(IMessage toMessage);
    List<MessageType> canHandle();
    void cancel();
    void activate();
    void deactivate();
    void isActive();
    void isExecuting();

}
