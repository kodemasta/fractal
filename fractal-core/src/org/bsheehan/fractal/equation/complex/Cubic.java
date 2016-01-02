package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;

/**
 * Created by bob on 12/27/15.
 */
public class Cubic implements Equation {

    @Override
    public short iterate(double x, double c) {
        return 0;
    }

    @Override
    public short iterate(Complex z, Complex c, short limit, double escape) {
        Complex z2 = new Complex(z);
        // if recursive calls do not create escaping orbit, then return the max
        // iteration allowed.
        for (short i = 0; i < limit; ++i) {
            z2.cubedAdd(c);
            if (z2.mag > escape)
                return i;
        }
        return (short)(limit-1);
    }

    @Override
    public String toString(){
        return "f(z) = z^3 + c";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.CUBIC;
    }
}
