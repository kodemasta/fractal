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
		double left = -2.0;
		double top = -1.5;
		double right = 1.0;
		double bottom = 1.5;

		FractalConfig config = new FractalConfig(left, top, right, bottom);

		fractalInfo = new FractalInfo(IteratedFunctionFactory.FractalType.MANDELBROT, IteratedFunctionFactory.FractalType.NONE, "Mandelbrot Fractal", "Mandelbrot", config);
	}

	/**
	 * This iterates over the constant of z = z^2 + constant
	 */
	public short iterate(Complex z, Complex c) {
		short minIteration = 2048;
		Complex z2 = new Complex(z);
		if (isPointInCardioidBulbs(c))
			return (short) (fractalInfo.config.maxIterations - 1);
		//if (c.magnitude() > fractalInfo.config.escapeRadius)
		//	return 0;

		double distance = Double.MAX_VALUE;
		// if recursive calls do not create escaping orbit, then return the max
		// iteration allowed.
		for (short i = 0; i < fractalInfo.config.maxIterations; ++i) {
			z2.squaredAdd(c);

			//Set new distance dist = min( dist, |z-point| )
			/*Complex zMinusPoint = new Complex(z2);
			zMinusPoint = zMinusPoint.subtract(new Complex(0,c.i));

			double zMinusPointModulus = zMinusPoint.magnitude2();
			if(zMinusPointModulus < distance) {
				distance = zMinusPointModulus;
				minIteration = i;
			}*/


			//currZ.squaredAbs();// burning ship
			if (z2.mag > fractalInfo.config.escapeRadius)
				//return minIteration;
				return i;
		}
		return
				(short) (fractalInfo.config.maxIterations - 1);
	}
}
