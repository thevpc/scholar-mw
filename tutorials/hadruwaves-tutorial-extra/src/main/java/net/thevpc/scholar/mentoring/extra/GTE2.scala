package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.MathScala._
import net.thevpc.scholar.hadruwaves.Physics._

object GTE2 extends App {
  Plot.plot(sqr(cos(sin(X)) / sin(X)) * domain((-PI + PI / 10) -> -((-PI + PI / 10))));
}
