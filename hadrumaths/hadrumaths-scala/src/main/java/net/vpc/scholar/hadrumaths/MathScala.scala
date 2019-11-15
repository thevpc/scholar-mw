/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.vpc.scholar.hadrumaths


import java.io.{File, UncheckedIOException}
import java.util
import java.util.concurrent.Callable

import net.vpc.common.jeep.ExpressionManager
import net.vpc.common.mon.{MonitoredAction, ProgressMonitor}
import net.vpc.common.util.{Chronometer, Converter, DoubleFormat, IndexSelectionStrategy, IntFilter, MemoryInfo, MemoryMeter, TypeName}
import net.vpc.scholar.hadrumaths.Maths._
import net.vpc.scholar.hadrumaths.geom.Point
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator
import net.vpc.scholar.hadrumaths.symbolic._
import net.vpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromTMatrix
import net.vpc.scholar.hadruplot.console.params.{ArrayParamSet, BooleanArrayParamSet, DoubleArrayParamSet, FloatArrayParamSet, IntArrayParamSet, LongArrayParamSet, Param, XParamSet}
import net.vpc.scholar.hadruplot.{AbsoluteSamples, Plot, PlotDoubleConverter, Samples}
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

  implicit def convertImplicitDoubleToComplex(x: Double): Complex = complex(x)

  implicit def convertImplicitArrayExprToExprList(x: Array[Expr]): TList[Expr] = Maths.elist(x: _*)

  implicit def convertImplicitArrayComplexToExprList(x: Array[Complex]): TList[Complex] = Maths.clist(x: _*)

  implicit def convertImplicitArrayComplexToVector(x: Array[Complex]): Vector = Maths.columnVector(x: _*)

  implicit def convertImplicitIntToComplex(x: Int): Complex = complex(x)

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

  //  def II(xdomain: Tuple2[Double,java.lang.Double]): Expr = {
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

  //  def IIx(a: Tuple2[Double,java.lang.Double]): Expr = {
  //    gateX(a._1, a._2)
  //  }
  //
  //  def IIy(a: Tuple2[Double,java.lang.Double]): Expr = {
  //    gateY(a._1, a._2)
  //  }
  //
  //  def IIz(a: Tuple2[Double,java.lang.Double]): Expr = {
  //    gateZ(a._1, a._2)
  //  }


  //  def II(a: Tuple2[Double,java.lang.Double], b: Tuple2[Double,java.lang.Double]): Expr = DoubleValue.valueOf(1, Domain.forBounds(a._1, a._2, b._1, b._2))
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
    def apply(x: java.lang.Double): Complex = value.computeComplex(x)

    def apply(x: Array[Double]): Array[Complex] = value.computeComplex(x)

    def apply(x: java.lang.Double, y: java.lang.Double): Complex = value.computeComplex(x, y)

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
      var c = CZERO;
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
      var c: Expr = CZERO;
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
      var c = CZERO;
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
      var c: Expr = CZERO;
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
      var c = CZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += value(i).toComplex
        i += 1;
      }
      c;
    }

    def sumc(f: (Int, Expr) => Complex) = {
      var c = CZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, value(i))
        i += 1;
      }
      c;
    }

    def sum(f: (Int, Expr) => Expr) = {
      var c: Expr = CZERO;
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
      var c = CZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += value(i)
        i += 1;
      }
      c;
    }

    def sumc(f: (Int, Expr) => Complex) = {
      var c = CZERO;
      var i = 0;
      val size: Int = value.size
      while (i < size) {
        c += f(i, expr(value(i)))
        i += 1;
      }
      c;
    }

    def sume(f: (Int, Expr) => Expr) = {
      var c: Expr = CZERO;
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

    def ^^(v: Complex): Complex = toComplex.pow(v);

    def +(v: Expr): Expr = toComplex.add(v.value)

    def -(v: Expr): Expr = toComplex.sub(v.value)

    def *(v: Expr): Expr = toComplex.mul(v.value)

    def /(v: Expr): Expr = toComplex.div(v.value)

    def %(v: Expr): Expr = toComplex.rem(v.value)

    def toComplex: Complex = Complex(value)
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

    def toComplex: Complex = Complex(value)
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


  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////
//  val PI: Double = Math.PI
//  val E: Double = Math.E
//  val DDZERO: DoubleToDouble = MathsBase.DDZERO
//  val DDNAN: DoubleToDouble = MathsBase.DDNAN
//  val DCZERO: DoubleToComplex = MathsBase.DCZERO
//  val DVZERO3: DoubleToVector = MathsBase.DVZERO3
//  val EZERO: Expr = MathsBase.EZERO
//  val EONE: Expr = MathsBase.EONE
//  val X: Expr = MathsBase.X
//  val Y: Expr = MathsBase.Y
//  val Z: Expr = MathsBase.Z
//  val HALF_PI: Double = MathsBase.HALF_PI
//  val I: Complex = MathsBase.I
//  val CNaN: Complex = MathsBase.CNaN
//  val CONE: Complex = MathsBase.CONE
//  val CZERO: Complex = MathsBase.CZERO //    public static  boolean DEBUG = true;
//
//  val DVZERO1: DoubleToVector = MathsBase.DVZERO1
//  val DVZERO2: DoubleToVector = MathsBase.DVZERO2
//  val î: Complex = MathsBase.î
//  val ê: Expr = MathsBase.ê
//  val ĉ: Complex = MathsBase.ĉ
//  val METER: Double = MathsBase.METER
//  val HZ: Double = MathsBase.HZ
//  val BYTE: Long = MathsBase.BYTE
//  val MILLISECOND: Long = MathsBase.MILLISECOND
//  /**
//   * kibibyte
//   */
//  val KiBYTE: Int = MathsBase.KiBYTE
//  /**
//   * mibibyte
//   */
//  val MiBYTE: Int = MathsBase.MiBYTE
//  /**
//   * TEBI Byte
//   */
//  val GiBYTE: Long = MathsBase.GiBYTE
//  val TiBYTE: Long = MathsBase.TiBYTE
//  /**
//   * PEBI Byte
//   */
//  val PiBYTE: Long = MathsBase.PiBYTE
//  /**
//   * exbibyte
//   */
//  val EiBYTE: Long = MathsBase.EiBYTE
//
//  val YOCTO: Double = MathsBase.YOCTO
//  val ZEPTO: Double = MathsBase.ZEPTO
//  val ATTO: Double = MathsBase.ATTO
//  val FEMTO: Double = MathsBase.FEMTO
//  val PICO: Double = MathsBase.PICO
//  val NANO: Double = MathsBase.NANO
//  val MICRO: Double = MathsBase.MICRO
//  val MILLI: Double = MathsBase.MILLI
//  val CENTI: Double = MathsBase.CENTI
//  val DECI: Double = MathsBase.DECI
//  /**
//   * DECA
//   */
//  val DECA: Int = MathsBase.DECA
//  /**
//   * HECTO
//   */
//  val HECTO: Int = MathsBase.HECTO
//
//  /**
//   * KILO
//   */
//  val KILO: Int = MathsBase.KILO
//  /**
//   * MEGA
//   */
//  val MEGA: Int = MathsBase.MEGA
//  val GIGA: Long = MathsBase.GIGA
//  /**
//   * TERA
//   */
//  val TERA: Long = MathsBase.TERA
//  /**
//   * PETA
//   */
//  val PETA: Long = MathsBase.PETA
//  /**
//   * EXA
//   */
//  val EXA: Long = MathsBase.EXA
//  /**
//   * ZETTA
//   */
//  val ZETTA: Long = MathsBase.ZETTA
//  /**
//   * YOTTA
//   */
//  val YOTTA: Long = MathsBase.YOTTA
//  val SECOND: Long = MathsBase.SECOND
//  val MINUTE: Long = MathsBase.MINUTE
//  val HOUR: Long = MathsBase.HOUR
//  val DAY: Long = MathsBase.DAY
//  val KHZ: Double = MathsBase.KHZ
//  val MHZ: Double = MathsBase.MHZ
//  val GHZ: Double = MathsBase.GHZ
//  val MILLIMETER: Double = MathsBase.MILLIMETER
//  val MM: Double = MathsBase.MM
//  val CM: Double = MathsBase.CM
//  val CENTIMETER: Double = MathsBase.CENTIMETER
//  /**
//   * light celerity. speed of light in vacuum
//   */
//  //    public static  final int C = 300000000;
//  val C: Int = MathsBase.C //m.s^-1
//
//  /**
//   * Newtonian constant of gravitation
//   */
//  val G: Double = MathsBase.G //m3·kg^−1·s^−2;
//
//  /**
//   * Planck constant
//   */
//  val H: Double = MathsBase.H //J·s;
//
//  /**
//   * Reduced Planck constant
//   */
//  val Hr: Double = MathsBase.Hr
//  /**
//   * magnetic constant (vacuum permeability)
//   */
//  val U0: Double = MathsBase.U0 //N·A−2
//
//  /**
//   * electric constant (vacuum permittivity) =1/(u0*C^2)
//   **/
//  val EPS0: Double = MathsBase.EPS0 //F·m−1
//
//  /**
//   * characteristic impedance of vacuum =1/(u0*C)
//   */
//  val Z0: Double = MathsBase.Z0
//  /**
//   * Coulomb's constant
//   */
//  val Ke: Double = MathsBase.Ke
//  /**
//   * elementary charge
//   */
//  val Qe: Double = MathsBase.Qe //C
//
//  val COMPLEX_VECTOR_SPACE: VectorSpace[Complex] = MathsBase.COMPLEX_VECTOR_SPACE
//  val EXPR_VECTOR_SPACE: VectorSpace[Expr] = MathsBase.EXPR_VECTOR_SPACE
//  val DOUBLE_VECTOR_SPACE: VectorSpace[java.lang.Double] = MathsBase.DOUBLE_VECTOR_SPACE
//  val X_AXIS: Int = MathsBase.X_AXIS
//  val Y_AXIS: Int = MathsBase.Y_AXIS
//  val Z_AXIS: Int = MathsBase.Z_AXIS
//  val MATRIX_STORE_MANAGER: TStoreManager[Matrix] = MathsBase.MATRIX_STORE_MANAGER
//  val TMATRIX_STORE_MANAGER: TStoreManager[TMatrix[_]] = MathsBase.TMATRIX_STORE_MANAGER
//
//  val TVECTOR_STORE_MANAGER: TStoreManager[TVector[_]] = MathsBase.TVECTOR_STORE_MANAGER
//
//  val VECTOR_STORE_MANAGER: TStoreManager[Vector] = MathsBase.VECTOR_STORE_MANAGER
//  val IDENTITY: Converter[_, _] = MathsBase.IDENTITY
//  val COMPLEX_TO_DOUBLE: Converter[Complex, java.lang.Double] = MathsBase.COMPLEX_TO_DOUBLE
//  val DOUBLE_TO_COMPLEX: Converter[java.lang.Double, Complex] = MathsBase.DOUBLE_TO_COMPLEX
//  val DOUBLE_TO_TVECTOR: Converter[java.lang.Double, TVector[_]] = MathsBase.DOUBLE_TO_TVECTOR
//  val TVECTOR_TO_DOUBLE: Converter[TVector[_], java.lang.Double] = MathsBase.TVECTOR_TO_DOUBLE
//  val COMPLEX_TO_TVECTOR: Converter[Complex, TVector[_]] = MathsBase.COMPLEX_TO_TVECTOR
//  val TVECTOR_TO_COMPLEX: Converter[TVector[_], Complex] = MathsBase.TVECTOR_TO_COMPLEX
//  val COMPLEX_TO_EXPR: Converter[Complex, Expr] = MathsBase.COMPLEX_TO_EXPR
//  val EXPR_TO_COMPLEX: Converter[Expr, Complex] = MathsBase.EXPR_TO_COMPLEX
//  val DOUBLE_TO_EXPR: Converter[java.lang.Double, Expr] = MathsBase.DOUBLE_TO_EXPR
//  val EXPR_TO_DOUBLE: Converter[Expr, java.lang.Double] = MathsBase.EXPR_TO_DOUBLE
//  //    public static  String getAxisLabel(int axis){
//  //        switch(axis){
//  //            case X_AXIS:return "X";
//  //            case Y_AXIS:return "Y";
//  //            case Z_AXIS:return "Z";
//  //        }
//  //        throw new IllegalArgumentException("Unknown Axis "+axis);
//  //    }
//  val $STRING: TypeName[String] = MathsBase.$STRING
//  val $COMPLEX: TypeName[Complex] = MathsBase.$COMPLEX
//  val $MATRIX: TypeName[Matrix] = MathsBase.$MATRIX
//  val $VECTOR: TypeName[Vector] = MathsBase.$VECTOR
//  val $CMATRIX: TypeName[TMatrix[Complex]] = MathsBase.$CMATRIX
//  val $CVECTOR: TypeName[TVector[Complex]] = MathsBase.$CVECTOR
//  val $DOUBLE: TypeName[java.lang.Double] = MathsBase.$DOUBLE
//  val $BOOLEAN: TypeName[java.lang.Boolean] = MathsBase.$BOOLEAN
//  val $POINT: TypeName[Point] = MathsBase.$POINT
//  val $FILE: TypeName[File] = MathsBase.$FILE
//  //</editor-fold>
//  val $INTEGER: TypeName[java.lang.Integer] = MathsBase.$INTEGER
//  val $LONG: TypeName[java.lang.Long] = MathsBase.$LONG
//  val $EXPR: TypeName[Expr] = MathsBase.$EXPR
//  val $CLIST: TypeName[TList[Complex]] = MathsBase.$CLIST
//  val $ELIST: TypeName[TList[Expr]] = MathsBase.$ELIST
//  val $DLIST: TypeName[TList[java.lang.Double]] = MathsBase.$DLIST
//  val $DLIST2: TypeName[TList[TList[java.lang.Double]]] = MathsBase.$DLIST2
//  val $ILIST: TypeName[TList[java.lang.Integer]] = MathsBase.$ILIST
//  val $BLIST: TypeName[TList[java.lang.Boolean]] = MathsBase.$BLIST
//  val $MLIST: TypeName[TList[Matrix]] = MathsBase.$MLIST
//  val Config: MathsConfig = MathsBase.Config
//  var DISTANCE_DOUBLE: DistanceStrategy[java.lang.Double] = MathsBase.DISTANCE_DOUBLE
//  var DISTANCE_COMPLEX: DistanceStrategy[Complex] = MathsBase.DISTANCE_COMPLEX
//  var DISTANCE_MATRIX: DistanceStrategy[Matrix] = MathsBase.DISTANCE_MATRIX
//  var DISTANCE_VECTOR: DistanceStrategy[Vector] = MathsBase.DISTANCE_VECTOR
//
//
//  def xdomain(min: Double, max: Double): Domain = {
//    return MathsBase.xdomain(min, max)
//  }
//
//  def ydomain(min: Double, max: Double): Domain = {
//    return MathsBase.ydomain(min, max)
//  }
//
//  def ydomain(min: DomainExpr, max: DomainExpr): DomainExpr = {
//    return MathsBase.ydomain(min, max)
//  }
//
//  def zdomain(min: Double, max: Double): Domain = {
//    return MathsBase.zdomain(min, max)
//  }
//
//  def zdomain(min: Expr, max: Expr): DomainExpr = {
//    return MathsBase.zdomain(min, max)
//  }
//
//  def domain(u: RightArrowUplet2.Double): Domain = {
//    return MathsBase.domain(u)
//  }
//
//  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
//    return MathsBase.domain(ux, uy)
//  }
//
//  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
//    return MathsBase.domain(ux, uy, uz)
//  }
//
//  def domain(u: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.domain(u)
//  }
//
//  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.domain(ux, uy)
//  }
//
//  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.domain(ux, uy, uz)
//  }
//
//  def domain(min: Expr, max: Expr): DomainExpr = {
//    return MathsBase.domain(min, max)
//  }
//
//  def domain(min: Double, max: Double): Domain = {
//    return MathsBase.domain(min, max)
//  }
//
//  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
//    return MathsBase.domain(xmin, xmax, ymin, ymax)
//  }
//
//  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
//    return MathsBase.domain(xmin, xmax, ymin, ymax)
//  }
//
//  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
//    return MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax)
//  }
//
//  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
//    return MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax)
//  }
//
//  def II(u: RightArrowUplet2.Double): Domain = {
//    return MathsBase.II(u)
//  }
//
//  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
//    return MathsBase.II(ux, uy)
//  }
//
//  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
//    return MathsBase.II(ux, uy, uz)
//  }
//
//  def II(u: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.II(u)
//  }
//
//  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.II(ux, uy)
//  }
//
//  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
//    return MathsBase.II(ux, uy, uz)
//  }
//
//  def II(min: Expr, max: Expr): DomainExpr = {
//    return MathsBase.II(min, max)
//  }
//
//  def II(min: Double, max: Double): Domain = {
//    return MathsBase.II(min, max)
//  }
//
//  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
//    return MathsBase.II(xmin, xmax, ymin, ymax)
//  }
//
//  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
//    return MathsBase.II(xmin, xmax, ymin, ymax)
//  }
//
//  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
//    return MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax)
//  }
//
//  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
//    return MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax)
//  }
//
//  def param(name: String): DoubleParam = {
//    return MathsBase.param(name)
//  }
//
//  def doubleParamSet(param: Param): DoubleArrayParamSet = {
//    return MathsBase.doubleParamSet(param)
//  }
//
//  def paramSet(param: Param, values: Array[Double]): DoubleArrayParamSet = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def paramSet(param: Param, values: Array[Float]): FloatArrayParamSet = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def floatParamSet(param: Param): FloatArrayParamSet = {
//    return MathsBase.floatParamSet(param)
//  }
//
//  def paramSet(param: Param, values: Array[Long]): LongArrayParamSet = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def longParamSet(param: Param): LongArrayParamSet = {
//    return MathsBase.longParamSet(param)
//  }
//
//  def paramSet[T](param: Param, values: Array[T]): ArrayParamSet[T] = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def objectParamSet[T](param: Param): ArrayParamSet[T] = {
//    return MathsBase.objectParamSet(param)
//  }
//
//  def paramSet(param: Param, values: Array[Int]): IntArrayParamSet[_ <: IntArrayParamSet[_ <: AnyRef]] = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def intParamSet(param: Param): IntArrayParamSet[_ <: IntArrayParamSet[_ <: AnyRef]] = {
//    return MathsBase.intParamSet(param)
//  }
//
//  def paramSet(param: Param, values: Array[Boolean]): BooleanArrayParamSet[_] = {
//    return MathsBase.paramSet(param, values)
//  }
//
//  def boolParamSet(param: Param): BooleanArrayParamSet[_] = {
//    return MathsBase.boolParamSet(param)
//  }
//
//  def xParamSet(xsamples: Int): XParamSet = {
//    return MathsBase.xParamSet(xsamples)
//  }
//
//  def xyParamSet(xsamples: Int, ysamples: Int): XParamSet = {
//    return MathsBase.xyParamSet(xsamples, ysamples)
//  }
//
//  def xyzParamSet(xsamples: Int, ysamples: Int, zsamples: Int): XParamSet = {
//    return MathsBase.xyzParamSet(xsamples, ysamples, zsamples)
//  }
//
//  def zerosMatrix(other: Matrix): Matrix = {
//    return MathsBase.zerosMatrix(other)
//  }
//
//  def constantMatrix(dim: Int, value: Complex): Matrix = {
//    return MathsBase.constantMatrix(dim, value)
//  }
//
//  def onesMatrix(dim: Int): Matrix = {
//    return MathsBase.onesMatrix(dim)
//  }
//
//  def onesMatrix(rows: Int, cols: Int): Matrix = {
//    return MathsBase.onesMatrix(rows, cols)
//  }
//
//  def constantMatrix(rows: Int, cols: Int, value: Complex): Matrix = {
//    return MathsBase.constantMatrix(rows, cols, value)
//  }
//
//  def zerosMatrix(dim: Int): Matrix = {
//    return MathsBase.zerosMatrix(dim)
//  }
//
//  def I(iValue: Array[Array[Complex]]): Matrix = {
//    return MathsBase.I(iValue)
//  }
//
//  def zerosMatrix(rows: Int, cols: Int): Matrix = {
//    return MathsBase.zerosMatrix(rows, cols)
//  }
//
//  def identityMatrix(c: Matrix): Matrix = {
//    return MathsBase.identityMatrix(c)
//  }
//
//  def NaNMatrix(dim: Int): Matrix = {
//    return MathsBase.NaNMatrix(dim)
//  }
//
//  def NaNMatrix(rows: Int, cols: Int): Matrix = {
//    return MathsBase.NaNMatrix(rows, cols)
//  }
//
//  def identityMatrix(dim: Int): Matrix = {
//    return MathsBase.identityMatrix(dim)
//  }
//
//  def identityMatrix(rows: Int, cols: Int): Matrix = {
//    return MathsBase.identityMatrix(rows, cols)
//  }
//
//  def matrix(matrix: Matrix): Matrix = {
//    return MathsBase.matrix(matrix)
//  }
//
//  def matrix(string: String): Matrix = {
//    return MathsBase.matrix(string)
//  }
//
//  def matrix(complex: Array[Array[Complex]]): Matrix = {
//    return MathsBase.matrix(complex)
//  }
//
//  def matrix(complex: Array[Array[Double]]): Matrix = {
//    return MathsBase.matrix(complex)
//  }
//
//  def matrix(rows: Int, cols: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.matrix(rows, cols, cellFactory)
//  }
//
//  def columnMatrix(values: Complex*): Matrix = {
//    return MathsBase.columnMatrix(values:_ *)
//  }
//
//  def columnMatrix(values: Double*): Matrix = {
//    return MathsBase.columnMatrix(values:_ *)
//  }
//
//  def rowMatrix(values: Double*): Matrix = {
//    return MathsBase.rowMatrix(values:_ *)
//  }
//
//  def rowMatrix(values: Complex*): Matrix = {
//    return MathsBase.rowMatrix(values:_ *)
//  }
//
//  def columnMatrix(rows: Int, cellFactory: VectorCell): Matrix = {
//    return MathsBase.columnMatrix(rows, cellFactory)
//  }
//
//  def rowMatrix(columns: Int, cellFactory: VectorCell): Matrix = {
//    return MathsBase.rowMatrix(columns, cellFactory)
//  }
//
//  def symmetricMatrix(rows: Int, cols: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.symmetricMatrix(rows, cols, cellFactory)
//  }
//
//  def hermitianMatrix(rows: Int, cols: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.hermitianMatrix(rows, cols, cellFactory)
//  }
//
//  def diagonalMatrix(rows: Int, cols: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.diagonalMatrix(rows, cols, cellFactory)
//  }
//
//  def diagonalMatrix(rows: Int, cellFactory: VectorCell): Matrix = {
//    return MathsBase.diagonalMatrix(rows, cellFactory)
//  }
//
//  def diagonalMatrix(c: Complex*): Matrix = {
//    return MathsBase.diagonalMatrix(c:_ *)
//  }
//
//  def matrix(dim: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.matrix(dim, cellFactory)
//  }
//
//  def matrix(rows: Int, columns: Int): Matrix = {
//    return MathsBase.matrix(rows, columns)
//  }
//
//  def symmetricMatrix(dim: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.symmetricMatrix(dim, cellFactory)
//  }
//
//  def hermitianMatrix(dim: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.hermitianMatrix(dim, cellFactory)
//  }
//
//  def diagonalMatrix(dim: Int, cellFactory: MatrixCell): Matrix = {
//    return MathsBase.diagonalMatrix(dim, cellFactory)
//  }
//
//  def randomRealMatrix(m: Int, n: Int): Matrix = {
//    return MathsBase.randomRealMatrix(m, n)
//  }
//
//  def randomRealMatrix(m: Int, n: Int, min: Int, max: Int): Matrix = {
//    return MathsBase.randomRealMatrix(m, n, min, max)
//  }
//
//  def randomRealMatrix(m: Int, n: Int, min: Double, max: Double): Matrix = {
//    return MathsBase.randomRealMatrix(m, n, min, max)
//  }
//
//  def randomImagMatrix(m: Int, n: Int, min: Double, max: Double): Matrix = {
//    return MathsBase.randomImagMatrix(m, n, min, max)
//  }
//
//  def randomImagMatrix(m: Int, n: Int, min: Int, max: Int): Matrix = {
//    return MathsBase.randomImagMatrix(m, n, min, max)
//  }
//
//  def randomMatrix(m: Int, n: Int, minReal: Int, maxReal: Int, minImag: Int, maxImag: Int): Matrix = {
//    return MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
//  }
//
//  def randomMatrix(m: Int, n: Int, minReal: Double, maxReal: Double, minImag: Double, maxImag: Double): Matrix = {
//    return MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
//  }
//
//  def randomMatrix(m: Int, n: Int, min: Double, max: Double): Matrix = {
//    return MathsBase.randomMatrix(m, n, min, max)
//  }
//
//  def randomMatrix(m: Int, n: Int, min: Int, max: Int): Matrix = {
//    return MathsBase.randomMatrix(m, n, min, max)
//  }
//
//  def randomImagMatrix(m: Int, n: Int): Matrix = {
//    return MathsBase.randomImagMatrix(m, n)
//  }
//
//  @throws[UncheckedIOException]
//  def loadTMatrix[T](componentType: TypeName[T], file: File): TMatrix[T] = {
//    return MathsBase.loadTMatrix(componentType, file)
//  }
//
//  @throws[UncheckedIOException]
//  def loadMatrix(file: File): Matrix = {
//    return MathsBase.loadMatrix(file)
//  }
//
//  @throws[UncheckedIOException]
//  def matrix(file: File): Matrix = {
//    return MathsBase.matrix(file)
//  }
//
//  @throws[UncheckedIOException]
//  def storeMatrix(m: Matrix, file: String): Unit = {
//    MathsBase.storeMatrix(m, file)
//  }
//
//  @throws[UncheckedIOException]
//  def storeMatrix(m: Matrix, file: File): Unit = {
//    MathsBase.storeMatrix(m, file)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalMatrix(file: String, item: TItem[Matrix]): Matrix = {
//    return MathsBase.loadOrEvalMatrix(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalVector(file: String, item: TItem[TVector[Complex]]): Vector = {
//    return MathsBase.loadOrEvalVector(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalMatrix(file: File, item: TItem[Matrix]): Matrix = {
//    return MathsBase.loadOrEvalMatrix(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalVector(file: File, item: TItem[TVector[Complex]]): Vector = {
//    return MathsBase.loadOrEvalVector(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalTMatrix[T](file: String, item: TItem[TMatrix[T]]): TMatrix[_] = {
//    return MathsBase.loadOrEvalTMatrix(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalTVector[T](file: String, item: TItem[TVector[T]]): TVector[T] = {
//    return MathsBase.loadOrEvalTVector(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalTMatrix[T](file: File, item: TItem[TMatrix[T]]): TMatrix[T] = {
//    return MathsBase.loadOrEvalTMatrix(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEvalTVector[T](file: File, item: TItem[TVector[T]]): TVector[_] = {
//    return MathsBase.loadOrEvalTVector(file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadOrEval[T](`type`: TypeName[T], file: File, item: TItem[T]): T = {
//    return MathsBase.loadOrEval(`type`, file, item)
//  }
//
//  @throws[UncheckedIOException]
//  def loadMatrix(file: String): Matrix = {
//    return MathsBase.loadMatrix(file)
//  }
//
//  def inv(c: Matrix): Matrix = {
//    return MathsBase.inv(c)
//  }
//
//  def tr(c: Matrix): Matrix = {
//    return MathsBase.tr(c)
//  }
//
//  def trh(c: Matrix): Matrix = {
//    return MathsBase.trh(c)
//  }
//
//  def transpose(c: Matrix): Matrix = {
//    return MathsBase.transpose(c)
//  }
//
//  def transposeHermitian(c: Matrix): Matrix = {
//    return MathsBase.transposeHermitian(c)
//  }
//
//  def rowVector(elems: Array[Complex]): Vector = {
//    return MathsBase.rowVector(elems)
//  }
//
//  def constantColumnVector(size: Int, c: Complex): Vector = {
//    return MathsBase.constantColumnVector(size, c)
//  }
//
//  def constantRowVector(size: Int, c: Complex): Vector = {
//    return MathsBase.constantRowVector(size, c)
//  }
//
//  def zerosVector(size: Int): Vector = {
//    return MathsBase.zerosVector(size)
//  }
//
//  def zerosColumnVector(size: Int): Vector = {
//    return MathsBase.zerosColumnVector(size)
//  }
//
//  def zerosRowVector(size: Int): Vector = {
//    return MathsBase.zerosRowVector(size)
//  }
//
//  def NaNColumnVector(dim: Int): Vector = {
//    return MathsBase.NaNColumnVector(dim)
//  }
//
//  def NaNRowVector(dim: Int): Vector = {
//    return MathsBase.NaNRowVector(dim)
//  }
//
//  def columnVector(expr: Array[Expr]): TVector[Expr] = {
//    return MathsBase.columnVector(expr)
//  }
//
//  def rowVector(expr: Array[Expr]): TVector[Expr] = {
//    return MathsBase.rowVector(expr)
//  }
//
//  def columnEVector(rows: Int, cellFactory: TVectorCell[Expr]): TVector[Expr] = {
//    return MathsBase.columnEVector(rows, cellFactory)
//  }
//
//  def rowEVector(rows: Int, cellFactory: TVectorCell[Expr]): TVector[Expr] = {
//    return MathsBase.rowEVector(rows, cellFactory)
//  }
//
//  def updatableOf[T](vector: TVector[T]): TVector[T] = {
//    return MathsBase.updatableOf(vector)
//  }
//
//  def copyOf(`val`: Array[Array[Complex]]): Array[Array[Complex]] = {
//    return MathsBase.copyOf(`val`)
//  }
//
//  def copyOf(`val`: Array[Complex]): Array[Complex] = {
//    return MathsBase.copyOf(`val`)
//  }
//
//  def copyOf[T](vector: TVector[T]): TList[T] = {
//    return MathsBase.copyOf(vector)
//  }
//
//  def columnTVector[T](cls: TypeName[T], cellFactory: TVectorModel[T]): TVector[T] = {
//    return MathsBase.columnTVector(cls, cellFactory)
//  }
//
//  def rowTVector[T](cls: TypeName[T], cellFactory: TVectorModel[T]): TVector[T] = {
//    return MathsBase.rowTVector(cls, cellFactory)
//  }
//
//  def columnTVector[T](cls: TypeName[T], rows: Int, cellFactory: TVectorCell[T]): TVector[T] = {
//    return MathsBase.columnTVector(cls, rows, cellFactory)
//  }
//
//  def rowTVector[T](cls: TypeName[T], rows: Int, cellFactory: TVectorCell[T]): TVector[T] = {
//    return MathsBase.rowTVector(cls, rows, cellFactory)
//  }
//
//  def columnVector(rows: Int, cellFactory: VectorCell): Vector = {
//    return MathsBase.columnVector(rows, cellFactory)
//  }
//
//  def rowVector(columns: Int, cellFactory: VectorCell): Vector = {
//    return MathsBase.rowVector(columns, cellFactory)
//  }
//
//  def columnVector(elems: Complex*): Vector = {
//    return MathsBase.columnVector(elems:_ *)
//  }
//
//  def columnVector(elems: Array[Double]): Vector = {
//    return MathsBase.columnVector(elems)
//  }
//
//  def rowVector(elems: Array[Double]): Vector = {
//    return MathsBase.rowVector(elems)
//  }
//
//  def column(elems: Array[Complex]): Vector = {
//    return MathsBase.column(elems)
//  }
//
//  def row(elems: Array[Complex]): Vector = {
//    return MathsBase.row(elems)
//  }
//
//  def trh(c: Vector): Vector = {
//    return MathsBase.trh(c)
//  }
//
//  def tr(c: Vector): Vector = {
//    return MathsBase.tr(c)
//  }
//
//  def I(iValue: Double): Complex = {
//    return MathsBase.I(iValue)
//  }
//
//  def abs(a: Complex): Complex = {
//    return MathsBase.abs(a)
//  }
//
//  def absdbl(a: Complex): Double = {
//    return MathsBase.absdbl(a)
//  }
//
//  def getColumn(a: Array[Array[Double]], index: Int): Array[Double] = {
//    return MathsBase.getColumn(a, index)
//  }
//
//  def dtimes(min: Double, max: Double, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Double] = {
//    return MathsBase.dtimes(min, max, times, maxTimes, strategy)
//  }
//
//  def dtimes(min: Double, max: Double, times: Int): Array[Double] = {
//    return MathsBase.dtimes(min, max, times)
//  }
//
//  def ftimes(min: Float, max: Float, times: Int): Array[Float] = {
//    return MathsBase.ftimes(min, max, times)
//  }
//
//  def ltimes(min: Long, max: Long, times: Int): Array[Long] = {
//    return MathsBase.ltimes(min, max, times)
//  }
//
//  def lsteps(min: Long, max: Long, step: Long): Array[Long] = {
//    return MathsBase.lsteps(min, max, step)
//  }
//
//  def itimes(min: Int, max: Int, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Int] = {
//    return MathsBase.itimes(min, max, times, maxTimes, strategy)
//  }
//
//  def dsteps(max: Int): Array[Double] = {
//    return MathsBase.dsteps(max)
//  }
//
//  def dsteps(min: Double, max: Double): Array[Double] = {
//    return MathsBase.dsteps(min, max)
//  }
//
//  def dstepsLength(min: Double, max: Double, step: Double): Double = {
//    return MathsBase.dstepsLength(min, max, step)
//  }
//
//  def dstepsElement(min: Double, max: Double, step: Double, index: Int): Double = {
//    return MathsBase.dstepsElement(min, max, step, index)
//  }
//
//  def dsteps(min: Double, max: Double, step: Double): Array[Double] = {
//    return MathsBase.dsteps(min, max, step)
//  }
//
//  def fsteps(min: Float, max: Float, step: Float): Array[Float] = {
//    return MathsBase.fsteps(min, max, step)
//  }
//
//  def isteps(min: Int, max: Int, step: Int): Array[Int] = {
//    return MathsBase.isteps(min, max, step)
//  }
//
//  def isteps(min: Int, max: Int, step: Int, filter: IntFilter): Array[Int] = {
//    return MathsBase.isteps(min, max, step, filter)
//  }
//
//  def itimes(min: Int, max: Int, times: Int): Array[Int] = {
//    return MathsBase.itimes(min, max, times)
//  }
//
//  def isteps(max: Int): Array[Int] = {
//    return MathsBase.isteps(max)
//  }
//
//  def isteps(min: Int, max: Int): Array[Int] = {
//    return MathsBase.isteps(min, max)
//  }
//
//  def itimes(min: Int, max: Int): Array[Int] = {
//    return MathsBase.itimes(min, max)
//  }
//
//  def hypot(a: Double, b: Double): Double = {
//    return MathsBase.hypot(a, b)
//  }
//
//  def sqr(d: Complex): Complex = {
//    return MathsBase.sqr(d)
//  }
//
//  def sqr(d: Double): Double = {
//    return MathsBase.sqr(d)
//  }
//
//  def sqr(d: Int): Int = {
//    return MathsBase.sqr(d)
//  }
//
//  def sqr(d: Long): Long = {
//    return MathsBase.sqr(d)
//  }
//
//  def sqr(d: Float): Float = {
//    return MathsBase.sqr(d)
//  }
//
//  def sincard(value: Double): Double = {
//    return MathsBase.sincard(value)
//  }
//
//  def minusOnePower(pow: Int): Int = {
//    return MathsBase.minusOnePower(pow)
//  }
//
//  def exp(c: Complex): Complex = {
//    return MathsBase.exp(c)
//  }
//
//  def sin(c: Complex): Complex = {
//    return MathsBase.sin(c)
//  }
//
//  def sinh(c: Complex): Complex = {
//    return MathsBase.sinh(c)
//  }
//
//  def cos(c: Complex): Complex = {
//    return MathsBase.cos(c)
//  }
//
//  def log(c: Complex): Complex = {
//    return MathsBase.log(c)
//  }
//
//  def log10(c: Complex): Complex = {
//    return MathsBase.log10(c)
//  }
//
//  def log10(c: Double): Double = {
//    return MathsBase.log10(c)
//  }
//
//  def log(c: Double): Double = {
//    return MathsBase.log(c)
//  }
//
//  def acotan(c: Double): Double = {
//    return MathsBase.acotan(c)
//  }
//
//  def exp(c: Double): Double = {
//    return MathsBase.exp(c)
//  }
//
//  def arg(c: Double): Double = {
//    return MathsBase.arg(c)
//  }
//
//  def db(c: Complex): Complex = {
//    return MathsBase.db(c)
//  }
//
//  def db2(c: Complex): Complex = {
//    return MathsBase.db2(c)
//  }
//
//  def cosh(c: Complex): Complex = {
//    return MathsBase.cosh(c)
//  }
//
//  def csum(c: Complex*): Complex = {
//    return MathsBase.csum(c:_ *)
//  }
//
//  def sum(c: Complex*): Complex = {
//    return MathsBase.sum(c:_ *)
//  }
//
//  def csum(c: TVectorModel[Complex]): Complex = {
//    return MathsBase.csum(c)
//  }
//
//  def csum(size: Int, c: TVectorCell[Complex]): Complex = {
//    return MathsBase.csum(size, c)
//  }
//
//  @throws[ArithmeticException]
//  def chbevl(x: Double, coef: Array[Double], N: Int): Double = {
//    return MathsBase.chbevl(x, coef, N)
//  }
//
//  def pgcd(a: Long, b: Long): Long = {
//    return MathsBase.pgcd(a, b)
//  }
//
//  def pgcd(a: Int, b: Int): Int = {
//    return MathsBase.pgcd(a, b)
//  }
//
//  def toDouble(c: Array[Array[Complex]], toDoubleConverter: PlotDoubleConverter): Array[Array[Double]] = {
//    return MathsBase.toDouble(c, toDoubleConverter)
//  }
//
//  def toDouble(c: Array[Complex], toDoubleConverter: PlotDoubleConverter): Array[Double] = {
//    return MathsBase.toDouble(c, toDoubleConverter)
//  }
//
//  def rangeCC(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
//    return MathsBase.rangeCC(orderedValues, min, max)
//  }
//
//  def rangeCO(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
//    return MathsBase.rangeCO(orderedValues, min, max)
//  }
//
//  def csqrt(d: Double): Complex = {
//    return MathsBase.csqrt(d)
//  }
//
//  def sqrt(d: Complex): Complex = {
//    return MathsBase.sqrt(d)
//  }
//
//  def dsqrt(d: Double): Double = {
//    return MathsBase.dsqrt(d)
//  }
//
//  def cotanh(c: Complex): Complex = {
//    return MathsBase.cotanh(c)
//  }
//
//  def tanh(c: Complex): Complex = {
//    return MathsBase.tanh(c)
//  }
//
//  def inv(c: Complex): Complex = {
//    return MathsBase.inv(c)
//  }
//
//  def tan(c: Complex): Complex = {
//    return MathsBase.tan(c)
//  }
//
//  def cotan(c: Complex): Complex = {
//    return MathsBase.cotan(c)
//  }
//
//  def vector(v: TVector[_]): Vector = {
//    return MathsBase.vector(v)
//  }
//
//  def pow(a: Complex, b: Complex): Complex = {
//    return MathsBase.pow(a, b)
//  }
//
//  def div(a: Complex, b: Complex): Complex = {
//    return MathsBase.div(a, b)
//  }
//
//  def add(a: Complex, b: Complex): Complex = {
//    return MathsBase.add(a, b)
//  }
//
//  def sub(a: Complex, b: Complex): Complex = {
//    return MathsBase.sub(a, b)
//  }
//
//  def norm(a: Matrix): Double = {
//    return MathsBase.norm(a)
//  }
//
//  def norm(a: Vector): Double = {
//    return MathsBase.norm(a)
//  }
//
//  def norm1(a: Matrix): Double = {
//    return MathsBase.norm1(a)
//  }
//
//  def norm2(a: Matrix): Double = {
//    return MathsBase.norm2(a)
//  }
//
//  def norm3(a: Matrix): Double = {
//    return MathsBase.norm3(a)
//  }
//
//  def normInf(a: Matrix): Double = {
//    return MathsBase.normInf(a)
//  }
//
//  def complex(fx: DoubleToDouble): DoubleToComplex = {
//    return MathsBase.complex(fx)
//  }
//
//  def complex(fx: DoubleToDouble, fy: DoubleToDouble): DoubleToComplex = {
//    return MathsBase.complex(fx, fy)
//  }
//
//  def randomDouble(value: Double): Double = {
//    return MathsBase.randomDouble(value)
//  }
//
//  def randomDouble(min: Double, max: Double): Double = {
//    return MathsBase.randomDouble(min, max)
//  }
//
//  def randomInt(value: Int): Int = {
//    return MathsBase.randomInt(value)
//  }
//
//  def randomInt(min: Int, max: Int): Int = {
//    return MathsBase.randomInt(min, max)
//  }
//
//  def randomComplex: Complex = {
//    return MathsBase.randomComplex
//  }
//
//  def randomBoolean: Boolean = {
//    return MathsBase.randomBoolean
//  }
//
//  def cross(x: Array[Double], y: Array[Double]): Array[Array[Double]] = {
//    return MathsBase.cross(x, y)
//  }
//
//  def cross(x: Array[Double], y: Array[Double], z: Array[Double]): Array[Array[Double]] = {
//    return MathsBase.cross(x, y, z)
//  }
//
//  def cross(x: Array[Double], y: Array[Double], z: Array[Double], filter: Double3Filter): Array[Array[Double]] = {
//    return MathsBase.cross(x, y, z, filter)
//  }
//
//  def cross(x: Array[Double], y: Array[Double], filter: Double2Filter): Array[Array[Double]] = {
//    return MathsBase.cross(x, y, filter)
//  }
//
//  def cross(x: Array[Int], y: Array[Int]): Array[Array[Int]] = {
//    return MathsBase.cross(x, y)
//  }
//
//  def cross(x: Array[Int], y: Array[Int], z: Array[Int]): Array[Array[Int]] = {
//    return MathsBase.cross(x, y, z)
//  }
//
//  def sineSeq(borders: String, m: Int, n: Int, domain: Domain): TList[_] = {
//    return MathsBase.sineSeq(borders, m, n, domain)
//  }
//
//  def sineSeq(borders: String, m: Int, n: Int, domain: Domain, plane: PlaneAxis): TList[_] = {
//    return MathsBase.sineSeq(borders, m, n, domain, plane)
//  }
//
//  def sineSeq(borders: String, m: DoubleParam, n: DoubleParam, domain: Domain): Expr = {
//    return MathsBase.sineSeq(borders, m, n, domain)
//  }
//
//  def rooftop(borders: String, nx: Int, ny: Int, domain: Domain): Expr = {
//    return MathsBase.rooftop(borders, nx, ny, domain)
//  }
//
//  def any(e: Double): Any = {
//    return MathsBase.any(e)
//  }
//
//  def any(e: Expr): Any = {
//    return MathsBase.any(e)
//  }
//
//  def any(e: Double): Any = {
//    return MathsBase.any(e)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, mmax: Int, n: DoubleParam, nmax: Int, filter: Int2Filter): TList[Expr] = {
//    return MathsBase.seq(pattern, m, mmax, n, nmax, filter)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, max: Int, filter: IntFilter): TList[Expr] = {
//    return MathsBase.seq(pattern, m, max, filter)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, mmax: Int, n: DoubleParam, nmax: Int, p: DoubleParam, pmax: Int, filter: Int3Filter): TList[Expr] = {
//    return MathsBase.seq(pattern, m, mmax, n, nmax, p, pmax, filter)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, mmax: Int, n: DoubleParam, nmax: Int): TList[Expr] = {
//    return MathsBase.seq(pattern, m, mmax, n, nmax)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): TList[Expr] = {
//    return MathsBase.seq(pattern, m, mvalues, n, nvalues)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, values: Array[Array[Double]]): TList[Expr] = {
//    return MathsBase.seq(pattern, m, n, values)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, p: DoubleParam, values: Array[Array[Double]]): TList[Expr] = {
//    return MathsBase.seq(pattern, m, n, p, values)
//  }
//
//  def seq(pattern: Expr, m: Array[DoubleParam], values: Array[Array[Double]]): TList[Expr] = {
//    return MathsBase.seq(pattern, m, values)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, min: Int, max: Int): TList[Expr] = {
//    return MathsBase.seq(pattern, m, min, max)
//  }
//
//  def seq(pattern: Expr, m: DoubleParam, values: Array[Double]): TList[Expr] = {
//    return MathsBase.seq(pattern, m, values)
//  }
//
//  def matrix(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): ExprMatrix2 = {
//    return MathsBase.matrix(pattern, m, mvalues, n, nvalues)
//  }
//
//  def cube(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double], p: DoubleParam, pvalues: Array[Double]): ExprCube = {
//    return MathsBase.cube(pattern, m, mvalues, n, nvalues, p, pvalues)
//  }
//
//  def derive(f: Expr, axis: Axis): Expr = {
//    return MathsBase.derive(f, axis)
//  }
//
//  def isReal(e: Expr): Boolean = {
//    return MathsBase.isReal(e)
//  }
//
//  def isImag(e: Expr): Boolean = {
//    return MathsBase.isImag(e)
//  }
//
//  def abs(e: Expr): Expr = {
//    return MathsBase.abs(e)
//  }
//
//  def db(e: Expr): Expr = {
//    return MathsBase.db(e)
//  }
//
//  def db2(e: Expr): Expr = {
//    return MathsBase.db2(e)
//  }
//
//  def complex(e: Int): Complex = {
//    return MathsBase.complex(e)
//  }
//
//  def complex(e: Double): Complex = {
//    return MathsBase.complex(e)
//  }
//
//  def complex(a: Double, b: Double): Complex = {
//    return MathsBase.complex(a, b)
//  }
//
//  def Double(e: Expr): Double = {
//    return MathsBase.Double(e)
//  }
//
//  def real(e: Expr): Expr = {
//    return MathsBase.real(e)
//  }
//
//  def imag(e: Expr): Expr = {
//    return MathsBase.imag(e)
//  }
//
//  def Complex(e: Expr): Complex = {
//    return MathsBase.Complex(e)
//  }
//
//  def complex(e: Expr): Complex = {
//    return MathsBase.complex(e)
//  }
//
//  def Complex(e: Double): Complex = {
//    return MathsBase.Complex(e)
//  }
//
//  def complex(e: Double): Complex = {
//    return MathsBase.complex(e)
//  }
//
//  def doubleValue(e: Expr): Double = {
//    return MathsBase.doubleValue(e)
//  }
//
//  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double], dx: Double, dy: Double, dz: Double, axis1: Axis, axis2: Axis, axis3: Axis): Discrete = {
//    return MathsBase.discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3)
//  }
//
//  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double], dx: Double, dy: Double, dz: Double): Discrete = {
//    return MathsBase.discrete(domain, model, x, y, z, dx, dy, dz)
//  }
//
//  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double]): Discrete = {
//    return MathsBase.discrete(domain, model, x, y, z)
//  }
//
//  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], dx: Double, dy: Double, dz: Double): Discrete = {
//    return MathsBase.discrete(domain, model, dx, dy, dz)
//  }
//
//  def discrete(model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double]): Discrete = {
//    return MathsBase.discrete(model, x, y, z)
//  }
//
//  def discrete(model: Array[Array[Complex]], x: Array[Double], y: Array[Double]): Discrete = {
//    return MathsBase.discrete(model, x, y)
//  }
//
//  def discrete(expr: Expr, xsamples: Array[Double], ysamples: Array[Double], zsamples: Array[Double]): Expr = {
//    return MathsBase.discrete(expr, xsamples, ysamples, zsamples)
//  }
//
//  def discrete(expr: Expr, samples: Samples): Expr = {
//    return MathsBase.discrete(expr, samples)
//  }
//
//  def abssqr(e: Expr): Expr = {
//    return MathsBase.abssqr(e)
//  }
//
//  def adaptiveEval(expr: Expr, config: AdaptiveConfig): AdaptiveResult1 = {
//    return MathsBase.adaptiveEval(expr, config)
//  }
//
//  def adaptiveEval[T](expr: AdaptiveFunction1[T], distance: DistanceStrategy[T], domain: DomainX, config: AdaptiveConfig): AdaptiveResult1 = {
//    return MathsBase.adaptiveEval(expr, distance, domain, config)
//  }
//
//  def discrete(expr: Expr): Discrete = {
//    return MathsBase.discrete(expr)
//  }
//
//  def vdiscrete(expr: Expr): VDiscrete = {
//    return MathsBase.vdiscrete(expr)
//  }
//
//  def discrete(expr: Expr, xSamples: Int): Expr = {
//    return MathsBase.discrete(expr, xSamples)
//  }
//
//  def discrete(expr: Expr, xSamples: Int, ySamples: Int): Expr = {
//    return MathsBase.discrete(expr, xSamples, ySamples)
//  }
//
//  def discrete(expr: Expr, xSamples: Int, ySamples: Int, zSamples: Int): Expr = {
//    return MathsBase.discrete(expr, xSamples, ySamples, zSamples)
//  }
//
//  def axis(e: Axis): AxisFunction = {
//    return MathsBase.axis(e)
//  }
//
//  def transformAxis(e: Expr, a1: AxisFunction, a2: AxisFunction, a3: AxisFunction): Expr = {
//    return MathsBase.transformAxis(e, a1, a2, a3)
//  }
//
//  def transformAxis(e: Expr, a1: Axis, a2: Axis, a3: Axis): Expr = {
//    return MathsBase.transformAxis(e, a1, a2, a3)
//  }
//
//  def transformAxis(e: Expr, a1: AxisFunction, a2: AxisFunction): Expr = {
//    return MathsBase.transformAxis(e, a1, a2)
//  }
//
//  def transformAxis(e: Expr, a1: Axis, a2: Axis): Expr = {
//    return MathsBase.transformAxis(e, a1, a2)
//  }
//
//  def sin2(d: Double): Double = {
//    return MathsBase.sin2(d)
//  }
//
//  def cos2(d: Double): Double = {
//    return MathsBase.cos2(d)
//  }
//
//  def isInt(d: Double): Boolean = {
//    return MathsBase.isInt(d)
//  }
//
//  def lcast[T](o: Any, `type`: Class[T]): T = {
//    return MathsBase.lcast(o, `type`)
//  }
//
//  def dump(o: Any): String = {
//    return MathsBase.dump(o)
//  }
//
//  def dumpSimple(o: Any): String = {
//    return MathsBase.dumpSimple(o)
//  }
//
//  def expr(value: Double, geometry: Geometry): DoubleToDouble = {
//    return MathsBase.expr(value, geometry)
//  }
//
//  def expr(value: Double, geometry: Domain): DoubleToDouble = {
//    return MathsBase.expr(value, geometry)
//  }
//
//  def expr(domain: Domain): DoubleToDouble = {
//    return MathsBase.expr(domain)
//  }
//
//  def expr(domain: Geometry): DoubleToDouble = {
//    return MathsBase.expr(domain)
//  }
//
//  def expr(a: Complex, geometry: Geometry): Expr = {
//    return MathsBase.expr(a, geometry)
//  }
//
//  def expr(a: Complex, geometry: Domain): Expr = {
//    return MathsBase.expr(a, geometry)
//  }
//
//  def preload[T <: Expr](list: TList[T]): TList[T] = {
//    return MathsBase.preload(list)
//  }
//
//  def withCache[T <: Expr](list: TList[T]): TList[T] = {
//    return MathsBase.withCache(list)
//  }
//
//  def abs[T](a: TList[T]): TList[T] = {
//    return MathsBase.abs(a)
//  }
//
//  def db[T](a: TList[T]): TList[T] = {
//    return MathsBase.db(a)
//  }
//
//  def db2[T](a: TList[T]): TList[T] = {
//    return MathsBase.db2(a)
//  }
//
//  def real[T](a: TList[T]): TList[T] = {
//    return MathsBase.real(a)
//  }
//
//  def imag[T](a: TList[T]): TList[T] = {
//    return MathsBase.imag(a)
//  }
//
//  def real(a: Complex): Double = {
//    return MathsBase.real(a)
//  }
//
//  def imag(a: Complex): Double = {
//    return MathsBase.imag(a)
//  }
//
//  def almostEqualRelative(a: Float, b: Float, maxRelativeError: Float): Boolean = {
//    return MathsBase.almostEqualRelative(a, b, maxRelativeError)
//  }
//
//  def almostEqualRelative(a: Double, b: Double, maxRelativeError: Double): Boolean = {
//    return MathsBase.almostEqualRelative(a, b, maxRelativeError)
//  }
//
//  def almostEqualRelative(a: Complex, b: Complex, maxRelativeError: Double): Boolean = {
//    return MathsBase.almostEqualRelative(a, b, maxRelativeError)
//  }
//
//  def dtimes(param: Param, min: Double, max: Double, times: Int): DoubleArrayParamSet = {
//    return MathsBase.dtimes(param, min, max, times)
//  }
//
//  def dsteps(param: Param, min: Double, max: Double, step: Double): DoubleArrayParamSet = {
//    return MathsBase.dsteps(param, min, max, step)
//  }
//
//  def itimes(param: Param, min: Int, max: Int, times: Int): IntArrayParamSet[_ <: IntArrayParamSet[_ <: AnyRef]] = {
//    return MathsBase.itimes(param, min, max, times)
//  }
//
//  def isteps(param: Param, min: Int, max: Int, step: Int): IntArrayParamSet[_ <: IntArrayParamSet[_ <: AnyRef]] = {
//    return MathsBase.isteps(param, min, max, step)
//  }
//
//  def ftimes(param: Param, min: Int, max: Int, times: Int): FloatArrayParamSet = {
//    return MathsBase.ftimes(param, min, max, times)
//  }
//
//  def fsteps(param: Param, min: Int, max: Int, step: Int): FloatArrayParamSet = {
//    return MathsBase.fsteps(param, min, max, step)
//  }
//
//  def ltimes(param: Param, min: Int, max: Int, times: Int): LongArrayParamSet = {
//    return MathsBase.ltimes(param, min, max, times)
//  }
//
//  def lsteps(param: Param, min: Int, max: Int, step: Long): LongArrayParamSet = {
//    return MathsBase.lsteps(param, min, max, step)
//  }
//
//  def sin(v: Vector): Vector = {
//    return MathsBase.sin(v)
//  }
//
//  def cos(v: Vector): Vector = {
//    return MathsBase.cos(v)
//  }
//
//  def tan(v: Vector): Vector = {
//    return MathsBase.tan(v)
//  }
//
//  def cotan(v: Vector): Vector = {
//    return MathsBase.cotan(v)
//  }
//
//  def tanh(v: Vector): Vector = {
//    return MathsBase.tanh(v)
//  }
//
//  def cotanh(v: Vector): Vector = {
//    return MathsBase.cotanh(v)
//  }
//
//  def cosh(v: Vector): Vector = {
//    return MathsBase.cosh(v)
//  }
//
//  def sinh(v: Vector): Vector = {
//    return MathsBase.sinh(v)
//  }
//
//  def log(v: Vector): Vector = {
//    return MathsBase.log(v)
//  }
//
//  def log10(v: Vector): Vector = {
//    return MathsBase.log10(v)
//  }
//
//  def db(v: Vector): Vector = {
//    return MathsBase.db(v)
//  }
//
//  def exp(v: Vector): Vector = {
//    return MathsBase.exp(v)
//  }
//
//  def acosh(v: Vector): Vector = {
//    return MathsBase.acosh(v)
//  }
//
//  def acos(v: Vector): Vector = {
//    return MathsBase.acos(v)
//  }
//
//  def asinh(v: Vector): Vector = {
//    return MathsBase.asinh(v)
//  }
//
//  def asin(v: Vector): Vector = {
//    return MathsBase.asin(v)
//  }
//
//  def atan(v: Vector): Vector = {
//    return MathsBase.atan(v)
//  }
//
//  def acotan(v: Vector): Vector = {
//    return MathsBase.acotan(v)
//  }
//
//  def imag(v: Vector): Vector = {
//    return MathsBase.imag(v)
//  }
//
//  def real(v: Vector): Vector = {
//    return MathsBase.real(v)
//  }
//
//  def abs(v: Vector): Vector = {
//    return MathsBase.abs(v)
//  }
//
//  def abs(v: Array[Complex]): Array[Complex] = {
//    return MathsBase.abs(v)
//  }
//
//  def avg(v: Vector): Complex = {
//    return MathsBase.avg(v)
//  }
//
//  def sum(v: Vector): Complex = {
//    return MathsBase.sum(v)
//  }
//
//  def prod(v: Vector): Complex = {
//    return MathsBase.prod(v)
//  }
//
//  def abs(v: Matrix): Matrix = {
//    return MathsBase.abs(v)
//  }
//
//  def sin(v: Matrix): Matrix = {
//    return MathsBase.sin(v)
//  }
//
//  def cos(v: Matrix): Matrix = {
//    return MathsBase.cos(v)
//  }
//
//  def tan(v: Matrix): Matrix = {
//    return MathsBase.tan(v)
//  }
//
//  def cotan(v: Matrix): Matrix = {
//    return MathsBase.cotan(v)
//  }
//
//  def tanh(v: Matrix): Matrix = {
//    return MathsBase.tanh(v)
//  }
//
//  def cotanh(v: Matrix): Matrix = {
//    return MathsBase.cotanh(v)
//  }
//
//  def cosh(v: Matrix): Matrix = {
//    return MathsBase.cosh(v)
//  }
//
//  def sinh(v: Matrix): Matrix = {
//    return MathsBase.sinh(v)
//  }
//
//  def log(v: Matrix): Matrix = {
//    return MathsBase.log(v)
//  }
//
//  def log10(v: Matrix): Matrix = {
//    return MathsBase.log10(v)
//  }
//
//  def db(v: Matrix): Matrix = {
//    return MathsBase.db(v)
//  }
//
//  def exp(v: Matrix): Matrix = {
//    return MathsBase.exp(v)
//  }
//
//  def acosh(v: Matrix): Matrix = {
//    return MathsBase.acosh(v)
//  }
//
//  def acos(v: Matrix): Matrix = {
//    return MathsBase.acos(v)
//  }
//
//  def asinh(v: Matrix): Matrix = {
//    return MathsBase.asinh(v)
//  }
//
//  def asin(v: Matrix): Matrix = {
//    return MathsBase.asin(v)
//  }
//
//  def atan(v: Matrix): Matrix = {
//    return MathsBase.atan(v)
//  }
//
//  def acotan(v: Matrix): Matrix = {
//    return MathsBase.acotan(v)
//  }
//
//  def imag(v: Matrix): Matrix = {
//    return MathsBase.imag(v)
//  }
//
//  def real(v: Matrix): Matrix = {
//    return MathsBase.real(v)
//  }
//
//  def real(v: Array[Complex]): Array[Complex] = {
//    return MathsBase.real(v)
//  }
//
//  def realdbl(v: Array[Complex]): Array[Double] = {
//    return MathsBase.realdbl(v)
//  }
//
//  def imag(v: Array[Complex]): Array[Complex] = {
//    return MathsBase.imag(v)
//  }
//
//  def imagdbl(v: Array[Complex]): Array[Double] = {
//    return MathsBase.imagdbl(v)
//  }
//
//  def avg(v: Matrix): Complex = {
//    return MathsBase.avg(v)
//  }
//
//  def sum(v: Matrix): Complex = {
//    return MathsBase.sum(v)
//  }
//
//  def prod(v: Matrix): Complex = {
//    return MathsBase.prod(v)
//  }
//
//  def roundEquals(a: Double, b: Double, epsilon: Double): Boolean = {
//    return MathsBase.roundEquals(a, b, epsilon)
//  }
//
//  def round(`val`: Double, precision: Double): Double = {
//    return MathsBase.round(`val`, precision)
//  }
//
//  def sqrt(v: Double, n: Int): Double = {
//    return MathsBase.sqrt(v, n)
//  }
//
//  def pow(v: Double, n: Double): Double = {
//    return MathsBase.pow(v, n)
//  }
//
//  def db(x: Double): Double = {
//    return MathsBase.db(x)
//  }
//
//  def acosh(x: Double): Double = {
//    return MathsBase.acosh(x)
//  }
//
//  def atanh(x: Double): Double = {
//    return MathsBase.atanh(x)
//  }
//
//  def acotanh(x: Double): Double = {
//    return MathsBase.acotanh(x)
//  }
//
//  def asinh(x: Double): Double = {
//    return MathsBase.asinh(x)
//  }
//
//  def db2(nbr: Double): Double = {
//    return MathsBase.db2(nbr)
//  }
//
//  def sqrt(nbr: Double): Double = {
//    return MathsBase.sqrt(nbr)
//  }
//
//  def inv(x: Double): Double = {
//    return MathsBase.inv(x)
//  }
//
//  def conj(x: Double): Double = {
//    return MathsBase.conj(x)
//  }
//
//  def sin2(x: Array[Double]): Array[Double] = {
//    return MathsBase.sin2(x)
//  }
//
//  def cos2(x: Array[Double]): Array[Double] = {
//    return MathsBase.cos2(x)
//  }
//
//  def sin(x: Array[Double]): Array[Double] = {
//    return MathsBase.sin(x)
//  }
//
//  def cos(x: Array[Double]): Array[Double] = {
//    return MathsBase.cos(x)
//  }
//
//  def tan(x: Array[Double]): Array[Double] = {
//    return MathsBase.tan(x)
//  }
//
//  def cotan(x: Array[Double]): Array[Double] = {
//    return MathsBase.cotan(x)
//  }
//
//  def sinh(x: Array[Double]): Array[Double] = {
//    return MathsBase.sinh(x)
//  }
//
//  def cosh(x: Array[Double]): Array[Double] = {
//    return MathsBase.cosh(x)
//  }
//
//  def tanh(x: Array[Double]): Array[Double] = {
//    return MathsBase.tanh(x)
//  }
//
//  def cotanh(x: Array[Double]): Array[Double] = {
//    return MathsBase.cotanh(x)
//  }
//
//  def max(a: Double, b: Double): Double = {
//    return MathsBase.max(a, b)
//  }
//
//  def max(a: Int, b: Int): Int = {
//    return MathsBase.max(a, b)
//  }
//
//  def max(a: Long, b: Long): Long = {
//    return MathsBase.max(a, b)
//  }
//
//  def min(a: Double, b: Double): Double = {
//    return MathsBase.min(a, b)
//  }
//
//  def min(arr: Array[Double]): Double = {
//    return MathsBase.min(arr)
//  }
//
//  def max(arr: Array[Double]): Double = {
//    return MathsBase.max(arr)
//  }
//
//  def avg(arr: Array[Double]): Double = {
//    return MathsBase.avg(arr)
//  }
//
//  def min(a: Int, b: Int): Int = {
//    return MathsBase.min(a, b)
//  }
//
//  def min(a: Complex, b: Complex): Complex = {
//    return MathsBase.min(a, b)
//  }
//
//  def max(a: Complex, b: Complex): Complex = {
//    return MathsBase.max(a, b)
//  }
//
//  def min(a: Long, b: Long): Long = {
//    return MathsBase.min(a, b)
//  }
//
//  def minMax(a: Array[Double]): Array[Double] = {
//    return MathsBase.minMax(a)
//  }
//
//  def minMaxAbs(a: Array[Double]): Array[Double] = {
//    return MathsBase.minMaxAbs(a)
//  }
//
//  def minMaxAbsNonInfinite(a: Array[Double]): Array[Double] = {
//    return MathsBase.minMaxAbsNonInfinite(a)
//  }
//
//  def avgAbs(arr: Array[Double]): Double = {
//    return MathsBase.avgAbs(arr)
//  }
//
//  def distances(arr: Array[Double]): Array[Double] = {
//    return MathsBase.distances(arr)
//  }
//
//  def div(a: Array[Double], b: Array[Double]): Array[Double] = {
//    return MathsBase.div(a, b)
//  }
//
//  def mul(a: Array[Double], b: Array[Double]): Array[Double] = {
//    return MathsBase.mul(a, b)
//  }
//
//  def sub(a: Array[Double], b: Array[Double]): Array[Double] = {
//    return MathsBase.sub(a, b)
//  }
//
//  def sub(a: Array[Double], b: Double): Array[Double] = {
//    return MathsBase.sub(a, b)
//  }
//
//  def add(a: Array[Double], b: Array[Double]): Array[Double] = {
//    return MathsBase.add(a, b)
//  }
//
//  def db(a: Array[Double]): Array[Double] = {
//    return MathsBase.db(a)
//  }
//
//  def sin(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.sin(c)
//  }
//
//  def sin2(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.sin2(c)
//  }
//
//  def sin(x: Double): Double = {
//    return MathsBase.sin(x)
//  }
//
//  def cos(x: Double): Double = {
//    return MathsBase.cos(x)
//  }
//
//  def tan(x: Double): Double = {
//    return MathsBase.tan(x)
//  }
//
//  def cotan(x: Double): Double = {
//    return MathsBase.cotan(x)
//  }
//
//  def sinh(x: Double): Double = {
//    return MathsBase.sinh(x)
//  }
//
//  def cosh(x: Double): Double = {
//    return MathsBase.cosh(x)
//  }
//
//  def tanh(x: Double): Double = {
//    return MathsBase.tanh(x)
//  }
//
//  def abs(a: Double): Double = {
//    return MathsBase.abs(a)
//  }
//
//  def abs(a: Int): Int = {
//    return MathsBase.abs(a)
//  }
//
//  def cotanh(x: Double): Double = {
//    return MathsBase.cotanh(x)
//  }
//
//  def acos(x: Double): Double = {
//    return MathsBase.acos(x)
//  }
//
//  def asin(x: Double): Double = {
//    return MathsBase.asin(x)
//  }
//
//  def atan(x: Double): Double = {
//    return MathsBase.atan(x)
//  }
//
//  def sum(c: Double*): Double = {
//    return MathsBase.sum(c:_ *)
//  }
//
//  def mul(a: Array[Double], b: Double): Array[Double] = {
//    return MathsBase.mul(a, b)
//  }
//
//  def mulSelf(x: Array[Double], v: Double): Array[Double] = {
//    return MathsBase.mulSelf(x, v)
//  }
//
//  def div(a: Array[Double], b: Double): Array[Double] = {
//    return MathsBase.div(a, b)
//  }
//
//  def divSelf(x: Array[Double], v: Double): Array[Double] = {
//    return MathsBase.divSelf(x, v)
//  }
//
//  def add(x: Array[Double], v: Double): Array[Double] = {
//    return MathsBase.add(x, v)
//  }
//
//  def addSelf(x: Array[Double], v: Double): Array[Double] = {
//    return MathsBase.addSelf(x, v)
//  }
//
//  def cos(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.cos(c)
//  }
//
//  def tan(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.tan(c)
//  }
//
//  def cotan(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.cotan(c)
//  }
//
//  def sinh(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.sinh(c)
//  }
//
//  def cosh(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.cosh(c)
//  }
//
//  def tanh(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.tanh(c)
//  }
//
//  def cotanh(c: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.cotanh(c)
//  }
//
//  def add(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.add(a, b)
//  }
//
//  def sub(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.sub(a, b)
//  }
//
//  def div(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.div(a, b)
//  }
//
//  def mul(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.mul(a, b)
//  }
//
//  def db(a: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.db(a)
//  }
//
//  def db2(a: Array[Array[Double]]): Array[Array[Double]] = {
//    return MathsBase.db2(a)
//  }
//
//  def If(cond: Expr, exp1: Expr, exp2: Expr): Expr = {
//    return MathsBase.If(cond, exp1, exp2)
//  }
//
//  def or(a: Expr, b: Expr): Expr = {
//    return MathsBase.or(a, b)
//  }
//
//  def and(a: Expr, b: Expr): Expr = {
//    return MathsBase.and(a, b)
//  }
//
//  def not(a: Expr): Expr = {
//    return MathsBase.not(a)
//  }
//
//  def eq(a: Expr, b: Expr): Expr = {
//    return MathsBase.eq(a, b)
//  }
//
//  def ne(a: Expr, b: Expr): Expr = {
//    return MathsBase.ne(a, b)
//  }
//
//  def gte(a: Expr, b: Expr): Expr = {
//    return MathsBase.gte(a, b)
//  }
//
//  def gt(a: Expr, b: Expr): Expr = {
//    return MathsBase.gt(a, b)
//  }
//
//  def lte(a: Expr, b: Expr): Expr = {
//    return MathsBase.lte(a, b)
//  }
//
//  def lt(a: Expr, b: Expr): Expr = {
//    return MathsBase.lt(a, b)
//  }
//
//  def cos(e: Expr): Expr = {
//    return MathsBase.cos(e)
//  }
//
//  def cosh(e: Expr): Expr = {
//    return MathsBase.cosh(e)
//  }
//
//  def sin(e: Expr): Expr = {
//    return MathsBase.sin(e)
//  }
//
//  def sincard(e: Expr): Expr = {
//    return MathsBase.sincard(e)
//  }
//
//  def sinh(e: Expr): Expr = {
//    return MathsBase.sinh(e)
//  }
//
//  def tan(e: Expr): Expr = {
//    return MathsBase.tan(e)
//  }
//
//  def tanh(e: Expr): Expr = {
//    return MathsBase.tanh(e)
//  }
//
//  def cotan(e: Expr): Expr = {
//    return MathsBase.cotan(e)
//  }
//
//  def cotanh(e: Expr): Expr = {
//    return MathsBase.cotanh(e)
//  }
//
//  def sqr(e: Expr): Expr = {
//    return MathsBase.sqr(e)
//  }
//
//  def sqrt(e: Expr): Expr = {
//    return MathsBase.sqrt(e)
//  }
//
//  def inv(e: Expr): Expr = {
//    return MathsBase.inv(e)
//  }
//
//  def neg(e: Expr): Expr = {
//    return MathsBase.neg(e)
//  }
//
//  def exp(e: Expr): Expr = {
//    return MathsBase.exp(e)
//  }
//
//  def atan(e: Expr): Expr = {
//    return MathsBase.atan(e)
//  }
//
//  def acotan(e: Expr): Expr = {
//    return MathsBase.acotan(e)
//  }
//
//  def acos(e: Expr): Expr = {
//    return MathsBase.acos(e)
//  }
//
//  def asin(e: Expr): Expr = {
//    return MathsBase.asin(e)
//  }
//
//  def integrate(e: Expr): Complex = {
//    return MathsBase.integrate(e)
//  }
//
//  def integrate(e: Expr, domain: Domain): Complex = {
//    return MathsBase.integrate(e, domain)
//  }
//
//  def esum(size: Int, f: TVectorCell[Expr]): Expr = {
//    return MathsBase.esum(size, f)
//  }
//
//  def esum(size1: Int, size2: Int, e: TMatrixCell[Expr]): Expr = {
//    return MathsBase.esum(size1, size2, e)
//  }
//
//  def csum(size1: Int, size2: Int, e: TMatrixCell[Complex]): Complex = {
//    return MathsBase.csum(size1, size2, e)
//  }
//
//  def seq(size1: Int, f: TVectorCell[Expr]): TVector[Expr] = {
//    return MathsBase.seq(size1, f)
//  }
//
//  def seq(size1: Int, size2: Int, f: TMatrixCell[Expr]): TVector[Expr] = {
//    return MathsBase.seq(size1, size2, f)
//  }
//
//  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProductCache(gp, fn, monitor)
//  }
//
//  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProductCache(sp, gp, fn, monitor)
//  }
//
//  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProductCache(sp, gp, fn, axis, monitor)
//  }
//
//  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProductCache(gp, fn, axis, monitor)
//  }
//
//  def gate(axis: Axis, a: Double, b: Double): Expr = {
//    return MathsBase.gate(axis, a, b)
//  }
//
//  def gate(axis: Expr, a: Double, b: Double): Expr = {
//    return MathsBase.gate(axis, a, b)
//  }
//
//  def gateX(a: Double, b: Double): Expr = {
//    return MathsBase.gateX(a, b)
//  }
//
//  def gateY(a: Double, b: Double): Expr = {
//    return MathsBase.gateY(a, b)
//  }
//
//  def gateZ(a: Double, b: Double): Expr = {
//    return MathsBase.gateZ(a, b)
//  }
//
//  def scalarProduct(f1: DoubleToDouble, f2: DoubleToDouble): Double = {
//    return MathsBase.scalarProduct(f1, f2)
//  }
//
//  def scalarProduct(f1: Expr, f2: TVector[Expr]): Vector = {
//    return MathsBase.scalarProduct(f1, f2)
//  }
//
//  def scalarProduct(f1: Expr, f2: TMatrix[Expr]): Matrix = {
//    return MathsBase.scalarProduct(f1, f2)
//  }
//
//  def scalarProduct(f2: TVector[Expr], f1: Expr): Vector = {
//    return MathsBase.scalarProduct(f2, f1)
//  }
//
//  def scalarProduct(f2: TMatrix[Expr], f1: Expr): Matrix = {
//    return MathsBase.scalarProduct(f2, f1)
//  }
//
//  def scalarProduct(domain: Domain, f1: Expr, f2: Expr): Complex = {
//    return MathsBase.scalarProduct(domain, f1, f2)
//  }
//
//  def scalarProduct(f1: Expr, f2: Expr): Complex = {
//    return MathsBase.scalarProduct(f1, f2)
//  }
//
//  def scalarProductMatrix(g: TVector[Expr], f: TVector[Expr]): Matrix = {
//    return MathsBase.scalarProductMatrix(g, f)
//  }
//
//  def scalarProduct(g: TVector[Expr], f: TVector[Expr]): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f)
//  }
//
//  def scalarProduct(g: TVector[Expr], f: TVector[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f, monitor)
//  }
//
//  def scalarProductMatrix(g: TVector[Expr], f: TVector[Expr], monitor: ProgressMonitor): Matrix = {
//    return MathsBase.scalarProductMatrix(g, f, monitor)
//  }
//
//  def scalarProduct(g: TVector[Expr], f: TVector[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f, axis, monitor)
//  }
//
//  def scalarProductMatrix(g: Array[Expr], f: Array[Expr]): Matrix = {
//    return MathsBase.scalarProductMatrix(g, f)
//  }
//
//  def scalarProduct(g: Matrix, f: Matrix): Complex = {
//    return MathsBase.scalarProduct(g, f)
//  }
//
//  def scalarProduct(g: Matrix, f: TVector[Expr]): Expr = {
//    return MathsBase.scalarProduct(g, f)
//  }
//
//  def scalarProductAll(g: Matrix, f: TVector[Expr]*): Expr = {
//    return MathsBase.scalarProductAll(g, f:_ *)
//  }
//
//  def scalarProduct(g: Array[Expr], f: Array[Expr]): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f)
//  }
//
//  def scalarProduct(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f, monitor)
//  }
//
//  def scalarProductMatrix(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): Matrix = {
//    return MathsBase.scalarProductMatrix(g, f, monitor)
//  }
//
//  def scalarProduct(g: Array[Expr], f: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
//    return MathsBase.scalarProduct(g, f, axis, monitor)
//  }
//
//  def elist(size: Int): ExprList = {
//    return MathsBase.elist(size)
//  }
//
//  def elist(row: Boolean, size: Int): ExprList = {
//    return MathsBase.elist(row, size)
//  }
//
//  def elist(vector: Expr*): ExprList = {
//    return MathsBase.elist(vector:_ *)
//  }
//
//  def elist(vector: TVector[Complex]): ExprList = {
//    return MathsBase.elist(vector)
//  }
//
//  def clist(vector: TVector[Expr]): TList[Complex] = {
//    return MathsBase.clist(vector)
//  }
//
//  def clist: TList[Complex] = {
//    return MathsBase.clist
//  }
//
//  def clist(size: Int): TList[Complex] = {
//    return MathsBase.clist(size)
//  }
//
//  def clist(vector: Complex*): TList[Complex] = {
//    return MathsBase.clist(vector:_ *)
//  }
//
//  def mlist: TList[Matrix] = {
//    return MathsBase.mlist
//  }
//
//  def mlist(size: Int): TList[Matrix] = {
//    return MathsBase.mlist(size)
//  }
//
//  def mlist(items: Matrix*): TList[Matrix] = {
//    return MathsBase.mlist(items:_ *)
//  }
//
//  def clist2: TList[TList[Complex]] = {
//    return MathsBase.clist2
//  }
//
//  def elist2: TList[TList[Expr]] = {
//    return MathsBase.elist2
//  }
//
//  def dlist2: TList[TList[java.lang.Double]] = {
//    return MathsBase.dlist2
//  }
//
//  def ilist2: TList[TList[Integer]] = {
//    return MathsBase.ilist2
//  }
//
//  def mlist2: TList[TList[Matrix]] = {
//    return MathsBase.mlist2
//  }
//
//  def blist2: TList[TList[java.lang.Boolean]] = {
//    return MathsBase.blist2
//  }
//
//  def list[T](`type`: TypeName[T]): TList[T] = {
//    return MathsBase.list(`type`)
//  }
//
//  def list[T](`type`: TypeName[T], initialSize: Int): TList[T] = {
//    return MathsBase.list(`type`, initialSize)
//  }
//
//  def listro[T](`type`: TypeName[T], row: Boolean, model: TVectorModel[T]): TList[T] = {
//    return MathsBase.listro(`type`, row, model)
//  }
//
//  def list[T](`type`: TypeName[T], row: Boolean, initialSize: Int): TList[T] = {
//    return MathsBase.list(`type`, row, initialSize)
//  }
//
//  def list[T](vector: TVector[T]): TList[T] = {
//    return MathsBase.list(vector)
//  }
//
//  def elist(vector: Matrix): ExprList = {
//    return MathsBase.elist(vector)
//  }
//
//  def vscalarProduct[T](vector: TVector[T], tVectors: TVector[TVector[T]]): TVector[T] = {
//    return MathsBase.vscalarProduct(vector, tVectors)
//  }
//
//  def elist: TList[Expr] = {
//    return MathsBase.elist
//  }
//
//  def concat[T](a: TList[T]*): TList[T] = {
//    return MathsBase.concat(a: _*)
//  }
//
//  def dlist: TList[java.lang.Double] = {
//    return MathsBase.dlist
//  }
//
//  def dlist(items: ToDoubleArrayAware): TList[java.lang.Double] = {
//    return MathsBase.dlist(items)
//  }
//
//  def dlist(items: Array[Double]): TList[java.lang.Double] = {
//    return MathsBase.dlist(items)
//  }
//
//  def dlist(row: Boolean, size: Int): TList[java.lang.Double] = {
//    return MathsBase.dlist(row, size)
//  }
//
//  def dlist(size: Int): TList[java.lang.Double] = {
//    return MathsBase.dlist(size)
//  }
//
//  def slist: TList[String] = {
//    return MathsBase.slist
//  }
//
//  def slist(items: Array[String]): TList[String] = {
//    return MathsBase.slist(items)
//  }
//
//  def slist(row: Boolean, size: Int): TList[String] = {
//    return MathsBase.slist(row, size)
//  }
//
//  def slist(size: Int): TList[String] = {
//    return MathsBase.slist(size)
//  }
//
//  def blist: TList[java.lang.Boolean] = {
//    return MathsBase.blist
//  }
//
//  def dlist(items: Array[Boolean]): TList[Boolean] = {
//    return MathsBase.dlist(items)
//  }
//
//  def blist(row: Boolean, size: Int): TList[Boolean] = {
//    return MathsBase.blist(row, size)
//  }
//
//  def blist(size: Int): TList[Boolean] = {
//    return MathsBase.blist(size)
//  }
//
//  def ilist: IntList = {
//    return MathsBase.ilist
//  }
//
//  def ilist(items: Array[Int]): TList[Integer] = {
//    return MathsBase.ilist(items)
//  }
//
//  def ilist(size: Int): TList[Integer] = {
//    return MathsBase.ilist(size)
//  }
//
//  def ilist(row: Boolean, size: Int): TList[Integer] = {
//    return MathsBase.ilist(row, size)
//  }
//
//  def llist: LongList = {
//    return MathsBase.llist
//  }
//
//  def llist(items: Array[Long]): TList[Long] = {
//    return MathsBase.llist(items)
//  }
//
//  def llist(size: Int): TList[Long] = {
//    return MathsBase.llist(size)
//  }
//
//  def llist(row: Boolean, size: Int): TList[Long] = {
//    return MathsBase.llist(row, size)
//  }
//
//  def sum[T](`type`: TypeName[T], arr: T*): T = {
//    return MathsBase.sum(`type`, arr)
//  }
//
//  def sum[T](`type`: TypeName[T], arr: TVectorModel[T]): T = {
//    return MathsBase.sum(`type`, arr)
//  }
//
//  def sum[T](`type`: TypeName[T], size: Int, arr: TVectorCell[T]): T = {
//    return MathsBase.sum(`type`, size, arr)
//  }
//
//  def mul[T](`type`: TypeName[T], arr: T*): T = {
//    return MathsBase.mul(`type`, arr:_ *)
//  }
//
//  def mul[T](`type`: TypeName[T], arr: TVectorModel[T]): T = {
//    return MathsBase.mul(`type`, arr)
//  }
//
//  def avg(d: Discrete): Complex = {
//    return MathsBase.avg(d)
//  }
//
//  def vsum(d: VDiscrete): DoubleToVector = {
//    return MathsBase.vsum(d)
//  }
//
//  def vavg(d: VDiscrete): DoubleToVector = {
//    return MathsBase.vavg(d)
//  }
//
//  def avg(d: VDiscrete): Complex = {
//    return MathsBase.avg(d)
//  }
//
//  def sum(arr: Expr*): Expr = {
//    return MathsBase.sum(arr:_ *)
//  }
//
//  def esum(arr: TVectorModel[Expr]): Expr = {
//    return MathsBase.esum(arr)
//  }
//
//  def mul[T](a: TMatrix[T], b: TMatrix[T]): TMatrix[T] = {
//    return MathsBase.mul(a, b)
//  }
//
//  def mul(a: Matrix, b: Matrix): Matrix = {
//    return MathsBase.mul(a, b)
//  }
//
//  def mul(a: Expr, b: Expr): Expr = {
//    return MathsBase.mul(a, b)
//  }
//
//  def edotmul(arr: TVector[Expr]*): TVector[Expr] = {
//    return MathsBase.edotmul(arr:_ *)
//  }
//
//  def edotdiv(arr: TVector[Expr]*): TVector[Expr] = {
//    return MathsBase.edotdiv(arr:_ *)
//  }
//
//  def cmul(arr: TVectorModel[Complex]): Complex = {
//    return MathsBase.cmul(arr)
//  }
//
//  def emul(arr: TVectorModel[Expr]): Expr = {
//    return MathsBase.emul(arr)
//  }
//
//  def mul(e: Expr*): Expr = {
//    return MathsBase.mul(e :_ *)
//  }
//
//  def pow(a: Expr, b: Expr): Expr = {
//    return MathsBase.pow(a, b)
//  }
//
//  def sub(a: Expr, b: Expr): Expr = {
//    return MathsBase.sub(a, b)
//  }
//
//  def add(a: Expr, b: java.lang.Double): Expr = {
//    return MathsBase.add(a, b)
//  }
//
//  def mul(a: Expr, b: java.lang.Double): Expr = {
//    return MathsBase.mul(a, b)
//  }
//
//  def sub(a: Expr, b: java.lang.Double): Expr = {
//    return MathsBase.sub(a, b)
//  }
//
//  def div(a: Expr, b: java.lang.Double): Expr = {
//    return MathsBase.div(a, b)
//  }
//
//  def add(a: Expr, b: Expr): Expr = {
//    return MathsBase.add(a, b)
//  }
//
//  def add(a: Expr*): Expr = {
//    return MathsBase.add(a :_ *)
//  }
//
//  def div(a: Expr, b: Expr): Expr = {
//    return MathsBase.div(a, b)
//  }
//
//  def rem(a: Expr, b: Expr): Expr = {
//    return MathsBase.rem(a, b)
//  }
//
//  def expr(value: Double): Expr = {
//    return MathsBase.expr(value)
//  }
//
//  def expr[T](vector: TVector[T]): TVector[Expr] = {
//    return MathsBase.expr(vector)
//  }
//
//  def expr(matrix: TMatrix[Complex]): TMatrix[Expr] = {
//    return MathsBase.expr(matrix)
//  }
//
//  def tmatrix[T](`type`: TypeName[T], model: TMatrixModel[T]): TMatrix[T] = {
//    return MathsBase.tmatrix(`type`, model)
//  }
//
//  def tmatrix[T](`type`: TypeName[T], rows: Int, columns: Int, model: TMatrixCell[T]): TMatrix[T] = {
//    return MathsBase.tmatrix(`type`, rows, columns, model)
//  }
//
//  def simplify[T](a: T): T = {
//    return MathsBase.simplify(a)
//  }
//
//  def simplify(a: Expr): Expr = {
//    return MathsBase.simplify(a)
//  }
//
//  def norm(a: Expr): Double = {
//    return MathsBase.norm(a)
//  }
//
//  def normalize[T](a: TList[T]): TList[T] = {
//    return MathsBase.normalize(a)
//  }
//
//  def normalize(a: Geometry): Expr = {
//    return MathsBase.normalize(a)
//  }
//
//  def normalize(a: Expr): Expr = {
//    return MathsBase.normalize(a)
//  }
//
//  def vector(fx: Expr, fy: Expr): DoubleToVector = {
//    return MathsBase.vector(fx, fy)
//  }
//
//  def vector(fx: Expr): DoubleToVector = {
//    return MathsBase.vector(fx)
//  }
//
//  def vector(fx: Expr, fy: Expr, fz: Expr): DoubleToVector = {
//    return MathsBase.vector(fx, fy, fz)
//  }
//
//  def cos[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.cos(a)
//  }
//
//  def cosh[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.cosh(a)
//  }
//
//  def sin[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.sin(a)
//  }
//
//  def sinh[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.sinh(a)
//  }
//
//  def tan[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.tan(a)
//  }
//
//  def tanh[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.tanh(a)
//  }
//
//  def cotan[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.cotan(a)
//  }
//
//  def cotanh[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.cotanh(a)
//  }
//
//  def sqr[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.sqr(a)
//  }
//
//  def sqrt[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.sqrt(a)
//  }
//
//  def inv[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.inv(a)
//  }
//
//  def neg[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.neg(a)
//  }
//
//  def exp[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.exp(a)
//  }
//
//  def simplify[T](a: TVector[T]): TVector[T] = {
//    return MathsBase.simplify(a)
//  }
//
//  def simplify[T](a: TList[T]): TList[T] = {
//    return MathsBase.simplify(a)
//  }
//
//  def addAll[T](e: TList[T], expressions: T*): TList[T] = {
//    return MathsBase.addAll(e, expressions :_ *)
//  }
//
//  def mulAll[T](e: TList[T], expressions: T*): TList[T] = {
//    return MathsBase.mulAll(e, expressions:_ *)
//  }
//
//  def pow[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.pow(a, b)
//  }
//
//  def sub[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.sub(a, b)
//  }
//
//  def div[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.div(a, b)
//  }
//
//  def rem[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.rem(a, b)
//  }
//
//  def add[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.add(a, b)
//  }
//
//  def mul[T](a: TList[T], b: T): TList[T] = {
//    return MathsBase.mul(a, b)
//  }
//
//  def loopOver(values: Array[Array[AnyRef]], action: LoopAction): Unit = {
//    MathsBase.loopOver(values, action)
//  }
//
//  def loopOver(values: Array[Loop], action: LoopAction): Unit = {
//    MathsBase.loopOver(values, action)
//  }
//
//  def formatMemory: String = {
//    return MathsBase.formatMemory
//  }
//
//  def formatMetric(value: Double): String = {
//    return MathsBase.formatMetric(value)
//  }
//
//  def memoryInfo: MemoryInfo = {
//    return MathsBase.memoryInfo
//  }
//
//  def memoryMeter: MemoryMeter = {
//    return MathsBase.memoryMeter
//  }
//
//  def inUseMemory: Long = {
//    return MathsBase.inUseMemory
//  }
//
//  def maxFreeMemory: Long = {
//    return MathsBase.maxFreeMemory
//  }
//
//  def formatMemory(bytes: Long): String = {
//    return MathsBase.formatMemory(bytes)
//  }
//
//  def formatFrequency(frequency: Double): String = {
//    return MathsBase.formatFrequency(frequency)
//  }
//
//  def formatDimension(dimension: Double): String = {
//    return MathsBase.formatDimension(dimension)
//  }
//
//  def formatPeriodNanos(period: Long): String = {
//    return MathsBase.formatPeriodNanos(period)
//  }
//
//  def formatPeriodMillis(period: Long): String = {
//    return MathsBase.formatPeriodMillis(period)
//  }
//
//  def sizeOf(src: Class[_]): Int = {
//    return MathsBase.sizeOf(src)
//  }
//
//  def invokeMonitoredAction[T](mon: ProgressMonitor, messagePrefix: String, run: MonitoredAction[T]): T = {
//    return MathsBase.invokeMonitoredAction(mon, messagePrefix, run)
//  }
//
//  def chrono: Chronometer = {
//    return MathsBase.chrono
//  }
//
//  def chrono(name: String): Chronometer = {
//    return MathsBase.chrono(name)
//  }
//
//  def chrono(name: String, r: Runnable): Chronometer = {
//    return MathsBase.chrono(name, r)
//  }
//
//  def chrono[V](name: String, r: Callable[V]): V = {
//    return MathsBase.chrono(name, r)
//  }
//
//  def solverExecutorService(threads: Int): SolverExecutorService = {
//    return MathsBase.solverExecutorService(threads)
//  }
//
//  def chrono(r: Runnable): Chronometer = {
//    return MathsBase.chrono(r)
//  }
//
//  def percentFormat: DoubleFormat = {
//    return MathsBase.percentFormat
//  }
//
//  def frequencyFormat: DoubleFormat = {
//    return MathsBase.frequencyFormat
//  }
//
//  def metricFormat: DoubleFormat = {
//    return MathsBase.metricFormat
//  }
//
//  def memoryFormat: DoubleFormat = {
//    return MathsBase.memoryFormat
//  }
//
//  def dblformat(format: String): DoubleFormat = {
//    return MathsBase.dblformat(format)
//  }
//
//  def resizePickFirst(array: Array[Double], newSize: Int): Array[Double] = {
//    return MathsBase.resizePickFirst(array, newSize)
//  }
//
//  def resizePickAverage(array: Array[Double], newSize: Int): Array[Double] = {
//    return MathsBase.resizePickAverage(array, newSize)
//  }
//
//  def toArray[T](t: Class[T], coll: java.util.Collection[T]): Array[T] = {
//    return MathsBase.toArray(t, coll)
//  }
//
//  def toArray[T](t: TypeName[T], coll: java.util.Collection[T]): Array[T] = {
//    return MathsBase.toArray(t, coll)
//  }
//
//  def rerr(a: Double, b: Double): Double = {
//    return MathsBase.rerr(a, b)
//  }
//
//  def rerr(a: Complex, b: Complex): Double = {
//    return MathsBase.rerr(a, b)
//  }
//
//  def define(name: String, f: CustomCCFunctionX): CustomCCFunctionXDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def define(name: String, f: CustomDCFunctionX): CustomDCFunctionXDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def define(name: String, f: CustomDDFunctionX): CustomDDFunctionXDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def define(name: String, f: CustomDDFunctionXY): CustomDDFunctionXYDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def define(name: String, f: CustomDCFunctionXY): CustomDCFunctionXYDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def define(name: String, f: CustomCCFunctionXY): CustomCCFunctionXYDefinition = {
//    return MathsBase.define(name, f)
//  }
//
//  def rerr(a: Matrix, b: Matrix): Double = {
//    return MathsBase.rerr(a, b)
//  }
//
//  def toDoubleArray[T <: Expr](c: TList[T]): DoubleList = {
//    return MathsBase.toDoubleArray(c)
//  }
//
//  def toDouble(c: Complex, d: PlotDoubleConverter): Double = {
//    return MathsBase.toDouble(c, d)
//  }
//
//  def conj(e: Expr): Expr = {
//    return MathsBase.conj(e)
//  }
//
//  def complex(t: TMatrix[_]): Complex = {
//    return MathsBase.complex(t)
//  }
//
//  def matrix(t: TMatrix[_]): Matrix = {
//    return MathsBase.matrix(t)
//  }
//
//  def ematrix(t: TMatrix[_]): TMatrix[Expr] = {
//    return MathsBase.ematrix(t)
//  }
//
//  def getVectorSpace[T](cls: TypeName[T]): VectorSpace[T] = {
//    return MathsBase.getVectorSpace(cls)
//  }
//
//  def refineSamples(values: TList[java.lang.Double], n: Int): DoubleList = {
//    return MathsBase.refineSamples(values, n)
//  }
//
//  def refineSamples(values: Array[Double], n: Int): Array[Double] = {
//    return MathsBase.refineSamples(values, n)
//  }
//
//  def getHadrumathsVersion: String = {
//    return MathsBase.getHadrumathsVersion
//  }
//
//  def expandComponentDimension(d1: ComponentDimension, d2: ComponentDimension): ComponentDimension = {
//    return MathsBase.expandComponentDimension(d1, d2)
//  }
//
//  def expandComponentDimension(e: Expr, d: ComponentDimension): Expr = {
//    return MathsBase.expandComponentDimension(e, d)
//  }
//
//  def atan2(y: Double, x: Double): Double = {
//    return MathsBase.atan2(y, x)
//  }
//
//  def ceil(a: Double): Double = {
//    return MathsBase.ceil(a)
//  }
//
//  def floor(a: Double): Double = {
//    return MathsBase.floor(a)
//  }
//
//  def round(a: Double): Long = {
//    return MathsBase.round(a)
//  }
//
//  def round(a: Float): Int = {
//    return MathsBase.round(a)
//  }
//
//  def random: Double = {
//    return MathsBase.random
//  }
//
//  def rightArrow[A, B](a: A, b: B): RightArrowUplet2[A, B] = {
//    return MathsBase.rightArrow(a, b)
//  }
//
//  def rightArrow(a: Double, b: Double): RightArrowUplet2.Double = {
//    return MathsBase.rightArrow(a, b)
//  }
//
//  def rightArrow(a: Complex, b: Complex): RightArrowUplet2.Complex = {
//    return MathsBase.rightArrow(a, b)
//  }
//
//  def rightArrow(a: Expr, b: Expr): RightArrowUplet2.Expr = {
//    return MathsBase.rightArrow(a, b)
//  }
//
//  def parseExpression(expression: String): Expr = {
//    return MathsBase.parseExpression(expression)
//  }
//
//  def createExpressionEvaluator: ExpressionManager = {
//    return MathsBase.createExpressionEvaluator
//  }
//
//  def createExpressionParser: ExpressionManager = {
//    return MathsBase.createExpressionParser
//  }
//
//  def evalExpression(expression: String): Expr = {
//    return MathsBase.evalExpression(expression)
//  }
//
//  def toRadians(a: Double): Double = {
//    return MathsBase.toRadians(a)
//  }
//
//  def toRadians(a: Array[Double]): Array[Double] = {
//    return MathsBase.toRadians(a)
//  }
//
//  def det(m: Matrix): Complex = {
//    return MathsBase.det(m)
//  }
//
//  def toInt(o: Any): Int = {
//    return MathsBase.toInt(o)
//  }
//
//  def toInt(o: Any, defaultValue: Integer): Int = {
//    return MathsBase.toInt(o, defaultValue)
//  }
//
//  def toLong(o: Any): Long = {
//    return MathsBase.toLong(o)
//  }
//
//  def toLong(o: Any, defaultValue: Long): Long = {
//    return MathsBase.toLong(o, defaultValue)
//  }
//
//  def toDouble(o: Any): Double = {
//    return MathsBase.toDouble(o)
//  }
//
//  def toDouble(o: Any, defaultValue: Double): Double = {
//    return MathsBase.toDouble(o, defaultValue)
//  }
//
//  def toFloat(o: Any): Float = {
//    return MathsBase.toFloat(o)
//  }
//
//  def toFloat(o: Any, defaultValue: Float): Float = {
//    return MathsBase.toFloat(o, defaultValue)
//  }
//
//  def DC(e: Expr): DoubleToComplex = {
//    return MathsBase.DC(e)
//  }
//
//  def DD(e: Expr): DoubleToDouble = {
//    return MathsBase.DD(e)
//  }
//
//  def DV(e: Expr): DoubleToVector = {
//    return MathsBase.DV(e)
//  }
//
//  def DM(e: Expr): DoubleToMatrix = {
//    return MathsBase.DM(e)
//  }
//
//  def matrix(e: Expr): Matrix = {
//    return MathsBase.matrix(e)
//  }


}

