import scala.collection.mutable._
import spinal.core._
import spinal.lib.dsptool._
import spinal.lib.experimental.math._

case class TwiddleFactorTable
(
  length: Int
)
{
  val t = ( math.log(length) / math.log(2) ).asInstanceOf[Int]
  require(length == math.pow(2, t).asInstanceOf[Int])

  val twiddle_factor = ArrayBuffer.fill(length - 1, 2)(0d)
  var L = 2
  var k = 0

  while(L <= length) {
    val theta = 2 * math.Pi / L
    for (j <- 0 until L/2) {
      twiddle_factor(k)(0) = math.cos(j*theta)
      twiddle_factor(k)(1) = -math.sin(j*theta)
      k += 1
    }
    L *= 2
  }
}

object TwiddleFactorTable {
  def getw(length: Int, cfg: HComplexConfig): Vec[HComplex] = {
    val twf = TwiddleFactorTable(length).twiddle_factor
    val table = Vec(HComplex(cfg), length-1)

    table.zipWithIndex foreach { case(dat, idx) =>
      dat.real := twf(idx)(0)
      dat.imag := twf(idx)(1)
    }

    table
  }
}
