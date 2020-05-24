import spinal.core._

case class MulUnit(
                  width: Int
                  ) extends BlackBox {
  val io = new Bundle {
    val a = in Bits(width bit)
    val b = in Bits(width bit)
    val c = out Bits(width bit)
  }

  noIoPrefix()
  addRTLPath("rtl/core.MulUnit.v")
}

object MulUnit {
  def apply(a: UInt, b: UInt): UInt = {
    require(a.getWidth == b.getWidth)
    val width = a.getWidth
    val mul_unit = MulUnit(width)
    mul_unit.io.a <> a.asBits
    mul_unit.io.b <> b.asBits
    mul_unit.io.c.asUInt
  }
  def apply(a: UFix, b: UFix): UFix = {
    require(a.getBitsWidth == b.getBitsWidth)
    val mul_unit = MulUnit(a.getBitsWidth)
    mul_unit.io.a := a.asBits
    mul_unit.io.b := b.asBits
    val ret = cloneOf(a)
    ret.assignFromBits(mul_unit.io.c)
    ret
  }
  def apply(a: SFix, b: SFix): SFix = {
    require(a.bitCount == b.bitCount)
    val mul_unit = MulUnit(a.bitCount)
    mul_unit.io.a := a.asBits
    mul_unit.io.b := b.asBits
    val ret = cloneOf(a)
    ret.assignFromBits(mul_unit.io.c)
    ret
  }
}
