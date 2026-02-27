package it.unibo.msrehab.rl.utils;

import java.util.concurrent.Callable;

public class ExceptionUtils
{
    public interface IExceptionRunnable
    {
        void run() throws Exception;
    }


    /**
     * Attempts to execute the given callable, returning its result if successful.
     * If the execution throws an exception, the provided default value is returned instead.
     *
     * @param runnable     the callable to execute
     * @param defaultValue the value to return if the callable throws an exception
     * @param <T>          the type of value returned by the callable
     * @return the result of the callable if executed successfully, or the default value if an exception occurs
     */
    public static <T> T attempt(Callable<T> runnable, T defaultValue)
    {
        try
        {
            return runnable.call();
        }
        catch(Exception e)
        {
            return defaultValue;
        }
    }


    /**
     * Attempts to execute the provided runnable. If the execution completes
     * without throwing an exception, the method returns true. Otherwise, it
     * catches the exception and returns false.
     *
     * @param runnable the runnable to execute, which may throw an exception
     * @return true if the runnable executes without throwing an exception, otherwise false
     */
    public static boolean attempt(IExceptionRunnable runnable)
    {
        try
        {
            runnable.run();
            return true;
        }
        catch(Exception e)
        {
            return false;
        }
    }

    /**
     * Executes the provided {@code callable} and returns its result. If the callable
     * throws an exception, it wraps the exception in a {@link RuntimeException} and rethrows it.
     *
     * @param callable the callable to execute
     * @param <T> the type of the result returned by the callable
     * @return the result of the callable execution
     * @throws RuntimeException if an exception occurs while executing the callable
     */
    public static <T> T throwRuntime(Callable<T> callable)
    {
        try
        {
            return callable.call();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
