package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.Maths
import net.thevpc.scholar.hadruplot.Plot

object EnvConfigurer {
  def getHadrumathsVersion(): String = {
    return getArtifactVersion("net.thevpc.scholar", "hadrumaths");
  }

  def getHadruwavesVersion(): String = {
    return getArtifactVersion("net.thevpc.scholar", "hadruwaves");
  }

  def getArtifactVersion(groupId: String, artifactId: String): String = {
    val url = Thread.currentThread().getContextClassLoader.getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
    if (url == null) {
      return "DEV-VERSION";
    }
    val p = new _root_.java.util.Properties();
    p.load(url.openStream());
    return p.getProperty("version");
  }

  private val HVERSION: String = getHadrumathsVersion()

  def configure(name: String): Unit = {
    println("====== Hadrumaths Version=" + HVERSION)
    println("====== Hadruwaves Version=" + getHadruwavesVersion())
    println("============================================================")
    println("")
    Maths.Config.setSimplifierCacheSize(1000000);
    Maths.Config.setCacheEnabled(false);
    Maths.Config.setRootCachePath(HVERSION);
    Plot.Config.setDefaultWindowTitle(HVERSION + "-" + name)
  }


}
