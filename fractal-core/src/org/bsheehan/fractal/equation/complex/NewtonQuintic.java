package org.bsheehan.fractal.equation.complex;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 12/27/15.
 */
public class NewtonQuintic implements Equation {
    //ComplexNumber root1 = new ComplexNumber(-0.809,0.587);
    //ComplexNumber root2 = new ComplexNumber(-0.809,-0.587);
    //ComplexNumber root3 = new ComplexNumber(0.309,0.951);
    //ComplexNumber root4 = new ComplexNumber(0.309,-0.951);
    //ComplexNumber root5 = new ComplexNumber(1.0,0.0);

    //int lastRoot = 0;

    final double epsilon = 0.1;

    //List<ComplexNumber> coeffsp = new ArrayList<ComplexNumber>();
    //List<ComplexNumber> coeffs = new ArrayList<ComplexNumber>();

    public NewtonQuintic()
    {
//        coeffsp = new ArrayList <ComplexNumber> ();
//        coeffsp.add(new ComplexNumber(5));
//        coeffsp.add(ComplexNumber.ZERO);
//        coeffsp.add(ComplexNumber.ZERO);
//        coeffsp.add(ComplexNumber.ZERO);
//        coeffsp.add(ComplexNumber.ZERO);
//
//        coeffs.add(ComplexNumber.ONE);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.ZERO);
//        coeffs.add(ComplexNumber.NEG_ONE);
    }

    @Override
    public short iterate(double x, double c) {
        return 0;
    }

    public short newtonRoot (double x0, double y0)
    {
        ComplexNumber z = new ComplexNumber(x0, y0);
        short numIters = 0;

        ComplexNumber c = new ComplexNumber(5);

        ComplexNumber numerator = z.mult(z).mult(z).mult(z).mult(z).add(ComplexNumber.NEG_ONE);
        while (numerator.mod() > 0.0001 && numIters < 256)
        {
            ComplexNumber term1 = z.mult(z).mult(z).mult(z);
            numerator = term1.mult(z).add(ComplexNumber.NEG_ONE);
            ComplexNumber denomenator = c.mult(term1);
            z = z.sub(numerator.div(denomenator));
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
//        else if ( (Math.abs(root4.re() - z0.re()) < epsilon) && (Math.abs(root4.im() - z0.im()) < epsilon))
//            lastRoot = 3;
//        else if ( (Math.abs(root5.re() - z0.re()) < epsilon) && (Math.abs(root5.im() - z0.im()) < epsilon))
//            lastRoot = 3;
//        else
//            lastRoot = 0;

        return numIters;
    }

//    public ComplexNumber f (ComplexNumber z)
//    {
//        ComplexNumber answer = coeffs.get(0);
//
//         for (int i = 1; i < coeffs.size(); i++)
//            answer = (answer.mult(z)).add(coeffs.get(i));
//        return answer;
//    }
//
//    public ComplexNumber fp (ComplexNumber z)
//    {
//        ComplexNumber answer = coeffsp.get(0);
//        for (int i = 1; i < coeffsp.size(); i++)
//            answer = (answer.mult(z)).add(coeffsp.get(i));
//        return answer;
//    }

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
        return "Quintic: f(z) = z^5 - 1";
    }

    @Override
    public EquationFactory.EquationType getType() {
        return EquationFactory.EquationType.QUINTIC_NEWTON;
    }
}
