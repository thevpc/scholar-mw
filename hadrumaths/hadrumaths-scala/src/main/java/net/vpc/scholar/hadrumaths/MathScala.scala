/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths


import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.symbolic._
import net.vpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromTMatrix
import net.vpc.scholar.hadruplot.{AbsoluteSamples, Plot, Samples}
//import java.util

import net.vpc.scholar
import net.vpc.scholar.hadrumaths.cache.{CacheEvaluator, PersistenceCache}
import net.vpc.scholar.hadrumaths.geom.Geometry

import scala.collection.{Iterable, Iterator}

object MathScala {

  //  implicit def convertE[T](indexedSeq: IndexedSeq[T]): Array[T] = indexedSeq.toArray[T]
  type EMatrix = TMatrix[Expr];
  type EVector = TVector[Expr];
  type EList = TList[Expr];
  type CMatrix = TMatrix[Complex];
  type CVector = TVector[Complex];
  type CList = TList[Complex];
  type DMatrix = TMatrix[Double];
  type DVector = TVector[Expr];
  type DList = TList[Double];
  type JPList[T] = java.util.List[T];
  type JPDouble = java.lang.Double;
  type JPLong = java.lang.Long;
  type JPInteger = java.lang.Integer;
  type JPFloat = java.lang.Float;
  type JPBoolean = java.lang.Boolean;

  //  def arr(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray()
  //
  //  def arr(x: Array[Double]): ToDoubleArrayAware = new ToDoubleArrayAwareConstant(x)

  def Plot(): net.vpc.scholar.hadruplot.PlotBuilder = net.vpc.scholar.hadruplot.Plot.builder()

