package org.bsheehan.fractal.equation;

import org.bsheehan.fractal.equation.complex.ComplexNumber;

/**
 * Created by bob on 12/27/15.
 */
public class Utils {
    /**
     * Lifted this optimization off Wikipedia. The central cardioid bulbs in the Mandelbrot set are guaranteed to generate
     * orbits that converge. This is the least optimal type of iteration that will always hit kMaxIterations.
     * If we detect a calculation in these bulbs, we can just set the iteration to kMaxIterations and move on.
     * @param z
     * @return
     */
    public static boolean isPointInCardioidBulbs(ComplexNumber z) {

        final double term1 = z.re() -.25;
        final double term2 = z.im()*z.im();
        final double q = term1*term1 + term2;
        return q*(q+term1) < .25*term2;
    }
}
