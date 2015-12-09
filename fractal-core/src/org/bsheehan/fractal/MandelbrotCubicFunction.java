package org.bsheehan.fractal;

import java.awt.*;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name MandelbrotFractal
 * @description A Mandelbrot set iterated function generator class
 */
public class MandelbrotCubicFunction extends AbstractFractalFunction {

	/**
	 * Constructor
	 */
	public MandelbrotCubicFunction() {
		fractalInfo = new FractalInfo(IteratedFunctionFactory.FractalType.MANDELBROT_CUBIC, "Mandelbrot Cubic Fractal", "Mandelbrot Cubic", new FractalConfig(-2.5, -1.75, 1.0, 1.75));
		fractalInfo.config.isCentered = true;
		reset();
	}

	public void reset() {
		double left = -1.75f;
		double top = -1.75f;
		double right = 1.75f;
		double bottom = 1.75f;

		this.fractalInfo.config.setFractalRegion(new Rectangle.Double(left, top, right - left, bottom - top));
	}

	/**
	 * This iterates over the constant of z = z^2 + constant
	 */
	public short iterate(Complex z, Complex c) {
		Complex z2 = new Complex(z);
//		if (isPointInCardioidBulbs(c))
//			return (short) (fractalInfo.config.maxIterations - 1);
//		if (c.magnitude() > fractalInfo.config.escapeRadius)
//			return 0;

		// if recursive calls do not create escaping orbit, then return the max
		// iteration allowed.
		for (short i = 0; i < fractalInfo.config.maxIterations; ++i) {
			z2.cubedAdd(c);
			//z2.add(c);
			//currZ.squaredAbs();// burning ship
			if (z2.mag > fractalInfo.config.escapeRadius)
				return i;
		}
		return (short) (fractalInfo.config.maxIterations - 1);
	}
}
