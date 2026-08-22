package net.thevpc.scholar.hadruwaves;


import net.thevpc.nuts.artifact.NId;
import net.thevpc.scholar.hadrumaths.HadrumathsService;
import net.thevpc.scholar.hadrumaths.HadrumathsServiceDesc;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
  * Created by vpc on 7/18/17.
  */
@HadrumathsServiceDesc(order = 500)
public class HadruwavesJavaService implements HadrumathsService {
  private static final Logger log  = Logger.getLogger(HadruwavesJavaService.class.getName());

  @Override
  public void installService() {
    log.log(Level.INFO, "Initializing Hadruwaves Scala extension component... : (hadruwaves-scala version "+getVersion()+")");

  }

  private String getVersion() {
    return NId.getForClass(HadruwavesJavaService.class).map(x->x.version().value()).orElse("DEV");
  }
}
