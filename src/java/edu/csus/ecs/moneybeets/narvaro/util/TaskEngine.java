/**
 * Narvaro: @VERSION@
 * Build Date: @DATE@
 * Commit Head: @HEAD@
 * JDK: @JDK@
 * ANT: @ANT@
 *
 */

package edu.csus.ecs.moneybeets.narvaro.util;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Performs tasks using worker threads. It also allows tasks to be scheduled
 * to be run at future dates. This enum mimics relevant methods in both 
 * {@link ExecutorService} and {@link Timer}. Any {@link TimerTask} that's
 * scheduled to be run in the future will automatically be run using the thread
 * executor's thread pool. This means that the standard restriction that TimerTasks
 * should be run quickly does not apply.
 *
 */
public enum TaskEngine {

    INSTANCE;
    
    /**
     * Constructs a new task engine.
     */
    private TaskEngine() {
        timer = new Timer("TaskEngine-timer", true);
        executor = Executors.newCachedThreadPool(new ThreadFactory() {
            
            final AtomicInteger threadNumber = new AtomicInteger(1);
            
            public Thread newThread(final Runnable runnable) {
                // Use our own naming scheme for the threads
                Thread thread = new Thread(Thread.currentThread().getThreadGroup(), 
                        runnable, "TaskEngine-pool-" + threadNumber.getAndIncrement(), 0);
                
                // Make workers daemon threads
                thread.setDaemon(true);
                if (thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
            
        });
    }
    
    private Timer timer;
    private ExecutorService executor;
    private Map<TimerTask, TimerTaskWrapper> wrappedTasks = new ConcurrentHashMap<TimerTask, TimerTaskWrapper>();
    
    /**
     * Submits a Runnable task for execution and returns a
     * Future representing that task.
     * 
     * @param task The task to submit.
     * @return A Future representing pending completion of the task,
     *          and whose <code>get()</code> method will return <code>null</code>
     *          upon completion.
     * @throws java.util.concurrent.RejectedExecutionException If the task cannot be scheduled for execution.
     * @throws NullPointerException If the task is null.        
     * 
     */
    public Future<?> submit(final Runnable task) {
        return executor.submit(task);
    }
    
    /**
     * Schedules the specified task for execution after the specified delay.
     * 
     * @param task The task to be scheduled.
     * @param delay The delay in milliseconds before the task is to be executed.
     * @throws IllegalArgumentException If <code>delay</code> is negative, 
     *              or <code>delay + System.currentTimeMillis()</code> is negative.
     * @throws IllegalStateException If the task was already scheduled or cancelled, 
     *              or timer was cancelled.
     */
    public void schedule(final TimerTask task, final long delay) {
        timer.schedule(new TimerTaskWrapper(task), delay);
    }
    
    /**
     * Schedules the specified task for execution at the specified time. If
     * the time is in the past, the task is scheduled for immediate execution.
     * 
     * @param task The task to be scheduled.
     * @param time The time at which the task is to be executed.
     * @throws IllegalArgumentException If <code>time.getTime()</code> is negative.
     * @throws IllegalStateException If the task was already scheduled or cancelled, 
     *              timer was cancelled, or timer thread terminated.
     */
    public void schedule(final TimerTask task, final Date time) {
        timer.schedule(new TimerTaskWrapper(task), time);
    }
    
    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning after the specified delay. Subsequent executions take place
     * at approximately regular intervals separated by the specified period.
     * 
     * <p>
     * In fixed-delay execution, each execution is scheduled relative to the
     * actual execution time of the previous execution. If an execution is delayed
     * for any reason (such as garbage collection or other background activity),
     * subsequent executions will be delayed as well. In the long run, the frequency
     * of execution will generally be slightly lower than the reciprocal of the specified
     * period (assuming the system clock underlying <code>Object.wait(long)</code> is accurate).
     * </p>
     * <p>
     * Fixed-delay execution is appropriate for recurring activities that require "smoothness".
     * In other words, it is appropriate for activities where it is more important to keep
     * the frequency accurate in the short run than in the long run. This includes most animation
     * tasks, such as blinking a cursor at regular intervals. It also includes tasks wherein
     * regular activity is performed in response to human input, such as automatically repeating
     * a character as long as a key is held down.
     * </p>
     * 
     * @param task The task to be scheduled.
     * @param delay Delay in milliseconds before task is to be executed.
     * @param period Time in milliseconds between successive task executions.
     * @throws IllegalArgumentException If <code>delay</code> is negative, or
     *              <code>delay + System.currentTimeMillis()</code> is negative.
     * @throws IllegalStateException If task was already scheduled or cancelled,
     *              timer was cancelled, or timer thread terminated.
     */
    public void schedule(final TimerTask task, final long delay, final long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.schedule(taskWrapper, delay, period);
    }
    
    /**
     * Schedules the specified task for repeated <i>fixed-delay execution</i>,
     * beginning at the specified time. Subsequent executions take place at approximately
     * regular intervals, separated by the specified period.
     * 
     * <p>
     * In fixed-delay execution, each execution is scheduled relative to
     * the actual execution time of the previous execution.  If an execution
     * is delayed for any reason (such as garbage collection or other
     * background activity), subsequent executions will be delayed as well.
     * In the long run, the frequency of execution will generally be slightly
     * lower than the reciprocal of the specified period (assuming the system
     * clock underlying <code>Object.wait(long)</code> is accurate).
     * </p>
     * <p>
     * Fixed-delay execution is appropriate for recurring activities
     * that require "smoothness."  In other words, it is appropriate for
     * activities where it is more important to keep the frequency accurate
     * in the short run than in the long run.  This includes most animation
     * tasks, such as blinking a cursor at regular intervals.  It also includes
     * tasks wherein regular activity is performed in response to human
     * input, such as automatically repeating a character as long as a key
     * is held down.
     * </p>
     *
     * @param task Task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period Time in milliseconds between successive task executions.
     * @throws IllegalArgumentException If <code>time.getTime()</code> is negative.
     * @throws IllegalStateException If task was already scheduled or cancelled,
     *              timer was cancelled, or timer thread terminated.
     */
    public void schedule(final TimerTask task, final Date firstTime, final long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.schedule(taskWrapper, firstTime, period);
    }
    
    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning after the specified delay.  Subsequent executions take place
     * at approximately regular intervals, separated by the specified period.
     *
     * <p>
     * In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <code>Object.wait(long)</code> is accurate).
     * </p>
     * <p>
     * Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     * </p>
     *
     * @param task Task to be scheduled.
     * @param delay Delay in milliseconds before task is to be executed.
     * @param period Time in milliseconds between successive task executions.
     * @throws IllegalArgumentException If <code>delay</code> is negative, or
     *         <code>delay + System.currentTimeMillis()</code> is negative.
     * @throws IllegalStateException If task was already scheduled or
     *         cancelled, timer was cancelled, or timer thread terminated.
     */
    public void scheduleAtFixedRate(final TimerTask task, final long delay, final long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.scheduleAtFixedRate(taskWrapper, delay, period);
    }
    
    /**
     * Schedules the specified task for repeated <i>fixed-rate execution</i>,
     * beginning at the specified time. Subsequent executions take place at
     * approximately regular intervals, separated by the specified period.
     *
     * <p>
     * In fixed-rate execution, each execution is scheduled relative to the
     * scheduled execution time of the initial execution.  If an execution is
     * delayed for any reason (such as garbage collection or other background
     * activity), two or more executions will occur in rapid succession to
     * "catch up."  In the long run, the frequency of execution will be
     * exactly the reciprocal of the specified period (assuming the system
     * clock underlying <code>Object.wait(long)</code> is accurate).
     * </p>
     * <p>
     * Fixed-rate execution is appropriate for recurring activities that
     * are sensitive to <i>absolute</i> time, such as ringing a chime every
     * hour on the hour, or running scheduled maintenance every day at a
     * particular time.  It is also appropriate for recurring activities
     * where the total time to perform a fixed number of executions is
     * important, such as a countdown timer that ticks once every second for
     * ten seconds.  Finally, fixed-rate execution is appropriate for
     * scheduling multiple repeating timer tasks that must remain synchronized
     * with respect to one another.
     * </p>
     *
     * @param task Task to be scheduled.
     * @param firstTime First time at which task is to be executed.
     * @param period Time in milliseconds between successive task executions.
     * @throws IllegalArgumentException If <code>time.getTime()</code> is negative.
     * @throws IllegalStateException If task was already scheduled or
     *         cancelled, timer was cancelled, or timer thread terminated.
     */
    public void scheduleAtFixedRate(final TimerTask task, final Date firstTime, final long period) {
        TimerTaskWrapper taskWrapper = new TimerTaskWrapper(task);
        wrappedTasks.put(task, taskWrapper);
        timer.scheduleAtFixedRate(taskWrapper, firstTime, period);
    }
    
    /**
     * Cancels the execution of a scheduled task. {@link java.util.TimerTask#cancel()}
     * 
     * @param task The scheduled task to cancel.
     */
    public void cancelScheduledTask(final TimerTask task) {
        TaskEngine.TimerTaskWrapper taskWrapper = wrappedTasks.remove(task);
        if (taskWrapper != null) {
            taskWrapper.cancel();
        }
    }
    
    /**
     * Shuts down the task engine service.
     */
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
            executor = null;
        }
        
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    
    /**
     * Wrapper class for standard TimerTask. It simply executes the
     * TimerTask using the executor's thread pool.
     */
    private class TimerTaskWrapper extends TimerTask {
        
        private TimerTask task;
        
        public TimerTaskWrapper(final TimerTask task) {
            this.task = task;
        }
        
        @Override
        public void run() {
            executor.submit(task);
        }
        
    }
    
}
