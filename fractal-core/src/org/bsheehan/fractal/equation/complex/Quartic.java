package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;

/**
 * Created by bob on 12/27/15.
 */
public class Quartic implements Equation {

    @Override
    public short iterate(double x, double c) {
        return 0;
    }

    @Override
    public short iterate(ComplexNumber z, ComplexNumber c, short limit, double escape) {

        // if recursive calls do not create escaping orbit, then return the max
        // iteration allowed.
        for (short i = 0; i < limit; ++i) {
            z=z.mult(z);
            z=z.mult(z).add(c);
            if (z.norm() > escape)
                return i;
        }
        return (short)(limit-1);
    }

    @Override
    public String toString(){
        return "Quartic: f(z) = z^4 + c";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.QUARTIC;
    }

//    @Override
//    public Complex getLastRoot(){
//        return null;
//    }
}
