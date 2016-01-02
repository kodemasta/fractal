package services

import java.util

import org.bsheehan.fractal._

/**
 * Created by bob on 11/28/15.
 */
object FractalService {

  def getFractals : util.List[IterableFractal] = {
     IterableFractalFactory.getFractals
  }

  def getColors : util.List[ColorInfo] = {
    ColorSet.getColorInfos
  }
}
