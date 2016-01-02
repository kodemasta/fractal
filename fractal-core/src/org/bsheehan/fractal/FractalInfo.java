package org.bsheehan.fractal;

/**
 * Created by bob on 11/28/15.
 */
public class FractalInfo {

    public IterableFractalFactory.FractalType type;
    public String description;
    public String name;;
    public FractalConfig config;

    public FractalInfo(IterableFractalFactory.FractalType type, String description, String name, FractalConfig config) {
        this.type = type;
        this.description = description;
        this.name = name;
        this.config = config;
    }

    public FractalInfo(IterableFractalFactory.FractalType type, String description, String name) {
        this.type = type;
        this.description = description;
        this.name = name;
        this.config = new FractalConfig();

    }
}
