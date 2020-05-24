import spinal.core._
import spinal.lib._

case class MyFFT(length: Int, cfg: HComplexConfig) extends Component {
  implicit val use_synthesizable_mul = false
  val io = new Bundle {
    val data_in = in( Flow( Vec(HComplex(cfg), length) ) )
    val data_out = out ( Flow( Vec(HComplex(cfg), length) ) )
  }

  val data_in: Vec[HComplex] = io.data_in.toReg()

  val twiddle_factor_table: Vec[HComplex] = TwiddleFactorTable.getw(length, cfg)

  val data_reorder: Vec[HComplex] = cloneOf(data_in)
  private def bitReverse(dat: Int, width: Int): Int = {
    var ret: Int = 0
    var dat_bin = dat.toBinaryString
    dat_bin = ( "0" * (width - dat_bin.length) + dat_bin )//.reverse
    dat_bin.zipWithIndex.foreach{case(c, i) =>
      ret += c.toString.toInt << i
    }
    val s = s"dat = ${dat}, width = ${width}, dat_bin = ${dat_bin.toString.reverse}, ret = ${ret.toString}"
    SpinalInfo(s)
    ret
  }

  data_in.zipWithIndex.foreach { case(dat, idx) =>
    // Important note: When Vec's address is UInt, the data to write in cannot be Int
    // Unless it's declare as Reg!!!! Otherwise, LATCH will be detected!!!
    data_reorder(bitReverse(idx, log2Up(length))) := dat
  }

  private def countUpFrom(cond: Bool, range: Range, name: String = "", step: Int=1) = new Area {
    // cond is a one-cycle impulse, when the cond is active, counter inside will
    // count up from cond's falling edge to a specific number(0 until x)
    // useful tool for scheduling the task.
    val cnt: Counter = Counter(range)
    val cond_period_minus_1: Bool = Reg(Bool()) init(False)
    when(cond) {
      cond_period_minus_1 := True
    }.elsewhen(cnt.willOverflow) {
      cond_period_minus_1 := False
    }
    val cond_period: Bool = cond | cond_period_minus_1
    when(cond_period) {
      // TODO: the counter in spinal lib is not support step increment
      // DO NOT USE delta factor
      for(_ <- 0 until step) {cnt.increment()}
    }/*.otherwise{
    cnt.clear()
  }*/
  }.setWeakName(name)

  val data_mid: Vec[HComplex] = Reg(cloneOf(data_in))
//  val current_level = Reg(UInt(log2Up(log2Up(length + 1)) bit)) init 0
  val current_level = countUpFrom(
    RegNext(io.data_in.valid), 0 until log2Up(length)+1, "current_level"
  ).cnt

  val LL =  (1 to log2Up(length)).map(1 << _)//.map(U(_))
  val rr =  LL.map(length / _)
  val LL2 =  LL.map(_ / 2)

  when(current_level === 0) {
    data_mid := data_reorder
  } otherwise {
      for {
        q <- 0 until log2Up(length)
        l = LL(q)
        r = rr(q)
        l2 = LL2(q)
        k <- 0 until r
        j <- 0 until l2
      } {
        when(current_level === (q+1)) {
          // LONG critical timing path!! need further pipeline
          val tmp = twiddle_factor_table(l2 - 1 + j) * data_mid(k*l + j + l2)
          data_mid(k*l + j + l2) := (data_mid(k*l + j) - tmp) >> U(1)
          data_mid(k*l + j) := (data_mid(k*l + j) + tmp) >> U(1)
        }
      }
  }

//  io.data_out.valid := current_level.willOverflow
  io.data_out.valid := RegNext(current_level.willOverflow)
  io.data_out.payload := data_mid

}

object MyFFT extends App{
  val len = 128
  val cfg = HComplexConfig(
    8, 8, useGauss = false
  )
  SpinalVerilog(MyFFT(len, cfg))
}
