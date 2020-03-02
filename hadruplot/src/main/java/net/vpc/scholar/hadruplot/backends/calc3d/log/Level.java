
package net.vpc.scholar.hadruplot.backends.calc3d.log;


/**
 * The different logging levels/priorities.
 */
public enum Level
{
	TRACE(0, "TRACE"),
	DEBUG(1,"DEBUG"),
	INFO(2, "INFO"),
	WARN(3, "WARN"),
	ERROR(4, "ERROR"),
	FATAL(5, "FATAL");
	
	
	private int iIntValue = -1;
	private String iDescription = null;
	
	
	/**
	 * Constructor.
	 * @param aIntValue the integer value of the level.
	 * @param aDescription the textual description of the level.
	 */
	Level(int aIntValue, String aDescription)
	{
		iIntValue = aIntValue;
		iDescription = aDescription;
	}
	
	
	/**
	 * Get the integer value of this level.
	 * @return a non-negative integer.
	 */
	public int intValue()
	{
		return iIntValue;
	}
	
	
	public String toString()
	{
		return iDescription;
	}
}


