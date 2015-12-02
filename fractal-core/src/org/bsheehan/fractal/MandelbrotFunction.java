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
		fractalInfo = new FractalInfo(IteratedFunctionFactory.FractalType.MANDELBROT, "todo", "todo", new FractalConfig(-2.5, -1.75, 1.0, 1.75));
		fractalInfo.config.itersMinNonZeroThreshold = fractalInfo.config.getMaxIterations()/10;
		fractalInfo.config.singleIterMaxThreshold = .2f;
		fractalInfo.config.isCentered = true;
		reset();
	}

	public void reset() {
		double left = -2.5f;
		double top = -1.75f;
		double right = 1.0f;
		double bottom = 1.75f;

		this.fractalInfo.config.setFractalRegion(new Rectangle.Double(left, top, right-left, bottom-top));
	}

	/**
	 * This iterates over the constant of z = z^2 + zConstant to define a Mandelbrot Set
	 */
	public short iterate(Complex c) {
		if (isPointInCardioidBulbs(c))
			return (short) (fractalInfo.config.maxIterations-1);
		if (c.square() > fractalInfo.config.escapeRadius)
			return 0;

		return this.iterateSelf(fractalInfo.config.zOrigin, c);
	}

	/**
	 * This will iterate the quadratic equation z = z^2 + zConstant in the complex
	 * plane. The result will be an orbit that is stable or diverges to the
	 * attractor at infinity. This method returns the number of iterations it
	 * takes for this iterator to escape a specific radius. If it takes more
	 * than a maximum number of iterations, the max number of iterations is
	 * returned.
	 * 
	 * @param z - complex variable z
	 * @param c - complex constant zConstant
	 * @return - number of iterations for magnitude to exceed certain value, kEscapeRadius
	 */
	private short iterateSelf(Complex z, Complex c) {

		// if recursive calls do not create escaping orbit, then return the max
		// iteration allowed.
		final Complex currZ = new Complex(z.getReal(), z.getImaginary());
		for (short i = 0; i < fractalInfo.config.maxIterations; ++i){
			currZ.squared();
			currZ.add(c);
			if (currZ.square() > fractalInfo.config.escapeRadius)
				return i;		
		}
		return (short) (fractalInfo.config.maxIterations-1);
	}
}
