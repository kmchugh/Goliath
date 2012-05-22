/* ========================================================
 * IAgent.java
 *
 * Author:      Ken McHugh
 * Created:
 *
 * Description
 * --------------------------------------------------------
 * General Class Description.
 * The interface class for Agents, which are pieces of code that act on the system and produce results, but timed to run
 * at set intervals
 *
 * Change Log
 * --------------------------------------------------------
 * Init.Date        Ref.            Description
 * --------------------------------------------------------
 * P.S.20110806    GJF - 40         Complete task modifications
 * ===================================================== */
package Goliath.Interfaces.Scheduling;

import Goliath.Collections.HashTable;
import Goliath.Collections.List;
import Goliath.Date;

/**
 * Interface class for Agents, which are pieces of code that act on the system and produce results, but timed to run.
 * Agents are run via the agent scheduler Scheduler.java
 * For example:
 * <pre>
 *      Example usage
 * </pre>
 *
 * @see         Related Class
 * @version     1.0
 * @author      Ken McHugh
 **/
public interface IAgent<T> extends Runnable
{

    boolean hasItems();

    boolean getContinueAfterError();

    void setContinueAfterError(boolean tlContinue);

    HashTable<T, List<Throwable>> getItemErrors();

    List<Throwable> getItemErrors(T toItem);

    List<Throwable> getErrors();

    List<T> getItems();

    int getItemCount();

    Date getStartDate();

    Date getEndDate();

    void setStartDate(Date toStartDate);

    void setEndDate(Date toEndDate);

    Date getLastRun();

    Date getNextRun();

    long getInterval();

    void setInterval(long tnInterval);

    void activate();

    void deactivate();

    boolean isActive();

    boolean isExecuting();

    void cancel();

    boolean isCancelled();

    void load();

    boolean toRun();

    @Override
    void run();
}
