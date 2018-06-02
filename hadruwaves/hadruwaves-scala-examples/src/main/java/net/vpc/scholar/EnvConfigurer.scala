package net.vpc.scholar

import java.util.Properties

import net.vpc.scholar.hadrumaths.{Maths, Plot}

object EnvConfigurer {
  def getHadrumathsVersion(): String = {
    return getArtifactVersion("net.vpc.scholar", "hadrumaths");
  }

  def getHadruwavesVersion(): String = {
    return getArtifactVersion("net.vpc.scholar", "hadruwaves");
  }

  def getArtifactVersion(groupId: String, artifactId: String): String = {
    val url = Thread.currentThread().getContextClassLoader.getResource("META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties");
    if(url==null){
      return "DEV-VERSION";
    }
    val p = new Properties();
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
    Plot.Config.setDefaultWindowTitle(HVERSION +"-"+name)
  }


}
