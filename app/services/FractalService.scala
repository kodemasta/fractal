package services

import java.awt.geom.Rectangle2D

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.google.inject.ImplementedBy
import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal.IterableFractalFactory.FractalType
import org.bsheehan.fractal.equation.EquationFactory.EquationType
import play.api.libs.json.JsValue

import scala.collection.mutable.ListBuffer
import scala.concurrent.Future
import scala.concurrent.duration._

/**
 * Created by bob on 11/28/15.
 */

class FractalService extends IFractalService {
  val actorsystem:ActorSystem = ActorSystem.create("fractal-actor-system")
  implicit val askTimeout = Timeout(20 seconds)
  val fractor = actorsystem.actorOf(Props(new Fractor()), name = "fractor")

  def getFractals: Future[Map[String, ListBuffer[JsValue]]] = {
    fractor.ask(GetFractals).mapTo[Map[String, ListBuffer[JsValue]]]
  }

  def getColors : Future[Map[String, ListBuffer[JsValue]]] = {
    fractor.ask(GetColors).mapTo[Map[String, ListBuffer[JsValue]]]
  }

  def getEquations(fractalType: FractalType): Future[Map[String, ListBuffer[JsValue]]] = {
    fractor.ask(GetEquations(fractalType)).mapTo[Map[String, ListBuffer[JsValue]]]
  }

  def createFractal(fractalType: FractalType,
                    equationType:EquationType,
                    colorSetId: ColorSetType,
                    rect:Rectangle2D.Double,
                    isJulia:Boolean,
                    imageWidth:Int,
                    imageHeight:Int,
                    cX:Double,
                    cY:Double): Future[Map[String, String]] = {
    fractor.ask(CreateFractal(fractalType,
      equationType,
      colorSetId,
      rect,
      isJulia,
      imageWidth,
      imageHeight,
      cX,
      cY)).mapTo[Map[String, String]]
  }
}

@ImplementedBy(classOf[FractalService])
trait IFractalService {
  def getFractals: Future[Map[String, ListBuffer[JsValue]]];
  def getColors : Future[Map[String, ListBuffer[JsValue]]];
  def getEquations(fractalType: FractalType): Future[Map[String, ListBuffer[JsValue]]];
  def createFractal(fractalType: FractalType,
                    equationType:EquationType,
                    colorSetId: ColorSetType,
                    rect:Rectangle2D.Double,
                    isJulia:Boolean,
                    imageWidth:Int,
                    imageHeight:Int,
                    cX:Double,
                    cY:Double): Future[Map[String, String]];
}
