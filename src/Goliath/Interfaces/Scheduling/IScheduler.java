/* ========================================================
 * IScheduler.java
 *
 * Author:      Ken McHugh
 * Created:
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 * The interface class for the agent scheduler that schedules tasks registered to it
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * P.S.20110806    GJF - 40         Complete task modifications
 * ===================================================== */
package Goliath.Interfaces.Scheduling;

/**
 * Scheduler interface for scheduler that schedules agents that implement the IAgent interface.
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0
 * @author      Ken McHugh
 **/
public interface IScheduler<T extends IAgent>
{

    void start();

    void stop();

    void pause();

    void resume();

    void register(Class<T> toAgentClass);

    void register(IAgent toAgent);

    void unregister(Class<T> toAgentClass);

    void unregister(IAgent toAgent);

    void updateAgent(IAgent toAgent);

    boolean isStarted();
}
