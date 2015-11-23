package org.bsheehan.fractal;

import java.awt.*;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name MandelbrotFractal
 * @description A Mandelbrot set iterated function generator class
 */
public class MandelbrotFunction extends AbstractFractalFunction {


	/**
	 * Constructor
	 */
	public MandelbrotFunction() {
		config.itersMinNonZeroThreshold = getMaxIterations()/10;
		config.singleIterMaxThreshold = .2f;
		config.isCentered = true;
		reset();
	}

	public void reset() {
		this.left = -2.0f;
		this.top = -1.5f;
		this.right = 1.0f;
		this.bottom = 1.5f;

		this.fractalRegion = new Rectangle.Float(this.left, this.top, this.right-this.left, this.bottom-this.top);
	}

	/**
	 * This iterates over the constant of z = z^2 + c to define a Mandelbrot Set
	 */
	public short iterate(Complex c) {
		if (isPointInCardioidBulbs(c))
			return (short) (this.maxIterations-1);
		if (c.square() > this.escapeRadius)
			return 0;

		return this.iterateSelf(this.z0, c);
	}

	/**
	 * This will iterate the quadratic equation z = z^2 + c in the complex
	 * plane. The result will be an orbit that is stable or diverges to the
	 * attractor at infinity. This method returns the number of iterations it
	 * takes for this iterator to escape a specific radius. If it takes more
	 * than a maximum number of iterations, the max number of iterations is
	 * returned.
	 * 
	 * @param z - complex variable z
	 * @param c - complex constant c
	 * @return - number of iterations for magnitude to exceed certain value, kEscapeRadius
	 */
	private short iterateSelf(Complex z, Complex c) {

		// if recursive calls do not create escaping orbit, then return the max
		// iteration allowed.
		final Complex currZ = new Complex(z.getReal(), z.getImaginary());
		for (short i = 0; i < this.maxIterations; ++i){
			currZ.squared();
			currZ.add(c);
			if (currZ.square() > this.escapeRadius)
				return i;		
		}
		return (short) (this.maxIterations-1);
	}
}
