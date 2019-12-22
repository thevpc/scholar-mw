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
import net.vpc.common.util.{ArrayUtils, Chronometer, Converter, DoubleFilter, DoubleFormat, IndexSelectionStrategy, IntFilter, MemoryInfo, MemoryMeter, TypeName}
import net.vpc.scholar.hadrumaths.geom.Point
import net.vpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator
import net.vpc.scholar.hadrumaths.symbolic._
import net.vpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromTComplexMatrix
import net.vpc.scholar.hadruplot.console.params.{ArrayParamSet, BooleanArrayParamSet, DoubleArrayParamSet, FloatArrayParamSet, IntArrayParamSet, LongArrayParamSet, Param, XParamSet}
import net.vpc.scholar.hadruplot.{AbsoluteSamples, AdaptiveSamples, Plot, PlotConfig, PlotDoubleConverter, RelativeSamples, Samples}
//import java.util

import net.vpc.scholar
import net.vpc.scholar.hadrumaths.cache.{CacheEvaluator, PersistenceCache}
import net.vpc.scholar.hadrumaths.geom.Geometry

import scala.collection.{Iterable, Iterator}

object MathScala {

  //  implicit def convertE[T](indexedSeq: IndexedSeq[T]): Array[T] = indexedSeq.toArray[T]
  type EMatrix = TMatrix[Expr];
  type EVector = TVector[Expr];
  type EList = TVector[Expr];
  type CMatrix = TMatrix[Complex];
  type CVector = TVector[Complex];
  type CList = TVector[Complex];
  type DMatrix = TMatrix[Double];
  type DVector = TVector[Expr];
  type DList = TVector[Double];
  type JPList[T] = java.util.List[T];
  type JPDouble = java.lang.Double;
  type JPLong = java.lang.Long;
  type JPInteger = java.lang.Integer;
  type JPFloat = java.lang.Float;
  type JPBoolean = java.lang.Boolean;

  private val SIMPLIFY_OPTION_PRESERVE_ROOT_NAME = new SimplifyOptions().setPreserveRootName(true)
  //  def arr(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray()
  //
  //  def arr(x: Array[Double]): ToDoubleArrayAware = new ToDoubleArrayAwareConstant(x)

  def Plot(): net.vpc.scholar.hadruplot.PlotBuilder = net.vpc.scholar.hadruplot.Plot.builder()

