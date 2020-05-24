import spinal.core._

case class MyUFix(width: Int) {
  val bit_vec = UInt(width bit)
  var fraction = 0

  def uq: QFormat = UQ(width, fraction)

  def fixTo(q: QFormat, roundType: RoundType): UFix = {
    require(!q.signed)
    val ret = UFix((q.width - q.fraction) exp, -q.fraction exp)
    ret.assignFromBits(bit_vec.tag(this.uq).fixTo(q, roundType).asBits)
    ret
  }

  def fixTo(q: QFormat): UFix = {
    fixTo(q, getFixRound())
  }
}

object MyUFix {
  implicit def toMyUFix(that: UFix): MyUFix = {
    val ret = new MyUFix(that.bitCount)
    ret.bit_vec := that.raw
    ret.fraction = -that.minExp
    ret
  }
}

case class MySFix(width: Int) {
  val bit_vec = SInt()
  var fraction = 0

  def sq: QFormat = SQ(width, fraction)

  def fixTo(q: QFormat, roundType: RoundType): SFix = {
    require(q.signed)
    val ret = SFix((q.width - q.fraction - 1) exp, -q.fraction exp)// an extra signed bit in integer
    ret.assignFromBits(bit_vec.tag(this.sq).fixTo(q, roundType).asBits)
    ret
  }

  def fixTo(q: QFormat): SFix = {
    fixTo(q, getFixRound())
  }
}

object MySFix {

  implicit def toMySFix(that: SFix): MySFix = {
    val ret = new MySFix(that.bitCount)
    ret.bit_vec := that.raw
    ret.fraction = -that.minExp
    ret
  }

}
