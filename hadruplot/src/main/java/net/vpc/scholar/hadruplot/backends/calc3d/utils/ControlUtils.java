
package net.vpc.scholar.hadruplot.backends.calc3d.utils;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

/**
 * Utility class to help working with controls.
 * @author William Bittle
 * @version 1.0.1
 * @since 1.0.0
 */
public class ControlUtils {
	/**
	 * Returns the double value of the number stored in the given text field.
	 * @param field the text field
	 * @return double the double value
	 */
	public static final double getDoubleValue(JFormattedTextField field) {
		Number number = (Number)field.getValue();
		return number.doubleValue();
	}
	
	/**
	 * Returns the int value of the number stored in the given text field.
	 * @param field the text field
	 * @return int the integer value
	 */
	public static final int getIntValue(JFormattedTextField field) {
		Number number = (Number)field.getValue();
		return number.intValue();
	}
	
	/**
	 * Returns the parent window for the given component.
	 * @param component the component
	 * @return Window
	 * @since 1.0.1
	 */
	public static final Window getParentWindow(Component component) {
		// get the parent frame
		Frame frame = JOptionPane.getFrameForComponent(component);
		// first check for a parent dialog component
		while (component != null) {
			component = component.getParent();
			if (component instanceof Dialog) {
				return (Dialog) component;
			}
		}
		// if nothing was found, then use the frame for the component
		return frame;
	}
}
