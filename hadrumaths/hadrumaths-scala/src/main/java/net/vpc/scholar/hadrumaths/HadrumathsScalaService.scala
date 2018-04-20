package net.vpc.scholar.hadrumaths

import java.util.logging.{Level, Logger}

import net.vpc.scholar.hadrumaths.util.IOUtils

/**
  * Created by vpc on 7/18/17.
  */
@HadrumathsServiceDesc(order = 500)
class HadrumathsScalaService extends HadrumathsService {
  private val log: Logger = Logger.getLogger(classOf[HadrumathsScalaService].getName())

  override def installService(): Unit = {
    log.log(Level.INFO, "Initializing Hadrumaths Scala extension component... : (hadrumaths-scala version "+IOUtils.getArtifactVersionOrDev("net.vpc.scholar","hadrumaths-scala")+")");
    Plot.Config.registerConverter(classOf[Tuple2[Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple2[Object, Object]]
      Array(tuple._1, tuple._2)
    })
    Plot.Config.registerConverter(classOf[Tuple3[Object, Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple3[Object, Object, Object]]
      Array(tuple._1, tuple._2, tuple._3)
    })
    Plot.Config.registerConverter(classOf[Tuple4[Object, Object, Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple4[Object, Object, Object, Object]]
      Array(tuple._1, tuple._2, tuple._3, tuple._4)
    })
    Plot.Config.registerConverter(classOf[Tuple5[Object, Object, Object, Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple5[Object, Object, Object, Object, Object]]
      Array(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5)
    })
    Plot.Config.registerConverter(classOf[Tuple6[Object, Object, Object, Object, Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple6[Object, Object, Object, Object, Object, Object]]
      Array(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6)
    })
    Plot.Config.registerConverter(classOf[Tuple7[Object, Object, Object, Object, Object, Object, Object]], (i: Object) => {
      val tuple = i.asInstanceOf[Tuple7[Object, Object, Object, Object, Object, Object, Object]]
      Array(tuple._1, tuple._2, tuple._3, tuple._4, tuple._5, tuple._6, tuple._7)
    })
  }
}
