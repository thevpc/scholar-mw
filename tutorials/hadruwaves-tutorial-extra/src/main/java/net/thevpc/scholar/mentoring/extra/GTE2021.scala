package net.thevpc.scholar.mentoring.extra

import net.thevpc.scholar.hadrumaths.MathScala._

object GTE2021 extends App {
  var f = 3 * GHZ;
  var λ = C / f;
  var W = λ / 4;
  var w = λ / 20;
  var L = 4 * λ;
  var l= λ/3;
  var k= param("k");
  var gk=cos((2*k+1)*PI/2*X/(l/3)) * II(0.0->l/3,-w/2->w/2);
  var g1=gk(k->1);
  var s=isteps(0,5);
  Plot.plot(s)
//  Plot.plot(gk.inflate(1->3))
}
