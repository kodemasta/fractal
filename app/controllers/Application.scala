package controllers

import java.awt.Point
import java.awt.image.{BufferedImage, DataBufferByte, Raster}
import java.nio.ByteBuffer
import java.util.Base64
import javax.imageio.ImageIO

import org.bsheehan.fractal.Fractal
import org.bsheehan.fractal.IFractal
import org.bsheehan.fractal.IteratedFunctionFactory
import org.bsheehan.fractal.IteratedFunctionFactory.FractalType
import org.bsheehan.fractal.IteratedFunctionFactory.FractalType
import org.bsheehan.fractal._
import play.api.libs.Files
import play.api.libs.Files.TemporaryFile
import play.api.mvc.{Action, Controller}


object Application extends Controller {
  def index = Action {
    Ok(views.html.index("Fractals 'R' Us"))
  }

  def image = Action {

    def createFractal(fractalType: IteratedFunctionFactory.FractalType): IFractal = {
      val fractal: IFractal = new Fractal(IteratedFunctionFactory.createIteratedFunction(fractalType))
      return fractal
    }

    val fractal:IFractal = createFractal(FractalType.MANDELBROT)

    fractal.setDims(400,400)
    fractal.setRandomColorSet()
    fractal.generate()
    fractal.assignColors()
    val buffer: ByteBuffer = fractal.getBufferColors
    buffer.rewind()
    val size = buffer.remaining()
    val byteArray :Array[Byte] = new Array[Byte](size)
    buffer.get(byteArray);

    val img = new BufferedImage(400,400, BufferedImage.TYPE_3BYTE_BGR)
    img.setData(Raster.createRaster(img.getSampleModel(), new DataBufferByte(buffer.array, size), new Point() ) );

    val temp: TemporaryFile = TemporaryFile.apply("fractal",".jpg")

    ImageIO.write(img, "jpg", temp.file); // Write the Buffered Image into an output file

    val source = scala.io.Source.fromFile(temp.file)(scala.io.Codec.ISO8859)
    val bytes= source.map(_.toByte).toArray
    source.close()

    temp.clean()

    val encodedBytes = Base64.getEncoder.encode( bytes)

    Ok(encodedBytes)
  }
}