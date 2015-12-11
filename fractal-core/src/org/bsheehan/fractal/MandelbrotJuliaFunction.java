package org.bsheehan.fractal;

public class MandelbrotJuliaFunction extends AbstractFractalFunction {

	/**
	 * Constructor
	 */
	public MandelbrotJuliaFunction() {
		double left = -2.0;
		double top = -2.0;
		double right = 2.0;
		double bottom = 2.0;

		FractalConfig config = new FractalConfig(left, top, right, bottom);

		fractalInfo = new FractalInfo(IteratedFunctionFactory.FractalType.MANDELBROT_JULIA, IteratedFunctionFactory.FractalType.MANDELBROT, "Mandelbrot Julia Set", "Mandelbrot Julia", config);
	}

	/**
	 * This iterates over the constant of z = z^2 + constant
	 */
	public short iterate(Complex z, Complex c) {
		Complex z2 = new Complex(z);
		if (isPointInCardioidBulbs(c))
			return (short) (fractalInfo.config.maxIterations - 1);
		//if (c.magnitude() > fractalInfo.config.escapeRadius)
		//	return 0;

		// if recursive calls do not create escaping orbit, then return the max
		// iteration allowed.
		for (short i = 0; i < fractalInfo.config.maxIterations; ++i) {
			z2.squaredAdd(c);
			//z2.add(c);
			//currZ.squaredAbs();// burning ship
			if (z2.mag > fractalInfo.config.escapeRadius)
				return i;
		}
		return (short) (fractalInfo.config.maxIterations - 1);
	}
}
