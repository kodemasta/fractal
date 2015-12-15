package org.bsheehan.fractal;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name MandelbrotFractal
 * @description A Mandelbrot set iterated function generator class
 */
public class MandelbrotQuarticJuliaFunction extends AbstractFractalFunction {

	/**
	 * Constructor
	 */
	public MandelbrotQuarticJuliaFunction() {
		FractalConfig config = new FractalConfig(-1.75, -1.75, 1.75, 1.75);
		fractalInfo = new FractalInfo(IteratedFunctionFactory.FractalType.MANDELBROT_QUARTIC_JULIA,
				IteratedFunctionFactory.FractalType.MANDELBROT_QUARTIC,
				"Mandelbrot Quartic Julia Fractal",
				"Mandelbrot Quartic (Julia)", config);
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
			z2.quarticAdd(c);
			//z2.add(c);
			//currZ.squaredAbs();// burning ship
			if (z2.mag > fractalInfo.config.escapeRadius)
				return i;
		}
		return (short) (fractalInfo.config.maxIterations - 1);
	}
}
