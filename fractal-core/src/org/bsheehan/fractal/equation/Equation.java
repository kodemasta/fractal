package org.bsheehan.fractal.equation;

import org.bsheehan.fractal.equation.complex.Complex;

/**
 * Created by bob on 12/27/15.
 */
public interface Equation {
    short iterate(double x, double c);
    short iterate(Complex x, Complex c, short maxIter, double escape);
    EquationFactory.EquationType getType();
}
