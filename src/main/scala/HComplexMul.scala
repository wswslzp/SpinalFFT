import spinal.core._

case class HComplexMul() extends Component {
  val cfg = HComplexConfig(
    intw = 8, fracw = 8
  )
  val a = in(HComplex(cfg))
  val b = in(HComplex(cfg))
  val c = out(HComplex(cfg))

  implicit val use_synthesizable_mul = false

  c := ( a * b ) >> 1

}

object HComplexMul extends App {
  SpinalVerilog(HComplexMul())
}
