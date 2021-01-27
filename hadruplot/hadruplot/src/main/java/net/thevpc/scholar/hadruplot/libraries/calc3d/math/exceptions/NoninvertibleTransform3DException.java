package net.thevpc.scholar.hadruplot.libraries.calc3d.math.exceptions;


/**The NoninvertibleTransformException class represents an exception that is thrown if an operation is performed requiring the inverse of an 
 *AffineTransform object but the AffineTransform is in a non-invertible state.
 * 
 * @author mahesh
 *
 */
public  class NoninvertibleTransform3DException	extends Exception  {

	private static final long serialVersionUID = 1L;

	public NoninvertibleTransform3DException() {
		System.out.println("inverse doesn't exists");
	}
}