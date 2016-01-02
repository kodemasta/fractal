package org.bsheehan.fractal.equation;

import org.bsheehan.fractal.equation.complex.Cubic;
import org.bsheehan.fractal.equation.complex.Quadratic;
import org.bsheehan.fractal.equation.complex.Quartic;

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
        QUARTIC
    };

    public static List<Equation> getEquations()
    {
        List<Equation> eqns = new ArrayList<Equation>();
        eqns.add(new Quartic());
        eqns.add(new Cubic());
        eqns.add(new Quadratic());
        return eqns;
    }
}
