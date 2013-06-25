package fr.lumisound

/**
 * Created with IntelliJ IDEA.
 * User: jeremiefourbil
 * Date: 19/06/13
 * Time: 14:45
 * To change this template use File | Settings | File Templates.
 */
object FFT {
  import scala.math._

  case class myComplex(re: Double, im: Double = 0.0) {
    def +(x: myComplex): myComplex = myComplex(re+x.re,im+x.im)
    def -(x: myComplex): myComplex = myComplex(re-x.re,im-x.im)
    def *(x: myComplex): myComplex = myComplex(re*x.re-im*x.im,re*x.im+im*x.re)
  }

  def fft(f: List[myComplex]): List[myComplex] = {
    import Stream._
    require((f.size==0)||(from(0) map {x=>pow(2,x).toInt}).takeWhile(_<2*f.size).toList.exists(_==f.size)==true,"list size "+f.size+" not allowed!")
    f.size match {
      case 0 => Nil
      case 1 => f
      case n => {
        val cis: Double => myComplex = phi => myComplex(cos(phi),sin(phi))
        val e = fft(f.zipWithIndex.filter(_._2%2==0).map(_._1))
        val o  = fft(f.zipWithIndex.filter(_._2%2!=0).map(_._1))
        import scala.collection.mutable.ListBuffer
        val lb = new ListBuffer[Pair[Int, myComplex]]()
        for (k <- 0 to n/2-1) {
          lb += Pair(k,e(k)+o(k)*cis(-2*Pi*k/n))
          lb += Pair(k+n/2,e(k)-o(k)*cis(-2*Pi*k/n))
        }
        lb.toList.sortWith((x,y)=>x._1<y._1).map(_._2)
      }
    }
  }
}
