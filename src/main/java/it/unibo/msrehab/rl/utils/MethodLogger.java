package it.unibo.msrehab.rl.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Provides a method logger utility to log method-level messages with hierarchical structure
 * and different logging levels. It facilitates tracking the execution flow across methods
 * and log messages by associating them with the method in which they occur.
 * Used for fine-grained logging of individual methods execution flow.
 */
public class MethodLogger
{
    public static final int LEVEL_DEBUG = 0;
    public static final int LEVEL_INFO = 1;
    public static final int LEVEL_WARN = 2;
    public static final int LEVEL_ERROR = 3;

    public static final int DEFAULT_METHOD_NAME_DEPTH = 4;


    private final Logger logger;

    private final StringBuilder sb = new StringBuilder();

    private String currentMethodName = "";

    private static int loggingLevel = LEVEL_WARN;
    private int currentLevel = LEVEL_WARN;

    public MethodLogger(Class<?> objectClass)
    {
        logger = LoggerFactory.getLogger(objectClass);
    }

    public static void setLoggingLevel(int level)
    {
        MethodLogger.loggingLevel = level;
    }


    /**
     * Flushes the current log messages stored in the buffer to the appropriate logging level.
     * Messages are logged based on the current logging level settings.
     * After flushing, the buffer is cleared, and the logging context is reset.
     *
     * @return the current MethodLogger instance for method chaining
     */
    public MethodLogger flush()
    {
        if (sb.length() == 0)
            return this;
        sb.delete(Math.max(0, sb.length() - 4), sb.length()); // Remove trailing arrow

        if (currentLevel >= loggingLevel)
        {
            switch (loggingLevel)
            {
                case LEVEL_DEBUG:
                    logger.debug(sb.toString());
                    break;
                case LEVEL_WARN:
                    logger.warn(sb.toString());
                    break;
                case LEVEL_ERROR:
                    logger.error(sb.toString());
                    break;
                case LEVEL_INFO:
                default:
                    logger.info(sb.toString());
            }
        }

        sb.delete(0, sb.length());
        currentLevel = loggingLevel;
        currentMethodName = "";
        return this;
    }

    private MethodLogger append(String msg, int level)
    {
        String methodName = getMethodName(DEFAULT_METHOD_NAME_DEPTH);
        if (!currentMethodName.equals(methodName))
        {
            methodStart(methodName);
            currentMethodName = methodName;
        }
        sb.append(msg).append(" -> ");
        currentLevel = level;
        return this;
    }

    public MethodLogger info(String msg)
    {
        return append(msg, LEVEL_INFO);
    }

    public MethodLogger warn(String msg)
    {
        return append(msg, LEVEL_WARN);
    }

    public MethodLogger error(String msg)
    {
        return append(msg, LEVEL_ERROR);
    }

    public MethodLogger info(String msg, Object... args)
    {
        return append(String.format(msg, args), LEVEL_INFO);
    }

    public MethodLogger warn(String msg, Object... args)
    {
        return append(String.format(msg, args), LEVEL_WARN);
    }

    public MethodLogger error(String msg, Object... args)
    {
        return append(String.format(msg, args), LEVEL_ERROR);
    }

    private static String getMethodName(int depth)
    {
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        return ste[depth].getMethodName();
    }

    private void methodStart(String methodName)
    {
        if (sb.length() > 0)
            flush();
        sb.append(String.format("[%s]", methodName)).append(" -> ");
    }
}
