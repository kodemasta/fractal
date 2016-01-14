package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;
import org.bsheehan.fractal.equation.Utils;

/**
 * Created by bob on 12/27/15.
 */
public class Nova implements Equation {

    @Override
    public short iterate(double x, double c) {
        return 0;
    }

    @Override
    public short iterate(ComplexNumber z, ComplexNumber c, short limit, double escape) {

        //if (Utils.isPointInCardioidBulbs(c))
        //    return (short)(limit-1);
        ComplexNumber c2 = new ComplexNumber(1,0);
        ComplexNumber c3 = new ComplexNumber(3,0);

        for (short i = 0; i < limit; ++i) {
            ComplexNumber term1 = z.sub(c2);
            term1 = term1.mult(term1).mult(term1);
            ComplexNumber term2 = c3.mult(z.mult(z));
            z = z.sub(term1.div(term2)).add(c);
            if (z.norm() > escape)
                return i;
        }
        return (short)(limit-1);
    }

    @Override
    public String toString(){
        return "Nova: f(z) = z-(z-1)^3/3z^2+c";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.NOVA;
    }

//    @Override
//    public Complex getLastRoot(){
//        return null;
//    }
}
