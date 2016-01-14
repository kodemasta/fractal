package org.bsheehan.fractal;

import java.nio.ByteBuffer;


/**
 * @author bsheehan@baymoon.com
 * @date April 12, 2011
 * 
 * @name IFractalImage
 * @description Interface for fractal generator implementors
 */
public interface IFractalImage {

	/** Set the display resolution **/
	public void setDims(int currDim, int currDim2);

	/** Generate an RGB buffer from the iterated function for this fractal **/
	public boolean generate(boolean julia);

	/** Assign RGB values for the calculated array of iteration values for this fractal **/
	public void assignColors();

	/** Set a color mapping for this fractal. This results in the displayed color palette. **/

	void setColorSet(ColorSet colorSet);

		/**
         * Return pixel height of generated fractal
         * @return
         */
	public int getHeight();

	/** Retrieve the RGB buffer values for use in texture mapping **/
	public ByteBuffer getBufferColors();

	/**
	 * Return pixel width of generated fractal
	 * @return
	 */
	public int getWidth();


	/** Get the iterated fractal method for use when generating fractals **/
	public IterableFractal getIterableFractal();


}
