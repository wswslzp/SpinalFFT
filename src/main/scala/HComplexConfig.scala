import spinal.core._

case class HComplexConfig
(
  intw: Int,
  fracw: Int,
  useGauss: Boolean = true,
  real_high: Boolean = false
)
{
  def getDataWidth: Int = intw + fracw
  def getComplexWidth: Int = (intw + fracw) * 2
  def getDataRange: Range = getComplexWidth-1 downto 0
  def getRealRange: Range = if (real_high) {
    getComplexWidth-1 downto getDataWidth
  } else {
    getDataWidth-1 downto 0
  }
  def getImagRange: Range = if (real_high) {
    getDataWidth-1 downto 0
  } else {
    getComplexWidth-1 downto getDataWidth
  }
}
