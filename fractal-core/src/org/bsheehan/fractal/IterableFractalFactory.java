package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.Equation;
import org.bsheehan.fractal.equation.EquationFactory;
import org.bsheehan.fractal.equation.complex.Cubic;
import org.bsheehan.fractal.equation.complex.Quadratic;
import org.bsheehan.fractal.equation.complex.Quartic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 12/27/15.
 */
public class IterableFractalFactory {

    public enum FractalType {
        NONE,
        MANDELBROT,
        JULIA
        //MANDELBROT_CUBIC,
        //MANDELBROT_JULIA,
        //MANDELBROT_CUBIC_JULIA,
        //MANDELBROT_QUARTIC,
        //MANDELBROT_QUARTIC_JULIA
    };

    public static List<IterableFractal> getFractals() {
        List<IterableFractal> fractals = new ArrayList<>();

        fractals.add(createIterableFractal(IterableFractalFactory.FractalType.JULIA));
        fractals.add(createIterableFractal(IterableFractalFactory.FractalType.MANDELBROT));

        return fractals;
    }

    /**
     * Create an instance of a complex plane iterative method class
     * @param type
     * @return
     */
    static public IterableFractal createIterableFractal2(IterableFractalFactory.FractalType type, EquationFactory.EquationType equationType) {

        FractalConfig config = null;
        switch (equationType) {
            case QUADRATIC: {
                config = new FractalConfig(new Quadratic(), -2.0, -2.0, 2.0, 2.0);
                break;
            }
            case CUBIC: {
                config = new FractalConfig(new Cubic(), -1.75, -1.75, 1.75, 1.75);
                break;
            }
            case QUARTIC: {
                config = new FractalConfig(new Quartic(), -1.75, -1.75, 1.75, 1.75);
                break;
            }
        }

        switch (type) {
            case MANDELBROT: {
                FractalInfo fractalInfo = new FractalInfo(
                        FractalType.MANDELBROT,
                        "Mandelbrot",
                        "Mandelbrot",
                        config);
                return new Mandelbrot(fractalInfo);
            }
            case JULIA: {
                FractalInfo fractalInfo = new FractalInfo(
                        FractalType.JULIA,
                        "Julia",
                        "Julia",
                        config);
                return new Mandelbrot(fractalInfo);
            }        }
        return null;
    }

    /**
     * Create an instance of a complex plane iterative method class
     * @param type
     * @return
     */
    static public IterableFractal createIterableFractal(IterableFractalFactory.FractalType type){
        switch(type){
            case MANDELBROT: {
                 FractalInfo fractalInfo = new FractalInfo(
                        IterableFractalFactory.FractalType.MANDELBROT,
                        "Mandelbrot",
                        "Mandelbrot");
                return new Mandelbrot(fractalInfo);
            }
            case JULIA: {
                FractalInfo fractalInfo = new FractalInfo(
                        IterableFractalFactory.FractalType.JULIA,
                         "Julia ",
                        "Julia");
                return new Mandelbrot(fractalInfo);
            }
        }
        return null;
    }
}
