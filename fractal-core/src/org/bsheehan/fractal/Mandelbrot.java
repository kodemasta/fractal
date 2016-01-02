package org.bsheehan.fractal;

import org.bsheehan.fractal.equation.complex.Complex;

/**
 * Created by bob on 12/27/15.
 */
public class Mandelbrot implements IterableFractal {

    private FractalInfo fractalInfo;

    public Mandelbrot(FractalInfo fractalInfo) {
        this.fractalInfo = fractalInfo;
    }

    public FractalInfo getInfo() {
        return fractalInfo;
    }

    @Override
    public short iterate(Complex z, Complex c) {
        return fractalInfo.config.iterate(z, c);
    }
}
