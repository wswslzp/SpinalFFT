import spinal.core._
import scala.language.postfixOps

case class HComplex(config:HComplexConfig) extends Bundle {
  import MyUFix.toMyUFix
  import MySFix.toMySFix
  val dw = config.intw + config.fracw
//  val resizeRange = ( config.intw + config.fracw*2 - 1) downto config.fracw
//  val real, imag = UInt(dw bit)//.tag(SQ(config.intw, config.fracw))
  val real, imag = SFix(( config.intw - 1) exp, -config.fracw exp)
  val q_format = SQ(config.getDataWidth, config.fracw)

  def +(that: HComplex): HComplex = {
    require(this.config == that.config)
    val result = HComplex(this.config)
    result.real := this.real + that.real
    result.imag := this.imag + that.imag
    result
  }

  def -(that: HComplex): HComplex = {
    require(this.config == that.config)
    val result = HComplex(this.config)
    result.real := this.real - that.real
    result.imag := this.imag - that.imag
    result
  }

  def >>(that: UInt): HComplex = {
    val ret = HComplex(this.config)
    ret.real.assignFromBits(( real.asBits.asSInt |>> that ).asBits)
    ret.imag.assignFromBits(( imag.asBits.asSInt |>> that ).asBits)
    ret
  }

  def +(that: SInt): HComplex = {
    val ret = HComplex(this.config)
//    ret.real := ( this.real + that ).resize(this.config.getDataWidth)
    ret.real := (this.real + that.toSFix).fixTo(this.q_format)
    ret.imag := this.imag
    ret
  }

  def *(that: HComplex)(implicit use_synthesizable_mul: Boolean = true): HComplex = {
    require(this.config == that.config)
    if (!use_synthesizable_mul) {
      val result = HComplex(this.config)
      if(config.useGauss) {
        val k1 = ( (this.real + this.imag) * that.real ).fixTo(q_format)
        val k2 = ( (that.imag - that.real) * this.real ).fixTo(q_format)
        val k3 = ( (that.real + that.imag) * this.imag ).fixTo(q_format)
        result.real := k1 - k3
        result.imag := k1 + k2
        result
      }else{
        result.real := ( this.real * that.real - this.imag * that.imag ).fixTo(q_format)
        result.imag := ( this.real * that.imag + this.imag * that.real ).fixTo(q_format)
        result
      }
    }
    else {
      val result = HComplex(this.config)
      if(config.useGauss) {
        val k1 = MulUnit(this.real + this.imag, that.real).fixTo(q_format)
        val k2 = MulUnit(that.imag - that.real, this.real).fixTo(q_format)
        val k3 = MulUnit(that.real + that.imag, this.imag).fixTo(q_format)
        result.real := k1 - k3
        result.imag := k1 + k2
        result
      }else{
        result.real := (MulUnit(this.real, that.real) - MulUnit(this.imag, that.imag)).fixTo(q_format)
        result.imag := (MulUnit(this.real, that.imag) + MulUnit(this.imag, that.real)).fixTo(q_format)
        result
      }
    }
  }

  def /(that: SFix): HComplex = {
    // complex number divide by the real number
    val ret = HComplex(this.config)
    ret.real := DivUnit(this.real, that)
    ret.imag := DivUnit(this.imag, that)
    ret
  }

  def :=(that: HComplex): Unit = {
    this.real := that.real
    this.imag := that.imag
  }

  def :=(that: Bits): Unit = {
    this := HComplex(this.config, that.resize(this.config.getComplexWidth))
  }
}

object HComplex {
  def apply(config: HComplexConfig, bits: Bits): HComplex = {
    val tmp = new HComplex(config)
    val dw = config.intw + config.fracw
    if (config.real_high) {
//      tmp.real := bits(dw*2-1 downto dw).asUInt
//      tmp.imag := bits(dw-1 downto 0).asUInt
      tmp.real.assignFromBits(bits(dw*2-1 downto dw))
      tmp.imag.assignFromBits(bits(dw-1 downto 0))
      tmp
    } else {
//      tmp.imag := bits(dw*2-1 downto dw).asUInt
//      tmp.real := bits(dw-1 downto 0).asUInt
      tmp.imag.assignFromBits(bits(dw*2-1 downto dw))
      tmp.real.assignFromBits(bits(dw-1 downto 0))
      tmp
    }
  }
}