  def samples(x: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray())

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray(), y.toDoubleArray)

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware, z: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray, y.toDoubleArray, z.toDoubleArray)

  implicit def convertImplicitToMatrixOfComplex(x: TMatrix[Complex]): Matrix = new ComplexMatrixFromTMatrix(x)

  implicit def convertImplicitToDoubleArrayAware(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray

  implicit def convertImplicitDoubleColonTuple2ToArray(x: DoubleColonTuple2): Array[Double] = Maths.dsteps(x.left, x.right)

  implicit def convertImplicitDoubleColonTuple3ToArray(x: DoubleColonTuple3): Array[Double] = Maths.dsteps(x._1, x._3, x._2)

  implicit def convertImplicitDoubleToComplex(x: Double): Complex = Complex.valueOf(x)

  implicit def convertImplicitArrayExprToExprList(x: Array[Expr]): TList[Expr] = Maths.elist(x: _*)

  implicit def convertImplicitArrayComplexToExprList(x: Array[Complex]): TList[Complex] = Maths.clist(x: _*)

  implicit def convertImplicitArrayComplexToVector(x: Array[Complex]): Vector = Maths.columnVector(x: _*)

  implicit def convertImplicitIntToComplex(x: Int): Complex = Complex.valueOf(x)

  implicit def convertImplicitii2dd(x: (Int, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitid2dd(x: (Int, Double)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitdi2dd(x: (Double, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitii2id2(x: Iterable[(Int, Int)]): Iterable[(Double, Double)] = new Iterable[(Double, Double)] {
    def iterator = convertii2id2(x.iterator);
  }

  implicit def convertDoubleArrayToDList(x: Array[Double]): DoubleList = DoubleArrayList.column(x)

  implicit def convertii2id2(x: Iterator[(Int, Int)]): Iterator[(Double, Double)] = new Iterator[(Double, Double)] {
    def next() = {
      val v: (Int, Int) = x.next()
      (v._1.asInstanceOf[Double], v._2.asInstanceOf[Double])
    }

    def hasNext = x.hasNext;
  }

  implicit def convertImpliciti2d(x: Int): Double = {
    x
  }

  implicit def convertImpliciti2d2(x: Tuple2[Int, Int]): Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def convertImpliciti2d4(x: Tuple2[Double, Int]): Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def convertImplicitTupleToElementOp(x: (Int, Expr) => Expr): ElementOp[Expr] = {
    return new ElementOp[Expr] {
      override def eval(index: Int, e: Expr): Expr = x(index, e)
    }
  }

  implicit def convertii1id1(x: Iterable[Int]): Iterable[Double] = new Iterable[Double] {
    def iterator = converconvertImplicitIi1id1(x.iterator);
  }

  implicit def converconvertImplicitIi1id1(x: Iterator[Int]): Iterator[Double] = new Iterator[Double] {
    def next() = {
      val v: Int = x.next()
      v.asInstanceOf[Double]
    }

    def hasNext = x.hasNext;
  }

  implicit def convertImplicitC10(x: Array[Expr]): TList[Expr] = {
    Maths.elist(x: _*)
  }

  implicit def convertImplicitC11(x: Array[DoubleToVector]): TList[Expr] = {
    Maths.elist(x: _*)
  }

  implicit def convertImplicitRightArrowUplet2Double(x: Tuple2[Double, Double]): net.vpc.scholar.hadrumaths.RightArrowUplet2.Double = {
    return new net.vpc.scholar.hadrumaths.RightArrowUplet2.Double(x._1, x._2)
  }

  //  def II(xdomain: Tuple2[Double, Double]): Expr = {
  //    Maths.expr(Domain.forBounds(xdomain._1, xdomain._2))
  //  }
  //
  //  def IIe(xdomain: Tuple2[Expr, Expr]): Expr = {
  //    DomainExpr.forBounds(xdomain._1, xdomain._2)
  //  }
  //
  //  def IIe(xdomain: Tuple2[Expr, Expr], ydomain: Tuple2[Expr, Expr]): Expr = {
  //    DomainExpr.forBounds(xdomain._1, xdomain._2, ydomain._1, ydomain._2)
  //  }
  //
  //  def IIe(xdomain: Tuple2[Expr, Expr], ydomain: Tuple2[Expr, Expr], zdomain: Tuple2[Expr, Expr]): Expr = {
  //    DomainExpr.forBounds(xdomain._1, xdomain._2, ydomain._1, ydomain._2, zdomain._1, zdomain._2)
  //  }

  //  def IIx(a: Tuple2[Double, Double]): Expr = {
  //    gateX(a._1, a._2)
  //  }
  //
  //  def IIy(a: Tuple2[Double, Double]): Expr = {
  //    gateY(a._1, a._2)
  //  }
  //
  //  def IIz(a: Tuple2[Double, Double]): Expr = {
  //    gateZ(a._1, a._2)
  //  }


  //  def II(a: Tuple2[Double, Double], b: Tuple2[Double, Double]): Expr = DoubleValue.valueOf(1, Domain.forBounds(a._1, a._2, b._1, b._2))
  //
  //  def II(a: Tuple2[Double, Double], b: Tuple2[Double, Double], c: Tuple2[Double, Double]): Expr = DoubleValue.valueOf(1, Domain.forBounds(a._1, a._2, b._1, b._2, c._1, c._2))

  //  def domain(a: Tuple2[Double, Double]): Domain = {
  //    Domain.forBounds(a._1, a._2);
  //  }
  //
  //  def domain(a: Tuple2[Double, Double], b: Tuple2[Double, Double]): Domain = {
  //    Domain.forBounds(a._1, a._2, b._1, b._2);
  //  }
  //
  //  def domain(a: Tuple2[Double, Double], b: Tuple2[Double, Double], c: Tuple2[Double, Double]): Domain = {
  //    Domain.forBounds(a._1, a._2, b._1, b._2, c._1, c._2);
  //  }

  def mape(a: Array[Expr], f: (Int, Expr) => Expr): Expr = {
    scholar.hadrumaths.Maths.esum(a.length, (i: Int) => f(i, a(i)))
  }

  implicit class SVector(value: TVector[Complex]) /*extends STVectorExpr(expr(value))*/ {
    def **(v: Tuple2[TVector[Complex], TVector[Complex]]): Complex = {
      value.scalarProductAll(Array((v._1), (v._2)): _*)
    }

    def ***(v: Tuple2[TVector[Complex], TVector[Complex]]): Complex = {
      value.scalarProductAll(Array((v._1), (v._2)): _*)
    }

    def **(v: TMatrix[Complex]): Complex = value.scalarProduct(v.toVector());

    //    def ***(v: TMatrix[Complex]): Complex = value.scalarProduct(true, v.toVector());


    def **(v: Vector): Complex = value.scalarProduct(v)

    //    def ***(v: Vector): Complex = value.scalarProduct(true, v)

    def **(v: TList[Expr]): Expr = {
      v.scalarProduct(value.asInstanceOf[TVector[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[TVector[Expr]])
    //    }

    def :**(v: java.util.List[Vector]): Vector = Maths.vector(value.vscalarProduct(v.toArray(Array.ofDim[Vector](v.size())): _ *))

    //    def :***(v: java.util.List[Vector]): Vector = Maths.vector(value.vscalarProduct(true, v.toArray(Array.ofDim[Vector](v.size())): _ *))

    def :**(v: Array[TVector[Complex]]): Vector = Maths.vector(value.vscalarProduct(v: _ *))

    //    def :***(v: Array[TVector[Complex]]): Vector = Maths.vector(value.vscalarProduct(true, v: _ *))

    def +(v: TVector[Complex]): Vector = Maths.vector(value.add(v))

    def -(v: TVector[Complex]): Vector = Maths.vector(value.sub(v))

    def +(v: Complex): Vector = Maths.vector(value.add(v))

    def -(v: Complex): Vector = Maths.vector(value.sub(v))

    def *(v: Complex): Vector = Maths.vector(value.mul(v))

    def /(v: Complex): Vector = Maths.vector(value.div(v))

    def %(v: Complex): Vector = Maths.vector(value.rem(v))

    //    def ^ (v: Complex): Vector = value.pow(v)

    //    def **(v: Tuple2[Vector, Vector]): Complex = {
    //      value.scalarProductAll(Array(v._1, v._2): _*)
    //    }
    //
    //    def **(v: Tuple3[Vector, Vector, Vector]): Complex = {
    //      value.scalarProductAll(Array(v._1, v._2, v._3): _*)
    //    }
    //
    //    def **(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
    //      value.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    //    }

    //    def **(v: Matrix): Complex = {
    //      v.scalarProduct(value)
    //    }


    def *(v: Vector): Matrix = {
      value.toMatrix.mul(v.toMatrix)
    }

    def *(v: Matrix): Matrix = {
      value.toMatrix.mul(v)
    }

    def :*(v: Vector): Vector = Maths.vector(value.dotmul(v))

    def :/(v: Vector): Vector = Maths.vector(value.dotdiv(v))

    def :^(v: Vector): Vector = Maths.vector(value.dotpow(v))

    def :^(v: Double): Vector = Maths.vector(value.dotpow(v))

    def :^(v: Complex): Vector = Maths.vector(value.dotpow(v))
  }

  implicit class STMatrix[T](val value: TMatrix[T]) {

  }

  implicit class SMatrix(val value: Matrix) {

    def :**[T](v: TVector[TVector[T]]): TVector[T] = {
      return Maths.vscalarProduct(value.toVector.asInstanceOf[TVector[T]], v);
    }

    //    def :***[T](v: TVector[TVector[T]]): TVector[T] = {
    //      return Maths.vhscalarProduct(value.toVector.asInstanceOf[TVector[T]], v);
    //    }

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

    def *(v: Vector): Matrix = value.mul(v.toMatrix)

    def *(v: TVector[Expr]): TMatrix[Expr] = ematrix(value.mul(matrix(v.toMatrix)))

    def *(v: Double): Matrix = value.mul(v)

    def :*(v: Matrix): Matrix = value.dotmul(v)

    def :/(v: Matrix): Matrix = value.dotdiv(v)

    def **(v: Matrix): Complex = {
      value.scalarProduct(v)
    }

    //    def ***(v: Matrix): Complex = {
    //      value.scalarProduct(true, v)
    //    }

    def **(v: Vector): Complex = {
      value.scalarProduct(v)
    }

    //    def ***(v: Vector): Complex = {
    //      value.scalarProduct(true, v)
    //    }

    def **(v: TList[Expr]): Expr = {
      v.scalarProduct(value.asInstanceOf[TMatrix[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[TMatrix[Expr]])
    //    }

    def **(v: Tuple2[Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2): _*)
    }

    //    def ***(v: Tuple2[Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2): _*)
    //    }

    def **(v: Tuple3[Vector, Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    //    def ***(v: Tuple3[Vector, Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2, v._3): _*)
    //    }

    def **(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

    //    def ***(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2, v._3, v._4): _*)
    //    }

    def /(v: Matrix): Matrix = value.div(v)

    def %(v: Matrix): Matrix = value.rem(v)

    def /(v: Complex): Matrix = value.div(v)

    def /(v: Double): Matrix = value.div(v)

    def :^(v: Double): Matrix = value.dotpow(v)

    def +(v: Vector): Matrix = value.add(v.toMatrix)

    def -(v: Vector): Matrix = value.sub(v.toMatrix)

    def /(v: Vector): Matrix = value.div(v.toMatrix)

    def %(v: Vector): Matrix = value.rem(v.toMatrix)

    def :*(v: Vector): Matrix = value.dotmul(v.toMatrix)

    def :/(v: Vector): Matrix = value.dotdiv(v.toMatrix)
  }

  implicit class SGemortry(val value: Geometry) {
    def *(v: Double): Expr = value.multiply(v);

    def *(v: Int): Expr = value.multiply(v);

    def *(v: Expr): Expr = value.multiply(v);

    def *(v: Geometry): Expr = value.multiply(Maths.expr(v));
  }


  implicit class SExpr(val value: Expr) extends Any(value, null, null) {

    def +(v: Expr): Expr = add(Any.unwrap(v))

    def !!(): Expr = simplify

    def unary_- : Expr = Maths.neg(value)

    def :+(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.append(Any.unwrap(value))
      e.appendAll(v)
      e;
    }
    def ++(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.append(Any.unwrap(value))
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(Maths.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    //    def **(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(value, Any.unwrap(v));

    def **(v: TVector[Expr]): Vector = scholar.hadrumaths.Maths.scalarProduct(value, v);

    //    def ***(v: TVector[Expr]): Vector = scholar.hadrumaths.Maths.scalarProduct(true, value, v);

    def **(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v));

    //    def ***(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v), true);

    def ^^(v: Expr): Expr = value.pow(Any.unwrap(v));

    def <(v: Expr): Expr = value.lt(Any.unwrap(v));

    def <=(v: Expr): Expr = value.lte(Any.unwrap(v));

    def >(v: Expr): Expr = value.gt(Any.unwrap(v));

    def >=(v: Expr): Expr = value.gte(Any.unwrap(v));

    def ~~(v: Expr): Expr = Maths.eq(value, Any.unwrap(v));

    def ===(v: Expr): Expr = Maths.eq(value, Any.unwrap(v));

    def <>(v: Expr): Expr = Maths.ne(value, Any.unwrap(v));

    def !==(v: Expr): Expr = Maths.ne(value, Any.unwrap(v));

    def &&(v: Expr): Expr = Maths.and(value, Any.unwrap(v));

    def ||(v: Expr): Expr = Maths.or(value, Any.unwrap(v));

    def /(v: Expr): Expr = div(Any.unwrap(v))

    def %(v: Expr): Expr = rem(Any.unwrap(v))

    def apply(f: Tuple2[ParamExpr, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  implicit class SDoubleToComplex(val value: DoubleToComplex) {
    def apply(x: Double): Complex = value.computeComplex(x)

    def apply(x: Array[Double]): Array[Complex] = value.computeComplex(x)

    def apply(x: Double, y: Double): Complex = value.computeComplex(x, y)

    def apply(x: Array[Double], y: Array[Double]): Array[Array[Complex]] = value.computeComplex(x, y)
  }

  implicit class SDoubleToDouble(val value: DoubleToDouble) {
    def apply(x: Double): Double = value.computeDouble(x)

    def apply(x: Array[Double]): Array[Double] = value.computeDouble(x)

    def apply(x: Double, y: Double): Double = value.computeDouble(x, y)

    def apply(x: Array[Double], y: Array[Double]): Array[Array[Double]] = value.computeDouble(x, y)
  }

  implicit class SComplex(val value: Complex) extends Any(value, null, null) {

    def +(v: Complex): Complex = value.add(v)

    def -(v: Complex): Complex = value.sub(v)

    def *(v: Complex): Complex = value.mul(v)

    def /(v: Complex): Complex = value.div(v)

    def %(v: Complex): Complex = value.rem(v)

    def +(v: Double): Complex = value.add(v)

    def -(v: Double): Complex = value.sub(v)

    def *(v: Double): Complex = value.mul(v)

    def /(v: Double): Complex = value.div(v)

    def %(v: Double): Complex = value.rem(v)

    def ^^(v: Expr): Expr = value.pow(v);

    def ^^(v: Complex): Complex = value.pow(v);

    def unary_- : Complex = value.neg()

    def +(v: Expr): Expr = add(v)

    def :+(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.append(value)
      e.appendAll(v)
      e;
    }
    def ++(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(Maths.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    def **(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(null, value, Any.unwrap(v));

    def ***(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(null, value, Any.unwrap(v));

    def /(v: Expr): Expr = div(Any.unwrap(v))

    def %(v: Expr): Expr = rem(Any.unwrap(v))

    def apply(f: Tuple2[ParamExpr, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  //  implicit class SObjectCacheManager(val value: PersistenceCache) {
  //
  //    def eval[T](key: String, monitor: ProgressMonitor, a: (AnyRef *) => AnyRef, anyParam: Object*): T = {
  //      value.evaluate(key, monitor, new CacheEvaluator {
  //        override def evaluate(args: Array[Object]): Object = a(args)
  //      }, anyParam: _*)
  //    }
  //
  //    //    def process[T](key: String, old: T, a: (AnyRef *) => AnyRef, anyParam: Object*): T = {
  //    //      if (old != null) {
  //    //        return old;
  //    //      }
  //    //      value.evaluate(key, monitor, new Evaluator {
  //    //        override def evaluate(args: Array[Object]): Object = a(args)
  //    //      }, anyParam: _*)
  //    //    }
  //  }

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

  implicit class SExpressionListArray(val value: Array[TList[Expr]]) {
    def !!(): Array[TList[Expr]] = simplify()

    def simplify(): Array[TList[Expr]] = {
      var c = new Array[TList[Expr]](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = Maths.simplify(value(i))
        i = i + 1;
      }
      return c;
    }
  }

  implicit class SExpressionListArray2(val value: Array[Array[TList[Expr]]]) {
    def !!(): Array[Array[TList[Expr]]] = simplify()

    def simplify(): Array[Array[TList[Expr]]] = {
      var c = new Array[Array[TList[Expr]]](value.length);
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

  implicit class STVector[T](val value: TVector[T]) extends scala.Iterable[T] {
    override def iterator: Iterator[T] = new Iterator[T] {
      private val jiter = value.iterator()

      override def hasNext: Boolean = jiter.hasNext

      override def next(): T = jiter.next();
    }
  }

  implicit class STList[T](value: TList[T]) extends STVector[T](value) {
    def :+(v: T): TList[T] = {
      value.append(v)
      value
    }

    def :+(v: TList[T]): TList[T] = {
      //TODO FIX ME
      return value.concat(v).asInstanceOf[TList[T]]
    }
    def ++(v: TList[T]): TList[T] = {
      //TODO FIX ME
      return value.concat(v).asInstanceOf[TList[T]]
    }
  }

  implicit class STVectorExpr(value: TVector[Expr]) extends STVector[Expr](value) {
    def **[P1 <: Expr, P2 <: Expr](v: Tuple2[TVector[P1], TVector[P2]]): Expr = {
      value.to($EXPR).scalarProductAll(expr(v._1), expr(v._2))
    }

    //    def ***[P1 <: Expr, P2 <: Expr](v: Tuple2[TVector[P1], TVector[P2]]): Expr = {
    //      value.scalarProductAll(true, expr(v._1), expr(v._2))
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TVector[P1], TVector[P2], TVector[P3]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TVector[P1], TVector[P2], TVector[P3]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TVector[P1], TVector[P2], TVector[P3], TVector[P4]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TVector[P1], TVector[P2], TVector[P3], TVector[P4]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    //    }

    def :*[P <: Expr](v: TVector[P]): TVector[Expr] = Maths.edotmul(value, v.asInstanceOf[TVector[Expr]]);

    def :*[P1 <: Expr, P2 <: Expr](v: Tuple2[TVector[P1], TVector[P2]]): TVector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TVector[P1], TVector[P2], TVector[P3]]): TVector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]], v._3.asInstanceOf[TVector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TVector[P1], TVector[P2], TVector[P3], TVector[P4]]): TVector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]], v._3.asInstanceOf[TVector[Expr]], v._4.asInstanceOf[TVector[Expr]])
    }

    def **[P1 <: Expr](v: Expr): TVector[Expr] = value.to($EXPR).scalarProduct(v);

    //    def ***[P1 <: Expr](v: Expr): TVector[Expr] = value.to($EXPR).scalarProduct(true, v);

    def **[P <: Expr](v: TVector[P]): Expr = value.to($EXPR).scalarProduct(v.to($EXPR));

    //    def ***[P <: Expr](v: TVector[P]): Expr = value.to($EXPR).scalarProduct(true, v.to($EXPR));

    def **[P <: Expr](v: TMatrix[P]): Expr = value.to($EXPR).scalarProduct((v.toVector().to($EXPR)));

    //    def ***[P <: Expr](v: TMatrix[P]): Expr = value.to($EXPR).scalarProduct(true, expr(v.toVector()));

    def :**[P1 <: Expr](v: TVector[P1]): TMatrix[Expr] = Maths.scalarProductMatrix(value, v.asInstanceOf[TVector[Expr]]).asInstanceOf[TMatrix[Expr]];

    def :***[P1 <: Expr](v: TVector[P1]): TMatrix[Expr] = Maths.scalarProductMatrix(value, v.asInstanceOf[TVector[Expr]]).asInstanceOf[TMatrix[Expr]];

    def :**(v: Vector): Matrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      Maths.scalarProductMatrix(value, columnVector(exprs))
    };

    def :***(v: Vector): Matrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      Maths.scalarProductMatrix(value, columnVector(exprs))
    };

    def :**(v: Matrix): Matrix = Maths.scalarProductMatrix(value, v.toVector.asInstanceOf[TVector[Expr]]);

    def :***(v: Matrix): Matrix = Maths.scalarProductMatrix(value, v.toVector.asInstanceOf[TVector[Expr]]);
  }

  implicit class SExprList(value: TList[Expr]) extends STVectorExpr(value) {

    def +(v: Expr): TList[Expr] = scholar.hadrumaths.Maths.add(value, v)

    def -(v: Expr): TList[Expr] = scholar.hadrumaths.Maths.sub(value, v)

    def *(v: Expr): TList[Expr] = Maths.mul(value, v)

    def /(v: Expr): TList[Expr] = scholar.hadrumaths.Maths.div(value, v)

    def %(v: Expr): TList[Expr] = scholar.hadrumaths.Maths.rem(value, v)

    def !(): TList[Expr] = Maths.simplify(value)

    def !!(): TList[Expr] = Maths.simplify(value).copy()


    def unary_- : TList[Expr] = {
      return value.eval(
        (i: Int, x: Expr) => (-x)
      )

    }


    def map(f: (Int, Expr) => Expr): TList[Expr] = {
      val max = value.size();
      val list = Maths.elist(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, value(n)));
        n += 1
      }
      list;
    }

    def cross(other: TList[Expr], f: IndexedExpr2 => Expr): TList[Expr] = {
      val nmax = value.size();
      val mmax = other.size();
      val list = Maths.elist(nmax * mmax);
      var n = 0;
      while (n < nmax) {
        var m = 0;
        while (m < mmax) {
          list.append(f(new IndexedExpr2(n, m, value(n), other(m))));
          m += 1;
        }
        n += 1
      }
      list;
    }


    //    def applyTransform(f: (Int, Expr) => Expr): ExprList = {
    //      value.eval(new ElementOp {
    //        override def process(i: Int, e: Expr): Expr = f(i, e);
    //      })
    //    }

    def ::+(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.appendAll(value)
      e.appendAll(v)
      e;
    }

    def ::+(v: Expr): TList[Expr] = {
      value.append(v);
      value
    }

    def ++(v: TList[Expr]): TList[Expr] = {
      var e = Maths.elist();
      e.appendAll(value)
      e.appendAll(v)
      e;
    }

    def ++(v: Expr): TList[Expr] = {
      value.append(v);
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

    def sum(f: (Int, Expr) => Expr) = {
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

  implicit class SDoubleList(val value: TList[java.lang.Double]) {


    def +(v: java.lang.Double): TList[java.lang.Double] = scholar.hadrumaths.Maths.add[java.lang.Double](value, v)

    def -(v: java.lang.Double): TList[java.lang.Double] = scholar.hadrumaths.Maths.sub[java.lang.Double](value, v)

    def *(v: java.lang.Double): TList[java.lang.Double] = Maths.mul[java.lang.Double](value, v)

    def /(v: java.lang.Double): TList[java.lang.Double] = scholar.hadrumaths.Maths.div[java.lang.Double](value, v)

    def %(v: java.lang.Double): TList[java.lang.Double] = scholar.hadrumaths.Maths.rem[java.lang.Double](value, v)

    def !!(): TList[java.lang.Double] = value;


    def unary_- : TList[java.lang.Double] = {
      return value.eval(
        (i: Int, x: java.lang.Double) => (-x)
      )

    }

    def ++(v: java.lang.Double): TList[java.lang.Double] = {
      value.append(v)
      value
    }
    def ++(v: TList[java.lang.Double]): TList[java.lang.Double] = {
      value.appendAll(v)
      value
    }
    def :+(v: java.lang.Double): TList[java.lang.Double] = {
      value.append(v)
      value
    }
    def :+(v: TList[java.lang.Double]): TList[java.lang.Double] = {
      value.appendAll(v)
      value
    }

    def baa(v: java.lang.Double): TList[java.lang.Double] = {
      value.append(v)
      value
    }


    def apply(row: Int): java.lang.Double = {
      value.get(row);
    }

    def map(f: (Int, Expr) => Expr): TList[Expr] = {
      val max = value.size();
      val list = Maths.elist(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, expr(value(n))));
        n += 1
      }
      list;
    }

    def cross(other: TList[Expr], f: IndexedExpr2 => Expr): TList[Expr] = {
      val nmax = value.size();
      val mmax = other.size();
      val list = Maths.elist(nmax * mmax);
      var n = 0;
      while (n < nmax) {
        var m = 0;
        while (m < mmax) {
          list.append(f(new IndexedExpr2(n, m, expr(value(n)), other(m))));
          m += 1;
        }
        n += 1
      }
      list;
    }

    //    def foreachIndex(f: (Int, Expr) => Unit): Unit = {
    //      var i = 0;
    //      while (i < value.size()) {
    //        f(i, expr(value(i)));
    //        i = i + 1;
    //      }
    //    }


    def sumc = {
      var c = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += value(i)
        i += 1;
      }
      c;
    }

    def sumc(f: (Int, Expr) => Complex) = {
      var c = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, expr(value(i)))
        i += 1;
      }
      c;
    }

    def sume(f: (Int, Expr) => Expr) = {
      var c: Expr = Complex.ZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, expr(value(i)))
        i += 1;
      }
      c;
    }


  }

  implicit class SDouble(val value: Double) {
    def ::(v: Double): DoubleColonTuple2 = new DoubleColonTuple2(value, v)

    def +(v: Complex): Complex = toComplex.add(v)

    def -(v: Complex): Complex = toComplex.sub(v)

    def *(v: Complex): Complex = toComplex.mul(v)

    def *(v: Geometry): Expr = toComplex.mul(Maths.expr(v))

    def /(v: Complex): Complex = toComplex.div(v)

    def %(v: Complex): Complex = toComplex.rem(v)

    def ^^(v: Complex): Complex = value.pow(v);

    def +(v: Expr): Expr = toComplex.add(v.value)

    def -(v: Expr): Expr = toComplex.sub(v.value)

    def *(v: Expr): Expr = toComplex.mul(v.value)

    def /(v: Expr): Expr = toComplex.div(v.value)

    def %(v: Expr): Expr = toComplex.rem(v.value)

    def toComplex: Complex = Complex.valueOf(value)
  }

  implicit class SInt(val value: Int) {
    def +(v: Complex): Complex = toComplex.add(v)

    def -(v: Complex): Complex = toComplex.sub(v)

    def *(v: Complex): Complex = toComplex.mul(v)

    def /(v: Complex): Complex = toComplex.div(v)

    def %(v: Complex): Complex = toComplex.rem(v)

    def +(v: Expr): Expr = toComplex.add(v.value)

    def -(v: Expr): Expr = toComplex.sub(v.value)

    def *(v: Expr): Expr = toComplex.mul(v.value)

    def /(v: Expr): Expr = toComplex.div(v.value)

    def %(v: Expr): Expr = toComplex.rem(v.value)

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
    def !!(): TMatrix[Expr] = {
      value.simplify()
    }

  }


  implicit class SExprCube(val value: ExprCube) {

    def !!(): ExprCube = {
      value.simplify()
    }
  }

}