  def samples(x: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray())

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray(), y.toDoubleArray)

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware, z: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray, y.toDoubleArray, z.toDoubleArray)

  implicit def convertImplicitToMatrixOfComplex(x: TMatrix[Complex]): ComplexMatrix = new ComplexMatrixFromTComplexMatrix(x)

  implicit def convertImplicitToDoubleArrayAware(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray

  implicit def convertImplicitDoubleColonTuple2ToArray(x: DoubleColonTuple2): Array[Double] = MathsBase.dsteps(x.left, x.right)

  implicit def convertImplicitDoubleColonTuple3ToArray(x: DoubleColonTuple3): Array[Double] = MathsBase.dsteps(x._1, x._3, x._2)

  implicit def convertImplicitDoubleToComplex(x: Double): Complex = complex(x)

  implicit def convertImplicitArrayExprToExprList(x: Array[Expr]): TVector[Expr] = MathsBase.elist(x: _*)

  implicit def convertImplicitArrayComplexToExprList(x: Array[Complex]): TVector[Complex] = MathsBase.clist(x: _*)

  implicit def convertImplicitArrayComplexToVector(x: Array[Complex]): ComplexVector = MathsBase.columnVector(x: _*)

  implicit def convertImplicitIntToComplex(x: Int): Complex = complex(x)

  implicit def convertImplicitii2dd(x: (Int, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitid2dd(x: (Int, Double)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitdi2dd(x: (Double, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitii2id2(x: Iterable[(Int, Int)]): Iterable[(Double, Double)] = new Iterable[(Double, Double)] {
    def iterator = convertii2id2(x.iterator);
  }

  implicit def convertDoubleArrayToDList(x: Array[Double]): DoubleVector = ArrayDoubleVector.column(x)

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
    new ElementOp[Expr] {
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

  implicit def convertImplicitC10(x: Array[Expr]): TVector[Expr] = {
    MathsBase.elist(x: _*)
  }

  implicit def convertImplicitC11(x: Array[DoubleToVector]): TVector[Expr] = {
    MathsBase.elist(x: _*)
  }

  implicit def convertImplicitRightArrowUplet2Double(x: Tuple2[Double, Double]): net.vpc.scholar.hadrumaths.RightArrowUplet2.Double = {
    new net.vpc.scholar.hadrumaths.RightArrowUplet2.Double(x._1, x._2)
  }

  //  def II(xdomain: Tuple2[Double,java.lang.Double]): Expr = {
  //    MathsBase.expr(Domain.forBounds(xdomain._1, xdomain._2))
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
    scholar.hadrumaths.MathsBase.esum(a.length, (i: Int) => f(i, a(i)))
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


    def **(v: ComplexVector): Complex = value.scalarProduct(v)

    //    def ***(v: Vector): Complex = value.scalarProduct(true, v)

    def **(v: TVector[Expr]): Expr = {
      v.scalarProduct(value.asInstanceOf[TVector[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[TList[Expr]])
    //    }

    def :**(v: java.util.List[ComplexVector]): ComplexVector = MathsBase.vector(value.vscalarProduct(v.toArray(Array.ofDim[ComplexVector](v.size())): _ *))

    //    def :***(v: java.util.List[Vector]): Vector = MathsBase.vector(value.vscalarProduct(true, v.toArray(Array.ofDim[Vector](v.size())): _ *))

    def :**(v: Array[TVector[Complex]]): ComplexVector = MathsBase.vector(value.vscalarProduct(v: _ *))

    //    def :***(v: Array[TList[Complex]]): Vector = MathsBase.vector(value.vscalarProduct(true, v: _ *))

    def +(v: TVector[Complex]): ComplexVector = MathsBase.vector(value.add(v))

    def -(v: TVector[Complex]): ComplexVector = MathsBase.vector(value.sub(v))

    def +(v: Complex): ComplexVector = MathsBase.vector(value.add(v))

    def -(v: Complex): ComplexVector = MathsBase.vector(value.sub(v))

    def *(v: Complex): ComplexVector = MathsBase.vector(value.mul(v))

    def /(v: Complex): ComplexVector = MathsBase.vector(value.div(v))

    def %(v: Complex): ComplexVector = MathsBase.vector(value.rem(v))

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


    def *(v: ComplexVector): ComplexMatrix = {
      MathsBase.matrix(value.toMatrix.mul(v.toMatrix))
    }

    def *(v: ComplexMatrix): ComplexMatrix = {
      MathsBase.matrix(value.toMatrix.mul(v))
    }

    def :*(v: ComplexVector): ComplexVector = MathsBase.vector(value.dotmul(v))

    def :/(v: ComplexVector): ComplexVector = MathsBase.vector(value.dotdiv(v))

    def :^(v: ComplexVector): ComplexVector = MathsBase.vector(value.dotpow(v))

    //    def :^(v: Double): Vector = MathsBase.vector(value.dotpow(complex(v)))
    //
    def :^(v: Complex): ComplexVector = MathsBase.vector(value.dotpow(v))
  }

  implicit class STMatrix[T](val value: TMatrix[T]) {

  }

  implicit class SMatrix(val value: ComplexMatrix) {

    def :**[T](v: TVector[TVector[T]]): TVector[T] = {
      MathsBase.vscalarProduct(value.toVector.asInstanceOf[TVector[T]], v);
    }

    //    def :***[T](v: TList[TList[T]]): TList[T] = {
    //      MathsBase.vhscalarProduct(value.toVector.asInstanceOf[TList[T]], v);
    //    }

    def +(v: ComplexMatrix): ComplexMatrix = value.add(v)

    def +(v: Array[Array[Complex]]): ComplexMatrix = value.add(v)

    def -(v: Array[Array[Complex]]): ComplexMatrix = value.sub(v)

    def *(v: Array[Array[Complex]]): ComplexMatrix = value.mul(v)

    def +(v: Complex): ComplexMatrix = value.add(v)

    //    def +(v: Double): Matrix = value.add(v)

    def -(v: ComplexMatrix): ComplexMatrix = value.sub(v)

    def -(v: Complex): ComplexMatrix = value.sub(v)

    //    def -(v: Double): Matrix = value.sub(v)

    def *(v: ComplexMatrix): ComplexMatrix = value.mul(v)


    def *(v: Complex): ComplexMatrix = value.mul(v)

    def *(v: ComplexVector): ComplexMatrix = value.mul(v.toMatrix)

    def *(v: TVector[Expr]): TMatrix[Expr] = ematrix(value.mul(matrix(v.toMatrix)))

    def *(v: Double): ComplexMatrix = value.mul(v)

    def :*(v: ComplexMatrix): ComplexMatrix = value.dotmul(v)

    def :/(v: ComplexMatrix): ComplexMatrix = value.dotdiv(v)

    def **(v: ComplexMatrix): Complex = {
      value.scalarProduct(v)
    }

    //    def ***(v: Matrix): Complex = {
    //      value.scalarProduct(true, v)
    //    }

    def **(v: ComplexVector): Complex = {
      value.scalarProduct(v)
    }

    //    def ***(v: Vector): Complex = {
    //      value.scalarProduct(true, v)
    //    }

    def **(v: TVector[Expr]): Expr = {
      v.scalarProduct(value.asInstanceOf[TMatrix[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[TMatrix[Expr]])
    //    }

    def **(v: Tuple2[ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2): _*)
    }

    //    def ***(v: Tuple2[Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2): _*)
    //    }

    def **(v: Tuple3[ComplexVector, ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    //    def ***(v: Tuple3[Vector, Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2, v._3): _*)
    //    }

    def **(v: Tuple4[ComplexVector, ComplexVector, ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

    //    def ***(v: Tuple4[Vector, Vector, Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2, v._3, v._4): _*)
    //    }

    def /(v: ComplexMatrix): ComplexMatrix = value.div(v)

    def %(v: ComplexMatrix): ComplexMatrix = value.rem(v)

    def /(v: Complex): ComplexMatrix = value.div(v)

    def /(v: Double): ComplexMatrix = value.div(v)

    def :^(v: Double): ComplexMatrix = value.dotpow(v)

    def +(v: ComplexVector): ComplexMatrix = value.add(v.toMatrix)

    def -(v: ComplexVector): ComplexMatrix = value.sub(v.toMatrix)

    def /(v: ComplexVector): ComplexMatrix = value.div(v.toMatrix)

    def %(v: ComplexVector): ComplexMatrix = value.rem(v.toMatrix)

    def :*(v: ComplexVector): ComplexMatrix = value.dotmul(v.toMatrix)

    def :/(v: ComplexVector): ComplexMatrix = value.dotdiv(v.toMatrix)
  }

  implicit class SGemortry(val value: Geometry) {
    def *(v: Double): Expr = value.multiply(v);

    def *(v: Int): Expr = value.multiply(v);

    def *(v: Expr): Expr = value.multiply(v);

    def *(v: Geometry): Expr = value.multiply(MathsBase.expr(v));
  }


  implicit class SExpr(val value: Expr) extends Any(value, null, null) {

    def +(v: Expr): Expr = add(Any.unwrap(v))

    def !!(): Expr = {
      simplify(SIMPLIFY_OPTION_PRESERVE_ROOT_NAME)
    }

    def unary_- : Expr = MathsBase.neg(value)

    def :+(v: TVector[Expr]): TVector[Expr] = {
      var e = MathsBase.elist();
      e.append(Any.unwrap(value))
      e.appendAll(v)
      e;
    }

    def ++(v: TVector[Expr]): TVector[Expr] = {
      var e = MathsBase.elist();
      e.append(Any.unwrap(value))
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(MathsBase.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    //    def **(v: Expr): Complex = scholar.hadrumaths.MathsBase.scalarProduct(value, Any.unwrap(v));

    def **(v: TVector[Expr]): ComplexVector = scholar.hadrumaths.MathsBase.scalarProduct(value, v);

    //    def ***(v: TList[Expr]): Vector = scholar.hadrumaths.MathsBase.scalarProduct(true, value, v);

    def **(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v));

    //    def ***(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v), true);

    def ^^(v: Expr): Expr = value.pow(Any.unwrap(v));

    def <(v: Expr): Expr = value.lt(Any.unwrap(v));

    def <=(v: Expr): Expr = value.lte(Any.unwrap(v));

    def >(v: Expr): Expr = value.gt(Any.unwrap(v));

    def >=(v: Expr): Expr = value.gte(Any.unwrap(v));

    def ~~(v: Expr): Expr = MathsBase.eq(value, Any.unwrap(v));

    def ===(v: Expr): Expr = MathsBase.eq(value, Any.unwrap(v));

    def :=(v: Expr): Expr = MathsBase.eq(value, Any.unwrap(v));

    def <>(v: Expr): Expr = MathsBase.ne(value, Any.unwrap(v));

    def !==(v: Expr): Expr = MathsBase.ne(value, Any.unwrap(v));

    def &&(v: Expr): Expr = MathsBase.and(value, Any.unwrap(v));

    def ||(v: Expr): Expr = MathsBase.or(value, Any.unwrap(v));

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

    def :+(v: TVector[Expr]): TVector[Expr] = {
      var e = MathsBase.elist();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def ++(v: TVector[Expr]): TVector[Expr] = {
      var e = MathsBase.elist();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = sub(Any.unwrap(v))

    def *(v: Geometry): Expr = mul(MathsBase.expr(v))

    def *(v: Expr): Expr = mul(Any.unwrap(v))

    def *(v: Domain): Expr = mul(DoubleValue.valueOf(1, v))

    def **(v: Expr): Complex = scholar.hadrumaths.MathsBase.scalarProduct(null, value, Any.unwrap(v));

    def ***(v: Expr): Complex = scholar.hadrumaths.MathsBase.scalarProduct(null, value, Any.unwrap(v));

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
  //    //        old;
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

  implicit class SExpressionListArray(val value: Array[TVector[Expr]]) {
    def !!(): Array[TVector[Expr]] = simplify()

    def simplify(): Array[TVector[Expr]] = {
      var c = new Array[TVector[Expr]](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = MathsBase.simplify(value(i))
        i = i + 1;
      }
      c;
    }
  }

  implicit class SExpressionListArray2(val value: Array[Array[TVector[Expr]]]) {
    def !!(): Array[Array[TVector[Expr]]] = simplify()

    def simplify(): Array[Array[TVector[Expr]]] = {
      var c = new Array[Array[TVector[Expr]]](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = value(i).simplify()
        i = i + 1;
      }
      c;
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

    def summ(f: (Int, T) => ComplexMatrix): ComplexMatrix = {
      val it: java.util.Iterator[T] = value.iterator()
      if (!it.hasNext) {
        return MathsBase.zerosMatrix(1);
      }
      var c: ComplexMatrix = f(0, it.next());
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

    def summ(f: (Int, T) => ComplexMatrix): ComplexMatrix = {
      val size: Int = value.size
      if (size == 0) {
        return MathsBase.zerosMatrix(1);
      }
      var c: ComplexMatrix = f(0, value(0));
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


  implicit class STList[T](value: TVector[T]) extends scala.Iterable[T] {

    override def iterator: Iterator[T] = new Iterator[T] {
      private val jiter = value.iterator()

      override def hasNext: Boolean = jiter.hasNext

      override def next(): T = jiter.next();
    }

    def :+(v: T): TVector[T] = {
      value.append(v)
      value
    }

    def :+(v: TVector[T]): TVector[T] =  value.concat(v)

    def ++(v: TVector[T]): TVector[T] =  value.concat(v)
  }

  implicit class STVectorExpr(value: TVector[Expr]) extends STList[Expr](value) {
    def **[P1 <: Expr, P2 <: Expr](v: Tuple2[TVector[P1], TVector[P2]]): Expr = {
      value.to($EXPR).scalarProductAll(expr(v._1), expr(v._2))
    }

    //    def ***[P1 <: Expr, P2 <: Expr](v: Tuple2[TList[P1], TList[P2]]): Expr = {
    //      value.scalarProductAll(true, expr(v._1), expr(v._2))
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TVector[P1], TVector[P2], TVector[P3]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TList[P1], TList[P2], TList[P3]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TVector[P1], TVector[P2], TVector[P3], TVector[P4]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TList[P1], TList[P2], TList[P3], TList[P4]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    //    }

    def :*[P <: Expr](v: TVector[P]): TVector[Expr] = MathsBase.edotmul(value, v.asInstanceOf[TVector[Expr]]);

    def :*[P1 <: Expr, P2 <: Expr](v: Tuple2[TVector[P1], TVector[P2]]): TVector[Expr] = {
      MathsBase.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TVector[P1], TVector[P2], TVector[P3]]): TVector[Expr] = {
      MathsBase.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]], v._3.asInstanceOf[TVector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TVector[P1], TVector[P2], TVector[P3], TVector[P4]]): TVector[Expr] = {
      MathsBase.edotmul(value, v._1.asInstanceOf[TVector[Expr]], v._2.asInstanceOf[TVector[Expr]], v._3.asInstanceOf[TVector[Expr]], v._4.asInstanceOf[TVector[Expr]])
    }

    def **[P1 <: Expr](v: Expr): TVector[Expr] = value.to($EXPR).scalarProduct(v);

    //    def ***[P1 <: Expr](v: Expr): TList[Expr] = value.to($EXPR).scalarProduct(true, v);

    def **[P <: Expr](v: TVector[P]): Expr = value.to($EXPR).scalarProduct(v.to($EXPR));

    //    def ***[P <: Expr](v: TList[P]): Expr = value.to($EXPR).scalarProduct(true, v.to($EXPR));

    def **[P <: Expr](v: TMatrix[P]): Expr = value.to($EXPR).scalarProduct((v.toVector().to($EXPR)));

    //    def ***[P <: Expr](v: TMatrix[P]): Expr = value.to($EXPR).scalarProduct(true, expr(v.toVector()));

    def :**[P1 <: Expr](v: TVector[P1]): TMatrix[Expr] = MathsBase.scalarProductMatrix(value, v.asInstanceOf[TVector[Expr]]).asInstanceOf[TMatrix[Expr]];

    def :***[P1 <: Expr](v: TVector[P1]): TMatrix[Expr] = MathsBase.scalarProductMatrix(value, v.asInstanceOf[TVector[Expr]]).asInstanceOf[TMatrix[Expr]];

    def :**(v: ComplexVector): ComplexMatrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      MathsBase.scalarProductMatrix(value, columnVector(exprs:_*))
    };

    def :***(v: ComplexVector): ComplexMatrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      MathsBase.scalarProductMatrix(value, columnVector(exprs:_*))
    };

    def :**(v: ComplexMatrix): ComplexMatrix = MathsBase.scalarProductMatrix(value, v.toVector.asInstanceOf[TVector[Expr]]);

    def :***(v: ComplexMatrix): ComplexMatrix = MathsBase.scalarProductMatrix(value, v.toVector.asInstanceOf[TVector[Expr]]);
  }

  implicit class SExprList(value: ExprVector) extends STVectorExpr(value) {

    override def :+(v: Expr): ExprVector = {
      value.append(v)
      value
    }

    override def :+(v: TVector[Expr]): ExprVector =  value.concat(v)


    def +(v: Expr): ExprVector = value.add(v)

    def -(v: Expr): ExprVector = value.sub(v)

    def *(v: Expr): ExprVector = value.mul(v)

    def /(v: Expr): ExprVector = value.div(v)

    def %(v: Expr): ExprVector = value.rem(v)

    def !!(): ExprVector = value.simplify(SIMPLIFY_OPTION_PRESERVE_ROOT_NAME)


    def unary_- : ExprVector = value.eval((i: Int, x: Expr) => (-x))


    def map(f: (Int, Expr) => Expr): ExprVector = {
      val max = value.size();
      val list = MathsBase.elist(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, value(n)));
        n += 1
      }
      list;
    }

    def cross(other: TVector[Expr], f: IndexedExpr2 => Expr): ExprVector = {
      val nmax = value.size();
      val mmax = other.size();
      val list = MathsBase.elist(nmax * mmax);
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

    def ::+(v: TVector[Expr]): ExprVector = {
      value.copy().appendAll(v)
    }

    def ::+(v: Expr): ExprVector = value.append(v)

    override def ++(v: TVector[Expr]): ExprVector = value.concat(v)

    def ++(v: Expr): ExprVector = value.append(v)

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

  implicit class SDoubleList(val value: TVector[java.lang.Double]) {


    def +(v: java.lang.Double): TVector[java.lang.Double] = scholar.hadrumaths.MathsBase.add[java.lang.Double](value, v)

    def -(v: java.lang.Double): TVector[java.lang.Double] = scholar.hadrumaths.MathsBase.sub[java.lang.Double](value, v)

    def *(v: java.lang.Double): TVector[java.lang.Double] = MathsBase.mul[java.lang.Double](value, v)

    def /(v: java.lang.Double): TVector[java.lang.Double] = scholar.hadrumaths.MathsBase.div[java.lang.Double](value, v)

    def %(v: java.lang.Double): TVector[java.lang.Double] = scholar.hadrumaths.MathsBase.rem[java.lang.Double](value, v)

    def !!(): TVector[java.lang.Double] = value;


    def unary_- : TVector[java.lang.Double] = {
     value.eval(
        (i: Int, x: java.lang.Double) => (-x)
      )

    }

    def ++(v: java.lang.Double): TVector[java.lang.Double] = {
      value.append(v)
      value
    }

    def ++(v: TVector[java.lang.Double]): TVector[java.lang.Double] = {
      value.appendAll(v)
      value
    }

    def :+(v: java.lang.Double): TVector[java.lang.Double] = {
      value.append(v)
      value
    }

    def :+(v: TVector[java.lang.Double]): TVector[java.lang.Double] = {
      value.appendAll(v)
      value
    }

    def baa(v: java.lang.Double): TVector[java.lang.Double] = {
      value.append(v)
      value
    }


    def apply(row: Int): java.lang.Double = {
      value.get(row);
    }

    def map(f: (Int, Expr) => Expr): TVector[Expr] = {
      val max = value.size();
      val list = MathsBase.elist(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, expr(value(n))));
        n += 1
      }
      list;
    }

    def cross(other: TVector[Expr], f: IndexedExpr2 => Expr): TVector[Expr] = {
      val nmax = value.size();
      val mmax = other.size();
      val list = MathsBase.elist(nmax * mmax);
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

    def *(v: Geometry): Expr = toComplex.mul(MathsBase.expr(v))

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
      value.simplify(SIMPLIFY_OPTION_PRESERVE_ROOT_NAME)
    }

  }


  implicit class SExprCube(val value: ExprCube) {

    def !!(): ExprCube = {
      value.simplify(SIMPLIFY_OPTION_PRESERVE_ROOT_NAME)
    }
  }


  ////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////////////////////////////////////
  val PI: Double = Math.PI
  val E: Double = Math.E
  val DDZERO: DoubleToDouble = MathsBase.DDZERO
  val DDNAN: DoubleToDouble = MathsBase.DDNAN
  val DCZERO: DoubleToComplex = MathsBase.DCZERO
  val DVZERO3: DoubleToVector = MathsBase.DVZERO3
  val EZERO: Expr = MathsBase.EZERO
  val EONE: Expr = MathsBase.EONE
  val X: Expr = MathsBase.X
  val Y: Expr = MathsBase.Y
  val Z: Expr = MathsBase.Z
  val HALF_PI: Double = MathsBase.HALF_PI
  val I: Complex = MathsBase.I
  val CNaN: Complex = MathsBase.CNaN
  val CONE: Complex = MathsBase.CONE
  val CZERO: Complex = MathsBase.CZERO //    public static  boolean DEBUG = true;

  val DVZERO1: DoubleToVector = MathsBase.DVZERO1
  val DVZERO2: DoubleToVector = MathsBase.DVZERO2
  val î: Complex = MathsBase.î
  val ê: Expr = MathsBase.ê
  val ĉ: Complex = MathsBase.ĉ
  val METER: Double = MathsBase.METER
  val HZ: Double = MathsBase.HZ
  val BYTE: Long = MathsBase.BYTE
  val MILLISECOND: Long = MathsBase.MILLISECOND
  /**
   * kibibyte
   */
  val KiBYTE: Int = MathsBase.KiBYTE
  /**
   * mibibyte
   */
  val MiBYTE: Int = MathsBase.MiBYTE
  /**
   * TEBI Byte
   */
  val GiBYTE: Long = MathsBase.GiBYTE
  val TiBYTE: Long = MathsBase.TiBYTE
  /**
   * PEBI Byte
   */
  val PiBYTE: Long = MathsBase.PiBYTE
  /**
   * exbibyte
   */
  val EiBYTE: Long = MathsBase.EiBYTE

  val YOCTO: Double = MathsBase.YOCTO
  val ZEPTO: Double = MathsBase.ZEPTO
  val ATTO: Double = MathsBase.ATTO
  val FEMTO: Double = MathsBase.FEMTO
  val PICO: Double = MathsBase.PICO
  val NANO: Double = MathsBase.NANO
  val MICRO: Double = MathsBase.MICRO
  val MILLI: Double = MathsBase.MILLI
  val CENTI: Double = MathsBase.CENTI
  val DECI: Double = MathsBase.DECI
  /**
   * DECA
   */
  val DECA: Int = MathsBase.DECA
  /**
   * HECTO
   */
  val HECTO: Int = MathsBase.HECTO

  /**
   * KILO
   */
  val KILO: Int = MathsBase.KILO
  /**
   * MEGA
   */
  val MEGA: Int = MathsBase.MEGA
  val GIGA: Long = MathsBase.GIGA
  /**
   * TERA
   */
  val TERA: Long = MathsBase.TERA
  /**
   * PETA
   */
  val PETA: Long = MathsBase.PETA
  /**
   * EXA
   */
  val EXA: Long = MathsBase.EXA
  /**
   * ZETTA
   */
  val ZETTA: Long = MathsBase.ZETTA
  /**
   * YOTTA
   */
  val YOTTA: Long = MathsBase.YOTTA
  val SECOND: Long = MathsBase.SECOND
  val MINUTE: Long = MathsBase.MINUTE
  val HOUR: Long = MathsBase.HOUR
  val DAY: Long = MathsBase.DAY
  val KHZ: Double = MathsBase.KHZ
  val MHZ: Double = MathsBase.MHZ
  val GHZ: Double = MathsBase.GHZ
  val MILLIMETER: Double = MathsBase.MILLIMETER
  val MM: Double = MathsBase.MM
  val CM: Double = MathsBase.CM
  val CENTIMETER: Double = MathsBase.CENTIMETER
  /**
   * light celerity. speed of light in vacuum
   */
  //    public static  final int C = 300000000;
  val C: Int = MathsBase.C //m.s^-1

  /**
   * Newtonian constant of gravitation
   */
  val G: Double = MathsBase.G //m3·kg^−1·s^−2;

  /**
   * Planck constant
   */
  val H: Double = MathsBase.H //J·s;

  /**
   * Reduced Planck constant
   */
  val Hr: Double = MathsBase.Hr
  /**
   * magnetic constant (vacuum permeability)
   */
  val U0: Double = MathsBase.U0 //N·A−2

  /**
   * electric constant (vacuum permittivity) =1/(u0*C^2)
   **/
  val EPS0: Double = MathsBase.EPS0 //F·m−1

  /**
   * characteristic impedance of vacuum =1/(u0*C)
   */
  val Z0: Double = MathsBase.Z0
  /**
   * Coulomb's constant
   */
  val Ke: Double = MathsBase.Ke
  /**
   * elementary charge
   */
  val Qe: Double = MathsBase.Qe //C

  val COMPLEX_VECTOR_SPACE: VectorSpace[Complex] = MathsBase.COMPLEX_VECTOR_SPACE
  val EXPR_VECTOR_SPACE: VectorSpace[Expr] = MathsBase.EXPR_VECTOR_SPACE
  val DOUBLE_VECTOR_SPACE: VectorSpace[java.lang.Double] = MathsBase.DOUBLE_VECTOR_SPACE
  val X_AXIS: Int = MathsBase.X_AXIS
  val Y_AXIS: Int = MathsBase.Y_AXIS
  val Z_AXIS: Int = MathsBase.Z_AXIS
  val MATRIX_STORE_MANAGER: TStoreManager[ComplexMatrix] = MathsBase.MATRIX_STORE_MANAGER
  val TMATRIX_STORE_MANAGER: TStoreManager[TMatrix[_]] = MathsBase.TMATRIX_STORE_MANAGER

  val TVECTOR_STORE_MANAGER: TStoreManager[TVector[_]] = MathsBase.TVECTOR_STORE_MANAGER

  val VECTOR_STORE_MANAGER: TStoreManager[ComplexVector] = MathsBase.VECTOR_STORE_MANAGER
  val IDENTITY: Converter[_, _] = MathsBase.IDENTITY
  val COMPLEX_TO_DOUBLE: Converter[Complex, java.lang.Double] = MathsBase.COMPLEX_TO_DOUBLE
  val DOUBLE_TO_COMPLEX: Converter[java.lang.Double, Complex] = MathsBase.DOUBLE_TO_COMPLEX
  val DOUBLE_TO_TVECTOR: Converter[java.lang.Double, TVector[_]] = MathsBase.DOUBLE_TO_TVECTOR
  val TVECTOR_TO_DOUBLE: Converter[TVector[_], java.lang.Double] = MathsBase.TVECTOR_TO_DOUBLE
  val COMPLEX_TO_TVECTOR: Converter[Complex, TVector[_]] = MathsBase.COMPLEX_TO_TVECTOR
  val TVECTOR_TO_COMPLEX: Converter[TVector[_], Complex] = MathsBase.TVECTOR_TO_COMPLEX
  val COMPLEX_TO_EXPR: Converter[Complex, Expr] = MathsBase.COMPLEX_TO_EXPR
  val EXPR_TO_COMPLEX: Converter[Expr, Complex] = MathsBase.EXPR_TO_COMPLEX
  val DOUBLE_TO_EXPR: Converter[java.lang.Double, Expr] = MathsBase.DOUBLE_TO_EXPR
  val EXPR_TO_DOUBLE: Converter[Expr, java.lang.Double] = MathsBase.EXPR_TO_DOUBLE
  //    public static  String getAxisLabel(int axis){
  //        switch(axis){
  //            case X_AXIS:return "X";
  //            case Y_AXIS:return "Y";
  //            case Z_AXIS:return "Z";
  //        }
  //        throw new IllegalArgumentException("Unknown Axis "+axis);
  //    }
  val $STRING: TypeName[String] = MathsBase.$STRING
  val $COMPLEX: TypeName[Complex] = MathsBase.$COMPLEX
  val $MATRIX: TypeName[ComplexMatrix] = MathsBase.$MATRIX
  val $VECTOR: TypeName[ComplexVector] = MathsBase.$VECTOR
  val $CMATRIX: TypeName[TMatrix[Complex]] = MathsBase.$CMATRIX
  val $CVECTOR: TypeName[TVector[Complex]] = MathsBase.$CVECTOR
  val $DOUBLE: TypeName[java.lang.Double] = MathsBase.$DOUBLE
  val $BOOLEAN: TypeName[java.lang.Boolean] = MathsBase.$BOOLEAN
  val $POINT: TypeName[Point] = MathsBase.$POINT
  val $FILE: TypeName[File] = MathsBase.$FILE
  //</editor-fold>
  val $INTEGER: TypeName[java.lang.Integer] = MathsBase.$INTEGER
  val $LONG: TypeName[java.lang.Long] = MathsBase.$LONG
  val $EXPR: TypeName[Expr] = MathsBase.$EXPR
  val $CLIST: TypeName[TVector[Complex]] = MathsBase.$CLIST
  val $ELIST: TypeName[TVector[Expr]] = MathsBase.$ELIST
  val $DLIST: TypeName[TVector[java.lang.Double]] = MathsBase.$DLIST
  val $DLIST2: TypeName[TVector[TVector[java.lang.Double]]] = MathsBase.$DLIST2
  val $ILIST: TypeName[TVector[java.lang.Integer]] = MathsBase.$ILIST
  val $BLIST: TypeName[TVector[java.lang.Boolean]] = MathsBase.$BLIST
  val $MLIST: TypeName[TVector[ComplexMatrix]] = MathsBase.$MLIST
  val Config: MathsConfig = MathsBase.Config
  val PlotConfig: PlotConfig = net.vpc.scholar.hadruplot.Plot.Config
  var DISTANCE_DOUBLE: DistanceStrategy[java.lang.Double] = MathsBase.DISTANCE_DOUBLE
  var DISTANCE_COMPLEX: DistanceStrategy[Complex] = MathsBase.DISTANCE_COMPLEX
  var DISTANCE_MATRIX: DistanceStrategy[ComplexMatrix] = MathsBase.DISTANCE_MATRIX
  var DISTANCE_VECTOR: DistanceStrategy[ComplexVector] = MathsBase.DISTANCE_VECTOR


  def xdomain(min: Double, max: Double): Domain = {
   MathsBase.xdomain(min, max)
  }

  def ydomain(min: Double, max: Double): Domain = {
   MathsBase.ydomain(min, max)
  }

  def ydomain(min: DomainExpr, max: DomainExpr): DomainExpr = {
   MathsBase.ydomain(min, max)
  }

  def zdomain(min: Double, max: Double): Domain = {
   MathsBase.zdomain(min, max)
  }

  def zdomain(min: Expr, max: Expr): DomainExpr = {
   MathsBase.zdomain(min, max)
  }

  def domain(u: RightArrowUplet2.Double): Domain = {
   MathsBase.domain(u)
  }

  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
   MathsBase.domain(ux, uy)
  }

  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
   MathsBase.domain(ux, uy, uz)
  }

  def domain(u: RightArrowUplet2.Expr): Expr = {
   MathsBase.domain(u)
  }

  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
   MathsBase.domain(ux, uy)
  }

  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
   MathsBase.domain(ux, uy, uz)
  }

  def domain(min: Expr, max: Expr): DomainExpr = {
   MathsBase.domain(min, max)
  }

  def domain(min: Double, max: Double): Domain = {
   MathsBase.domain(min, max)
  }

  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
   MathsBase.domain(xmin, xmax, ymin, ymax)
  }

  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
   MathsBase.domain(xmin, xmax, ymin, ymax)
  }

  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
   MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
   MathsBase.domain(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def II(u: RightArrowUplet2.Double): Domain = {
   MathsBase.II(u)
  }

  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
   MathsBase.II(ux, uy)
  }

  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
   MathsBase.II(ux, uy, uz)
  }

  def II(u: RightArrowUplet2.Expr): Expr = {
   MathsBase.II(u)
  }

  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
   MathsBase.II(ux, uy)
  }

  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
   MathsBase.II(ux, uy, uz)
  }

  def II(min: Expr, max: Expr): DomainExpr = {
   MathsBase.II(min, max)
  }

  def II(min: Double, max: Double): Domain = {
   MathsBase.II(min, max)
  }

  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
   MathsBase.II(xmin, xmax, ymin, ymax)
  }

  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
   MathsBase.II(xmin, xmax, ymin, ymax)
  }

  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
   MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
   MathsBase.II(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def param(name: String): DoubleParam = {
   MathsBase.param(name)
  }

  def doubleParamSet(param: Param): DoubleArrayParamSet = {
   MathsBase.doubleParamSet(param)
  }

  def paramSet(param: Param, values: Array[Double]): DoubleArrayParamSet = {
   MathsBase.paramSet(param, values)
  }

  def paramSet(param: Param, values: Array[Float]): FloatArrayParamSet = {
   MathsBase.paramSet(param, values)
  }

  def floatParamSet(param: Param): FloatArrayParamSet = {
   MathsBase.floatParamSet(param)
  }

  def paramSet(param: Param, values: Array[Long]): LongArrayParamSet = {
   MathsBase.paramSet(param, values)
  }

  def longParamSet(param: Param): LongArrayParamSet = {
   MathsBase.longParamSet(param)
  }

  def paramSet[T](param: Param, values: Array[T]): ArrayParamSet[T] = {
    //Ignore Idea Error, this compiles in maven
   MathsBase.paramSet(param, values.asInstanceOf[Array[T with Object]])
  }

  def objectParamSet[T](param: Param): ArrayParamSet[T] = {
   MathsBase.objectParamSet(param)
  }

  def paramSet(param: Param, values: Array[Int]): IntArrayParamSet = {
   MathsBase.paramSet(param, values)
  }

  def intParamSet(param: Param): IntArrayParamSet = {
   MathsBase.intParamSet(param)
  }

  def paramSet(param: Param, values: Array[Boolean]): BooleanArrayParamSet = {
   MathsBase.paramSet(param, values)
  }

  def boolParamSet(param: Param): BooleanArrayParamSet = {
   MathsBase.boolParamSet(param)
  }

  def xParamSet(xsamples: Int): XParamSet = {
   MathsBase.xParamSet(xsamples)
  }

  def xyParamSet(xsamples: Int, ysamples: Int): XParamSet = {
   MathsBase.xyParamSet(xsamples, ysamples)
  }

  def xyzParamSet(xsamples: Int, ysamples: Int, zsamples: Int): XParamSet = {
   MathsBase.xyzParamSet(xsamples, ysamples, zsamples)
  }

  def zerosMatrix(other: ComplexMatrix): ComplexMatrix = {
   MathsBase.zerosMatrix(other)
  }

  def constantMatrix(dim: Int, value: Complex): ComplexMatrix = {
   MathsBase.constantMatrix(dim, value)
  }

  def onesMatrix(dim: Int): ComplexMatrix = {
   MathsBase.onesMatrix(dim)
  }

  def onesMatrix(rows: Int, cols: Int): ComplexMatrix = {
   MathsBase.onesMatrix(rows, cols)
  }

  def constantMatrix(rows: Int, cols: Int, value: Complex): ComplexMatrix = {
   MathsBase.constantMatrix(rows, cols, value)
  }

  def zerosMatrix(dim: Int): ComplexMatrix = {
   MathsBase.zerosMatrix(dim)
  }

  def I(iValue: Array[Array[Complex]]): ComplexMatrix = {
   MathsBase.I(iValue)
  }

  def zerosMatrix(rows: Int, cols: Int): ComplexMatrix = {
   MathsBase.zerosMatrix(rows, cols)
  }

  def identityMatrix(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.identityMatrix(c)
  }

  def NaNMatrix(dim: Int): ComplexMatrix = {
   MathsBase.NaNMatrix(dim)
  }

  def NaNMatrix(rows: Int, cols: Int): ComplexMatrix = {
   MathsBase.NaNMatrix(rows, cols)
  }

  def identityMatrix(dim: Int): ComplexMatrix = {
   MathsBase.identityMatrix(dim)
  }

  def identityMatrix(rows: Int, cols: Int): ComplexMatrix = {
   MathsBase.identityMatrix(rows, cols)
  }

  def matrix(matrix: ComplexMatrix): ComplexMatrix = {
   MathsBase.matrix(matrix)
  }

  def matrix(string: String): ComplexMatrix = {
   MathsBase.matrix(string)
  }

  def matrix(complex: Array[Array[Complex]]): ComplexMatrix = {
   MathsBase.matrix(complex)
  }

  def matrix(complex: Array[Array[Double]]): ComplexMatrix = {
   MathsBase.matrix(complex)
  }

  def matrix(rows: Int, cols: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.matrix(rows, cols, cellFactory)
  }

  def columnMatrix(values: Complex*): ComplexMatrix = {
   MathsBase.columnMatrix(values: _ *)
  }

  // removed because doubles will be converted to complexes
  //  def columnMatrix(values: Double*): Matrix = {
  //   MathsBase.columnMatrix(values:_ *)
  //  }

  def columnMatrix(rows: Int, cellFactory: TVectorCell[Complex]): ComplexMatrix = {
   MathsBase.columnMatrix(rows, cellFactory)
  }

  //  REMOVED because doubles will be converted to complexes
  //  def rowMatrix(values: Double*): Matrix = {
  //   MathsBase.rowMatrix(values:_ *)
  //  }

  def rowMatrix(values: Complex*): ComplexMatrix = {
   MathsBase.rowMatrix(values: _ *)
  }


  def rowMatrix(columns: Int, cellFactory: TVectorCell[Complex]): ComplexMatrix = {
   MathsBase.rowMatrix(columns, cellFactory)
  }

  def symmetricMatrix(rows: Int, cols: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.symmetricMatrix(rows, cols, cellFactory)
  }

  def hermitianMatrix(rows: Int, cols: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.hermitianMatrix(rows, cols, cellFactory)
  }

  def diagonalMatrix(rows: Int, cellFactory: TVectorCell[Complex]): ComplexMatrix = {
   MathsBase.diagonalMatrix(rows, cellFactory)
  }

  def diagonalMatrix(c: Complex*): ComplexMatrix = {
   MathsBase.diagonalMatrix(c: _ *)
  }

  def matrix(dim: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.matrix(dim, cellFactory)
  }

  def matrix(rows: Int, columns: Int): ComplexMatrix = {
   MathsBase.matrix(rows, columns)
  }

  def symmetricMatrix(dim: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.symmetricMatrix(dim, cellFactory)
  }

  def hermitianMatrix(dim: Int, cellFactory: TMatrixCell[Complex]): ComplexMatrix = {
   MathsBase.hermitianMatrix(dim, cellFactory)
  }

  def randomRealMatrix(m: Int, n: Int): ComplexMatrix = {
   MathsBase.randomRealMatrix(m, n)
  }

  def randomRealMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
   MathsBase.randomRealMatrix(m, n, min, max)
  }

  def randomRealMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
   MathsBase.randomRealMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
   MathsBase.randomImagMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
   MathsBase.randomImagMatrix(m, n, min, max)
  }

  def randomMatrix(m: Int, n: Int, minReal: Int, maxReal: Int, minImag: Int, maxImag: Int): ComplexMatrix = {
   MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
  }

  def randomMatrix(m: Int, n: Int, minReal: Double, maxReal: Double, minImag: Double, maxImag: Double): ComplexMatrix = {
   MathsBase.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
  }

  def randomMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
   MathsBase.randomMatrix(m, n, min, max)
  }

  def randomMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
   MathsBase.randomMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int): ComplexMatrix = {
   MathsBase.randomImagMatrix(m, n)
  }

  @throws[UncheckedIOException]
  def loadTMatrix[T](componentType: TypeName[T], file: File): TMatrix[T] = {
   MathsBase.loadTMatrix(componentType, file)
  }

  @throws[UncheckedIOException]
  def loadMatrix(file: File): ComplexMatrix = {
   MathsBase.loadMatrix(file)
  }

  @throws[UncheckedIOException]
  def matrix(file: File): ComplexMatrix = {
   MathsBase.matrix(file)
  }

  @throws[UncheckedIOException]
  def storeMatrix(m: ComplexMatrix, file: String): Unit = {
    MathsBase.storeMatrix(m, file)
  }

  @throws[UncheckedIOException]
  def storeMatrix(m: ComplexMatrix, file: File): Unit = {
    MathsBase.storeMatrix(m, file)
  }

  @throws[UncheckedIOException]
  def loadOrEvalMatrix(file: String, item: TItem[ComplexMatrix]): ComplexMatrix = {
   MathsBase.loadOrEvalMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalVector(file: String, item: TItem[TVector[Complex]]): ComplexVector = {
   MathsBase.loadOrEvalVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalMatrix(file: File, item: TItem[ComplexMatrix]): ComplexMatrix = {
   MathsBase.loadOrEvalMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalVector(file: File, item: TItem[TVector[Complex]]): ComplexVector = {
   MathsBase.loadOrEvalVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTMatrix[T](file: String, item: TItem[TMatrix[T]]): TMatrix[_] = {
   MathsBase.loadOrEvalTMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTVector[T](file: String, item: TItem[TVector[T]]): TVector[T] = {
   MathsBase.loadOrEvalTVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTMatrix[T](file: File, item: TItem[TMatrix[T]]): TMatrix[T] = {
   MathsBase.loadOrEvalTMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTVector[T](file: File, item: TItem[TVector[T]]): TVector[_] = {
   MathsBase.loadOrEvalTVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEval[T](`type`: TypeName[T], file: File, item: TItem[T]): T = {
   MathsBase.loadOrEval(`type`, file, item)
  }

  @throws[UncheckedIOException]
  def loadMatrix(file: String): ComplexMatrix = {
   MathsBase.loadMatrix(file)
  }

  def inv(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.inv(c)
  }

  def tr(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.tr(c)
  }

  def trh(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.trh(c)
  }

  def transpose(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.transpose(c)
  }

  def transposeHermitian(c: ComplexMatrix): ComplexMatrix = {
   MathsBase.transposeHermitian(c)
  }

  def rowVector(elems: Array[Complex]): ComplexVector = {
   MathsBase.rowVector(elems)
  }

  def constantColumnVector(size: Int, c: Complex): ComplexVector = {
   MathsBase.constantColumnVector(size, c)
  }

  def constantRowVector(size: Int, c: Complex): ComplexVector = {
   MathsBase.constantRowVector(size, c)
  }

  def zerosVector(size: Int): ComplexVector = {
   MathsBase.zerosVector(size)
  }

  def zerosColumnVector(size: Int): ComplexVector = {
   MathsBase.zerosColumnVector(size)
  }

  def zerosRowVector(size: Int): ComplexVector = {
   MathsBase.zerosRowVector(size)
  }

  def NaNColumnVector(dim: Int): ComplexVector = {
   MathsBase.NaNColumnVector(dim)
  }

  def NaNRowVector(dim: Int): ComplexVector = {
   MathsBase.NaNRowVector(dim)
  }

  def columnVector(expr: Expr*): TVector[Expr] = {
   MathsBase.columnVector(expr:_ *)
  }

  def rowVector(expr: Expr*): TVector[Expr] = {
   MathsBase.rowVector(expr :_ *)
  }

  def columnEVector(rows: Int, cellFactory: TVectorCell[Expr]): ExprVector = {
   MathsBase.columnEVector(rows, cellFactory)
  }

  def rowEVector(rows: Int, cellFactory: TVectorCell[Expr]): ExprVector = {
   MathsBase.rowEVector(rows, cellFactory)
  }

  def updatableOf[T](vector: TVector[T]): TVector[T] = {
   MathsBase.updatableOf(vector)
  }

  def copyOf(`val`: Array[Array[Complex]]): Array[Array[Complex]] = {
   MathsBase.copyOf(`val`)
  }

  def copyOf(`val`: Array[Complex]): Array[Complex] = {
   MathsBase.copyOf(`val`)
  }

  def copyOf[T](vector: TVector[T]): TVector[T] = {
   MathsBase.copyOf(vector)
  }

  def columnTVector[T](cls: TypeName[T], cellFactory: TVectorModel[T]): TVector[T] = {
   MathsBase.columnTVector(cls, cellFactory)
  }

  def rowTVector[T](cls: TypeName[T], cellFactory: TVectorModel[T]): TVector[T] = {
   MathsBase.rowTVector(cls, cellFactory)
  }

  def columnTVector[T](cls: TypeName[T], rows: Int, cellFactory: TVectorCell[T]): TVector[T] = {
   MathsBase.columnTVector(cls, rows, cellFactory)
  }

  def rowTVector[T](cls: TypeName[T], rows: Int, cellFactory: TVectorCell[T]): TVector[T] = {
   MathsBase.rowTVector(cls, rows, cellFactory)
  }

  def columnVector(rows: Int, cellFactory: TVectorCell[Complex]): ComplexVector = {
   MathsBase.columnVector(rows, cellFactory)
  }

  def rowVector(columns: Int, cellFactory: TVectorCell[Complex]): ComplexVector = {
   MathsBase.rowVector(columns, cellFactory)
  }

  def columnVector(elems: Complex*): ComplexVector = {
   MathsBase.columnVector(elems: _ *)
  }

  def columnVector(elems: Array[Double]): ComplexVector = {
   MathsBase.columnVector(elems)
  }

  def rowVector(elems: Array[Double]): ComplexVector = {
   MathsBase.rowVector(elems)
  }

  def column(elems: Array[Complex]): ComplexVector = {
   MathsBase.column(elems)
  }

  def row(elems: Array[Complex]): ComplexVector = {
   MathsBase.row(elems)
  }

  def trh(c: ComplexVector): ComplexVector = {
   MathsBase.trh(c)
  }

  def tr(c: ComplexVector): ComplexVector = {
   MathsBase.tr(c)
  }

  def I(iValue: Double): Complex = {
   MathsBase.I(iValue)
  }

  def abs(a: Complex): Complex = {
   MathsBase.abs(a)
  }

  def absdbl(a: Complex): Double = {
   MathsBase.absdbl(a)
  }

  def getColumn(a: Array[Array[Double]], index: Int): Array[Double] = {
   MathsBase.getColumn(a, index)
  }

  def dtimes(min: Double, max: Double, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Double] = {
   MathsBase.dtimes(min, max, times, maxTimes, strategy)
  }

  def dtimes(min: Double, max: Double, times: Int): Array[Double] = {
   MathsBase.dtimes(min, max, times)
  }

  def ftimes(min: Float, max: Float, times: Int): Array[Float] = {
   MathsBase.ftimes(min, max, times)
  }

  def ltimes(min: Long, max: Long, times: Int): Array[Long] = {
   MathsBase.ltimes(min, max, times)
  }

  def lsteps(min: Long, max: Long, step: Long): Array[Long] = {
   MathsBase.lsteps(min, max, step)
  }

  def itimes(min: Int, max: Int, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Int] = {
   MathsBase.itimes(min, max, times, maxTimes, strategy)
  }

  def dsteps(max: Int): Array[Double] = {
   MathsBase.dsteps(max)
  }

  def dsteps(min: Double, max: Double): Array[Double] = {
   MathsBase.dsteps(min, max)
  }

  def dstepsLength(min: Double, max: Double, step: Double): Double = {
   MathsBase.dstepsLength(min, max, step)
  }

  def dstepsElement(min: Double, max: Double, step: Double, index: Int): Double = {
   MathsBase.dstepsElement(min, max, step, index)
  }

  def dsteps(min: Double, max: Double, step: Double): Array[Double] = {
   MathsBase.dsteps(min, max, step)
  }

  def fsteps(min: Float, max: Float, step: Float): Array[Float] = {
   MathsBase.fsteps(min, max, step)
  }

  def isteps(min: Int, max: Int, step: Int): Array[Int] = {
   MathsBase.isteps(min, max, step)
  }

  def isteps(min: Int, max: Int, step: Int, filter: IntFilter): Array[Int] = {
   MathsBase.isteps(min, max, step, filter)
  }

  def itimes(min: Int, max: Int, times: Int): Array[Int] = {
   MathsBase.itimes(min, max, times)
  }

  def isteps(max: Int): Array[Int] = {
   MathsBase.isteps(max)
  }

  def isteps(min: Int, max: Int): Array[Int] = {
   MathsBase.isteps(min, max)
  }

  def itimes(min: Int, max: Int): Array[Int] = {
   MathsBase.itimes(min, max)
  }

  def hypot(a: Double, b: Double): Double = {
   MathsBase.hypot(a, b)
  }

  def sqr(d: Complex): Complex = {
   MathsBase.sqr(d)
  }

  def sqr(d: Double): Double = {
   MathsBase.sqr(d)
  }

  def sqr(d: Int): Int = {
   MathsBase.sqr(d)
  }

  def sqr(d: Long): Long = {
   MathsBase.sqr(d)
  }

  def sqr(d: Float): Float = {
   MathsBase.sqr(d)
  }

  def sincard(value: Double): Double = {
   MathsBase.sincard(value)
  }

  def minusOnePower(pow: Int): Int = {
   MathsBase.minusOnePower(pow)
  }

  def exp(c: Complex): Complex = {
   MathsBase.exp(c)
  }

  def sin(c: Complex): Complex = {
   MathsBase.sin(c)
  }

  def sinh(c: Complex): Complex = {
   MathsBase.sinh(c)
  }

  def cos(c: Complex): Complex = {
   MathsBase.cos(c)
  }

  def log(c: Complex): Complex = {
   MathsBase.log(c)
  }

  def log10(c: Complex): Complex = {
   MathsBase.log10(c)
  }

  def log10(c: Double): Double = {
   MathsBase.log10(c)
  }

  def log(c: Double): Double = {
   MathsBase.log(c)
  }

  def acotan(c: Double): Double = {
   MathsBase.acotan(c)
  }

  def exp(c: Double): Double = {
   MathsBase.exp(c)
  }

  def arg(c: Double): Double = {
   MathsBase.arg(c)
  }

  def db(c: Complex): Complex = {
   MathsBase.db(c)
  }

  def db2(c: Complex): Complex = {
   MathsBase.db2(c)
  }

  def cosh(c: Complex): Complex = {
   MathsBase.cosh(c)
  }

  def csum(c: Complex*): Complex = {
   MathsBase.csum(c: _ *)
  }

  def sum(c: Complex*): Complex = {
   MathsBase.sum(c: _ *)
  }

  def csum(c: TVectorModel[Complex]): Complex = {
   MathsBase.csum(c)
  }

  def csum(size: Int, c: TVectorCell[Complex]): Complex = {
   MathsBase.csum(size, c)
  }

  @throws[ArithmeticException]
  def chbevl(x: Double, coef: Array[Double], N: Int): Double = {
   MathsBase.chbevl(x, coef, N)
  }

  def pgcd(a: Long, b: Long): Long = {
   MathsBase.pgcd(a, b)
  }

  def pgcd(a: Int, b: Int): Int = {
   MathsBase.pgcd(a, b)
  }

  def toDouble(c: Array[Array[Complex]], toDoubleConverter: PlotDoubleConverter): Array[Array[Double]] = {
   MathsBase.toDouble(c, toDoubleConverter)
  }

  def toDouble(c: Array[Complex], toDoubleConverter: PlotDoubleConverter): Array[Double] = {
   MathsBase.toDouble(c, toDoubleConverter)
  }

  def rangeCC(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
   MathsBase.rangeCC(orderedValues, min, max)
  }

  def rangeCO(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
   MathsBase.rangeCO(orderedValues, min, max)
  }

  def csqrt(d: Double): Complex = {
   MathsBase.csqrt(d)
  }

  def sqrt(d: Complex): Complex = {
   MathsBase.sqrt(d)
  }

  def dsqrt(d: Double): Double = {
   MathsBase.dsqrt(d)
  }

  def cotanh(c: Complex): Complex = {
   MathsBase.cotanh(c)
  }

  def tanh(c: Complex): Complex = {
   MathsBase.tanh(c)
  }

  def inv(c: Complex): Complex = {
   MathsBase.inv(c)
  }

  def tan(c: Complex): Complex = {
   MathsBase.tan(c)
  }

  def cotan(c: Complex): Complex = {
   MathsBase.cotan(c)
  }

  def vector(v: TVector[_]): ComplexVector = {
   MathsBase.vector(v)
  }

  def pow(a: Complex, b: Complex): Complex = {
   MathsBase.pow(a, b)
  }

  def div(a: Complex, b: Complex): Complex = {
   MathsBase.div(a, b)
  }

  def add(a: Complex, b: Complex): Complex = {
   MathsBase.add(a, b)
  }

  def sub(a: Complex, b: Complex): Complex = {
   MathsBase.sub(a, b)
  }

  def norm(a: ComplexMatrix): Double = {
   MathsBase.norm(a)
  }

  def norm(a: ComplexVector): Double = {
    MathsBase.norm(a)
  }

  def norm1(a: ComplexMatrix): Double = {
    MathsBase.norm1(a)
  }

  def norm2(a: ComplexMatrix): Double = {
    MathsBase.norm2(a)
  }

  def norm3(a: ComplexMatrix): Double = {
    MathsBase.norm3(a)
  }

  def normInf(a: ComplexMatrix): Double = {
    MathsBase.normInf(a)
  }

  def complex(fx: DoubleToDouble): DoubleToComplex = {
    MathsBase.complex(fx)
  }

  def complex(fx: DoubleToDouble, fy: DoubleToDouble): DoubleToComplex = {
    MathsBase.complex(fx, fy)
  }

  def randomDouble(value: Double): Double = {
    MathsBase.randomDouble(value)
  }

  def randomDouble(min: Double, max: Double): Double = {
    MathsBase.randomDouble(min, max)
  }

  def randomInt(value: Int): Int = {
    MathsBase.randomInt(value)
  }

  def randomInt(min: Int, max: Int): Int = {
    MathsBase.randomInt(min, max)
  }

  def randomComplex: Complex = {
    MathsBase.randomComplex
  }

  def randomBoolean: Boolean = {
    MathsBase.randomBoolean
  }

  def cross(x: Array[Double], y: Array[Double]): Array[Array[Double]] = {
    MathsBase.cross(x, y)
  }

  def cross(x: Array[Double], y: Array[Double], z: Array[Double]): Array[Array[Double]] = {
    MathsBase.cross(x, y, z)
  }

  def cross(x: Array[Double], y: Array[Double], z: Array[Double], filter: Double3Filter): Array[Array[Double]] = {
    MathsBase.cross(x, y, z, filter)
  }

  def cross(x: Array[Double], y: Array[Double], filter: Double2Filter): Array[Array[Double]] = {
    MathsBase.cross(x, y, filter)
  }

  def cross(x: Array[Int], y: Array[Int]): Array[Array[Int]] = {
    MathsBase.cross(x, y)
  }

  def cross(x: Array[Int], y: Array[Int], z: Array[Int]): Array[Array[Int]] = {
    MathsBase.cross(x, y, z)
  }

  def sineSeq(borders: String, m: Int, n: Int, domain: Domain): TVector[_] = {
    MathsBase.sineSeq(borders, m, n, domain)
  }

  def sineSeq(borders: String, m: Int, n: Int, domain: Domain, plane: PlaneAxis): TVector[_] = {
    MathsBase.sineSeq(borders, m, n, domain, plane)
  }

  def sineSeq(borders: String, m: DoubleParam, n: DoubleParam, domain: Domain): Expr = {
    MathsBase.sineSeq(borders, m, n, domain)
  }

  def rooftop(borders: String, nx: Int, ny: Int, domain: Domain): Expr = {
    MathsBase.rooftop(borders, nx, ny, domain)
  }

  def any(e: Double): Any = {
    MathsBase.any(e)
  }

  def any(e: Expr): Any = {
    MathsBase.any(e)
  }

  //  def any(e: Double): Any = {
  //   MathsBase.any(e)
  //  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double, filter: Double2Filter): ExprVector = {
    MathsBase.seq(pattern, m, mmax, n, nmax, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, max: Double, filter: DoubleFilter): ExprVector = {
    MathsBase.seq(pattern, m, max, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double, p: DoubleParam, pmax: Double, filter: Double3Filter): ExprVector = {
    MathsBase.seq(pattern, m, mmax, n, nmax, p, pmax, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double): ExprVector = {
    MathsBase.seq(pattern, m, mmax, n, nmax)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): ExprVector = {
    MathsBase.seq(pattern, m, mvalues, n, nvalues)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, values: Array[Array[Double]]): ExprVector = {
    MathsBase.seq(pattern, m, n, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, p: DoubleParam, values: Array[Array[Double]]): ExprVector = {
    MathsBase.seq(pattern, m, n, p, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: Array[DoubleParam], values: Array[Array[Double]]): ExprVector = {
   MathsBase.seq(pattern, m, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, min: Double, max: Double): ExprVector = {
    MathsBase.seq(pattern, m, min, max)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, values: Array[Double]): ExprVector = {
    MathsBase.seq(pattern, m, values)
  }

  def matrix(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): ExprMatrix = {
    MathsBase.matrix(pattern, m, mvalues, n, nvalues)
  }

  def cube(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double], p: DoubleParam, pvalues: Array[Double]): ExprCube = {
    MathsBase.cube(pattern, m, mvalues, n, nvalues, p, pvalues)
  }

  def derive(f: Expr, axis: Axis): Expr = {
    MathsBase.derive(f, axis)
  }

  def isReal(e: Expr): Boolean = {
    MathsBase.isReal(e)
  }

  def isImag(e: Expr): Boolean = {
    MathsBase.isImag(e)
  }

  def abs(e: Expr): Expr = {
   MathsBase.abs(e)
  }

  def db(e: Expr): Expr = {
   MathsBase.db(e)
  }

  def db2(e: Expr): Expr = {
   MathsBase.db2(e)
  }

  def complex(e: Int): Complex = {
   MathsBase.complex(e)
  }

  def complex(e: Double): Complex = {
   MathsBase.complex(e)
  }

  def complex(a: Double, b: Double): Complex = {
   MathsBase.complex(a, b)
  }

  def Double(e: Expr): Double = {
   MathsBase.Double(e)
  }

  def real(e: Expr): Expr = {
   MathsBase.real(e)
  }

  def imag(e: Expr): Expr = {
   MathsBase.imag(e)
  }

  def Complex(e: Expr): Complex = {
   MathsBase.Complex(e)
  }

  def complex(e: Expr): Complex = {
    MathsBase.complex(e)
  }

  def Complex(e: Double): Complex = {
    MathsBase.Complex(e)
  }

  def doubleValue(e: Expr): Double = {
   MathsBase.doubleValue(e)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double], dx: Double, dy: Double, dz: Double, axis1: Axis, axis2: Axis, axis3: Axis): Discrete = {
   MathsBase.discrete(domain, model, x, y, z, dx, dy, dz, axis1, axis2, axis3)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double], dx: Double, dy: Double, dz: Double): Discrete = {
   MathsBase.discrete(domain, model, x, y, z, dx, dy, dz)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double]): Discrete = {
   MathsBase.discrete(domain, model, x, y, z)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], dx: Double, dy: Double, dz: Double): Discrete = {
   MathsBase.discrete(domain, model, dx, dy, dz)
  }

  def discrete(model: Array[Array[Array[Complex]]], x: Array[Double], y: Array[Double], z: Array[Double]): Discrete = {
   MathsBase.discrete(model, x, y, z)
  }

  def discrete(model: Array[Array[Complex]], x: Array[Double], y: Array[Double]): Discrete = {
   MathsBase.discrete(model, x, y)
  }

  def discrete(expr: Expr, xsamples: Array[Double], ysamples: Array[Double], zsamples: Array[Double]): Expr = {
   MathsBase.discrete(expr, xsamples, ysamples, zsamples)
  }

  def discrete(expr: Expr, samples: Samples): Expr = {
   MathsBase.discrete(expr, samples)
  }

  def abssqr(e: Expr): Expr = {
   MathsBase.abssqr(e)
  }

  def adaptiveEval(expr: Expr, config: AdaptiveConfig): AdaptiveResult1 = {
   MathsBase.adaptiveEval(expr, config)
  }

  def adaptiveEval[T](expr: AdaptiveFunction1[T], distance: DistanceStrategy[T], domain: DomainX, config: AdaptiveConfig): AdaptiveResult1 = {
   MathsBase.adaptiveEval(expr, distance, domain, config)
  }

  def discrete(expr: Expr): Discrete = {
   MathsBase.discrete(expr)
  }

  def vdiscrete(expr: Expr): VDiscrete = {
   MathsBase.vdiscrete(expr)
  }

  def discrete(expr: Expr, xSamples: Int): Expr = {
   MathsBase.discrete(expr, xSamples)
  }

  def discrete(expr: Expr, xSamples: Int, ySamples: Int): Expr = {
   MathsBase.discrete(expr, xSamples, ySamples)
  }

  def discrete(expr: Expr, xSamples: Int, ySamples: Int, zSamples: Int): Expr = {
   MathsBase.discrete(expr, xSamples, ySamples, zSamples)
  }

  def axis(e: Axis): AxisFunction = {
   MathsBase.axis(e)
  }

  def transformAxis(e: Expr, a1: AxisFunction, a2: AxisFunction, a3: AxisFunction): Expr = {
   MathsBase.transformAxis(e, a1, a2, a3)
  }

  def transformAxis(e: Expr, a1: Axis, a2: Axis, a3: Axis): Expr = {
   MathsBase.transformAxis(e, a1, a2, a3)
  }

  def transformAxis(e: Expr, a1: AxisFunction, a2: AxisFunction): Expr = {
   MathsBase.transformAxis(e, a1, a2)
  }

  def transformAxis(e: Expr, a1: Axis, a2: Axis): Expr = {
   MathsBase.transformAxis(e, a1, a2)
  }

  def sin2(d: Double): Double = {
   MathsBase.sin2(d)
  }

  def cos2(d: Double): Double = {
   MathsBase.cos2(d)
  }

  def isInt(d: Double): Boolean = {
   MathsBase.isInt(d)
  }

  def lcast[T](o: Any, `type`: Class[T]): T = {
   MathsBase.lcast(o, `type`)
  }

  def dump(o: scala.Any): String = {
   MathsBase.dump(o)
  }

  def dumpSimple(o: scala.Any): String = {
   MathsBase.dumpSimple(o)
  }

  def expr(value: Double, geometry: Geometry): DoubleToDouble = {
   MathsBase.expr(value, geometry)
  }

  def expr(value: Double, geometry: Domain): DoubleToDouble = {
   MathsBase.expr(value, geometry)
  }

  def expr(domain: Domain): DoubleToDouble = {
   MathsBase.expr(domain)
  }

  def expr(domain: Geometry): DoubleToDouble = {
   MathsBase.expr(domain)
  }

  def expr(a: Complex, geometry: Geometry): Expr = {
   MathsBase.expr(a, geometry)
  }

  def expr(a: Complex, geometry: Domain): Expr = {
   MathsBase.expr(a, geometry)
  }

  def preload[T <: Expr](list: TVector[T]): TVector[T] = {
   MathsBase.preload(list)
  }

  def withCache[T <: Expr](list: TVector[T]): TVector[T] = {
   MathsBase.withCache(list)
  }

  def abs[T](a: TVector[T]): TVector[T] = {
   MathsBase.abs(a)
  }

  def db[T](a: TVector[T]): TVector[T] = {
   MathsBase.db(a)
  }

  def db2[T](a: TVector[T]): TVector[T] = {
   MathsBase.db2(a)
  }

  def real[T](a: TVector[T]): TVector[T] = {
   MathsBase.real(a)
  }

  def imag[T](a: TVector[T]): TVector[T] = {
   MathsBase.imag(a)
  }

  def real(a: Complex): Double = {
   MathsBase.real(a)
  }

  def imag(a: Complex): Double = {
   MathsBase.imag(a)
  }

  def almostEqualRelative(a: Float, b: Float, maxRelativeError: Float): Boolean = {
   MathsBase.almostEqualRelative(a, b, maxRelativeError)
  }

  def almostEqualRelative(a: Double, b: Double, maxRelativeError: Double): Boolean = {
   MathsBase.almostEqualRelative(a, b, maxRelativeError)
  }

  def almostEqualRelative(a: Complex, b: Complex, maxRelativeError: Double): Boolean = {
   MathsBase.almostEqualRelative(a, b, maxRelativeError)
  }

  def dtimes(param: Param, min: Double, max: Double, times: Int): DoubleArrayParamSet = {
   MathsBase.dtimes(param, min, max, times)
  }

  def dsteps(param: Param, min: Double, max: Double, step: Double): DoubleArrayParamSet = {
   MathsBase.dsteps(param, min, max, step)
  }

  def itimes(param: Param, min: Int, max: Int, times: Int): IntArrayParamSet = {
   MathsBase.itimes(param, min, max, times)
  }

  def isteps(param: Param, min: Int, max: Int, step: Int): IntArrayParamSet = {
   MathsBase.isteps(param, min, max, step)
  }

  def ftimes(param: Param, min: Int, max: Int, times: Int): FloatArrayParamSet = {
   MathsBase.ftimes(param, min, max, times)
  }

  def fsteps(param: Param, min: Int, max: Int, step: Int): FloatArrayParamSet = {
   MathsBase.fsteps(param, min, max, step)
  }

  def ltimes(param: Param, min: Int, max: Int, times: Int): LongArrayParamSet = {
   MathsBase.ltimes(param, min, max, times)
  }

  def lsteps(param: Param, min: Int, max: Int, step: Long): LongArrayParamSet = {
   MathsBase.lsteps(param, min, max, step)
  }

  def sin(v: ComplexVector): ComplexVector = {
   MathsBase.sin(v)
  }

  def cos(v: ComplexVector): ComplexVector = {
   MathsBase.cos(v)
  }

  def tan(v: ComplexVector): ComplexVector = {
   MathsBase.tan(v)
  }

  def cotan(v: ComplexVector): ComplexVector = {
   MathsBase.cotan(v)
  }

  def tanh(v: ComplexVector): ComplexVector = {
   MathsBase.tanh(v)
  }

  def cotanh(v: ComplexVector): ComplexVector = {
   MathsBase.cotanh(v)
  }

  def cosh(v: ComplexVector): ComplexVector = {
   MathsBase.cosh(v)
  }

  def sinh(v: ComplexVector): ComplexVector = {
   MathsBase.sinh(v)
  }

  def log(v: ComplexVector): ComplexVector = {
   MathsBase.log(v)
  }

  def log10(v: ComplexVector): ComplexVector = {
   MathsBase.log10(v)
  }

  def db(v: ComplexVector): ComplexVector = {
   MathsBase.db(v)
  }

  def exp(v: ComplexVector): ComplexVector = {
   MathsBase.exp(v)
  }

  def acosh(v: ComplexVector): ComplexVector = {
   MathsBase.acosh(v)
  }

  def acos(v: ComplexVector): ComplexVector = {
   MathsBase.acos(v)
  }

  def asinh(v: ComplexVector): ComplexVector = {
   MathsBase.asinh(v)
  }

  def asin(v: ComplexVector): ComplexVector = {
   MathsBase.asin(v)
  }

  def atan(v: ComplexVector): ComplexVector = {
   MathsBase.atan(v)
  }

  def acotan(v: ComplexVector): ComplexVector = {
   MathsBase.acotan(v)
  }

  def imag(v: ComplexVector): ComplexVector = {
   MathsBase.imag(v)
  }

  def real(v: ComplexVector): ComplexVector = {
   MathsBase.real(v)
  }

  def abs(v: ComplexVector): ComplexVector = {
   MathsBase.abs(v)
  }

  def abs(v: Array[Complex]): Array[Complex] = {
   MathsBase.abs(v)
  }

  def avg(v: ComplexVector): Complex = {
   MathsBase.avg(v)
  }

  def sum(v: ComplexVector): Complex = {
   MathsBase.sum(v)
  }

  def prod(v: ComplexVector): Complex = {
   MathsBase.prod(v)
  }

  def abs(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.abs(v)
  }

  def sin(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.sin(v)
  }

  def cos(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.cos(v)
  }

  def tan(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.tan(v)
  }

  def cotan(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.cotan(v)
  }

  def tanh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.tanh(v)
  }

  def cotanh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.cotanh(v)
  }

  def cosh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.cosh(v)
  }

  def sinh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.sinh(v)
  }

  def log(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.log(v)
  }

  def log10(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.log10(v)
  }

  def db(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.db(v)
  }

  def exp(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.exp(v)
  }

  def acosh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.acosh(v)
  }

  def acos(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.acos(v)
  }

  def asinh(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.asinh(v)
  }

  def asin(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.asin(v)
  }

  def atan(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.atan(v)
  }

  def acotan(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.acotan(v)
  }

  def imag(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.imag(v)
  }

  def real(v: ComplexMatrix): ComplexMatrix = {
   MathsBase.real(v)
  }

  def real(v: Array[Complex]): Array[Complex] = {
   MathsBase.real(v)
  }

  def realdbl(v: Array[Complex]): Array[Double] = {
   MathsBase.realdbl(v)
  }

  def imag(v: Array[Complex]): Array[Complex] = {
   MathsBase.imag(v)
  }

  def imagdbl(v: Array[Complex]): Array[Double] = {
   MathsBase.imagdbl(v)
  }

  def avg(v: ComplexMatrix): Complex = {
   MathsBase.avg(v)
  }

  def sum(v: ComplexMatrix): Complex = {
   MathsBase.sum(v)
  }

  def prod(v: ComplexMatrix): Complex = {
   MathsBase.prod(v)
  }

  def roundEquals(a: Double, b: Double, epsilon: Double): Boolean = {
   MathsBase.roundEquals(a, b, epsilon)
  }

  def round(`val`: Double, precision: Double): Double = {
   MathsBase.round(`val`, precision)
  }

  def sqrt(v: Double, n: Int): Double = {
   MathsBase.sqrt(v, n)
  }

  def pow(v: Double, n: Double): Double = {
   MathsBase.pow(v, n)
  }

  def db(x: Double): Double = {
   MathsBase.db(x)
  }

  def acosh(x: Double): Double = {
   MathsBase.acosh(x)
  }

  def atanh(x: Double): Double = {
   MathsBase.atanh(x)
  }

  def acotanh(x: Double): Double = {
   MathsBase.acotanh(x)
  }

  def asinh(x: Double): Double = {
   MathsBase.asinh(x)
  }

  def db2(nbr: Double): Double = {
   MathsBase.db2(nbr)
  }

  def sqrt(nbr: Double): Double = {
   MathsBase.sqrt(nbr)
  }

  def inv(x: Double): Double = {
   MathsBase.inv(x)
  }

  def conj(x: Double): Double = {
   MathsBase.conj(x)
  }

  def sin2(x: Array[Double]): Array[Double] = {
   MathsBase.sin2(x)
  }

  def cos2(x: Array[Double]): Array[Double] = {
   MathsBase.cos2(x)
  }

  def sin(x: Array[Double]): Array[Double] = {
   MathsBase.sin(x)
  }

  def cos(x: Array[Double]): Array[Double] = {
   MathsBase.cos(x)
  }

  def tan(x: Array[Double]): Array[Double] = {
   MathsBase.tan(x)
  }

  def cotan(x: Array[Double]): Array[Double] = {
   MathsBase.cotan(x)
  }

  def sinh(x: Array[Double]): Array[Double] = {
   MathsBase.sinh(x)
  }

  def cosh(x: Array[Double]): Array[Double] = {
   MathsBase.cosh(x)
  }

  def tanh(x: Array[Double]): Array[Double] = {
   MathsBase.tanh(x)
  }

  def cotanh(x: Array[Double]): Array[Double] = {
   MathsBase.cotanh(x)
  }

  def max(a: Double, b: Double): Double = {
   MathsBase.max(a, b)
  }

  def max(a: Int, b: Int): Int = {
   MathsBase.max(a, b)
  }

  def max(a: Long, b: Long): Long = {
   MathsBase.max(a, b)
  }

  def min(a: Double, b: Double): Double = {
   MathsBase.min(a, b)
  }

  def min(arr: Array[Double]): Double = {
   MathsBase.min(arr)
  }

  def max(arr: Array[Double]): Double = {
   MathsBase.max(arr)
  }

  def avg(arr: Array[Double]): Double = {
   MathsBase.avg(arr)
  }

  def min(a: Int, b: Int): Int = {
   MathsBase.min(a, b)
  }

  def min(a: Complex, b: Complex): Complex = {
   MathsBase.min(a, b)
  }

  def max(a: Complex, b: Complex): Complex = {
   MathsBase.max(a, b)
  }

  def min(a: Long, b: Long): Long = {
   MathsBase.min(a, b)
  }

  def minMax(a: Array[Double]): Array[Double] = {
   MathsBase.minMax(a)
  }

  def minMaxAbs(a: Array[Double]): Array[Double] = {
   MathsBase.minMaxAbs(a)
  }

  def minMaxAbsNonInfinite(a: Array[Double]): Array[Double] = {
   MathsBase.minMaxAbsNonInfinite(a)
  }

  def avgAbs(arr: Array[Double]): Double = {
   MathsBase.avgAbs(arr)
  }

  def distances(arr: Array[Double]): Array[Double] = {
   MathsBase.distances(arr)
  }

  def div(a: Array[Double], b: Array[Double]): Array[Double] = {
   MathsBase.div(a, b)
  }

  def mul(a: Array[Double], b: Array[Double]): Array[Double] = {
   MathsBase.mul(a, b)
  }

  def sub(a: Array[Double], b: Array[Double]): Array[Double] = {
   MathsBase.sub(a, b)
  }

  def sub(a: Array[Double], b: Double): Array[Double] = {
   MathsBase.sub(a, b)
  }

  def add(a: Array[Double], b: Array[Double]): Array[Double] = {
   MathsBase.add(a, b)
  }

  def db(a: Array[Double]): Array[Double] = {
   MathsBase.db(a)
  }

  def sin(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.sin(c)
  }

  def sin2(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.sin2(c)
  }

  def sin(x: Double): Double = {
   MathsBase.sin(x)
  }

  def cos(x: Double): Double = {
   MathsBase.cos(x)
  }

  def tan(x: Double): Double = {
   MathsBase.tan(x)
  }

  def cotan(x: Double): Double = {
   MathsBase.cotan(x)
  }

  def sinh(x: Double): Double = {
   MathsBase.sinh(x)
  }

  def cosh(x: Double): Double = {
   MathsBase.cosh(x)
  }

  def tanh(x: Double): Double = {
   MathsBase.tanh(x)
  }

  def abs(a: Double): Double = {
   MathsBase.abs(a)
  }

  def abs(a: Int): Int = {
   MathsBase.abs(a)
  }

  def cotanh(x: Double): Double = {
   MathsBase.cotanh(x)
  }

  def acos(x: Double): Double = {
   MathsBase.acos(x)
  }

  def asin(x: Double): Double = {
   MathsBase.asin(x)
  }

  def atan(x: Double): Double = {
   MathsBase.atan(x)
  }

  def sum(c: Double*): Double = {
   MathsBase.sum(c: _ *)
  }

  def mul(a: Array[Double], b: Double): Array[Double] = {
   MathsBase.mul(a, b)
  }

  def mulSelf(x: Array[Double], v: Double): Array[Double] = {
   MathsBase.mulSelf(x, v)
  }

  def div(a: Array[Double], b: Double): Array[Double] = {
   MathsBase.div(a, b)
  }

  def divSelf(x: Array[Double], v: Double): Array[Double] = {
   MathsBase.divSelf(x, v)
  }

  def add(x: Array[Double], v: Double): Array[Double] = {
   MathsBase.add(x, v)
  }

  def addSelf(x: Array[Double], v: Double): Array[Double] = {
   MathsBase.addSelf(x, v)
  }

  def cos(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.cos(c)
  }

  def tan(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.tan(c)
  }

  def cotan(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.cotan(c)
  }

  def sinh(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.sinh(c)
  }

  def cosh(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.cosh(c)
  }

  def tanh(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.tanh(c)
  }

  def cotanh(c: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.cotanh(c)
  }

  def add(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.add(a, b)
  }

  def sub(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.sub(a, b)
  }

  def div(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.div(a, b)
  }

  def mul(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.mul(a, b)
  }

  def db(a: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.db(a)
  }

  def db2(a: Array[Array[Double]]): Array[Array[Double]] = {
   MathsBase.db2(a)
  }

  def If(cond: Expr, exp1: Expr, exp2: Expr): IfExpr = {
   MathsBase.If(cond, exp1, exp2)
  }

  def If(cond: Expr, exp1: Expr): IfExpr = {
   MathsBase.If(cond, exp1)
  }

  def If(cond: Expr): IfExpr = {
   MathsBase.If(cond)
  }

  def or(a: Expr, b: Expr): Expr = {
   MathsBase.or(a, b)
  }

  def and(a: Expr, b: Expr): Expr = {
   MathsBase.and(a, b)
  }

  def not(a: Expr): Expr = {
   MathsBase.not(a)
  }

  def eq(a: Expr, b: Expr): Expr = {
   MathsBase.eq(a, b)
  }

  def ne(a: Expr, b: Expr): Expr = {
   MathsBase.ne(a, b)
  }

  def gte(a: Expr, b: Expr): Expr = {
   MathsBase.gte(a, b)
  }

  def gt(a: Expr, b: Expr): Expr = {
   MathsBase.gt(a, b)
  }

  def lte(a: Expr, b: Expr): Expr = {
   MathsBase.lte(a, b)
  }

  def lt(a: Expr, b: Expr): Expr = {
   MathsBase.lt(a, b)
  }

  def cos(e: Expr): Expr = {
   MathsBase.cos(e)
  }

  def cosh(e: Expr): Expr = {
   MathsBase.cosh(e)
  }

  def sin(e: Expr): Expr = {
   MathsBase.sin(e)
  }

  def sincard(e: Expr): Expr = {
   MathsBase.sincard(e)
  }

  def sinh(e: Expr): Expr = {
   MathsBase.sinh(e)
  }

  def tan(e: Expr): Expr = {
   MathsBase.tan(e)
  }

  def tanh(e: Expr): Expr = {
   MathsBase.tanh(e)
  }

  def cotan(e: Expr): Expr = {
   MathsBase.cotan(e)
  }

  def cotanh(e: Expr): Expr = {
   MathsBase.cotanh(e)
  }

  def sqr(e: Expr): Expr = {
   MathsBase.sqr(e)
  }

  def sqrt(e: Expr): Expr = {
   MathsBase.sqrt(e)
  }

  def inv(e: Expr): Expr = {
   MathsBase.inv(e)
  }

  def neg(e: Expr): Expr = {
   MathsBase.neg(e)
  }

  def exp(e: Expr): Expr = {
   MathsBase.exp(e)
  }

  def atan(e: Expr): Expr = {
   MathsBase.atan(e)
  }

  def acotan(e: Expr): Expr = {
   MathsBase.acotan(e)
  }

  def acos(e: Expr): Expr = {
   MathsBase.acos(e)
  }

  def asin(e: Expr): Expr = {
   MathsBase.asin(e)
  }

  def integrate(e: Expr): Complex = {
   MathsBase.integrate(e)
  }

  def integrate(e: Expr, domain: Domain): Complex = {
   MathsBase.integrate(e, domain)
  }

  def esum(size: Int, f: TVectorCell[Expr]): Expr = {
   MathsBase.esum(size, f)
  }

  def esum(size1: Int, size2: Int, e: TMatrixCell[Expr]): Expr = {
   MathsBase.esum(size1, size2, e)
  }

  def csum(size1: Int, size2: Int, e: TMatrixCell[Complex]): Complex = {
   MathsBase.csum(size1, size2, e)
  }

  def seq(size1: Int, f: TVectorCell[Expr]): TVector[Expr] = {
   MathsBase.seq(size1, f)
  }

  def seq(size1: Int, size2: Int, f: TMatrixCell[Expr]): TVector[Expr] = {
   MathsBase.seq(size1, size2, f)
  }

  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProductCache(gp, fn, monitor)
  }

  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProductCache(sp, gp, fn, monitor)
  }

  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProductCache(sp, gp, fn, axis, monitor)
  }

  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProductCache(gp, fn, axis, monitor)
  }

  def gate(axis: Axis, a: Double, b: Double): Expr = {
   MathsBase.gate(axis, a, b)
  }

  def gate(axis: Expr, a: Double, b: Double): Expr = {
   MathsBase.gate(axis, a, b)
  }

  def gateX(a: Double, b: Double): Expr = {
   MathsBase.gateX(a, b)
  }

  def gateY(a: Double, b: Double): Expr = {
   MathsBase.gateY(a, b)
  }

  def gateZ(a: Double, b: Double): Expr = {
   MathsBase.gateZ(a, b)
  }

  def scalarProduct(f1: DoubleToDouble, f2: DoubleToDouble): Double = {
   MathsBase.scalarProduct(f1, f2)
  }

  def scalarProduct(f1: Expr, f2: TVector[Expr]): ComplexVector = {
   MathsBase.scalarProduct(f1, f2)
  }

  def scalarProduct(f1: Expr, f2: TMatrix[Expr]): ComplexMatrix = {
   MathsBase.scalarProduct(f1, f2)
  }

  def scalarProduct(f2: TVector[Expr], f1: Expr): ComplexVector = {
   MathsBase.scalarProduct(f2, f1)
  }

  def scalarProduct(f2: TMatrix[Expr], f1: Expr): ComplexMatrix = {
   MathsBase.scalarProduct(f2, f1)
  }

  def scalarProduct(domain: Domain, f1: Expr, f2: Expr): Complex = {
   MathsBase.scalarProduct(domain, f1, f2)
  }

  def scalarProduct(f1: Expr, f2: Expr): Complex = {
   MathsBase.scalarProduct(f1, f2)
  }

  def scalarProductMatrix(g: TVector[Expr], f: TVector[Expr]): ComplexMatrix = {
   MathsBase.scalarProductMatrix(g, f)
  }

  def scalarProduct(g: TVector[Expr], f: TVector[Expr]): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f)
  }

  def scalarProduct(g: TVector[Expr], f: TVector[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f, monitor)
  }

  def scalarProductMatrix(g: TVector[Expr], f: TVector[Expr], monitor: ProgressMonitor): ComplexMatrix = {
   MathsBase.scalarProductMatrix(g, f, monitor)
  }

  def scalarProduct(g: TVector[Expr], f: TVector[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f, axis, monitor)
  }

  def scalarProductMatrix(g: Array[Expr], f: Array[Expr]): ComplexMatrix = {
   MathsBase.scalarProductMatrix(g, f)
  }

  def scalarProduct(g: ComplexMatrix, f: ComplexMatrix): Complex = {
   MathsBase.scalarProduct(g, f)
  }

  def scalarProduct(g: ComplexMatrix, f: TVector[Expr]): Expr = {
   MathsBase.scalarProduct(g, f)
  }

  def scalarProductAll(g: ComplexMatrix, f: TVector[Expr]*): Expr = {
   MathsBase.scalarProductAll(g, f: _ *)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr]): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f, monitor)
  }

  def scalarProductMatrix(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): ComplexMatrix = {
   MathsBase.scalarProductMatrix(g, f, monitor)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): TMatrix[Complex] = {
   MathsBase.scalarProduct(g, f, axis, monitor)
  }

  def elist(size: Int): ExprVector = {
   MathsBase.elist(size)
  }

  def elist(row: Boolean, size: Int): ExprVector = {
   MathsBase.elist(row, size)
  }

  def elist(vector: Expr*): ExprVector = {
   MathsBase.elist(vector: _ *)
  }

  def elist(vector: TVector[Complex]): ExprVector = {
   MathsBase.elist(vector)
  }

  def clist(vector: TVector[Expr]): TVector[Complex] = {
   MathsBase.clist(vector)
  }

  def clist: TVector[Complex] = {
   MathsBase.clist
  }

  def clist(size: Int): TVector[Complex] = {
   MathsBase.clist(size)
  }

  def clist(vector: Complex*): TVector[Complex] = {
   MathsBase.clist(vector: _ *)
  }

  def mlist: TVector[ComplexMatrix] = {
   MathsBase.mlist
  }

  def mlist(size: Int): TVector[ComplexMatrix] = {
   MathsBase.mlist(size)
  }

  def mlist(items: ComplexMatrix*): TVector[ComplexMatrix] = {
   MathsBase.mlist(items: _ *)
  }

  def clist2: TVector[TVector[Complex]] = {
   MathsBase.clist2
  }

  def elist2: TVector[TVector[Expr]] = {
   MathsBase.elist2
  }

  def dlist2: TVector[TVector[java.lang.Double]] = {
   MathsBase.dlist2
  }

  def ilist2: TVector[TVector[Integer]] = {
   MathsBase.ilist2
  }

  def mlist2: TVector[TVector[ComplexMatrix]] = {
   MathsBase.mlist2
  }

  def blist2: TVector[TVector[java.lang.Boolean]] = {
   MathsBase.blist2
  }

  def list[T](typeName: TypeName[T]): TVector[T] = {
   MathsBase.list(typeName)
  }

  def list[T](typeName: TypeName[T], initialSize: Int): TVector[T] = {
   MathsBase.list(typeName, initialSize)
  }

  def listro[T](typeName: TypeName[T], row: Boolean, model: TVectorModel[T]): TVector[T] = {
   MathsBase.listro(typeName, row, model)
  }

  def list[T](typeName: TypeName[T], row: Boolean, initialSize: Int): TVector[T] = {
   MathsBase.list(typeName, row, initialSize)
  }

  def list[T](vector: TVector[T]): TVector[T] = {
   MathsBase.list(vector)
  }

  def elist(vector: ComplexMatrix): ExprVector = {
   MathsBase.elist(vector)
  }

  def vscalarProduct[T](vector: TVector[T], tVectors: TVector[TVector[T]]): TVector[T] = {
   MathsBase.vscalarProduct(vector, tVectors)
  }

  def elist: TVector[Expr] = {
   MathsBase.elist
  }

  def concat[T](a: TVector[T]*): TVector[T] = {
   MathsBase.concat(a: _*)
  }

  def dlist: TVector[java.lang.Double] = {
   MathsBase.dlist
  }

  def dlist(items: ToDoubleArrayAware): TVector[java.lang.Double] = {
   MathsBase.dlist(items)
  }

  def dlist(items: Array[Double]): TVector[java.lang.Double] = {
   MathsBase.dlist(items)
  }

  def dlist(row: Boolean, size: Int): TVector[java.lang.Double] = {
   MathsBase.dlist(row, size)
  }

  def dlist(size: Int): TVector[java.lang.Double] = {
   MathsBase.dlist(size)
  }

  def slist: TVector[String] = {
   MathsBase.slist
  }

  def slist(items: Array[String]): TVector[String] = {
   MathsBase.slist(items)
  }

  def slist(row: Boolean, size: Int): TVector[String] = {
   MathsBase.slist(row, size)
  }

  def slist(size: Int): TVector[String] = {
   MathsBase.slist(size)
  }

  def blist: TVector[java.lang.Boolean] = {
   MathsBase.blist
  }

  def dlist(items: Array[Boolean]): TVector[java.lang.Boolean] = {
   MathsBase.dlist(items)
  }

  def blist(row: Boolean, size: Int): TVector[java.lang.Boolean] = {
   MathsBase.blist(row, size)
  }

  def blist(size: Int): TVector[java.lang.Boolean] = {
   MathsBase.blist(size)
  }

  def ilist: IntVector = {
   MathsBase.ilist
  }

  def ilist(items: Array[Int]): TVector[Integer] = {
   MathsBase.ilist(items)
  }

  def ilist(size: Int): TVector[Integer] = {
   MathsBase.ilist(size)
  }

  def ilist(row: Boolean, size: Int): TVector[Integer] = {
   MathsBase.ilist(row, size)
  }

  def llist: LongVector = {
   MathsBase.llist
  }

  def llist(items: Array[Long]): TVector[java.lang.Long] = {
   MathsBase.llist(items)
  }

  def llist(size: Int): TVector[java.lang.Long] = {
   MathsBase.llist(size)
  }

  def llist(row: Boolean, size: Int): TVector[java.lang.Long] = {
   MathsBase.llist(row, size)
  }

  def sum[T](`type`: TypeName[T], arr: T*): T = {
   MathsBase.sum(`type`, arr: _ *)
  }

  def sum[T](`type`: TypeName[T], arr: TVectorModel[T]): T = {
   MathsBase.sum(`type`, arr)
  }

  def sum[T](`type`: TypeName[T], size: Int, arr: TVectorCell[T]): T = {
   MathsBase.sum(`type`, size, arr)
  }

  def mul[T](`type`: TypeName[T], arr: T*): T = {
   MathsBase.mul(`type`, arr: _ *)
  }

  def mul[T](`type`: TypeName[T], arr: TVectorModel[T]): T = {
   MathsBase.mul(`type`, arr)
  }

  def avg(d: Discrete): Complex = {
   MathsBase.avg(d)
  }

  def vsum(d: VDiscrete): DoubleToVector = {
   MathsBase.vsum(d)
  }

  def vavg(d: VDiscrete): DoubleToVector = {
   MathsBase.vavg(d)
  }

  def avg(d: VDiscrete): Complex = {
   MathsBase.avg(d)
  }

  def sum(arr: Expr*): Expr = {
   MathsBase.sum(arr: _ *)
  }

  def esum(arr: TVectorModel[Expr]): Expr = {
   MathsBase.esum(arr)
  }

  def mul[T](a: TMatrix[T], b: TMatrix[T]): TMatrix[T] = {
   MathsBase.mul(a, b)
  }

  def mul(a: ComplexMatrix, b: ComplexMatrix): ComplexMatrix = {
   MathsBase.mul(a, b)
  }

  def mul(a: Expr, b: Expr): Expr = {
   MathsBase.mul(a, b)
  }

  def edotmul(arr: TVector[Expr]*): TVector[Expr] = {
   MathsBase.edotmul(arr: _ *)
  }

  def edotdiv(arr: TVector[Expr]*): TVector[Expr] = {
   MathsBase.edotdiv(arr: _ *)
  }

  def cmul(arr: TVectorModel[Complex]): Complex = {
   MathsBase.cmul(arr)
  }

  def emul(arr: TVectorModel[Expr]): Expr = {
   MathsBase.emul(arr)
  }

  def mul(e: Expr*): Expr = {
   MathsBase.mul(e: _ *)
  }

  def pow(a: Expr, b: Expr): Expr = {
   MathsBase.pow(a, b)
  }

  def sub(a: Expr, b: Expr): Expr = {
   MathsBase.sub(a, b)
  }

  def add(a: Expr, b: java.lang.Double): Expr = {
   MathsBase.add(a, b)
  }

  def mul(a: Expr, b: java.lang.Double): Expr = {
   MathsBase.mul(a, b)
  }

  def sub(a: Expr, b: java.lang.Double): Expr = {
   MathsBase.sub(a, b)
  }

  def div(a: Expr, b: java.lang.Double): Expr = {
   MathsBase.div(a, b)
  }

  def add(a: Expr, b: Expr): Expr = {
   MathsBase.add(a, b)
  }

  def add(a: Expr*): Expr = {
   MathsBase.add(a: _ *)
  }

  def div(a: Expr, b: Expr): Expr = {
   MathsBase.div(a, b)
  }

  def rem(a: Expr, b: Expr): Expr = {
   MathsBase.rem(a, b)
  }

  def expr(value: Double): Expr = {
   MathsBase.expr(value)
  }

  def expr[T](vector: TVector[T]): TVector[Expr] = {
   MathsBase.expr(vector)
  }

  def expr(matrix: TMatrix[Complex]): TMatrix[Expr] = {
   MathsBase.expr(matrix)
  }

  def tmatrix[T](`type`: TypeName[T], model: TMatrixModel[T]): TMatrix[T] = {
    MathsBase.tmatrix(`type`, model)
  }

  def tmatrix[T](`type`: TypeName[T], rows: Int, columns: Int, model: TMatrixCell[T]): TMatrix[T] = {
    MathsBase.tmatrix(`type`, rows, columns, model)
  }

  def simplify[T](a: T): T = {
    MathsBase.simplify(a)
  }

  def simplify(a: Expr): Expr = {
    MathsBase.simplify(a)
  }

  def simplify[T](a: T, simplifyOptions: SimplifyOptions): T = {
    MathsBase.simplify(a,simplifyOptions)
  }

  def simplify(a: Expr,simplifyOptions: SimplifyOptions): Expr = {
    MathsBase.simplify(a,simplifyOptions)
  }

  def norm(a: Expr): Double = {
    MathsBase.norm(a)
  }

  def normalize[T](a: TVector[T]): TVector[T] = {
    MathsBase.normalize(a)
  }

  def normalize(a: Geometry): Expr = {
    MathsBase.normalize(a)
  }

  def normalize(a: Expr): Expr = {
    MathsBase.normalize(a)
  }

  def vector(fx: Expr, fy: Expr): DoubleToVector = {
   MathsBase.vector(fx, fy)
  }

  def vector(fx: Expr): DoubleToVector = {
   MathsBase.vector(fx)
  }

  def vector(fx: Expr, fy: Expr, fz: Expr): DoubleToVector = {
   MathsBase.vector(fx, fy, fz)
  }

  def cos[T](a: TVector[T]): TVector[T] = {
   MathsBase.cos(a)
  }

  def cosh[T](a: TVector[T]): TVector[T] = {
   MathsBase.cosh(a)
  }

  def sin[T](a: TVector[T]): TVector[T] = {
   MathsBase.sin(a)
  }

  def sinh[T](a: TVector[T]): TVector[T] = {
   MathsBase.sinh(a)
  }

  def tan[T](a: TVector[T]): TVector[T] = {
   MathsBase.tan(a)
  }

  def tanh[T](a: TVector[T]): TVector[T] = {
   MathsBase.tanh(a)
  }

  def cotan[T](a: TVector[T]): TVector[T] = {
   MathsBase.cotan(a)
  }

  def cotanh[T](a: TVector[T]): TVector[T] = {
   MathsBase.cotanh(a)
  }

  def sqr[T](a: TVector[T]): TVector[T] = {
   MathsBase.sqr(a)
  }

  def sqrt[T](a: TVector[T]): TVector[T] = {
   MathsBase.sqrt(a)
  }

  def inv[T](a: TVector[T]): TVector[T] = {
   MathsBase.inv(a)
  }

  def neg[T](a: TVector[T]): TVector[T] = {
   MathsBase.neg(a)
  }

  def exp[T](a: TVector[T]): TVector[T] = {
   MathsBase.exp(a)
  }

  def simplify[T](a: TVector[T]): TVector[T] = {
   MathsBase.simplify(a)
  }

  def addAll[T](e: TVector[T], expressions: T*): TVector[T] = {
   MathsBase.addAll(e, expressions: _ *)
  }

  def mulAll[T](e: TVector[T], expressions: T*): TVector[T] = {
   MathsBase.mulAll(e, expressions: _ *)
  }

  def pow[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.pow(a, b)
  }

  def sub[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.sub(a, b)
  }

  def div[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.div(a, b)
  }

  def rem[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.rem(a, b)
  }

  def add[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.add(a, b)
  }

  def mul[T](a: TVector[T], b: T): TVector[T] = {
   MathsBase.mul(a, b)
  }

  def loopOver(values: Array[Array[AnyRef]], action: LoopAction): Unit = {
    MathsBase.loopOver(values, action)
  }

  def loopOver(values: Array[Loop], action: LoopAction): Unit = {
    MathsBase.loopOver(values, action)
  }

  def formatMemory: String = {
   MathsBase.formatMemory
  }

  def formatMetric(value: Double): String = {
   MathsBase.formatMetric(value)
  }

  def memoryInfo: MemoryInfo = {
   MathsBase.memoryInfo
  }

  def memoryMeter: MemoryMeter = {
   MathsBase.memoryMeter
  }

  def inUseMemory: Long = {
   MathsBase.inUseMemory
  }

  def maxFreeMemory: Long = {
   MathsBase.maxFreeMemory
  }

  def formatMemory(bytes: Long): String = {
   MathsBase.formatMemory(bytes)
  }

  def formatFrequency(frequency: Double): String = {
   MathsBase.formatFrequency(frequency)
  }

  def formatDimension(dimension: Double): String = {
   MathsBase.formatDimension(dimension)
  }

  def formatPeriodNanos(period: Long): String = {
   MathsBase.formatPeriodNanos(period)
  }

  def formatPeriodMillis(period: Long): String = {
   MathsBase.formatPeriodMillis(period)
  }

  def sizeOf(src: Class[_]): Int = {
   MathsBase.sizeOf(src)
  }

  def invokeMonitoredAction[T](mon: ProgressMonitor, messagePrefix: String, run: MonitoredAction[T]): T = {
   MathsBase.invokeMonitoredAction(mon, messagePrefix, run)
  }

  def chrono(): Chronometer = {
   MathsBase.chrono
  }

  def chrono(name: String): Chronometer = {
   MathsBase.chrono(name)
  }

  def chrono(name: String, r: Runnable): Chronometer = {
   MathsBase.chrono(name, r)
  }

  def chrono[V](name: String, r: Callable[V]): V = {
   MathsBase.chrono(name, r)
  }

  def solverExecutorService(threads: Int): SolverExecutorService = {
   MathsBase.solverExecutorService(threads)
  }

  def chrono(r: Runnable): Chronometer = {
   MathsBase.chrono(r)
  }

  def percentFormat: DoubleFormat = {
   MathsBase.percentFormat
  }

  def frequencyFormat: DoubleFormat = {
   MathsBase.frequencyFormat
  }

  def metricFormat: DoubleFormat = {
   MathsBase.metricFormat
  }

  def memoryFormat: DoubleFormat = {
   MathsBase.memoryFormat
  }

  def dblformat(format: String): DoubleFormat = {
   MathsBase.dblformat(format)
  }

  def resizePickFirst(array: Array[Double], newSize: Int): Array[Double] = {
   MathsBase.resizePickFirst(array, newSize)
  }

  def resizePickAverage(array: Array[Double], newSize: Int): Array[Double] = {
   MathsBase.resizePickAverage(array, newSize)
  }

  def toArray[T](t: java.lang.Class[T], coll: java.util.Collection[T]): Array[_ <: T] = {
    MathsBase.toArray[T](t, coll);
  }

  def toArray[T](t: TypeName[T], coll: java.util.Collection[_ >: T]): Array[T] = {
    //   MathsBase.toArray(t, coll)
   MathsBase.toArray(t, null).asInstanceOf[Array[T]]
  }

  def rerr(a: Double, b: Double): Double = {
   MathsBase.rerr(a, b)
  }

  def rerr(a: Complex, b: Complex): Double = {
   MathsBase.rerr(a, b)
  }

  def define(name: String, f: CustomCCFunctionX): CustomCCFunctionXExpr = {
   MathsBase.define(name, f)
  }

  def define(name: String, f: CustomDCFunctionX): CustomDCFunctionXExpr = {
   MathsBase.define(name, f)
  }

  def define(name: String, f: CustomDDFunctionX): CustomDDFunctionXExpr = {
   MathsBase.define(name, f)
  }

  def define(name: String, f: CustomDDFunctionXY): CustomDDFunctionXYExpr = {
   MathsBase.define(name, f)
  }

  def define(name: String, f: CustomDCFunctionXY): CustomDCFunctionXYExpr = {
   MathsBase.define(name, f)
  }

  def define(name: String, f: CustomCCFunctionXY): CustomCCFunctionXYExpr = {
   MathsBase.define(name, f)
  }

  def rerr(a: ComplexMatrix, b: ComplexMatrix): Double = {
   MathsBase.rerr(a, b)
  }

  def toDoubleArray[T <: Expr](c: TVector[T]): DoubleVector = {
   MathsBase.toDoubleArray(c)
  }

  def toDouble(c: Complex, d: PlotDoubleConverter): Double = {
   MathsBase.toDouble(c, d)
  }

  def conj(e: Expr): Expr = {
   MathsBase.conj(e)
  }

  def complex(t: TMatrix[_]): Complex = {
   MathsBase.complex(t)
  }

  def matrix(t: TMatrix[_]): ComplexMatrix = {
   MathsBase.matrix(t)
  }

  def ematrix(t: TMatrix[_]): TMatrix[Expr] = {
   MathsBase.ematrix(t)
  }

  def getVectorSpace[T](cls: TypeName[T]): VectorSpace[T] = {
   MathsBase.getVectorSpace(cls)
  }

  def refineSamples(values: TVector[java.lang.Double], n: Int): DoubleVector = {
   MathsBase.refineSamples(values, n)
  }

  def refineSamples(values: Array[Double], n: Int): Array[Double] = {
   MathsBase.refineSamples(values, n)
  }

  def getHadrumathsVersion: String = {
   MathsBase.getHadrumathsVersion
  }

  def expandComponentDimension(d1: ComponentDimension, d2: ComponentDimension): ComponentDimension = {
   MathsBase.expandComponentDimension(d1, d2)
  }

  def expandComponentDimension(e: Expr, d: ComponentDimension): Expr = {
   MathsBase.expandComponentDimension(e, d)
  }

  def atan2(y: Double, x: Double): Double = {
   MathsBase.atan2(y, x)
  }

  def ceil(a: Double): Double = {
   MathsBase.ceil(a)
  }

  def floor(a: Double): Double = {
   MathsBase.floor(a)
  }

  def round(a: Double): Long = {
   MathsBase.round(a)
  }

  def round(a: Float): Int = {
   MathsBase.round(a)
  }

  def random: Double = {
   MathsBase.random
  }

  def rightArrow[A, B](a: A, b: B): RightArrowUplet2[A, B] = {
   MathsBase.rightArrow(a, b)
  }

  def rightArrow(a: Double, b: Double): RightArrowUplet2.Double = {
   MathsBase.rightArrow(a, b)
  }

  def rightArrow(a: Complex, b: Complex): RightArrowUplet2.Complex = {
   MathsBase.rightArrow(a, b)
  }

  def rightArrow(a: Expr, b: Expr): RightArrowUplet2.Expr = {
   MathsBase.rightArrow(a, b)
  }

  def parseExpression(expression: String): Expr = {
   MathsBase.parseExpression(expression)
  }

  def createExpressionEvaluator: ExpressionManager = {
   MathsBase.createExpressionEvaluator
  }

  def createExpressionParser: ExpressionManager = {
   MathsBase.createExpressionParser
  }

  def evalExpression(expression: String): Expr = {
   MathsBase.evalExpression(expression)
  }

  def toRadians(a: Double): Double = {
   MathsBase.toRadians(a)
  }

  def toRadians(a: Array[Double]): Array[Double] = {
   MathsBase.toRadians(a)
  }

  def det(m: ComplexMatrix): Complex = {
   MathsBase.det(m)
  }

  def toInt(o: Any): Int = {
   MathsBase.toInt(o)
  }

  def toInt(o: Any, defaultValue: Integer): Int = {
   MathsBase.toInt(o, defaultValue)
  }

  def toLong(o: Any): Long = {
   MathsBase.toLong(o)
  }

  def toLong(o: Any, defaultValue: Long): Long = {
   MathsBase.toLong(o, defaultValue)
  }

  def toDouble(o: Any): Double = {
   MathsBase.toDouble(o)
  }

  def toDouble(o: Any, defaultValue: Double): Double = {
   MathsBase.toDouble(o, defaultValue)
  }

  def toFloat(o: Any): Float = {
   MathsBase.toFloat(o)
  }

  def toFloat(o: Any, defaultValue: Float): Float = {
   MathsBase.toFloat(o, defaultValue)
  }

  def DC(e: Expr): DoubleToComplex = {
   MathsBase.DC(e)
  }

  def DD(e: Expr): DoubleToDouble = {
   MathsBase.DD(e)
  }

  def DV(e: Expr): DoubleToVector = {
   MathsBase.DV(e)
  }

  def DM(e: Expr): DoubleToMatrix = {
   MathsBase.DM(e)
  }

  def matrix(e: Expr): ComplexMatrix = {
   MathsBase.matrix(e)
  }

  ///////////////////////////////////////////

  def absoluteSamples(x: Array[Double], y: Array[Double], z: Array[Double]) = Samples.absolute(x,y,z)

  def absoluteSamples(x: Array[Double], y: Array[Double]) = Samples.absolute(x,y)

  def absoluteSamples(x: Array[Double]) = Samples.absolute(x)

  def relativeSamples(x: Array[Double], y: Array[Double], z: Array[Double]) = Samples.relative(x,y,z)

  def relativeSamples(x: Array[Double], y: Array[Double]) = Samples.relative(x,y)

  def relativeSamples(x: Array[Double]) = Samples.relative(x)

  def relativeSamples(x: Int, y: Int, z: Int) = Samples.relative(x,y,z)

  def relativeSamples(x: Int, y: Int) = Samples.relative(x,y)

  def relativeSamples(x: Int) = Samples.relative(x)

  def adaptiveSamples() = Samples.adaptive()

  def adaptiveSamples(min: Int, max: Int)= Samples.adaptive(min,max)

}

