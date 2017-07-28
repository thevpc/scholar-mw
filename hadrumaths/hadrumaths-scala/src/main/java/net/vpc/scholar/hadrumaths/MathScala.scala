/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths

import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.symbolic._
import net.vpc.scholar.hadrumaths.util.ComputationMonitor

//import java.util

import net.vpc.scholar
import net.vpc.scholar.hadrumaths.cache.{Evaluator, PersistenceCache}
import net.vpc.scholar.hadrumaths.geom.Geometry

import scala.collection.{Iterable, Iterator}

object MathScala {

//  implicit def convertE[T](indexedSeq: IndexedSeq[T]): Array[T] = indexedSeq.toArray[T]

  def arr(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray()
  def arr(x: Array[Double]): ToDoubleArrayAware = new ToDoubleArrayAwareConstant(x)
  def samples(x: ToDoubleArrayAware): Samples = Samples.absolute(arr(x))
  def samples(x: ToDoubleArrayAware,y: ToDoubleArrayAware): Samples = Samples.absolute(arr(x),arr(y))
  def samples(x: ToDoubleArrayAware,y: ToDoubleArrayAware,z: ToDoubleArrayAware): Samples = Samples.absolute(arr(x),arr(y),arr(z))
  implicit def convertToDoubleArrayAware(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray
  implicit def convertDoubleColonTuple2ToArray(x: DoubleColonTuple2): Array[Double] = Maths.dsteps(x.left,x.right)
  implicit def convertDoubleColonTuple3ToArray(x: DoubleColonTuple3): Array[Double] = Maths.dsteps(x._1,x._3,x._2)
  implicit def convertDoubleToComplex(x: Double): Complex = Complex.valueOf(x)

  implicit def convertArrayExprToExprList(x: Array[Expr]): ExprList = new ArrayExprList(x: _*)

  implicit def convertArrayComplexToExprList(x: Array[Complex]): ExprList = new ArrayExprList(x: _*)

  implicit def convertArrayComplexToVector(x: Array[Complex]): Vector = Maths.columnVector(x)

  implicit def convertIntToComplex(x: Int): Complex = Complex.valueOf(x)

  implicit def convertii2dd(x: (Int, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertid2dd(x: (Int, Double)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertdi2dd(x: (Double, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertii2id2(x: Iterable[(Int, Int)]): Iterable[(Double, Double)] = new Iterable[(Double, Double)] {
    def iterator = convertii2id2(x.iterator);
  }

  implicit def convertii2id2(x: Iterator[(Int, Int)]): Iterator[(Double, Double)] = new Iterator[(Double, Double)] {
    def next() = {
      val v: (Int, Int) = x.next()
      (v._1.asInstanceOf[Double], v._2.asInstanceOf[Double])
    }

    def hasNext = x.hasNext;
  }

  implicit def i2d(x: Int): Double = {
    x
  }

  implicit def i2d2(x: Tuple2[Int, Int]): Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def i2d4(x: Tuple2[Double, Int]): Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def tupleToElementOp(x: (Int, Expr) => Expr): ElementOp = {
    return new ElementOp {
      override def eval(index: Int, e: Expr): Expr = x(index, e)
    }
  }

  implicit def convertii1id1(x: Iterable[Int]): Iterable[Double] = new Iterable[Double] {
    def iterator = convertii1id1(x.iterator);
  }

  implicit def convertii1id1(x: Iterator[Int]): Iterator[Double] = new Iterator[Double] {
    def next() = {
      val v: Int = x.next()
      v.asInstanceOf[Double]
    }

    def hasNext = x.hasNext;
  }

  implicit def c10(x: Array[Expr]): ExprList = {
    new ArrayExprList(x: _*)
  }

  implicit def c11(x: Array[DoubleToVector]): ExprList = {
    new ArrayExprList(x: _*)
  }

  def II(xdomain: Tuple2[Double, Double]): Expr = {
    Maths.expr(Domain.forBounds(xdomain._1, xdomain._2))
  }

  def IIe(xdomain: Tuple2[Expr, Expr]): Expr = {
    DomainExpr.forBounds(xdomain._1, xdomain._2)
  }

  def IIe(xdomain: Tuple2[Expr, Expr], ydomain: Tuple2[Expr, Expr]): Expr = {
    DomainExpr.forBounds(xdomain._1, xdomain._2, ydomain._1, ydomain._2)
  }

  def IIe(xdomain: Tuple2[Expr, Expr], ydomain: Tuple2[Expr, Expr], zdomain: Tuple2[Expr, Expr]): Expr = {
    DomainExpr.forBounds(xdomain._1, xdomain._2, ydomain._1, ydomain._2, zdomain._1, zdomain._2)
  }

  def IIx(a: Tuple2[Double, Double]): Expr = {
    gateX(a._1, a._2)
  }

  def IIy(a: Tuple2[Double, Double]): Expr = {
    gateY(a._1, a._2)
  }

  def IIz(a: Tuple2[Double, Double]): Expr = {
    gateZ(a._1, a._2)
  }


  def II(a: Tuple2[Double, Double], b: Tuple2[Double, Double]): Expr = DoubleValue.valueOf(1, Domain.forBounds(a._1, a._2, b._1, b._2))

  def II(a: Tuple2[Double, Double], b: Tuple2[Double, Double], c: Tuple2[Double, Double]): Expr = DoubleValue.valueOf(1, Domain.forBounds(a._1, a._2, b._1, b._2, c._1, c._2))

  def domain(a: Tuple2[Double, Double]): Domain = {
    Domain.forBounds(a._1, a._2);
  }

  def domain(a: Tuple2[Double, Double], b: Tuple2[Double, Double]): Domain = {
    Domain.forBounds(a._1, a._2, b._1, b._2);
  }

  def domain(a: Tuple2[Double, Double], b: Tuple2[Double, Double], c: Tuple2[Double, Double]): Domain = {
    Domain.forBounds(a._1, a._2, b._1, b._2, c._1, c._2);
  }

  def mape(a: Array[Expr], f: (Int, Expr) => Expr): Expr = {
    val max = a.length;
    val list = new ArrayExprList(max);
    var n = 0;
    while (n < max) {
      list.add(f(n, a(n)));
      n += 1
    }
    scholar.hadrumaths.Maths.sum(list)
  }

  implicit class SVector(val value: Vector) {
    def **(v: Vector): Complex = value.scalarProduct(v)

    def +(v: Vector): Vector = value.add(v)

    def -(v: Vector): Vector = value.sub(v)

    def +(v: Complex): Vector = value.add(v)

    def -(v: Complex): Vector = value.sub(v)

    def *(v: Complex): Vector = value.mul(v)

    def /(v: Complex): Vector = value.div(v)

    //    def ^ (v: Complex): Vector = value.pow(v)

    def **(v: Tuple2[Vector, Vector]): Complex = {
      value.scalarProductAll(Array(v._1, v._2): _*)
    }

    def **(v: Tuple3[Vector, Vector, Vector]): Complex = {
      value.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    def **(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
      value.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

    def **(v: Matrix): Complex = {
      v.scalarProduct(value)
    }

    def **(v: ExprList): Expr = {
      v.scalarProduct(value)
    }

    def :*(v: Vector): Vector = value.dotmul(v)

    def :/(v: Vector): Vector = value.dotdiv(v)

    def :^(v: Vector): Vector = value.dotpow(v)

    def :^(v: Double): Vector = value.dotpow(v)

    def :^(v: Complex): Vector = value.dotpow(v)
  }

  implicit class SMatrix(val value: Matrix) {
    def +(v: Matrix): Matrix = value.add(v)

    def +(v: Array[Array[Complex]]): Matrix = value.add(v)

    def -(v: Array[Array[Complex]]): Matrix = value.sub(v)

    def *(v: Array[Array[Complex]]): Matrix = value.mul(v)

    def +(v: Complex): Matrix = value.add(v)

    def +(v: Double): Matrix = value.add(v)

    def -(v: Matrix): Matrix = value.sub(v)

    def -(v: Complex): Matrix = value.sub(v)

    def -(v: Double): Matrix = value.sub(v)

    def *(v: Matrix): Matrix = value.mul(v)


    def *(v: Complex): Matrix = value.mul(v)

    def *(v: Double): Matrix = value.mul(v)

    def :*(v: Matrix): Matrix = value.dotmul(v)

    def :/(v: Matrix): Matrix = value.dotdiv(v)

    def **(v: Matrix): Complex = {
      value.scalarProduct(v)
    }

    def **(v: Vector): Complex = {
      value.scalarProduct(v)
    }

    def **(v: ExprList): Expr = {
      v.scalarProduct(value)
    }

    def **(v: Tuple2[Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2): _*)
    }

    def **(v: Tuple3[Vector, Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    def **(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

    def /(v: Matrix): Matrix = value.div(v)

    def /(v: Complex): Matrix = value.div(v)

    def /(v: Double): Matrix = value.div(v)

    def :^(v: Double): Matrix = value.dotpow(v)

    def *(v: Vector): Matrix = value.mul(v.toMatrix)

    def +(v: Vector): Matrix = value.add(v.toMatrix)

    def -(v: Vector): Matrix = value.sub(v.toMatrix)

    def /(v: Vector): Matrix = value.div(v.toMatrix)

    def :*(v: Vector): Matrix = value.dotmul(v.toMatrix)

    def :/(v: Vector): Matrix = value.dotdiv(v.toMatrix)
  }

  implicit class SGemortry(val value: Geometry) {
    def *(v: Double): Expr = mul(Maths.expr(value), Complex.valueOf(v));

    def *(v: Expr): Expr = mul(Maths.expr(value), v);

    def *(v: Geometry): Expr = mul(Maths.expr(value), Maths.expr(v));
  }


  implicit class SExpr(val value: Expr) extends Any(value) {

    def +(v: Expr): Expr = add(Any.unwrap(v))

    def !!(): Expr = simplify

    def unary_- : Expr = if (value.isInstanceOf[Complex]) value.asInstanceOf[Complex].neg() else new Neg(Any.unwrap(value))

    def :+(v: ExprList): ExprList = {
      var e = new ArrayExprList();
      e.add(Any.unwrap(value))
      e.addAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(Maths.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    def **(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(null, value, Any.unwrap(v));
    def ***(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v));

    def ^^(v: Expr): Expr = value.pow(Any.unwrap(v));

    def <(v: Expr): Expr = value.lt(Any.unwrap(v));
    def <=(v: Expr): Expr = value.lte(Any.unwrap(v));
    def >(v: Expr): Expr = value.gt(Any.unwrap(v));
    def >=(v: Expr): Expr = value.gte(Any.unwrap(v));
    def ~~(v: Expr): Expr = Maths.eq(value,Any.unwrap(v));
    def ===(v: Expr): Expr = Maths.eq(value,Any.unwrap(v));
    def <>(v: Expr): Expr = Maths.ne(value,Any.unwrap(v));
    def !==(v: Expr): Expr = Maths.ne(value,Any.unwrap(v));
    def &&(v: Expr): Expr = Maths.and(value,Any.unwrap(v));
    def ||(v: Expr): Expr = Maths.or(value,Any.unwrap(v));

    def /(v: Expr): Expr = div(Any.unwrap(v))

    def apply(f: Tuple2[ParamExpr, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  implicit class SDoubleToComplex(val value: DoubleToComplex) {
    def apply(x:Double) :Complex = value.computeComplex(x)
    def apply(x:Array[Double]) :Array[Complex] = value.computeComplex(x)
    def apply(x:Double,y:Double) :Complex = value.computeComplex(x,y)
    def apply(x:Array[Double],y:Array[Double]) :Array[Array[Complex]] = value.computeComplex(x,y)
  }

  implicit class SComplex(val value: Complex) extends Any(value) {

    def +(v: Complex): Complex = value.add(v)

    def -(v: Complex): Complex = value.sub(v)

    def *(v: Complex): Complex = value.mul(v)

    def /(v: Complex): Complex = value.div(v)

    def +(v: Double): Complex = value.add(v)

    def -(v: Double): Complex = value.sub(v)

    def *(v: Double): Complex = value.mul(v)

    def /(v: Double): Complex = value.div(v)

    def ^^(v: Expr): Expr = value.pow(v);

    def ^^(v: Complex): Complex = value.pow(v);

    def unary_- : Complex = value.neg()

    def +(v: Expr): Expr = add(v)

    def :+(v: ExprList): ExprList = {
      var e = new ArrayExprList();
      e.add(value)
      e.addAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(Maths.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    def **(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(null, value, Any.unwrap(v));

    def /(v: Expr): Expr = div(Any.unwrap(v))

    def apply(f: Tuple2[ParamExpr, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  implicit class SObjectCacheManager(val value: PersistenceCache) {

    def eval[T](key: String, monitor:ComputationMonitor, a: (AnyRef *) => AnyRef, anyParam: Object*): T = {
      value.evaluate(key, monitor,new Evaluator {
        override def evaluate(args: Array[Object]): Object = a(args)
      }, anyParam: _*)
    }

//    def process[T](key: String, old: T, a: (AnyRef *) => AnyRef, anyParam: Object*): T = {
//      if (old != null) {
//        return old;
//      }
//      value.evaluate(key, monitor, new Evaluator {
//        override def evaluate(args: Array[Object]): Object = a(args)
//      }, anyParam: _*)
//    }
  }

  implicit class SJList[T](val value: java.util.List[T]) {
    def transform[U](f: (Int, T) => U): java.util.List[U] = {
      var v = new java.util.ArrayList[U](value.size());
      val it: java.util.Iterator[T] = value.iterator()
      var i = 0;
      while (it.hasNext) {
        v.add(f(i, it.next));
        i = i + 1
      }
      v;
    }

    def apply(i: Int): T = value.get(i)
  }

  implicit class SExpressionListArray(val value: Array[ExprList]) {
    def !!(): Array[ExprList] = simplify()

    def simplify(): Array[ExprList] = {
      var c = new Array[ExprList](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = value(i).simplify()
        i = i + 1;
      }
      return c;
    }
  }

  implicit class SExpressionListArray2(val value: Array[Array[ExprList]]) {
    def !!(): Array[Array[ExprList]] = simplify()

    def simplify(): Array[Array[ExprList]] = {
      var c = new Array[Array[ExprList]](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = value(i).simplify()
        i = i + 1;
      }
      return c;
    }
  }

  implicit class SJCollection[T](val value: java.util.Collection[T]) {
    def sumc(f: (Int, T) => Expr): Complex = {
      var c = Complex.ZERO;
      val it: java.util.Iterator[T] = value.iterator()
      var i = 0;
      while (it.hasNext) {
        c += f(i, it.next).toComplex;
        i = i + 1
      }
      c;
    }

    def summ(f: (Int, T) => Matrix): Matrix = {
      val it: java.util.Iterator[T] = value.iterator()
      if (!it.hasNext) {
        return Maths.zerosMatrix(1);
      }
      var c: Matrix = f(0, it.next());
      var i = 0;
      while (it.hasNext) {
        c += f(i, it.next);
        i = i + 1
      }
      c;
    }

    def sume(f: (Int, T) => Expr) = {
      var c: Expr = Complex.ZERO;
      val it: java.util.Iterator[T] = value.iterator()
      var i = 0;
      while (it.hasNext) {
        c += f(i, it.next);
        i = i + 1
      }
      c;
    }

    def foreach[U](f: T => U) {
      val it: java.util.Iterator[T] = value.iterator()
      while (it.hasNext) {
        f(it.next);
      }
    }

    def foreachIndex[U](f: (Int, T) => U) {
      val it: java.util.Iterator[T] = value.iterator()
      var i = 0;
      while (it.hasNext) {
        f(i, it.next);
        i = i + 1
      }
    }

    def length: Int = {
      value.size()
    }
  }

  implicit class SList[T](val value: List[T]) {
    def transform[U](f: (Int, T) => U): List[U] = {
      var v = List[U]();
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        v = v :+ (f(i, value(i)));
        i = i + 1
      }
      v;
    }

    def sumc(f: (Int, T) => Expr): Complex = {
      var c = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, value(i)).toComplex
        i += 1;
      }
      c;
    }

    def summ(f: (Int, T) => Matrix): Matrix = {
      val size: Int = value.size
      if (size == 0) {
        return Maths.zerosMatrix(1);
      }
      var c: Matrix = f(0, value(0));
      var i = 1;
      while (i < size) {
        c += f(i, value(i))
        i += 1;
      }
      c;
    }

    def sume(f: (Int, T) => Expr) = {
      var c: Expr = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, value(i))
        i += 1;
      }
      c;
    }
  }

  implicit class SExprSeq(val value: ExprList) {
    def **(v: Expr): Expr = value.scalarProduct(v);

    def **(v: ExprList): Expr = value.scalarProduct(v);

    def **(v: Matrix): Expr = value.scalarProduct(v);

    def **(v: Vector): Expr = value.scalarProduct(v);

    def :**(v: ExprList): Matrix = Maths.scalarProductMatrix(value, v);

    def :**(v: Vector): Matrix = Maths.scalarProductMatrix(value, v.toArray);

    def :**(v: Matrix): Matrix = Maths.scalarProductMatrix(value, v.toVector.toArray);

    def +(v: Expr): ExprList = scholar.hadrumaths.Maths.add(value, v)

    def -(v: Expr): ExprList = scholar.hadrumaths.Maths.sub(value, v)

    def *(v: Expr): ExprList = Maths.mul(value, v)

    def /(v: Expr): ExprList = scholar.hadrumaths.Maths.div(value, v)

    def !!(): ExprList = value.simplify


    def unary_- : ExprList = {
      return value.transform(
        (i: Int, x: Expr) => (-x)
      )

    }

    def :+(v: Expr): ExprList = {
      value.asInstanceOf[ArrayExprList].add(v)
      value
    }

    def :+(v: ExprList): ExprList = {
      value.addAll(v)
      value
    }

    def apply(row: Int): Expr = {
      value.get(row);
    }

    def map(f: (Int, Expr) => Expr): ExprList = {
      val max = value.size();
      val list = new ArrayExprList(max);
      var n = 0;
      while (n < max) {
        list.add(f(n, value(n)));
        n += 1
      }
      list;
    }

    def cross(other: ExprList, f: IndexedExpr2 => Expr): ExprList = {
      val nmax = value.size();
      val mmax = other.size();
      val list = new ArrayExprList(nmax * mmax);
      var n = 0;
      while (n < nmax) {
        var m = 0;
        while (m < mmax) {
          list.add(f(new IndexedExpr2(n, m, value(n), other(m))));
          m += 1;
        }
        n += 1
      }
      list;
    }

    def foreachIndex(f: (Int, Expr) => Unit): Unit = {
      var i = 0;
      while (i < value.size()) {
        f(i, value(i));
        i = i + 1;
      }
    }

    //    def applyTransform(f: (Int, Expr) => Expr): ExprList = {
    //      value.transform(new ElementOp {
    //        override def process(i: Int, e: Expr): Expr = f(i, e);
    //      })
    //    }

    def ::+(v: ExprList): ExprList = {
      var e = new ArrayExprList();
      e.addAll(value)
      e.addAll(v)
      e;
    }

    def ::+(v: Expr): ExprList = {
      value.add(v);
      value
    }

    def sumc = {
      var c = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += value(i).toComplex
        i += 1;
      }
      c;
    }

    def sumc(f: (Int, Expr) => Complex) = {
      var c = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, value(i))
        i += 1;
      }
      c;
    }

    def sume(f: (Int, Expr) => Expr) = {
      var c: Expr = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, value(i))
        i += 1;
      }
      c;
    }

    def **(v: Tuple2[ExprList, ExprList]): Expr = {
      value.scalarProductAll(Array(v._1, v._2): _*)
    }

    def **(v: Tuple3[ExprList, ExprList, ExprList]): Expr = {
      value.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    def **(v: Tuple4[ExprList, ExprList, ExprList, ExprList]): Expr = {
      value.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

  }

  implicit class SDouble(val value: Double) {
    def ::(v: Double): DoubleColonTuple2 = new DoubleColonTuple2(value,v)
    def +(v: Complex): Complex = toComplex.add(v)

    def -(v: Complex): Complex = toComplex.sub(v)

    def *(v: Complex): Complex = toComplex.mul(v)

    def *(v: Geometry): Expr = toComplex.mul(Maths.expr(v))

    def /(v: Complex): Complex = toComplex.div(v)

    def ^^(v: Complex): Complex = value.pow(v);

    def +(v: Expr): Expr = toComplex.add(v.value)

    def -(v: Expr): Expr = toComplex.sub(v.value)

    def *(v: Expr): Expr = toComplex.mul(v.value)

    def /(v: Expr): Expr = toComplex.div(v.value)

    def toComplex: Complex = Complex.valueOf(value)
  }

  implicit class SInt(val value: Int) {
    def +(v: Complex): Complex = toComplex.add(v)

    def -(v: Complex): Complex = toComplex.sub(v)

    def *(v: Complex): Complex = toComplex.mul(v)

    def /(v: Complex): Complex = toComplex.div(v)

    def +(v: Expr): Expr = toComplex.add(v.value)

    def -(v: Expr): Expr = toComplex.sub(v.value)

    def *(v: Expr): Expr = toComplex.mul(v.value)

    def /(v: Expr): Expr = toComplex.div(v.value)

    def toComplex: Complex = Complex.valueOf(value)
  }

  implicit class SDomainXY(val value: Domain) {
    def /(elements: Int) = value.times(if (elements <= 0) 0 else (elements + 1));

    def /(elements: (Int, Int)): AbsoluteSamples = value.times(
      if (elements._1 <= 0) 0 else (elements._1 - 1),
      if (elements._2 <= 0) 0 else (elements._2 - 1)
    );

    def /!(step: Double): Samples = value.steps(step);

    def /!(step: (Double, Double)): Samples = value.steps(step._1, step._1);
  }


  implicit class SExprMatrix(val value: ExprMatrix) {
    def !!(): ExprMatrix = {
      value.simplify()
    }

  }


  implicit class SExprCube(val value: ExprCube) {

    def !!(): ExprCube = {
      value.simplify()
    }
  }
}

