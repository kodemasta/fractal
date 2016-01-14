package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 12/27/15.
 */
public class NewtonCubic implements Equation {
    //ComplexNumber root1 = new ComplexNumber(1,0);
    //ComplexNumber root2 = new ComplexNumber(-0.5,0.86603);
    //ComplexNumber root3 = new ComplexNumber(-0.5,-0.86603);
    //int lastRoot = 0;

    final double epsilon = 0.1;

    //List<ComplexNumber> coeffsp = new ArrayList<ComplexNumber>();
    //List<ComplexNumber> coeffs = new ArrayList<ComplexNumber>();

    public NewtonCubic()
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

    public short newtonRoot (double x0, double y0)
    {
        ComplexNumber z = new ComplexNumber(x0, y0);
        short numIters = 0;

        ComplexNumber c = new ComplexNumber(3);

        ComplexNumber numerator = z.mult(z).mult(z).add(ComplexNumber.NEG_ONE);
        while (numerator.mod() > 0.0001 && numIters < 256)
        {
            ComplexNumber term1 = z.mult(z);
            numerator = z.mult(term1).add(ComplexNumber.NEG_ONE);
            ComplexNumber denominator= c.mult(term1);
            z = z.sub(numerator.div(denominator));
            numIters++;
        }

//        if (numIters >= 256)
//            z0 = ComplexNumber.INFINITY;

//        if ( (Math.abs(root1.re() - z0.re()) < epsilon) && (Math.abs(root1.im() - z0.im()) < epsilon)) {
//            lastRoot = 1;
//        } else if ( (Math.abs(root2.re()- z0.re()) < epsilon) && (Math.abs(root2.im() - z0.im()) < epsilon))
//            lastRoot = 2;
//        else if ( (Math.abs(root3.re() - z0.re()) < epsilon) && (Math.abs(root3.im() - z0.im()) < epsilon))
//            lastRoot = 3;
//        else
//            lastRoot = 0;

        return numIters;
    }



    @Override
    public short iterate(ComplexNumber z, ComplexNumber c, short limit, double escape) {
        return newtonRoot(z.re(),z.im());
    }

//    @Override
//    public Complex getLastRoot(){
//        return null;
//    }

    @Override
    public String toString(){
        return "Cubic: f(z) = z^3 - 1";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.CUBIC_NEWTON;
    }
}
