package controllers

import java.awt.Point
import java.awt.geom.Rectangle2D
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.nio.ByteBuffer
import java.util
import java.util.Base64
import javax.imageio.ImageIO

import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal.IterableFractalFactory.FractalType
import org.bsheehan.fractal._
import org.bsheehan.fractal.equation.EquationFactory.EquationType
import org.bsheehan.fractal.equation.complex.ComplexNumber
import org.bsheehan.fractal.equation.{Equation, EquationFactory}
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

  val colorMapCache:mutable.Map[ColorSetType,ColorSet] = new mutable.HashMap[ColorSetType,ColorSet];
  colorMapCache.put(ColorSetType.COLORMAP_EARTH, new ColorSet(2048, ColorSetType.COLORMAP_EARTH))
  colorMapCache.put(ColorSetType.COLORMAP_SUNSET, new ColorSet(2048, ColorSetType.COLORMAP_SUNSET))
  colorMapCache.put(ColorSetType.COLORMAP_GRAY, new ColorSet(2048, ColorSetType.COLORMAP_GRAY))
  colorMapCache.put(ColorSetType.COLORMAP_BINARY, new ColorSet(2048, ColorSetType.COLORMAP_BINARY))
  colorMapCache.put(ColorSetType.COLORMAP_BLACK, new ColorSet(2048, ColorSetType.COLORMAP_BLACK))
  colorMapCache.put(ColorSetType.COLORMAP_WHITE, new ColorSet(2048, ColorSetType.COLORMAP_WHITE))
  colorMapCache.put(ColorSetType.COLORMAP_RGB, new ColorSet(2048, ColorSetType.COLORMAP_RGB))
  colorMapCache.put(ColorSetType.COLORMAP_CMY, new ColorSet(2048, ColorSetType.COLORMAP_CMY))
  colorMapCache.put(ColorSetType.COLORMAP_FIRE, new ColorSet(2048, ColorSetType.COLORMAP_FIRE))
  colorMapCache.put(ColorSetType.COLORMAP_SNOW, new ColorSet(2048, ColorSetType.COLORMAP_SNOW))
  colorMapCache.put(ColorSetType.COLORMAP_GRAY2, new ColorSet(2048, ColorSetType.COLORMAP_GRAY2))

  val fractalService = FractalService

  def index = Action {
    Ok(views.html.index("FractalImage Scope"))
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

  def equations = Action {
    implicit request => {
      val queryString: Map[String, Seq[String]] = request.queryString;
      val id: String = queryString.get("id").get.head

      val fractalType: FractalType = FractalType.get(id.toInt)

      val eqnMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
      val eqns: util.List[Equation] = EquationFactory.getEquations(fractalType);
      eqns.asScala.toList.foreach (node => {
        val eqnMap: Map[String, String] = Map(
          "id" -> node.getType.ordinal().toString,
          "name" -> node.toString
        )
        eqnMapList.+=:(Json.toJson(eqnMap))
      })

      val eqnMapListMap: Map[String, ListBuffer[JsValue]] =  Map("equations" -> eqnMapList)
      Ok(Json.toJson(eqnMapListMap)).as("application/json")
    }
  }
  
  def fractals = Action {
      implicit request => {

        val fractalMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
        val iterableFractals: util.List[IterableFractal] = IterableFractalFactory.getFractals();
        iterableFractals.asScala.toList.foreach (node => {
          //val fractal = models.FractalImage(node.getInfo.`type`.ordinal().toString, node.getInfo.name, node.getInfo.description)

          val fractalMap: Map[String, String] = Map(
            "id" -> node.getInfo.`type`.getValue().toString,
            //"parentId" -> fractal.parentId,
            //"region" -> Json.toJson(fractal.region).toString(),
            "name" -> node.getInfo.name,
            "description" -> node.getInfo.description
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

      def createFractal(fractalType: IterableFractalFactory.FractalType, equationType: EquationType): IFractalImage = {
        //todo use fractal factory
        //if (fractalType.equals(FractalType.NEWTON))
        //  return new org.bsheehan.fractal.NewtonFractalImage(IterableFractalFactory.createIterableFractal2(fractalType, equationType))
        //else
          return new org.bsheehan.fractal.FractalImage(IterableFractalFactory.createIterableFractal2(fractalType, equationType))
      }

      val json = request.body.asJson.get
      val region = json \ "region"
      val imageSize = json \ "size"
      val julia: JsValue = json \ "julia"
      val colorId: JsValue = json \ "colorId"
      val equationId: JsValue = json \ "equationId"
      val id: JsValue = json \ "id"

      val colorSetId: ColorSetType = ColorSetType.values()(colorId.as[String].toInt)
      val equationType: EquationType = EquationType.values()(equationId.as[String].toInt)
      val fractalType: FractalType = FractalType.get(id.as[String].toInt)

      val fractal: IFractalImage = createFractal(fractalType, equationType)
      var rect:Rectangle2D.Double = null;
      if (region != JsNull) {
         rect = new Rectangle2D.Double((region \ "x").as[Double],
          (region \ "y").as[Double],
          (region \ "w").as[Double],
          (region \ "h").as[Double])
          fractal.getIterableFractal.getInfo.config.setFractalRegion(rect)
      }else {
        //special case Quadratic Mandelbrot !
        if (fractalType.equals(FractalType.MANDELBROT) && equationType.equals(EquationType.QUADRATIC)) {
          rect = new Rectangle2D.Double(-2.0, -1.5, 3.0, 3.0);
          fractal.getIterableFractal.getInfo.config.setFractalRegion(rect);
        }
        else
          rect = fractal.getIterableFractal.getInfo.config.getFractalRegion
      }

      if (julia.as[JsObject].fields.size != 0)
        fractal.getIterableFractal.getInfo.config.zConstant = new ComplexNumber((julia \ "x").as[Double], (julia \ "y").as[Double]);

      fractal.setDims((imageSize \ "w").as[Int], (imageSize \ "h").as[Int])
      if (fractalType == FractalType.JULIA || fractalType == FractalType.NEWTON)
        fractal.generate(true);
      else
        fractal.generate(false);

      fractal.setColorSet(colorMapCache.get(colorSetId).get)
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

      val result: Map[String, String] = Map(
        "image" -> Base64.getEncoder.encodeToString(bytes),
        "region" -> Json.toJson(rect).toString()
      )

      Ok(Json.toJson(result)).as("application/json")
    }
  }
}