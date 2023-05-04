
package net.thevpc.scholar.hadruplot.libraries.calc3d.log;

import java.io.PrintStream;


/**
 * The logging class.
 */
public final class Logger
{
	private static Level iMinimumLevel = Level.DEBUG;
	
	private String iCategory = null;
	
	
	/**
	 * Constructor.
	 * @param aCategory the logging category (gets prepended to all log 
	 * output).
	 */
	private Logger(String aCategory) 
	{
		iCategory = " [" + aCategory + "] ";
	}
	
	
	
	/**
	 * Set the minimum logging level required in order for a log message to 
	 * be processed.
	 * @param aLevel the level.
	 */
	public static void setMinimumLevel(Level aLevel)
	{
		iMinimumLevel = aLevel;
	}
	
	
	
	
	/**
	 * Get a logger for the given category.
	 * @param aCategory the logging category.
	 * @return a non-null logger.
	 */
	public static Logger getLogger(String aCategory)
	{
		return new Logger(aCategory);
	}
	
	
    /**
     * Log an object at {@link Level#TRACE} priority.
     */
    public void trace(Object aObj)
    {
        trace(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#TRACE} priority.
     */
    public void trace(String aMessage)
    {
        trace(aMessage, null);
    }
    /**
     * Log an exception at {@link Level#TRACE} priority.
     */
    public void trace(Throwable aException)
    {
        trace(null, aException);
    }        
    /**
     * Log a message and exception at {@link Level#TRACE} priority.
     */
    public void trace(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.TRACE);
    }
    
    
    
    /**
     * Log an object at {@link Level#DEBUG} priority.
     */
    public void debug(Object aObj)
    {
    	debug(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#DEBUG} priority.
     */
    public void debug(String aMessage)
    {
        debug(aMessage, null);
    }
    /**
     * Log an exception at {@link Level#DEBUG} priority.
     */
    public void debug(Throwable aException)
    {
        debug(null, aException);
    }        
    /**
     * Log a message and exception at {@link Level#DEBUG} priority.
     */
    public void debug(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.DEBUG);
    }    
    
    
    
    /**
     * Log an object at {@link Level#INFO} priority.
     */
    public void info(Object aObj)
    {
    	info(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#INFO} priority.
     */    
    public void info(String aMessage)
    {
        info(aMessage, null);        
    }
    /**
     * Log an exception at {@link Level#INFO} priority.
     */    
    public void info(Throwable aException)
    {
        info(null, aException);        
    }
    /**
     * Log a message and exception at {@link Level#INFO} priority.
     */    
    public void info(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.INFO);        
    }

    

    /**
     * Log an object at {@link Level#WARN} priority.
     */
    public void warn(Object aObj)
    {
    	warn(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#WARN} priority.
     */    
    public void warn(String aMessage)
    {
        warn(aMessage, null);       
    }
    /**
     * Log an exception at {@link Level#WARN} priority.
     */    
    public void warn(Throwable aException)
    {
        warn(null, aException);       
    }
    /**
     * Log a message and exception at {@link Level#WARN} priority.
     */    
    public void warn(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.WARN);       
    }
    
    
    
    
    /**
     * Log an object at {@link Level#ERROR} priority.
     */
    public void error(Object aObj)
    {
    	error(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#ERROR} priority.
     */    
    public void error(String aMessage)
    {
        error(aMessage, null);       
    }
    /**
     * Log an exception at {@link Level#ERROR} priority.
     */    
    public void error(Throwable aException)
    {
        error(null, aException);       
    }
    /**
     * Log a message and exception at {@link Level#ERROR} priority.
     */    
    public void error(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.ERROR);       
    }

    

    /**
     * Log an object at {@link Level#FATAL} priority.
     */
    public void fatal(Object aObj)
    {
    	fatal(aObj.toString(), null);
    }    
    /**
     * Log a message at {@link Level#FATAL} priority.
     */    
    public void fatal(String aMessage)
    {
        fatal(aMessage, null);       
    }
    /**
     * Log an exception at {@link Level#FATAL} priority.
     */    
    public void fatal(Throwable aException)
    {
        fatal(null, aException);       
    }
    /**
     * Log a message and exception at {@link Level#FATAL} priority.
     */    
    public void fatal(String aMessage, Throwable aException)
    {
        log(aMessage, aException, Level.FATAL);       
    }
    
    


	/**
	 * Log a message.
	 * @param aMsg the message. May be null.
	 * @param aThrowable the exception to log. May be null. 
	 * @param aLevel the level to log at.
	 */
	private void log(String aMsg, Throwable aThrowable, Level aLevel)
	{
		if (aLevel.intValue() < iMinimumLevel.intValue())
			return;
		
        PrintStream outputStream = System.out;

        // are we printing an error instead?
        if (Level.ERROR.intValue() <= aLevel.intValue())
        {
            outputStream = System.err;
        }

        if (null == aMsg && null != aThrowable)
        {
        	aMsg = aThrowable.toString();
        }
        
        StringBuilder out = new StringBuilder();
        out.append(aLevel);
        out.append(iCategory);
        out.append(aMsg);
        
        outputStream.println( out.toString() );
        
        if (null != aThrowable)
        {
        	aThrowable.printStackTrace(outputStream);
        }
	
	}
	
}




