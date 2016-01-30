package controllers

import java.awt.geom.Rectangle2D
import com.google.inject.Inject
import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal.IterableFractalFactory.FractalType
import org.bsheehan.fractal.equation.EquationFactory.EquationType
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, AnyContent, Controller}
import services.IFractalService
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.libs.json._

class Application @Inject() (fractalService:IFractalService) extends Controller {

  def index = Action {
    Ok(views.html.index("FractalScope"))
  }

  def equations: Action[AnyContent] = Action.async {
    implicit request => {
      val queryString: Map[String, Seq[String]] = request.queryString;
      val id: String = queryString.get("id").get.head
      val fractalType: FractalType = FractalType.get(id.toInt)
      val future: Future[Map[String, ListBuffer[JsValue]]] = fractalService.getEquations(fractalType)
      future.map(equations => Ok(Json.toJson(equations)).as("application/json"))
    }
  }
  
  def fractals: Action[AnyContent] = Action.async {
      implicit request => {
        val future: Future[Map[String, ListBuffer[JsValue]]] = fractalService.getFractals
        future.map(fractals => Ok(Json.toJson(fractals)).as("application/json"))
    }
  }

  def colors: Action[AnyContent] = Action.async {
    implicit request => {
      val future: Future[Map[String, ListBuffer[JsValue]]] = fractalService.getColors
      future.map(colors => Ok(Json.toJson(colors)).as("application/json"))
    }
  }

//  def color = Action {
//    implicit request =>{
//      val json = request.body.asJson.get
//      val id: JsValue = json \ "id"
//
//      val color: ColorSetType = ColorSetType.values()(id.as[String].toInt)
//
//      //noop for now TODO cleanup
//      Ok("all good")
//    }
//  }

  def fractal = Action.async {

    implicit request => {

      val json = request.body.asJson.get
      val region = json \ "region"
      val imageSize = json \ "size"
      val julia: JsValue = json \ "julia"
      val colorId: JsValue = json \ "colorId"
      val equationId: JsValue = json \ "equationId"
      val id: JsValue = json \ "id"
      var rect: Rectangle2D.Double = new Rectangle2D.Double()
      if (region != JsNull) {
        rect = new Rectangle2D.Double(
          (region \ "x").as[Double],
          (region \ "y").as[Double],
          (region \ "w").as[Double],
          (region \ "h").as[Double])
      }
      val imageWidth: Int = (imageSize \ "w").as[Int]
      val imageHeight: Int = (imageSize \ "h").as[Int]
      val cX: Double = (julia \ "x").as[Double]
      val cY: Double = (julia \ "y").as[Double]

      val colorSetId: ColorSetType = ColorSetType.values()(colorId.as[String].toInt)
      val equationType: EquationType = EquationType.values()(equationId.as[String].toInt)
      val fractalType: FractalType = FractalType.get(id.as[String].toInt)
      val isJulia: Boolean = julia.as[JsObject].fields.size != 0

      val future: Future[Map[String, String]] = fractalService.createFractal(fractalType,
        equationType,
        colorSetId,
        rect,
        isJulia,
        imageWidth,
        imageHeight,
        cX,
        cY)
      future.map(result => Ok(Json.toJson(result)).as("application/json"))
    }
  }
}