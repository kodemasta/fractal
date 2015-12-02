package controllers

import java.awt.Point
import java.awt.geom.Rectangle2D
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.nio.ByteBuffer
import java.util
import java.util.Base64
import javax.imageio.ImageIO

import com.fasterxml.jackson.databind.JsonNode
import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal._
import org.bsheehan.fractal.IteratedFunctionFactory.FractalType
import play.api.libs.Files.TemporaryFile
import play.api.libs.json.{Json, JsPath, Writes, JsValue}
import play.api.mvc.{Action, Controller}

import play.api.libs.json._
import services.FractalService
import scala.collection.JavaConverters._

// IMPORTANT import this to have the required tools in your scope
import play.api.libs.json._
// imports required functional generic structures
import play.api.libs.functional.syntax._

object Application extends Controller {

  val colorSetType = ColorSetType.COLORMAP_RANDOM;
  val randomColorSet:ColorSet = new ColorSet(0, colorSetType)

  val fractalService = FractalService

  def index = Action {
    Ok(views.html.index("Fractalator"))
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

        var js = Json.obj()
        fractalInfos.asScala.toList.foreach (node => {
          val mandelbrot = models.Fractal(node.`type`.ordinal(), node.config.getFractalRegion)
          js = Json.obj(
            "id" -> mandelbrot.id,
            "region" -> Json.toJson(mandelbrot.region)
          )

        })
        Ok(js).as("application/json")
    }

  }

    def color = Action {
      implicit request =>{
        randomColorSet.setColorSet(colorSetType)
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
      val imageSize = json \ "size"

      val rect = new Rectangle2D.Double((region \ "x").as[Double],
        (region \ "y").as[Double],
        (region \ "w").as[Double],
        (region \ "h").as[Double])

      val fractal: IFractal = createFractal(FractalType.MANDELBROT)

      fractal.getFractalFunction.getConfig.setFractalRegion(rect)

      fractal.setDims((imageSize \ "w").as[Int], (imageSize \ "h").as[Int])
      fractal.generate()
      randomColorSet.setMaxIterations(fractal.getFractalFunction.getConfig.getMaxIterations)
      fractal.setColorSet(randomColorSet)
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