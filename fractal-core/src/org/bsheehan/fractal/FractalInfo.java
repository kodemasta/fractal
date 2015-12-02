package org.bsheehan.fractal;

/**
 * Created by bob on 11/28/15.
 */
public class FractalInfo {

    public IteratedFunctionFactory.FractalType type;
    public String description;
    public String name;;
    public FractalConfig config;

    public FractalInfo(IteratedFunctionFactory.FractalType type, String description, String name, FractalConfig config) {
        this.type = type;
        this.description = description;
        this.name = name;
        this.config = config;
    }
}
