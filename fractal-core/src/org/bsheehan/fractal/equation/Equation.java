package org.bsheehan.fractal.equation;

import org.bsheehan.fractal.equation.complex.ComplexNumber;

/**
 * Created by bob on 12/27/15.
 */
public interface Equation {
    short iterate(double x, double c);
    short iterate(ComplexNumber x, ComplexNumber c, short maxIter, double escape);
    EquationFactory.EquationType getType();
    //Complex getLastRoot();
}
