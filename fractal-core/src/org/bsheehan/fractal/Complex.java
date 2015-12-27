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
	public double mag = 0;
	/** Construct a Complex */
	public Complex(double r, double i) {
		this.r = r;
		this.i = i;
	}

	public Complex(Complex z) {
		this.r = z.r;
		this.i = z.i;
	}

	/** Display the current Complex as a String, for use in
	 * println() and elsewhere.
	 */
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer().append(this.r);
		//if (this.i>0)
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

	/** set values **/
	public void setValues(Complex z){
		this.r = z.r;
		this.i = z.i;
	}

	/** Return the magnitude of a complex number */
	public double magnitude() {
		//return Math.sqrt(square());
		return mag;
	}

	/** Return the magnitude of a complex number */
	public double magnitude2() {
		double squaredR = this.r*this.r;
		double squaredI = this.i*this.i;
		mag = squaredR + squaredI;
		return mag;
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
	public Complex multiply(Complex z) {
		double r = this.r;
		this.r = this.r*z.r - this.i*z.i;
		this.i = r*z.i + this.i*z.r;
		return this;
	}

	public Complex multiplyAdd(Complex z, Complex c) {
		double r = this.r;
		this.r = this.r*z.r - this.i*z.i +c.r;
		this.i = r*z.i + this.i*z.r + c.i;

		return this;
	}

	/** Multiply this Complex times another one
	 */
	public Complex squared() {

		double squaredR = this.r*this.r;
		double squaredI = this.i*this.i;
		mag = squaredR + squaredI;
		this.i = 2.0*this.r*this.i;
		this.r = squaredR - squaredI;
		return this;
	}

	public Complex cubed() {

		double oldR = this.r;
		double oldI = this.i;
		return this.squared().multiply(new Complex(oldR, oldI));
	}

	public Complex cubedAdd(Complex c) {

		double oldR = this.r;
		double oldI = this.i;
		return this.squared().multiply(new Complex(oldR, oldI)).add(c);
	}

	public Complex squaredAdd(Complex c) {

		this.squaredR = this.r*this.r;
		this.squaredI = this.i*this.i;
		this.mag = this.squaredR + this.squaredI;
		double r2 = this.squaredR - this.squaredI  + c.r;
		this.i = 2.0*this.r*this.i + c.i;
		this.r = r2;

		return this;
	}


	/** Multiply this Complex times another one - used for burning ship fractal
	 */
	public Complex squaredAbs() {
		final double r1 = Math.abs(this.r);
		final double i1 =  Math.abs(this.i);
		this.squaredR = r1*r1;
		this.squaredI = i1*i1;
		this.r = this.squaredR - this.squaredI;
		this.i = 2.0*r1*i1;
		return this;
	}

	/** Multiply this Complex times another one
	 */
	public Complex quarticAdd(Complex c) {
		this.squared().squared();
		//this.mag = this.squaredR + this.squaredI;
		this.r += c.r;
		this.i += c.i;
		return this;
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
