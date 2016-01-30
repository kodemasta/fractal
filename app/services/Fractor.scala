package services

import java.awt.Point
import java.awt.geom.Rectangle2D
import java.awt.image.{DataBufferByte, Raster, BufferedImage}
import java.nio.ByteBuffer
import java.util
import java.util.Base64
import javax.imageio.ImageIO

import akka.actor.Actor
import akka.event.Logging
import org.bsheehan.fractal.ColorSet.ColorSetType
import org.bsheehan.fractal.IterableFractalFactory.FractalType
import org.bsheehan.fractal.equation.EquationFactory.EquationType
import org.bsheehan.fractal.equation.complex.ComplexNumber
import org.bsheehan.fractal.equation.{EquationFactory, Equation}
import org.bsheehan.fractal._
import play.api.libs.Files.TemporaryFile
import play.api.libs.json._
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
  * Created by bob on 1/27/16.
  */
case class GetFractals()
case class GetColors()
case class GetEquations(fractalType:FractalType)
case class CreateFractal(fractalType:FractalType,
                         equationType:EquationType,
                         colorSetId:ColorSetType,
                         rect:Rectangle2D.Double,
                         isJulia:Boolean,
                         imageWidth:Int,
                         imageHeight:Int,
                         cX:Double,
                         cY:Double)

class Fractor extends Actor {

  // JSON converter for Rectangle2D.Float
  implicit object Rectangle2DDoubleWrites extends Writes[Rectangle2D.Double] {
    def writes(r: Rectangle2D.Double) = Json.obj(
      "x" -> JsNumber(r.getX),
      "y" -> JsNumber(r.getY),
      "w" -> JsNumber(r.getWidth),
      "h" -> JsNumber(r.getHeight)
    )
  }

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

  val log = Logging(context.system, this)

  override def receive: Receive = {
    case GetFractals ⇒ {
      log.info("received GetFractals")
      val iterableFractals: util.List[IterableFractal] = IterableFractalFactory.getFractals

      val fractalMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
      iterableFractals.asScala.toList.foreach (node =>
      {
        val fractalMap: Map[String, String] = Map(
          "id" -> node.getInfo.`type`.getValue().toString,
          "name" -> node.getInfo.name,
          "description" -> node.getInfo.description
        )
        fractalMapList.+=:(Json.toJson(fractalMap))
      })

      sender ! Map("fractals" -> fractalMapList)
    }
    case GetColors ⇒ {
      log.info("received GetColors")
      val colorInfos: util.List[ColorInfo] = ColorSet.getColorInfos
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

      sender ! Map("colors" -> colorInfoMapList)
    }
    case GetEquations(fractalType: FractalType) => {
      log.info("received GetEquations")
      val eqnMapList: ListBuffer[JsValue] = new ListBuffer[JsValue]()
      val eqns: util.List[Equation] = EquationFactory.getEquations(fractalType);
      eqns.asScala.toList.foreach (node => {
        val eqnMap: Map[String, String] = Map(
          "id" -> node.getType.ordinal().toString,
          "name" -> node.toString
        )
        eqnMapList.+=:(Json.toJson(eqnMap))
      })

      sender ! Map("equations" -> eqnMapList)
    }
    case CreateFractal(fractalType:FractalType,
      equationType:EquationType,
      colorSetId:ColorSetType,
      rect:Rectangle2D.Double,
      isJulia:Boolean,
      imageWidth:Int,
      imageHeight:Int,
      cX:Double,
      cY:Double) => {

      log.info("received CreateFractal")

      val fractal: IFractalImage = new org.bsheehan.fractal.FractalImage(IterableFractalFactory.createIterableFractal2(fractalType, equationType))

      var region = rect
      if (!rect.getHeight.equals(0.0)) {
        fractal.getIterableFractal.getInfo.config.setFractalRegion(rect)
      }else {
        //special case Quadratic Mandelbrot !
        if (fractalType.equals(FractalType.MANDELBROT) && equationType.equals(EquationType.QUADRATIC)) {
          region = new Rectangle2D.Double(-2.0, -1.5, 3.0, 3.0);
          fractal.getIterableFractal.getInfo.config.setFractalRegion(region);
        }
        else
          region = fractal.getIterableFractal.getInfo.config.getFractalRegion
      }

      if (isJulia)
        fractal.getIterableFractal.getInfo.config.zConstant = new ComplexNumber(cX,cY);

      fractal.setDims(imageWidth, imageHeight)
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

      val img = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR)
      img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(buffer.array, size), new Point()));

      val temp: TemporaryFile = TemporaryFile.apply("fractal", ".jpg")

      ImageIO.write(img, "jpg", temp.file); // Write the Buffered Image into an output file

      val source = scala.io.Source.fromFile(temp.file)(scala.io.Codec.ISO8859)
      val bytes = source.map(_.toByte).toArray
      source.close()

      temp.clean()

      val encodedBytes: Array[Byte] = Base64.getEncoder.encode(bytes)

      sender ! Map(
        "image" -> Base64.getEncoder.encodeToString(bytes),
        "region" -> Json.toJson(region).toString()
      )
    }
    case _ ⇒ {
      log.error("received unknown message")
    }
  }
}
