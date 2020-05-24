import spinal.core._

case class DivUnit(width: Int) extends BlackBox {
  val io = new Bundle {
    val a = in Bits(width bit)
    val b = in Bits(width bit)
    val c = out Bits(width bit)
  }

  noIoPrefix()
}

object DivUnit {
//  def apply[T <: Data](a: T, b: T): T = {
//    require(a.getBitsWidth == b.getBitsWidth)
//    val div_unit = DivUnit(a.getBitsWidth)
//    div_unit.io.a := a.asBits
//    div_unit.io.b := b.asBits
//    div_unit.io.c.asInstanceOf[T]
//  }
  def apply(a: UFix, b: UFix): UFix = {
    require(a.getBitsWidth == b.getBitsWidth)
    val div_unit = DivUnit(a.getBitsWidth)
    div_unit.io.a := a.asBits
    div_unit.io.b := b.asBits
    val ret = cloneOf(a)
    ret.assignFromBits(div_unit.io.c)
    ret
  }
  def apply(a: SFix, b: SFix): SFix = {
    require(a.bitCount == b.bitCount)
    val div_unit = DivUnit(a.bitCount)
    div_unit.io.a := a.asBits
    div_unit.io.b := b.asBits
    val ret = cloneOf(a)
    ret.assignFromBits(div_unit.io.c)
    ret
  }
}
