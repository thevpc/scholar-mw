package net.thevpc.scholar.mentoring.ch03_hadruwaves.d_mom

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadrumaths.symbolic.DoubleToVector
import net.thevpc.scholar.hadrumaths.{Expr, InverseStrategy, Maths, ScalarProductOperatorFactory}
import net.thevpc.scholar.hadruwaves.mom.{BoxSpace, MomStructure, ProjectType}

object d_2019_09_19_unitCell_MoM_Analysis {
  Config.setSimplifierCacheSize(1000000);

  def main(args: Array[String]): Unit = {

    Maths.Config.setDefaultMatrixInverseStrategy(InverseStrategy.SOLVE)
    Maths.Config.setScalarProductOperator(ScalarProductOperatorFactory.hardFormal()); // !=quad
    Plot.console()
    // NbModeFunctions
    var MN=20_000;

    // NbEssaiFunctions
    val P: Int = 8
    val Q: Int = 8
    // Paramters
    var n = param("n")
    var m = param("m")
    var p = param("p")
    var q = param("q")
    // Parameters
    var freq = 5.0 * GHZ;
    var wfreq = 2 * PI * freq;
    var lambda=C/freq;
    val epsr = 1.0;
    // cell
    var px=24*MM;
    var py=18*MM;
    var cx=0.75*px;
    var cy=0.5*py;
    var dCell  = domain((-px/2)->(px/2),(-py/2)->(py/2))
    var dPatch = domain((-cx/2)->(cx/2),(-cy/2)->(cy/2))

    val NPatch=sqrt(2.0/(cx*cy));
    val gpqx = (NPatch * sin(p*PI*(X+cx/2)/cx) * cos(q*PI*(Y  + cy/2)/cy)) * dPatch
    val gpqy = (NPatch * cos(p*PI*(X+cx/2)/cx) * sin(q*PI*(Y  + cy/2)/cy)) * dPatch
    val gpq  = vector(gpqx, gpqy);

    // *************************  Essai vector ************************
    var pp = 0;
    var qq = 0;
    var essai =elist();
    // Essai Ligne
    while (pp < P) {
      qq = 0;
      while (qq < Q) {
        if ((pp != 0) || (qq != 0)) {
//          val value: Expr = gpq(p -> pp)(q -> qq)
          essai :+= gpq(p -> pp)(q -> qq);
        }
        //println(" Line: p= "+pp+"   q= "+qq);
        qq += 1;
      }
      pp += 1;
    }
//    Plot.console();
    println("essai number=" +essai.length())
    Plot.title("Essai").domain(dCell).plot(essai)
//    if(true)
//    return
// structure definition: four periodic walls,
    var str=MomStructure.PPPP(dCell,freq,MN,
      BoxSpace.matchedLoad(),
      BoxSpace.matchedLoad())
    str.setProjectType(ProjectType.WAVE_GUIDE)
    str.testFunctions(essai)
    var sparams=dtimes(4*GHZ,6*GHZ,20).map(
      ff=>str.frequency(ff).sparameters().evalComplex()

    )//.toArray[Complex]
    Plot.title("SParams").plot( sparams)


  }// end main

}
