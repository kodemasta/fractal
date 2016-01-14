package org.bsheehan.fractal.equation;

import org.bsheehan.fractal.IterableFractalFactory;
import org.bsheehan.fractal.equation.complex.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 12/28/15.
 */
public class EquationFactory {

    public enum EquationType {
        NONE,
        QUADRATIC,
        CUBIC,
        QUARTIC,
        QUINTIC,
        SINE,
        NOVA,

        CUBIC_NEWTON,
        QUINTIC_NEWTON,
        SINE_NEWTON
     };

    public static List<Equation> getEquations(IterableFractalFactory.FractalType fractalType)
    {
        List<Equation> eqns = new ArrayList<Equation>();
        if ( (fractalType.getValue() == IterableFractalFactory.FractalType.MANDELBROT.getValue()) ||
                (fractalType.getValue() == IterableFractalFactory.FractalType.JULIA.getValue()))
        {
            //eqns.add(new Nova());
            ///eqns.add(new Sine());
            eqns.add(new Quintic());
            eqns.add(new Quartic());
            eqns.add(new Cubic());
            eqns.add(new Quadratic());
        } else if (fractalType.getValue() == IterableFractalFactory.FractalType.NEWTON.getValue()) {
            eqns.add(new NewtonQuintic());
            eqns.add(new NewtonCubic());
            eqns.add(new NewtonSine());
        }
        else
            throw new RuntimeException("no equations found for fractal type: " + fractalType.toString());

        return eqns;
    }
}
