package net.vpc.scholar.hadruwaves

import java.util.logging.{Level, Logger}

import net.vpc.scholar.hadrumaths.io.IOUtils
import net.vpc.scholar.hadrumaths.{HadrumathsService, HadrumathsServiceDesc}

/**
  * Created by vpc on 7/18/17.
  */
@HadrumathsServiceDesc(order = 500)
class HadruwavesScalaService extends HadrumathsService {
  private val log: Logger = Logger.getLogger(classOf[HadruwavesScalaService].getName())

  override def installService(): Unit = {
    log.log(Level.INFO, "Initializing Hadruwaves Scala extension component... : (hadruwaves-scala version "+IOUtils.getArtifactVersionOrDev("net.vpc.scholar","hadruwaves-scala")+")");
  }
}
