package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;

/**
 * Created by bob on 12/27/15.
 */
public class Sine implements Equation {
    //ComplexNumber root1 = new ComplexNumber(1,0);
    //ComplexNumber root2 = new ComplexNumber(-0.5,0.86603);
    //ComplexNumber root3 = new ComplexNumber(-0.5,-0.86603);
    //int lastRoot = 0;

    final double epsilon = 0.1;

    //List<ComplexNumber> coeffsp = new ArrayList<ComplexNumber>();
    //List<ComplexNumber> coeffs = new ArrayList<ComplexNumber>();

    public Sine()
    {

//        coeffs.add(ComplexNumber.ONE);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.NEG_ONE);
//
//        coeffsp.add(new ComplexNumber(3));
//        coeffsp.add(ComplexNumber.ZERO);
//        coeffsp.add(ComplexNumber.ZERO);
    }

    @Override
    public short iterate(double x, double c) {
        return 0;
    }


    @Override
    public short iterate(ComplexNumber z, ComplexNumber c, short limit, double escape) {
        for (short i = 0; i < limit; ++i) {
            z=z.mult(ComplexNumber.sin(z)).add(c);
            if (z.norm() > escape)
                return i;
        }
        return (short)(limit-1);
    }

//    @Override
//    public Complex getLastRoot(){
//        return null;
//    }

    @Override
    public String toString(){
        return "Sine: f(z) = sin(z)";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.SINE;
    }
}
