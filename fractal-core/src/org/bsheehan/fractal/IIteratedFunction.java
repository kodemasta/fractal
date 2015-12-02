package org.bsheehan.fractal;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name IIteratedFunction
 * @description Interface for classes that define iteration function systems on the complex plane. 
 */
public interface IIteratedFunction {

	FractalConfig getConfig();


		/**
         * reset to original boundary that defines the fractal region in the complex plane.
         */
	void reset();

	/**
	 * Adjust the center point of the region of interest for this iterated system.
	 * @param center
	 */
	void setCenter(Point.Double center);
	/**
	 * Called to iterate for a given point in the complex plane
	 * @param z
	 * @return
	 */
	short iterate(Complex z);

	void setScale(double screenAspectRatio);

}
