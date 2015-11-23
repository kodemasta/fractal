package org.bsheehan.fractal;

/** 
 * NOT WRITTEN BY BSHEEHAN@BAYMOON.COM. Some edits were made however.
 * 
 * A class to represent Complex Numbers. A Complex object is
 * immutable once created; the add, subtract and multiply routines
 * return newly-created Complex objects containing the results.
 *
 * @author Ian F. Darwin, inspired by David Flanagan.
 * @version $Id: Complex.java,v 1.3 2004/05/13 22:28:59 ian Exp $
 **/
public class Complex {

	/** The real part */
	public double r;
	/** The imaginary part */
	public double i;
	private double squaredR = 0;
	private double squaredI = 0;

	/** Construct a Complex */
	public Complex(double rr, double ii) {
		this.r = rr;
		this.i = ii;
	}

	/** Display the current Complex as a String, for use in
	 * println() and elsewhere.
	 */
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer().append(this.r);
		if (this.i>0)
			sb.append('+');  // else append(i) appends - sign
		return sb.append(this.i).append('i').toString();
	}

	/** Return just the Real part */
	public double getReal() {
		return this.r;
	}

	/** Return just the Real part */
	public double getImaginary() {
		return this.i;
	}

	/** set values **/
	public void setValues(double r, double i){
		this.r = r;
		this.i = i;
	}

	/** Return the magnitude of a complex number */
	public double magnitude() {
		return Math.sqrt(square());
	}

	/** Return the magnitude of a complex number */
	public double square() {
		return this.squaredR + this.squaredI;
	}

	/** Add another Complex to this one
	 */
	public Complex add(Complex other) {
		this.r += other.r; 
		this.i += other.i; 
		return this;
	}

	/** Add two Complexes
	 */
	public static Complex add(Complex c1, Complex c2) {
		return new Complex(c1.r+c2.r, c1.i+c2.i);
	}



	/** Subtract another Complex from this one
	 */
	public Complex subtract(Complex other) {
		this.r -= other.r; 
		this.i -= other.i; 
		return this;
	}

	/** Subtract two Complexes
	 */
	public static Complex subtract(Complex c1, Complex c2) {
		return new Complex(c1.r-c2.r, c1.i-c2.i);
	}

	/** Multiply this Complex times another one
	 */
	public Complex multiply(Complex other) {
		final double r1 = this.r;
		final double i1 = this.i;
		final double r2= other.r;
		final double i2 = other.i;
		this.r = r1*r2 - i1*i2;
		this.i = r1*i2 + i1*r2;
		return this;
	}

	/** Multiply this Complex times another one
	 */
	public Complex squared() {
		final double r1 = this.r;
		final double i1 = this.i;
		this.squaredR = r1*r1;
		this.squaredI = i1*i1;
		this.r = this.squaredR - this.squaredI;
		this.i = 2.0*r1*i1;
		return this;
	}

	/** Multiply this Complex times another one
	 */
	public Complex quartic() {
		final double r1 = this.r;
		final double i1 = this.i;
		this.r = r1*r1 - i1*i1;
		this.i = 2.0*r1*i1;
		return this.squared().squared();
	}

	/** Multiply two Complexes
	 */
	public static Complex multiply(Complex c1, Complex c2) {
		return new Complex(c1.r*c2.r - c1.i*c2.i, c1.r*c2.i + c1.i*c2.r);
	}

	/** dived by another complex instance **/
	public Complex divide(Complex other) {
		final double rTemp = this.r;
		final double iTemp = this.i;

		this.r = (rTemp*other.r+iTemp*other.i)/(other.r*other.r+other.i*other.i);
		this.i = (iTemp*other.r-rTemp*other.i)/(other.r*other.r+other.i*other.i);
		return this;
	}

	/** Divide c1 by c2.
	 * @author Gisbert Selke.
	 */
	public static Complex divide(Complex c1, Complex c2) {
		return new Complex(
				(c1.r*c2.r+c1.i*c2.i)/(c2.r*c2.r+c2.i*c2.i),
				(c1.i*c2.r-c1.r*c2.i)/(c2.r*c2.r+c2.i*c2.i));
	}

	/* Compare this Complex number with another
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Complex))
			throw new IllegalArgumentException(
					"Complex.equals argument must be a Complex");
		final Complex other = (Complex)o;
		return this.r == other.r && this.i == other.i;
	}

	/* Generate a hashCode; not sure how well distributed these are.
	 */
	@Override
	public int hashCode() {
		return (int)( this.r) |  (int)this.i;
	}

	/**
	 * main for testing 
	 * @param args
	 */
	public static void main(String[] args) {
		final Complex c = new Complex(3,  5);
		final Complex d = new Complex(2, -2);
		System.out.println(c);
		System.out.println(c + ".getReal() = " + c.getReal());
		System.out.println(c + " + " + d + " = " + c.add(d));
		System.out.println(c + " + " + d + " = " + Complex.add(c, d));
		System.out.println(c + " * " + d + " = " + c.multiply(d));
		System.out.println(Complex.divide(c, d));
	}
}
