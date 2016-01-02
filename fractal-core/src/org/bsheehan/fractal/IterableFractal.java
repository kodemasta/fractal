package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.complex.Complex;

/**
 * Created by bob on 12/27/15.
 */
public interface IterableFractal {
    short iterate(Complex z, Complex c);
    FractalInfo getInfo();
}
