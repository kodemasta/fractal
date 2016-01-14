package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.complex.ComplexNumber;

/**
 * Created by bob on 12/27/15.
 */
public interface IterableFractal {
    short iterate(ComplexNumber z, ComplexNumber c);
    //Complex getLastRoot();
    FractalInfo getInfo();
}
