package org.bsheehan.fractal;


import java.awt.*;

/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name AbstractFractalFunction
 * @description Base class for iteration functions on the complex plane. 
 */
public abstract class AbstractFractalFunction implements IIteratedFunction {

	protected FractalInfo fractalInfo;

	public FractalInfo getInfo() {
		return fractalInfo;
	}

	/** 
	 * Set the center point of where a particular rectangular region of the function is to be calculated.
	 **/
	public void setCenter(Point.Double center) {
		double offsetX = center.x - (double)fractalInfo.config.fractalRegion.getCenterX();
		double offsetY = center.y - (double)fractalInfo.config.fractalRegion.getCenterY();

		fractalInfo.config.fractalRegion.setRect(fractalInfo.config.fractalRegion.x - offsetX,
				fractalInfo.config.fractalRegion.y - offsetY,
				fractalInfo.config.fractalRegion.getWidth(),
				fractalInfo.config.fractalRegion.getHeight());
	}

	/**
	 * Lifted this optimization off Wikipedia. The central cardioid bulbs in the Mandelbrot set are guaranteed to generate
	 * orbits that converge. This is the least optimal type of iteration that will always hit kMaxIterations.
	 * If we detect a calculation in these bulbs, we can just set the iteration to kMaxIterations and move on.
	 * @param z
	 * @return
	 */
	protected boolean isPointInCardioidBulbs(Complex z) {

		final double term1 = z.r -.25;
		final double term2 = z.i*z.i;
		final double q = term1*term1 + term2;
		return q*(q+term1) < .25*term2;
	}

	/**
	 * Set region zoom about the center point of defined boundary region.
	 */
	public void setZoom(double zoom) {
		final double centerX = (double)fractalInfo.config.fractalRegion.getCenterX();
		final double centerY = (double)fractalInfo.config.fractalRegion.getCenterY();
		final double halfWidth = (double)fractalInfo.config.fractalRegion.getWidth() * zoom * .5f;
		final double halfHeight = (double)fractalInfo.config.fractalRegion.getHeight() * zoom * .5f;

		fractalInfo.config.fractalRegion.setRect(centerX - halfWidth, centerY - halfHeight, halfWidth*2f, halfHeight*2f );
	}
	
	public void setScale(double screenAspectRatio)
	{
		double left =  (double)fractalInfo.config.fractalRegion.getX()*screenAspectRatio;
		double right =  (double)fractalInfo.config.fractalRegion.getY()*screenAspectRatio;

		fractalInfo.config.fractalRegion.setRect(left, fractalInfo.config.fractalRegion.getY(), right-left, fractalInfo.config.fractalRegion.getHeight());
	}

	public FractalConfig getConfig() {
		return fractalInfo.config;
	}

}
