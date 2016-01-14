package org.bsheehan.fractal.equation.complex;

public class ComplexNumber
{
    public double a;
    public double b;

    public static final ComplexNumber ONE = new ComplexNumber (1);
    public static final ComplexNumber ZERO = new ComplexNumber (0);
    public static final ComplexNumber NEG_ONE = new ComplexNumber (-1);
    public static final ComplexNumber INFINITY = new ComplexNumber(Double.POSITIVE_INFINITY,
            Double.POSITIVE_INFINITY);

    public ComplexNumber (double a, double b)
    {
        this.a = a;
        this.b = b;
    }

    public ComplexNumber (double a)
    {
        this.a = a;
        this.b = 0.;
    }

    public ComplexNumber (final ComplexNumber z)
    {
        this.a = z.a;
        this.b = z.b;
    }

    public void set(ComplexNumber z){
        this.a = z.a;
        this.b = z.b;
    }

    public double re ()
    {
        return a;
    }

    public double im ()
    {
        return b;
    }

    public ComplexNumber add (ComplexNumber z)
    {
        return new ComplexNumber (a + z.a, b + z.b);
    }

    public ComplexNumber sub (ComplexNumber z)
    {
        return new ComplexNumber (a - z.a, b - z.b);
    }

    public ComplexNumber mult (ComplexNumber z)
    {
        return new ComplexNumber (a * z.a - b * z.b, a * z.b + b * z.a);
    }

    public static ComplexNumber conj (ComplexNumber z)
    {
        return new ComplexNumber (z.a, -1 * z.b);
    }

    public static ComplexNumber cos(ComplexNumber z) {
        return new ComplexNumber(Math.cos(z.a)*Math.cosh(z.b), -Math.sin(z.a)*Math.sinh(z.b));
    }

    public static ComplexNumber sin(ComplexNumber z) {
        return new ComplexNumber(Math.sin(z.a)*Math.cosh(z.b), Math.cos(z.a)*Math.sinh(z.b));
    }

    public ComplexNumber div (double d)
    {
        if (d == 0)
        {
            //System.err.println("ERROR: Tried to divide " + this.toString() + " by 0!");
            return INFINITY ;
        }
        return new ComplexNumber(a / d, b / d);
    }

    public ComplexNumber div (ComplexNumber z)
    {
        ComplexNumber answer = this.mult(conj(z));
        return answer.div(norm(z));
    }

    public double norm ()
    {
        return this.a * this.a + this.b * this.b;
    }

    public static double norm (ComplexNumber z)
    {
        return z.a * z.a + z.b * z.b;
    }

    public double mod ()
    {
        return Math.sqrt(norm(this));
    }

    public static double dist (ComplexNumber z, ComplexNumber w)
    {
        return (z.sub(w)).mod();
    }

    public double arg ()
    {
        if (norm(this) <= 1e-8)
            return 0;
        return Math.atan(b / a);
    }


    public String toString ()
    {
        return a + " + (" + b + ")i";
    }

}