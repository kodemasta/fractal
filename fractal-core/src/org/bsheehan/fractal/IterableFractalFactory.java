package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.EquationFactory;
import org.bsheehan.fractal.equation.complex.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bob on 12/27/15.
 */
public class IterableFractalFactory {

    public enum FractalType {
        MANDELBROT(1),
        JULIA(2),
        NEWTON(4);

        private int value;

        FractalType(int value) {
            this.value = value;
        }

        public int getValue(){
            return value;
        }

        static public FractalType get(int value){
            for (FractalType type: FractalType.values()){
                if (type.getValue() == value)
                    return type;
            }
            return null;
        }
    };

    public static List<IterableFractal> getFractals() {
        List<IterableFractal> fractals = new ArrayList<>();

        fractals.add(createIterableFractal(FractalType.NEWTON));
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
            case QUINTIC: {
                config = new FractalConfig(new Quintic(), -1.75, -1.75, 1.75, 1.75);
                break;
            }
            case SINE: {
                config = new FractalConfig(new Sine(), -1.75, -1.75, 1.75, 1.75);
                break;
            }
            case NOVA: {
                config = new FractalConfig(new Nova(), -1.75, -1.75, 1.75, 1.75);
                break;
            }
            case SINE_NEWTON: {
                config = new FractalConfig(new NewtonSine(), -2.0, -2.0, 2.0, 2.0);
                break;
            }
            case CUBIC_NEWTON: {
                config = new FractalConfig(new NewtonCubic(), -2.0, -2.0, 2.0, 2.0);
                break;
            }
            case QUINTIC_NEWTON: {
                config = new FractalConfig(new NewtonQuintic(), -2.0, -2.0, 2.0, 2.0);
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
            }
            case NEWTON: {
                FractalInfo fractalInfo = new FractalInfo(
                        FractalType.NEWTON,
                        "Newton",
                        "Newton",
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
            case NEWTON: {
                FractalInfo fractalInfo = new FractalInfo(
                        FractalType.NEWTON,
                        "Newton ",
                        "Newton");
                return new Mandelbrot(fractalInfo);
            }
        }
        return null;
    }
}
