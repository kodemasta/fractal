package controllers

import java.awt.Point
import java.awt.geom.Rectangle2D
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.nio.ByteBuffer
import java.util
import java.util.Base64
import javax.imageio.ImageIO

import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal.IteratedFunctionFactory.FractalType
import org.bsheehan.fractal._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{JsValue, Json, Writes}
import play.api.mvc.{Action, Controller}
import services.FractalService

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

// IMPORTANT import this to have the required tools in your scope
import play.api.libs.json._
// imports required functional generic structures

object Application extends Controller {

  val colorSetMap:mutable.Map[ColorSetType,ColorSet] = new mutable.HashMap[ColorSetType,ColorSet];
  colorSetMap.put(ColorSetType.COLORMAP_EARTH, new ColorSet(2048, ColorSetType.COLORMAP_EARTH))
  colorSetMap.put(ColorSetType.COLORMAP_SUNSET, new ColorSet(2048, ColorSetType.COLORMAP_SUNSET))
  colorSetMap.put(ColorSetType.COLORMAP_GRAY, new ColorSet(2048, ColorSetType.COLORMAP_GRAY))
  colorSetMap.put(ColorSetType.COLORMAP_BINARY, new ColorSet(2048, ColorSetType.COLORMAP_BINARY))
  colorSetMap.put(ColorSetType.COLORMAP_BLACK, new ColorSet(2048, ColorSetType.COLORMAP_BLACK))
  colorSetMap.put(ColorSetType.COLORMAP_WHITE, new ColorSet(2048, ColorSetType.COLORMAP_WHITE))
  colorSetMap.put(ColorSetType.COLORMAP_RGB, new ColorSet(2048, ColorSetType.COLORMAP_RGB))
  colorSetMap.put(ColorSetType.COLORMAP_CMY, new ColorSet(2048, ColorSetType.COLORMAP_CMY))
  colorSetMap.put(ColorSetType.COLORMAP_FIRE, new ColorSet(2048, ColorSetType.COLORMAP_FIRE))
  colorSetMap.put(ColorSetType.COLORMAP_SNOW, new ColorSet(2048, ColorSetType.COLORMAP_SNOW))
  colorSetMap.put(ColorSetType.COLORMAP_GRAY2, new ColorSet(2048, ColorSetType.COLORMAP_GRAY2))


  val fractalService = FractalService

  def index = Action {
    Ok(views.html.index("Fractal Scope"))
  }

  // JSON converter for Rectangle2D.Float
  implicit object Rectangle2DDoubleWrites extends Writes[Rectangle2D.Double] {
    def writes(r: Rectangle2D.Double) = Json.obj(
      "x" -> JsNumber(r.getX),
      "y" -> JsNumber(r.getY),
      "w" -> JsNumber(r.getWidth),
      "h" -> JsNumber(r.getHeight)
    )
  }

  def fractals = Action {
      implicit request => {

        val fractalInfos: util.List[FractalInfo] = fractalService.getFractals;

        val fractalMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
        fractalInfos.asScala.toList.foreach (node => {
          val fractal = models.Fractal(node.`type`.ordinal().toString, node.`parentType`.ordinal().toString, node.config.getFractalRegion, node.name, node.description)

          val fractalMap: Map[String, String] = Map(
            "id" -> fractal.id,
            "parentId" -> fractal.parentId,
            "region" -> Json.toJson(fractal.region).toString(),
            "name" -> fractal.name,
            "description" -> fractal.description
          )
          fractalMapList.+=:(Json.toJson(fractalMap))
        })

        val fractalMapListMap: Map[String, ListBuffer[JsValue]] =  Map("fractals" -> fractalMapList)
        Ok(Json.toJson(fractalMapListMap)).as("application/json")
    }
  }

  def colors = Action {
    implicit request => {

      val colorInfos: util.List[ColorInfo] = fractalService.getColors;

      val colorInfoMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
      colorInfos.asScala.toList.foreach (node => {
        val color = models.ColorInfo(node.`type`.toString, node.name, node.description)

        val colorInfoMap: Map[String, String] = Map(
          "id" -> color.id,
          "name" -> color.name,
          "description" -> color.description
        )
        colorInfoMapList.+=:(Json.toJson(colorInfoMap))
      })

      val colorInfoMapListMap: Map[String, ListBuffer[JsValue]] =  Map("colors" -> colorInfoMapList)
      Ok(Json.toJson(colorInfoMapListMap)).as("application/json")
    }
  }

    def color = Action {
      implicit request =>{
        val json = request.body.asJson.get
        val id: JsValue = json \ "id"

        val color: ColorSetType = ColorSetType.values()(id.as[String].toInt)

        //noop for now TODO cleanup
        Ok("all good")
      }
    }

    def fractal = Action {
    {
      implicit request =>

      def createFractal(fractalType: IteratedFunctionFactory.FractalType): IFractal = {
        val fractal: IFractal = new org.bsheehan.fractal.Fractal(IteratedFunctionFactory.createIteratedFunction(fractalType))
        return fractal
      }

      val json = request.body.asJson.get
      val region = json \ "region"
      //val region = Json.parse(regionStr.toString())
      val imageSize = json \ "size"
      val julia: JsValue = json \ "julia"
      val colorId: JsValue = json \ "colorId"
      val id: JsValue = json \ "id"

      val colorSetId: ColorSetType = ColorSetType.values()(colorId.as[String].toInt)
      if (colorSetId== null)
        BadRequest("color id not supported " + colorId.as[String])

      val fractalId: FractalType = FractalType.values()(id.as[String].toInt)

      val rect = new Rectangle2D.Double((region \ "x").as[Double],
      (region \ "y").as[Double],
      (region \ "w").as[Double],
      (region \ "h").as[Double])

      val fractal: IFractal = createFractal(fractalId)

      fractal.getFractalFunction.getConfig.setFractalRegion(rect)
      if (julia.as[JsObject].fields.size != 0)
        fractal.getFractalFunction.getConfig.zConstant = new Complex((julia \ "x").as[Double], (julia \ "y").as[Double]);

      fractal.setDims((imageSize \ "w").as[Int], (imageSize \ "h").as[Int])
      if (fractalId ==FractalType.MANDELBROT_CUBIC_JULIA ||
        fractalId==FractalType.MANDELBROT_JULIA ||
        fractalId==FractalType.MANDELBROT_QUARTIC_JULIA)
        fractal.generate(true);
      else
        fractal.generate(false);

      fractal.setColorSet(colorSetMap.get(colorSetId).get)
      fractal.assignColors()
      val buffer: ByteBuffer = fractal.getBufferColors
      buffer.rewind()
      val size = buffer.remaining()
      val byteArray: Array[Byte] = new Array[Byte](size)
      buffer.get(byteArray);

      val img = new BufferedImage((imageSize \ "w").as[Int], (imageSize \ "h").as[Int], BufferedImage.TYPE_3BYTE_BGR)
      img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(buffer.array, size), new Point()));

      val temp: TemporaryFile = TemporaryFile.apply("fractal", ".jpg")

      ImageIO.write(img, "jpg", temp.file); // Write the Buffered Image into an output file

      val source = scala.io.Source.fromFile(temp.file)(scala.io.Codec.ISO8859)
      val bytes = source.map(_.toByte).toArray
      source.close()

      temp.clean()

      val encodedBytes: Array[Byte] = Base64.getEncoder.encode(bytes)

      Ok(encodedBytes)
    }
  }
}