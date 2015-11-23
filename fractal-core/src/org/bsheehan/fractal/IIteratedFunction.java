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
	/**
	 * Return max iterations allowable for iterating each orbit in the complex plane
	 * @return
	 */
	short getMaxIterations();

	/**
	 * Return rectangular region where fractal is defined
	 * @return
	 */
	Rectangle2D.Float getFractalRegion();
	
	FractalConfig getFractalConfig();

	/**
	 * reset to original boundary that defines the fractal region in the complex plane.
	 */
	void reset();

	/**
	 * For each orbit iterated over the complex region we set initial values.
	 * @param z0
	 * @param c
	 */
	void setInitialConditions(Complex z0, Complex c);
	void setInitialConditions(IIteratedFunction func);

	/**
	 * Adjust the center point of the region of interest for this iterated system.
	 * @param center
	 */
	void setCenter(Point.Float center);

	/**
	 * Adjust the region of interest for this iterated system by zooming around the
	 * existing center point for this fractal.
	 */
	void setZoom(float zoom);

	/**
	 * Select a region in the defined boundary for this iterative function. The idea is to locate cool looking
	 * areas for rendering as texture maps. This method supports that effort by supplying candidate regions for display.
	 * @param centered 
	 */
	void setRandomRegion(boolean centered);

	/**
	 * Called to iterate for a given point in the complex plane
	 * @param z
	 * @return
	 */
	short iterate(Complex z);

	void setScale(float screenAspectRatio);

	void setFractalRegion(Rectangle.Float coolRegion);

}
