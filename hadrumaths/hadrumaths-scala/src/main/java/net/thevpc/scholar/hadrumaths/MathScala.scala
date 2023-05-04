/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.thevpc.scholar.hadrumaths

import java.io.{File, UncheckedIOException}
import java.util.concurrent.Callable
import java.util.function
import java.util.function.{DoublePredicate, Function, IntPredicate, Supplier, ToDoubleFunction}

import net.thevpc.jeep.JContext
import net.thevpc.common.mon.{MonitoredAction, ProgressMonitor}
import net.thevpc.common.util._
import net.thevpc.common.time._
import net.thevpc.scholar.hadrumaths.MathScala.SComplexVector
import net.thevpc.scholar.hadrumaths.cache.PersistenceCacheBuilder
import net.thevpc.scholar.hadrumaths.geom.Point
import net.thevpc.scholar.hadrumaths.scalarproducts.ScalarProductOperator
import net.thevpc.scholar.hadrumaths.symbolic._
import net.thevpc.scholar.hadrumaths.symbolic.double2complex.CDiscrete
import net.thevpc.scholar.hadrumaths.symbolic.double2double.{AxisFunction, DoubleParam}
import net.thevpc.scholar.hadrumaths.symbolic.double2vector.VDiscrete
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.cond.IfExpr
import net.thevpc.scholar.hadrumaths.symbolic.polymorph.{Any, ParametrizedScalarProduct}
import net.thevpc.scholar.hadrumaths.util.adapters.ComplexMatrixFromComplexMatrix
import net.thevpc.scholar.hadruplot.console.params._
import net.thevpc.scholar.hadruplot.{PlotConfig, PlotDoubleConverter}
//import java.util

import net.thevpc.scholar
import net.thevpc.scholar.hadrumaths.geom.Geometry

import scala.collection.{Iterable, Iterator}

object MathScala {

  private val SIMPLIFY_OPTION_PRESERVE_ROOT_NAME = new SimplifyOptions().setPreserveRootName(true)
  //  def arr(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray()
  //
  //  def arr(x: Array[Double]): ToDoubleArrayAware = new ToDoubleArrayAwareConstant(x)

  def Plot(): net.thevpc.scholar.hadruplot.PlotBuilder = net.thevpc.scholar.hadruplot.Plot.builder()

  def samples(x: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray())

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray(), y.toDoubleArray)

  def samples(x: ToDoubleArrayAware, y: ToDoubleArrayAware, z: ToDoubleArrayAware): Samples = Samples.absolute(x.toDoubleArray, y.toDoubleArray, z.toDoubleArray)

  implicit def convertImplicitDoubleToExpr(x: Double): DoubleExpr = DoubleExpr.of(x)

  implicit def convertImplicitIntToExpr(x: Int): DoubleExpr = DoubleExpr.of(x)

  //  implicit def convertImplicitIntToComplex(x: Int): Complex = Maths.complex(x.asInstanceOf[java.lang.Double])

  implicit def convertImplicitToMatrixOfComplex(x: Matrix[Complex]): ComplexMatrix = new ComplexMatrixFromComplexMatrix(x)

  implicit def convertImplicitToDoubleArrayAware(x: ToDoubleArrayAware): Array[Double] = x.toDoubleArray

  implicit def convertImplicitDoubleColonTuple2ToArray(x: DoubleColonTuple2): Array[Double] = Maths.dsteps(x.left, x.right)

  implicit def convertImplicitDoubleColonTuple3ToArray(x: DoubleColonTuple3): Array[Double] = Maths.dsteps(x._1, x._3, x._2)

  implicit def convertImplicitArrayExprToExprList(x: Array[Expr]): Vector[Expr] = Maths.evector(x: _*)

  implicit def convertImplicitArrayComplexToExprList(x: Array[Complex]): Vector[Complex] = Maths.cvector(x: _*)

  implicit def convertImplicitArrayComplexToVector(x: Array[Complex]): ComplexVector = Maths.columnVector(x: _*)

  implicit def convertImplicitii2dd(x: (Int, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitid2dd(x: (Int, Double)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitdi2dd(x: (Double, Int)): (Double, Double) = (x._1.asInstanceOf[Double], x._2.asInstanceOf[Double])

  implicit def convertImplicitii2id2(x: Iterable[(Int, Int)]): Iterable[(Double, Double)] = new Iterable[(Double, Double)] {
    def iterator = convertImplicitii2id2(x.iterator);
  }

  implicit def convertImplicitDoubleArrayToDList(x: Array[Double]): DoubleVector = ArrayDoubleVector.column(x)

  implicit def convertImplicitii2id2(x: Iterator[(Int, Int)]): Iterator[(Double, Double)] = new Iterator[(Double, Double)] {
    def next() = {
      val v: (Int, Int) = x.next()
      (v._1.asInstanceOf[Double], v._2.asInstanceOf[Double])
    }

    def hasNext = x.hasNext;
  }

  implicit def convertImpliciti2d(x: Int): Double = {
    x
  }

  implicit def convertComplexVector(x: Vector[Complex]): SComplexVector = {
    return SComplexVector(DefaultComplexVector.of(x));
  }

  implicit def convertComplexMatrix(x: Matrix[Complex]): SComplexMatrix = {
    return SComplexMatrix(DefaultComplexMatrix.of(x));
  }

  implicit def convertImpliciti2d2(x: scala.Tuple2[Int, Int]): scala.Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def convertImpliciti2d4(x: scala.Tuple2[Double, Int]): scala.Tuple2[Double, Double] = {
    x._1.asInstanceOf[Double] -> x._2.asInstanceOf[Double]
  }

  implicit def convertImplicitTupleToElementOp(x: (Int, Expr) => Expr): VectorOp[Expr] = {
    new VectorOp[Expr] {
      override def eval(index: Int, e: Expr): Expr = x(index, e)
    }
  }

  implicit def convertImplicitii1id1(x: Iterable[Int]): Iterable[Double] = new Iterable[Double] {
    def iterator = converImplicitIi1id1(x.iterator);
  }

  implicit def converImplicitIi1id1(x: Iterator[Int]): Iterator[Double] = new Iterator[Double] {
    def next() = {
      val v: Int = x.next()
      v.asInstanceOf[Double]
    }

    def hasNext = x.hasNext;
  }

  implicit def convertImplicitC10(x: Array[Expr]): Vector[Expr] = {
    Maths.evector(x: _*)
  }

  implicit def convertImplicitC11(x: Array[DoubleToVector]): Vector[Expr] = {
    Maths.evector(x: _*)
  }

  implicit def convertImplicitRightArrowUplet2Double(x: scala.Tuple2[Double, Double]): net.thevpc.scholar.hadrumaths.RightArrowUplet2.Double = {
    new net.thevpc.scholar.hadrumaths.RightArrowUplet2.Double(x._1, x._2)
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

  implicit class SComplexVector(value: ComplexVector) /*extends STVectorExpr(expr(value))*/ {
    def **(v: scala.Tuple2[ComplexVector, ComplexVector]): Complex = {
      value.scalarProductAll(Array((v._1), (v._2)): _*)
    }

    def ***(v: scala.Tuple2[ComplexVector, ComplexVector]): Complex = {
      value.scalarProductAll(Array((v._1), (v._2)): _*)
    }

    def **(v: ComplexMatrix): Complex = value.scalarProduct(v.toVector());

    //    def ***(v: Matrix[Complex]): Complex = value.scalarProduct(true, v.toVector());


    def **(v: ComplexVector): Complex = value.scalarProduct(v)

    //    def ***(v: Vector): Complex = value.scalarProduct(true, v)

    def **(v: ExprVector): Expr = {
      v.scalarProduct(value.asInstanceOf[Vector[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[TList[Expr]])
    //    }

    def :**(v: java.util.List[ComplexVector]): ComplexVector = value.vscalarProduct(v.toArray(Array.ofDim[ComplexVector](v.size())): _ *)

    //    def :***(v: java.util.List[Vector]): Vector = Maths.vector(value.vscalarProduct(true, v.toArray(Array.ofDim[Vector](v.size())): _ *))

    def :**(v: Array[Vector[Complex]]): ComplexVector = (value.vscalarProduct(v: _ *))

    def :**(v: ComplexMatrix): ComplexVector = (value.vscalarProduct(v.toVector))

    def :**(v: ComplexVector): ComplexVector = (value.vscalarProduct(v))

    //    def :***(v: Array[TList[Complex]]): Vector = Maths.vector(value.vscalarProduct(true, v: _ *))

    def +(v: Vector[Complex]): ComplexVector = (value.add(v))

    def -(v: Vector[Complex]): ComplexVector = (value.sub(v))

    def +(v: Complex): ComplexVector = (value.add(v))

    def -(v: Complex): ComplexVector = (value.sub(v))

    def *(v: Complex): ComplexVector = (value.mul(v))

    def /(v: Complex): ComplexVector = (value.div(v))

    def %(v: Complex): ComplexVector = value.rem(v)

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
      Maths.matrix(value.toMatrix.mul(v.toMatrix))
    }

    def *(v: ComplexMatrix): ComplexMatrix = {
      Maths.matrix(value.toMatrix.mul(v))
    }

    def :*(v: ComplexVector): ComplexVector = (value.dotmul(v))

    def :/(v: ComplexVector): ComplexVector = (value.dotdiv(v))

    def :^(v: ComplexVector): ComplexVector = (value.dotpow(v))

    //    def :^(v: Double): Vector = Maths.vector(value.dotpow(complex(v)))
    //
    def :^(v: Complex): ComplexVector = (value.dotpow(v))
  }

  implicit class STMatrix[T](val value: Matrix[T]) {

  }

  implicit class SComplexMatrix(val value: ComplexMatrix) {

    def :**[T](v: Vector[Vector[T]]): Vector[T] = {
      Maths.vscalarProduct(value.toVector.asInstanceOf[Vector[T]], v);
    }

    //    def :***[T](v: TList[TList[T]]): TList[T] = {
    //      Maths.vhscalarProduct(value.toVector.asInstanceOf[TList[T]], v);
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

    def *(v: Vector[Expr]): Matrix[Expr] = ematrix(value.mul(matrix(v.toMatrix)))

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

    def **(v: Vector[Expr]): Expr = {
      v.scalarProduct(value.asInstanceOf[Matrix[Expr]])
    }

    //    def ***(v: TList[Expr]): Expr = {
    //      v.scalarProduct(true, value.asInstanceOf[Matrix[Expr]])
    //    }

    def **(v: scala.Tuple2[ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2): _*)
    }

    //    def ***(v: Tuple2[Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2): _*)
    //    }

    def **(v: scala.Tuple3[ComplexVector, ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3): _*)
    }

    //    def ***(v: scala.Tuple3[Vector, Vector, Vector]): Complex = {
    //      value.toVector.scalarProductAll(true, Array(v._1, v._2, v._3): _*)
    //    }

    def **(v: scala.Tuple4[ComplexVector, ComplexVector, ComplexVector, ComplexVector]): Complex = {
      value.toVector.scalarProductAll(Array(v._1, v._2, v._3, v._4): _*)
    }

    //    def ***(v: scala.Tuple4[Vector, Vector, Vector, Vector]): Complex = {
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
    def *(v: Double): Expr = value.mul(v);

    def *(v: Int): Expr = value.mul(v);

    def *(v: Expr): Expr = value.mul(v);

    def *(v: Geometry): Expr = value.mul(Maths.expr(v));
  }


  implicit class SExpr(val value: Expr) {
    def +(v: Expr): Expr = value.plus(v)

    def !!(): Expr = {
      simplify(value, SIMPLIFY_OPTION_PRESERVE_ROOT_NAME).asInstanceOf[Expr]
    }

    def unary_- : Expr = value.neg()

    def :+(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def ++(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = value.sub(Any.unwrap(v))

    def *(v: Geometry): Expr = value.mul(Maths.expr(v))

    def *(v: Expr): Expr = value.mul(Any.unwrap(v))

    def *(v: Domain): Expr = value.mul(Maths.expr(1, v))

    //    def **(v: Expr): Complex = scholar.hadrumaths.Maths.scalarProduct(value, Any.unwrap(v));

    def **(v: Vector[Expr]): ComplexVector = Maths.scalarProduct(value, v);

    //    def ***(v: TList[Expr]): Vector = scholar.hadrumaths.Maths.scalarProduct(true, value, v);

    def **(v: Expr): Expr = ParametrizedScalarProduct.of(value, Any.unwrap(v));

    //    def ***(v: Expr): Expr = new ParametrizedScalarProduct(value, Any.unwrap(v), true);

    def ^^(v: Expr): Expr = Maths.pow(value, Any.unwrap(v));

    def <(v: Expr): Expr = Maths.lt(value, Any.unwrap(v));

    def <=(v: Expr): Expr = Maths.lte(value, Any.unwrap(v));

    def >(v: Expr): Expr = Maths.gt(value, Any.unwrap(v));

    def >=(v: Expr): Expr = Maths.gte(value, Any.unwrap(v));

    def ~~(v: Expr): Expr = Maths.eq(value, Any.unwrap(v));

    def ===(v: Expr): Expr = Maths.eq(value, Any.unwrap(v));

    def :=(v: Expr): Expr = Maths.eq(value, Any.unwrap(v));

    def <>(v: Expr): Expr = Maths.ne(value, Any.unwrap(v));

    def !==(v: Expr): Expr = Maths.ne(value, Any.unwrap(v));

    def &&(v: Expr): Expr = Maths.and(value, Any.unwrap(v));

    def ||(v: Expr): Expr = Maths.or(value, Any.unwrap(v));

    def /(v: Expr): Expr = value.div(Any.unwrap(v));

    def %(v: Expr): Expr = Maths.rem(value, Any.unwrap(v));

    def apply(f: scala.Tuple2[DoubleParam, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  implicit class SDoubleToComplex(val value: DoubleToComplex) {
    def apply(x: java.lang.Double): Complex = value.evalComplex(x)

    def apply(x: Array[Double]): Array[Complex] = value.evalComplex(x)

    def apply(x: java.lang.Double, y: java.lang.Double): Complex = value.evalComplex(x, y)

    def apply(x: Array[Double], y: Array[Double]): Array[Array[Complex]] = value.evalComplex(x, y)
  }

  implicit class SDoubleToDouble(val value: DoubleToDouble) {
    def apply(x: Double): Double = value.evalDouble(x)

    def apply(x: Array[Double]): Array[Double] = value.evalDouble(x)

    def apply(x: Double, y: Double): Double = value.evalDouble(x, y)

    def apply(x: Array[Double], y: Array[Double]): Array[Array[Double]] = value.evalDouble(x, y)
  }

  implicit class SComplex(val value: Complex) {

    def +(v: Complex): Complex = value.plus(v)

    def -(v: Complex): Complex = value.minus(v)

    def *(v: Complex): Complex = value.mul(v)

    def /(v: Complex): Complex = value.div(v)

    def %(v: Complex): Complex = value.rem(v)

    def +(v: DoubleExpr): Complex = value.plus(v)

    def -(v: DoubleExpr): Complex = value.minus(v)

    def *(v: DoubleExpr): Complex = value.mul(v)

    def /(v: DoubleExpr): Complex = value.div(v)

    def %(v: DoubleExpr): Complex = value.rem(v)

    def +(v: Double): Complex = value.plus(v)

    def -(v: Double): Complex = value.sub(v)

    def *(v: Double): Complex = value.mul(v)

    def /(v: Double): Complex = value.div(v)

    def %(v: Double): Complex = value.rem(v)

    def ^^(v: Expr): Expr = Maths.pow(value, v);

    def ^^(v: Complex): Complex = value.pow(v);

    def unary_- : Complex = value.neg()

    def +(v: Expr): Expr = value.plus(v)

    def :+(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def ++(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = value.sub(Any.unwrap(v))

    def *(v: Geometry): Expr = value.mul(Maths.expr(v))

    def *(v: Expr): Expr = value.mul(Any.unwrap(v))

    def *(v: Domain): Expr = value.mul(Maths.expr(1, v))

    def **(v: Expr): Complex = Maths.scalarProduct(null, value, Any.unwrap(v)).toComplex;

    def ***(v: Expr): Complex = Maths.scalarProduct(null, value, Any.unwrap(v)).toComplex;

    def /(v: Expr): Expr = value.div(Any.unwrap(v))

    def %(v: Expr): Expr = Maths.rem(value, Any.unwrap(v))

    def apply(f: scala.Tuple2[DoubleParam, Expr]): Expr = {
      value.setParam(f._1, Any.unwrap(f._2));
    }
  }

  implicit class SDoubleExpr(val value: DoubleExpr) {

    def +(v: Complex): Complex = value.add(v)

    def -(v: Complex): Complex = value.sub(v)

    def *(v: Complex): Complex = value.mul(v)

    def /(v: Complex): Complex = value.div(v)

    def %(v: Complex): Complex = value.rem(v)

    def +(v: DoubleExpr): DoubleExpr = value.add(v)

    def -(v: DoubleExpr): DoubleExpr = value.sub(v)

    def *(v: DoubleExpr): DoubleExpr = value.mul(v)

    def /(v: DoubleExpr): DoubleExpr = value.div(v)

    def %(v: DoubleExpr): DoubleExpr = value.rem(v)

    def +(v: Double): DoubleExpr = value.add(v)

    def -(v: Double): DoubleExpr = value.sub(v)

    def *(v: Double): DoubleExpr = value.mul(v)

    def /(v: Double): DoubleExpr = value.div(v)

    def %(v: Double): DoubleExpr = value.rem(v)

    def ^^(v: Expr): Expr = value.pow(v);

    def ^^(v: Complex): Complex = value.pow(v);

    def unary_- : DoubleExpr = value.neg()

    def +(v: Expr): Expr = value.plus(v)

    def :+(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def ++(v: Vector[Expr]): Vector[Expr] = {
      var e = Maths.evector();
      e.append(value)
      e.appendAll(v)
      e;
    }

    def -(v: Expr): Expr = value.sub(Any.unwrap(v))

    def *(v: Geometry): Expr = value.mul(Maths.expr(v))

    def *(v: Expr): Expr = value.mul(Any.unwrap(v))

    def *(v: Domain): Expr = value.mul(v)

    def **(v: Expr): Complex = Maths.scalarProduct(null, value, Any.unwrap(v)).toComplex;

    def ***(v: Expr): Complex = Maths.scalarProduct(null, value, Any.unwrap(v)).toComplex;

    def /(v: Expr): Expr = value.div(Any.unwrap(v))

    def %(v: Expr): Expr = Maths.rem(value, Any.unwrap(v))


    def apply(f: scala.Tuple2[DoubleParam, Expr]): Expr = {
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

  implicit class SExpressionListArray(val value: Array[Vector[Expr]]) {
    def !!(): Array[Vector[Expr]] = simplify()

    def simplify(): Array[Vector[Expr]] = {
      var c = new Array[Vector[Expr]](value.length);
      var i = 0;
      while (i < value.length) {
        c(i) = Maths.simplify(value(i))
        i = i + 1;
      }
      c;
    }
  }

  implicit class SExpressionListArray2(val value: Array[Array[Vector[Expr]]]) {
    def !!(): Array[Array[Vector[Expr]]] = simplify()

    def simplify(): Array[Array[Vector[Expr]]] = {
      var c = new Array[Array[Vector[Expr]]](value.length);
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
        return Maths.zerosMatrix(1);
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
        return Maths.zerosMatrix(1);
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


  implicit class STList[T](value: Vector[T]) extends scala.Iterable[T] {

    override def iterator: Iterator[T] = new Iterator[T] {
      private val jiter = value.iterator()

      override def hasNext: Boolean = jiter.hasNext

      override def next(): T = jiter.next();
    }

    def :+(v: T): Vector[T] = {
      value.append(v)
      value
    }

    def :+(v: Vector[T]): Vector[T] = value.concat(v)

    def ++(v: Vector[T]): Vector[T] = value.concat(v)
  }

  implicit class STVectorExpr(value: Vector[Expr]) extends STList[Expr](value) {
    def **[P1 <: Expr, P2 <: Expr](v: scala.Tuple2[Vector[P1], Vector[P2]]): Expr = {
      value.to($EXPR).scalarProductAll(expr(v._1), expr(v._2))
    }

    //    def ***[P1 <: Expr, P2 <: Expr](v: scala.Tuple2[TList[P1], TList[P2]]): Expr = {
    //      value.scalarProductAll(true, expr(v._1), expr(v._2))
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: scala.Tuple3[Vector[P1], Vector[P2], Vector[P3]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: Tuple3[TList[P1], TList[P2], TList[P3]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3)): _*)
    //    }

    def **[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: scala.Tuple4[Vector[P1], Vector[P2], Vector[P3], Vector[P4]]): Expr = {
      value.scalarProductAll(Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    }

    //    def ***[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: Tuple4[TList[P1], TList[P2], TList[P3], TList[P4]]): Expr = {
    //      value.scalarProductAll(true, Array(expr(v._1), expr(v._2), expr(v._3), expr(v._4)): _*)
    //    }

    def :*[P <: Expr](v: Vector[P]): Vector[Expr] = Maths.edotmul(value, v.asInstanceOf[Vector[Expr]]);

    def :*[P1 <: Expr, P2 <: Expr](v: scala.Tuple2[Vector[P1], Vector[P2]]): Vector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[Vector[Expr]], v._2.asInstanceOf[Vector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr](v: scala.Tuple3[Vector[P1], Vector[P2], Vector[P3]]): Vector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[Vector[Expr]], v._2.asInstanceOf[Vector[Expr]], v._3.asInstanceOf[Vector[Expr]])
    }

    def :*[P1 <: Expr, P2 <: Expr, P3 <: Expr, P4 <: Expr](v: scala.Tuple4[Vector[P1], Vector[P2], Vector[P3], Vector[P4]]): Vector[Expr] = {
      Maths.edotmul(value, v._1.asInstanceOf[Vector[Expr]], v._2.asInstanceOf[Vector[Expr]], v._3.asInstanceOf[Vector[Expr]], v._4.asInstanceOf[Vector[Expr]])
    }

    def **[P1 <: Expr](v: Expr): Vector[Expr] = value.to($EXPR).scalarProduct(v);

    //    def ***[P1 <: Expr](v: Expr): TList[Expr] = value.to($EXPR).scalarProduct(true, v);

    def **[P <: Expr](v: Vector[P]): Expr = value.to($EXPR).scalarProduct(v.to($EXPR));

    //    def ***[P <: Expr](v: TList[P]): Expr = value.to($EXPR).scalarProduct(true, v.to($EXPR));

    def **[P <: Expr](v: Matrix[P]): Expr = value.to($EXPR).scalarProduct((v.toVector().to($EXPR)));

    //    def ***[P <: Expr](v: Matrix[P]): Expr = value.to($EXPR).scalarProduct(true, expr(v.toVector()));

    def :**[P1 <: Expr](v: Vector[P1]): Matrix[Expr] = Maths.scalarProductMatrix(value, v.asInstanceOf[Vector[Expr]]).asInstanceOf[Matrix[Expr]];

    def :***[P1 <: Expr](v: Vector[P1]): Matrix[Expr] = Maths.scalarProductMatrix(value, v.asInstanceOf[Vector[Expr]]).asInstanceOf[Matrix[Expr]];

    def :**(v: ComplexVector): ComplexMatrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      Maths.scalarProductMatrix(value, columnVector(exprs: _*))
    };

    def :***(v: ComplexVector): ComplexMatrix = {
      val exprs: Array[Expr] = v.toArray().asInstanceOf[Array[Expr]]
      Maths.scalarProductMatrix(value, columnVector(exprs: _*))
    };

    def :**(v: ComplexMatrix): ComplexMatrix = Maths.scalarProductMatrix(value, v.toVector.asInstanceOf[Vector[Expr]]);

    def :***(v: ComplexMatrix): ComplexMatrix = Maths.scalarProductMatrix(value, v.toVector.asInstanceOf[Vector[Expr]]);
  }

  implicit class SExprList(value: ExprVector) extends STVectorExpr(value) {

    override def :+(v: Expr): ExprVector = {
      value.append(v)
      value
    }

    override def :+(v: Vector[Expr]): ExprVector = value.concat(v)


    def +(v: Expr): ExprVector = value.add(v)

    def -(v: Expr): ExprVector = value.sub(v)

    def *(v: Expr): ExprVector = value.mul(v)

    def /(v: Expr): ExprVector = value.div(v)

    def %(v: Expr): ExprVector = value.rem(v)

    def !!(): ExprVector = value.simplify(SIMPLIFY_OPTION_PRESERVE_ROOT_NAME)


    def unary_- : ExprVector = value.eval((i: Int, x: Expr) => (-x))


    def map(f: (Int, Expr) => Expr): ExprVector = {
      val max = value.size();
      val list = Maths.evector(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, value(n)));
        n += 1
      }
      list;
    }

    def cross(other: Vector[Expr], f: IndexedExpr2 => Expr): ExprVector = {
      val nmax = value.size();
      val mmax = other.size();
      val list = Maths.evector(nmax * mmax);
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
    //      value.eval(new VectorOp {
    //        override def process(i: Int, e: Expr): Expr = f(i, e);
    //      })
    //    }

    def ::+(v: Vector[Expr]): ExprVector = {
      value.copy().appendAll(v)
    }

    def ::+(v: Expr): ExprVector = value.append(v)

    override def ++(v: Vector[Expr]): ExprVector = value.concat(v)

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

  implicit class SDoubleList(val value: Vector[java.lang.Double]) {


    def +(v: java.lang.Double): Vector[java.lang.Double] = scholar.hadrumaths.Maths.add[java.lang.Double](value, v)

    def -(v: java.lang.Double): Vector[java.lang.Double] = scholar.hadrumaths.Maths.minus[java.lang.Double](value, v)

    def *(v: java.lang.Double): Vector[java.lang.Double] = Maths.mul[java.lang.Double](value, v)

    def /(v: java.lang.Double): Vector[java.lang.Double] = scholar.hadrumaths.Maths.div[java.lang.Double](value, v)

    def %(v: java.lang.Double): Vector[java.lang.Double] = scholar.hadrumaths.Maths.rem[java.lang.Double](value, v)

    def !!(): Vector[java.lang.Double] = value;


    def unary_- : Vector[java.lang.Double] = {
      value.eval(
        (i: Int, x: java.lang.Double) => (-x)
      )

    }

    def ++(v: java.lang.Double): Vector[java.lang.Double] = {
      value.append(v)
      value
    }

    def ++(v: Vector[java.lang.Double]): Vector[java.lang.Double] = {
      value.appendAll(v)
      value
    }

    def :+(v: java.lang.Double): Vector[java.lang.Double] = {
      value.append(v)
      value
    }

    def :+(v: Vector[java.lang.Double]): Vector[java.lang.Double] = {
      value.appendAll(v)
      value
    }

    def baa(v: java.lang.Double): Vector[java.lang.Double] = {
      value.append(v)
      value
    }


    def apply(row: Int): java.lang.Double = {
      value.get(row);
    }

    def map(f: (Int, Expr) => Expr): Vector[Expr] = {
      val max = value.size();
      val list = Maths.evector(max);
      var n = 0;
      while (n < max) {
        list.append(f(n, expr(value(n))));
        n += 1
      }
      list;
    }

    def cross(other: Vector[Expr], f: IndexedExpr2 => Expr): Vector[Expr] = {
      val nmax = value.size();
      val mmax = other.size();
      val list = Maths.evector(nmax * mmax);
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

    def ^^(v: Complex): Complex = toNumber.pow(v);

    def +(v: Expr): Expr = toNumber.plus(v.value)

    def -(v: Expr): Expr = toNumber.sub(v.value)

    def *(v: Expr): Expr = toNumber.mul(v.value)

    def /(v: Expr): Expr = toNumber.div(v.value)

    def %(v: Expr): Expr = toNumber.rem(v.value)

    def +(v: DoubleExpr): DoubleExpr = toNumber.add(v.value)

    def -(v: DoubleExpr): DoubleExpr = toNumber.sub(v.value)

    def *(v: DoubleExpr): DoubleExpr = toNumber.mul(v.value)

    def /(v: DoubleExpr): DoubleExpr = toNumber.div(v.value)

    def %(v: DoubleExpr): DoubleExpr = toNumber.rem(v.value)

    def +(v: Complex): Complex = toNumber.add(v.value)

    def -(v: Complex): Complex = toNumber.sub(v.value)

    def *(v: Complex): Complex = toNumber.mul(v.value)

    def /(v: Complex): Complex = toNumber.div(v.value)

    def %(v: Complex): Complex = toNumber.rem(v.value)

    def <>(v: Expr): Expr = toNumber.ne(Any.unwrap(v))

    def ===(v: Expr): Expr = toNumber.eq(Any.unwrap(v))

    def !==(v: Expr): Expr = toNumber.ne(Any.unwrap(v))

    def ||(v: Expr): Expr = toNumber.or(Any.unwrap(v))

    def &&(v: Expr): Expr = toNumber.and(Any.unwrap(v))

    def <(v: Expr): Expr = toNumber.lt(Any.unwrap(v))

    def <=(v: Expr): Expr = toNumber.lte(Any.unwrap(v))

    def >(v: Expr): Expr = toNumber.gt(Any.unwrap(v))

    def >=(v: Expr): Expr = toNumber.gte(Any.unwrap(v))

    def <>(v: Double): Expr = toNumber.ne(DoubleExpr.of(v))

    def ===(v: Double): Expr = toNumber.eq(DoubleExpr.of(v))

    def !==(v: Double): Expr = toNumber.ne(DoubleExpr.of(v))

    def ||(v: Double): Expr = toNumber.or(DoubleExpr.of(v))

    def &&(v: Double): Expr = toNumber.and(DoubleExpr.of(v))

    def <(v: Double): Expr = toNumber.lt(DoubleExpr.of(v))

    def <=(v: Double): Expr = toNumber.lte(DoubleExpr.of(v))

    def >(v: Double): Expr = toNumber.gt(DoubleExpr.of(v))

    def >=(v: Double): Expr = toNumber.gte(DoubleExpr.of(v))

    def toComplex: Complex = Complex(value)

    def toNumber: DoubleExpr = DoubleExpr.of(value)
  }

  implicit class SInt(val value: Int) {
    def +(v: Expr): Expr = Maths.expr(value).plus(v.value)

    def -(v: Expr): Expr = Maths.expr(value).sub(v.value)

    def *(v: Expr): Expr = Maths.expr(value).mul(v.value)

    def /(v: Expr): Expr = Maths.expr(value).div(v.value)

    def %(v: Expr): Expr = Maths.expr(value).rem(v.value)

    def +(v: DoubleExpr): DoubleExpr = toNumber.add(v.value)

    def -(v: DoubleExpr): DoubleExpr = toNumber.sub(v.value)

    def *(v: DoubleExpr): DoubleExpr = toNumber.mul(v.value)

    def /(v: DoubleExpr): DoubleExpr = toNumber.div(v.value)

    def %(v: DoubleExpr): DoubleExpr = toNumber.rem(v.value)

    def +(v: Complex): Complex = toNumber.add(v.value)

    def -(v: Complex): Complex = toNumber.sub(v.value)

    def *(v: Complex): Complex = toNumber.mul(v.value)

    def /(v: Complex): Complex = toNumber.div(v.value)

    def %(v: Complex): Complex = toNumber.rem(v.value)

    def toComplex: Complex = Complex(value)

    def toDouble: Double = value

    def toNumber: DoubleExpr = DoubleExpr.of(value)
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
    def !!(): Matrix[Expr] = {
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
  val π: Double = Math.PI
  val E: Double = Math.E
  val DDZERO: DoubleToDouble = Maths.ZERO
  val DDNAN: DoubleToDouble = Maths.DDNAN
  val DCZERO: DoubleToComplex = Maths.DCZERO
  val DVZERO3: DoubleToVector = Maths.DVZERO3
  val EZERO: Expr = Maths.EZERO
  val EONE: Expr = Maths.EONE
  val X: Expr = Maths.X
  val Y: Expr = Maths.Y
  val Z: Expr = Maths.Z
  val HALF_PI: Double = Maths.HALF_PI
  val I: Complex = Maths.I
  val CNaN: Complex = Maths.CNaN
  val CONE: Complex = Maths.CONE
  val CZERO: Complex = Maths.CZERO //    public static  boolean DEBUG = true;

  val DVZERO1: DoubleToVector = Maths.DVZERO1
  val DVZERO2: DoubleToVector = Maths.DVZERO2
  val î: Complex = Maths.î
  val ê: Expr = Maths.ê
  val ĉ: Complex = Maths.ĉ
  val METER: Double = Maths.METER
  val HZ: Double = Maths.HZ
  val BYTE: Long = Maths.BYTE
  val MILLISECOND: Long = Maths.MILLISECOND
  /**
   * kibibyte
   */
  val KiBYTE: Int = Maths.KiBYTE
  /**
   * mibibyte
   */
  val MiBYTE: Int = Maths.MiBYTE
  /**
   * TEBI Byte
   */
  val GiBYTE: Long = Maths.GiBYTE
  val TiBYTE: Long = Maths.TiBYTE
  /**
   * PEBI Byte
   */
  val PiBYTE: Long = Maths.PiBYTE
  /**
   * exbibyte
   */
  val EiBYTE: Long = Maths.EiBYTE

  val YOCTO: Double = Maths.YOCTO
  val ZEPTO: Double = Maths.ZEPTO
  val ATTO: Double = Maths.ATTO
  val FEMTO: Double = Maths.FEMTO
  val PICO: Double = Maths.PICO
  val NANO: Double = Maths.NANO
  val MICRO: Double = Maths.MICRO
  val MILLI: Double = Maths.MILLI
  val CENTI: Double = Maths.CENTI
  val DECI: Double = Maths.DECI
  /**
   * DECA
   */
  val DECA: Int = Maths.DECA
  /**
   * HECTO
   */
  val HECTO: Int = Maths.HECTO

  /**
   * KILO
   */
  val KILO: Int = Maths.KILO
  /**
   * MEGA
   */
  val MEGA: Int = Maths.MEGA
  val GIGA: Long = Maths.GIGA
  /**
   * TERA
   */
  val TERA: Long = Maths.TERA
  /**
   * PETA
   */
  val PETA: Long = Maths.PETA
  /**
   * EXA
   */
  val EXA: Long = Maths.EXA
  /**
   * ZETTA
   */
  val ZETTA: Long = Maths.ZETTA
  /**
   * YOTTA
   */
  val YOTTA: Long = Maths.YOTTA
  val SECOND: Long = Maths.SECOND
  val MINUTE: Long = Maths.MINUTE
  val HOUR: Long = Maths.HOUR
  val DAY: Long = Maths.DAY
  val KHZ: Double = Maths.KHZ
  val MHZ: Double = Maths.MHZ
  val GHZ: Double = Maths.GHZ
  val MILLIMETER: Double = Maths.MILLIMETER
  val MM: Double = Maths.MM
  val CM: Double = Maths.CM
  val CENTIMETER: Double = Maths.CENTIMETER
  /**
   * light celerity. speed of light in vacuum
   */
  //    public static  final int C = 300000000;
  val C: Int = Maths.C //m.s^-1

  /**
   * Newtonian constant of gravitation
   */
  val G: Double = Maths.G //m3·kg^−1·s^−2;

  /**
   * Planck constant
   */
  val H: Double = Maths.H //J·s;

  /**
   * Reduced Planck constant
   */
  val Hr: Double = Maths.Hr
  /**
   * magnetic constant (vacuum permeability)
   */
  val U0: Double = Maths.U0 //N·A−2

  /**
   * electric constant (vacuum permittivity) =1/(u0*C^2)
   **/
  val EPS0: Double = Maths.EPS0 //F·m−1

  /**
   * characteristic impedance of vacuum =1/(u0*C)
   */
  val Z0: Double = Maths.Z0
  /**
   * Coulomb's constant
   */
  val Ke: Double = Maths.Ke
  /**
   * elementary charge
   */
  val Qe: Double = Maths.Qe //C

  val COMPLEX_VECTOR_SPACE: VectorSpace[Complex] = Maths.COMPLEX_VECTOR_SPACE
  val EXPR_VECTOR_SPACE: VectorSpace[Expr] = Maths.EXPR_VECTOR_SPACE
  val DOUBLE_VECTOR_SPACE: VectorSpace[java.lang.Double] = Maths.DOUBLE_VECTOR_SPACE
  val X_AXIS: Int = Maths.X_AXIS
  val Y_AXIS: Int = Maths.Y_AXIS
  val Z_AXIS: Int = Maths.Z_AXIS
  val MATRIX_STORE_MANAGER: StoreManager[ComplexMatrix] = Maths.MATRIX_STORE_MANAGER
  val TMATRIX_STORE_MANAGER: StoreManager[Matrix[_]] = Maths.TMATRIX_STORE_MANAGER

  val TVECTOR_STORE_MANAGER: StoreManager[Vector[_]] = Maths.TVECTOR_STORE_MANAGER

  val VECTOR_STORE_MANAGER: StoreManager[ComplexVector] = Maths.VECTOR_STORE_MANAGER
  val IDENTITY: function.Function[_, _] = Maths.IDENTITY
  val COMPLEX_TO_DOUBLE: function.Function[Complex, java.lang.Double] = Maths.COMPLEX_TO_DOUBLE
  val DOUBLE_TO_COMPLEX: function.Function[java.lang.Double, Complex] = Maths.DOUBLE_TO_COMPLEX
  val DOUBLE_TO_TVECTOR: function.Function[java.lang.Double, Vector[_]] = Maths.DOUBLE_TO_TVECTOR
  val TVECTOR_TO_DOUBLE: function.Function[Vector[_], java.lang.Double] = Maths.TVECTOR_TO_DOUBLE
  val COMPLEX_TO_TVECTOR: function.Function[Complex, Vector[_]] = Maths.COMPLEX_TO_TVECTOR
  val TVECTOR_TO_COMPLEX: function.Function[Vector[_], Complex] = Maths.TVECTOR_TO_COMPLEX
  val COMPLEX_TO_EXPR: function.Function[Complex, Expr] = Maths.COMPLEX_TO_EXPR
  val EXPR_TO_COMPLEX: function.Function[Expr, Complex] = Maths.EXPR_TO_COMPLEX
  val DOUBLE_TO_EXPR: function.Function[java.lang.Double, Expr] = Maths.DOUBLE_TO_EXPR
  val EXPR_TO_DOUBLE: function.Function[Expr, java.lang.Double] = Maths.EXPR_TO_DOUBLE
  //    public static  String getAxisLabel(int axis){
  //        switch(axis){
  //            case X_AXIS:return "X";
  //            case Y_AXIS:return "Y";
  //            case Z_AXIS:return "Z";
  //        }
  //        throw new IllegalArgumentException("Unknown Axis "+axis);
  //    }
  val $STRING: TypeName[String] = Maths.$STRING
  val $COMPLEX: TypeName[Complex] = Maths.$COMPLEX
  val $MATRIX: TypeName[ComplexMatrix] = Maths.$MATRIX
  val $VECTOR: TypeName[ComplexVector] = Maths.$VECTOR
  val $CMATRIX: TypeName[Matrix[Complex]] = Maths.$CMATRIX
  val $CVECTOR: TypeName[Vector[Complex]] = Maths.$CVECTOR
  val $DOUBLE: TypeName[java.lang.Double] = Maths.$DOUBLE
  val $BOOLEAN: TypeName[java.lang.Boolean] = Maths.$BOOLEAN
  val $POINT: TypeName[Point] = Maths.$POINT
  val $FILE: TypeName[File] = Maths.$FILE
  //</editor-fold>
  val $INTEGER: TypeName[java.lang.Integer] = Maths.$INTEGER
  val $LONG: TypeName[java.lang.Long] = Maths.$LONG
  val $EXPR: TypeName[Expr] = Maths.$EXPR
  val $CLIST: TypeName[Vector[Complex]] = Maths.$CLIST
  val $ELIST: TypeName[Vector[Expr]] = Maths.$EVECTOR
  val $DLIST: TypeName[Vector[java.lang.Double]] = Maths.$DVECTOR
  val $DLIST2: TypeName[Vector[Vector[java.lang.Double]]] = Maths.$DLIST2
  val $ILIST: TypeName[Vector[java.lang.Integer]] = Maths.$IVECTOR
  val $BLIST: TypeName[Vector[java.lang.Boolean]] = Maths.$BVECTOR
  val $MLIST: TypeName[Vector[ComplexMatrix]] = Maths.$MVECTOR
  val Config: MathsConfig = Maths.Config
  val PlotConfig: PlotConfig = net.thevpc.scholar.hadruplot.Plot.Config
  var DISTANCE_DOUBLE: DistanceStrategy[java.lang.Double] = Maths.DISTANCE_DOUBLE
  var DISTANCE_COMPLEX: DistanceStrategy[Complex] = Maths.DISTANCE_COMPLEX
  var DISTANCE_MATRIX: DistanceStrategy[ComplexMatrix] = Maths.DISTANCE_MATRIX
  var DISTANCE_VECTOR: DistanceStrategy[ComplexVector] = Maths.DISTANCE_VECTOR
  /**
   * numeric scalar product operator
   */
  val NUMERIC_SP: ScalarProductOperator = Maths.NUMERIC_SP

  /**
   * formal scalar product operator with error fallback
   */
  val FORMAL_SP: ScalarProductOperator = Maths.FORMAL_SP

  /**
   * formal scalar product operator with numeric fallback
   */
  val DEFAULT_SP: ScalarProductOperator = Maths.DEFAULT_SP


  def xdomain(min: Double, max: Double): Domain = {
    Maths.xdomain(min, max)
  }

  def ydomain(min: Double, max: Double): Domain = {
    Maths.ydomain(min, max)
  }

  def ydomain(min: DomainExpr, max: DomainExpr): DomainExpr = {
    Maths.ydomain(min, max)
  }

  def zdomain(min: Double, max: Double): Domain = {
    Maths.zdomain(min, max)
  }

  def zdomain(min: Expr, max: Expr): DomainExpr = {
    Maths.zdomain(min, max)
  }

  def domain(u: RightArrowUplet2.Double): Domain = {
    Maths.domain(u)
  }

  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
    Maths.domain(ux, uy)
  }

  def domain(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
    Maths.domain(ux, uy, uz)
  }

  def domain(u: RightArrowUplet2.Expr): Expr = {
    Maths.domain(u)
  }

  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
    Maths.domain(ux, uy)
  }

  def domain(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
    Maths.domain(ux, uy, uz)
  }

  def domain(min: Expr, max: Expr): DomainExpr = {
    Maths.domain(min, max)
  }

  def domain(min: Double, max: Double): Domain = {
    Maths.domain(min, max)
  }

  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
    Maths.domain(xmin, xmax, ymin, ymax)
  }

  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
    Maths.domain(xmin, xmax, ymin, ymax)
  }

  def domain(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
    Maths.domain(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def domain(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
    Maths.domain(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def II(u: RightArrowUplet2.Double): Domain = {
    Maths.II(u)
  }

  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double): Domain = {
    Maths.II(ux, uy)
  }

  def II(ux: RightArrowUplet2.Double, uy: RightArrowUplet2.Double, uz: RightArrowUplet2.Double): Domain = {
    Maths.II(ux, uy, uz)
  }

  def II(u: RightArrowUplet2.Expr): Expr = {
    Maths.II(u)
  }

  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr): Expr = {
    Maths.II(ux, uy)
  }

  def II(ux: RightArrowUplet2.Expr, uy: RightArrowUplet2.Expr, uz: RightArrowUplet2.Expr): Expr = {
    Maths.II(ux, uy, uz)
  }

  def II(min: Expr, max: Expr): DomainExpr = {
    Maths.II(min, max)
  }

  def II(min: Double, max: Double): Domain = {
    Maths.II(min, max)
  }

  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double): Domain = {
    Maths.II(xmin, xmax, ymin, ymax)
  }

  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr): DomainExpr = {
    Maths.II(xmin, xmax, ymin, ymax)
  }

  def II(xmin: Double, xmax: Double, ymin: Double, ymax: Double, zmin: Double, zmax: Double): Domain = {
    Maths.II(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def II(xmin: Expr, xmax: Expr, ymin: Expr, ymax: Expr, zmin: Expr, zmax: Expr): DomainExpr = {
    Maths.II(xmin, xmax, ymin, ymax, zmin, zmax)
  }

  def param(name: String): DoubleParam = {
    Maths.param(name)
  }

//  def doubleParamSet(param: CParam): DoubleArrayParamSet = {
//    Maths.doubleParamSet(param)
//  }
//
//  def paramSet(param: CParam, values: Array[Double]): DoubleArrayParamSet = {
//    Maths.paramSet(param, values)
//  }
//
//  def paramSet(param: CParam, values: Array[Float]): FloatArrayParamSet = {
//    Maths.paramSet(param, values)
//  }
//
//  def floatParamSet(param: CParam): FloatArrayParamSet = {
//    Maths.floatParamSet(param)
//  }
//
//  def paramSet(param: CParam, values: Array[Long]): LongArrayParamSet = {
//    Maths.paramSet(param, values)
//  }
//
//  def longParamSet(param: CParam): LongArrayParamSet = {
//    Maths.longParamSet(param)
//  }
//
//  def paramSet[T](param: CParam, values: Array[T]): ArrayParamSet[T] = {
//    //Ignore Idea Error, this compiles in maven
//    Maths.paramSet(param, values.asInstanceOf[Array[T with Object]])
//  }
//
//  def objectParamSet[T](param: CParam): ArrayParamSet[T] = {
//    Maths.objectParamSet(param)
//  }
//
//  def paramSet(param: CParam, values: Array[Int]): IntArrayParamSet = {
//    Maths.paramSet(param, values)
//  }
//
//  def intParamSet(param: CParam): IntArrayParamSet = {
//    Maths.intParamSet(param)
//  }
//
//  def paramSet(param: CParam, values: Array[Boolean]): BooleanArrayParamSet = {
//    Maths.paramSet(param, values)
//  }
//
//  def boolParamSet(param: CParam): BooleanArrayParamSet = {
//    Maths.boolParamSet(param)
//  }
//
//  def xParamSet(xsamples: Int): XParamSet = {
//    Maths.xParamSet(xsamples)
//  }
//
//  def xyParamSet(xsamples: Int, ysamples: Int): XParamSet = {
//    Maths.xyParamSet(xsamples, ysamples)
//  }
//
//  def xyzParamSet(xsamples: Int, ysamples: Int, zsamples: Int): XParamSet = {
//    Maths.xyzParamSet(xsamples, ysamples, zsamples)
//  }

  def zerosMatrix(other: ComplexMatrix): ComplexMatrix = {
    Maths.zerosMatrix(other)
  }

  def constantMatrix(dim: Int, value: Complex): ComplexMatrix = {
    Maths.constantMatrix(dim, value)
  }

  def onesMatrix(dim: Int): ComplexMatrix = {
    Maths.onesMatrix(dim)
  }

  def onesMatrix(rows: Int, cols: Int): ComplexMatrix = {
    Maths.onesMatrix(rows, cols)
  }

  def constantMatrix(rows: Int, cols: Int, value: Complex): ComplexMatrix = {
    Maths.constantMatrix(rows, cols, value)
  }

  def zerosMatrix(dim: Int): ComplexMatrix = {
    Maths.zerosMatrix(dim)
  }

  def I(iValue: Array[Array[Complex]]): ComplexMatrix = {
    //    matrix(iValue).mul(I)
    matrix(iValue).mul(net.thevpc.scholar.hadrumaths.Complex.of(0, 1))
    //TODO
    //    return null;
    //   Maths.I(iValue)
  }

  def zerosMatrix(rows: Int, cols: Int): ComplexMatrix = {
    Maths.zerosMatrix(rows, cols)
  }

  def identityMatrix(c: ComplexMatrix): ComplexMatrix = {
    Maths.identityMatrix(c)
  }

  def NaNMatrix(dim: Int): ComplexMatrix = {
    Maths.NaNMatrix(dim)
  }

  def NaNMatrix(rows: Int, cols: Int): ComplexMatrix = {
    Maths.NaNMatrix(rows, cols)
  }

  def identityMatrix(dim: Int): ComplexMatrix = {
    Maths.identityMatrix(dim)
  }

  def identityMatrix(rows: Int, cols: Int): ComplexMatrix = {
    Maths.identityMatrix(rows, cols)
  }

  def matrix(matrix: ComplexMatrix): ComplexMatrix = {
    Maths.matrix(matrix)
  }

  def matrix(string: String): ComplexMatrix = {
    Maths.matrix(string)
  }

  def matrix(complex: Array[Array[Complex]]): ComplexMatrix = {
    Maths.matrix(complex)
  }

  def matrix(complex: Array[Array[Double]]): ComplexMatrix = {
    Maths.matrix(complex)
  }

  def matrix(rows: Int, cols: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.matrix(rows, cols, cellFactory)
  }

  def columnMatrix(values: Complex*): ComplexMatrix = {
    Maths.columnMatrix(values: _ *)
  }

  // removed because doubles will be converted to complexes
  //  def columnMatrix(values: Double*): Matrix = {
  //   Maths.columnMatrix(values:_ *)
  //  }

  def columnMatrix(rows: Int, cellFactory: VectorCell[Complex]): ComplexMatrix = {
    Maths.columnMatrix(rows, cellFactory)
  }

  //  REMOVED because doubles will be converted to complexes
  //  def rowMatrix(values: Double*): Matrix = {
  //   Maths.rowMatrix(values:_ *)
  //  }

  def rowMatrix(values: Complex*): ComplexMatrix = {
    Maths.rowMatrix(values: _ *)
  }


  def rowMatrix(columns: Int, cellFactory: VectorCell[Complex]): ComplexMatrix = {
    Maths.rowMatrix(columns, cellFactory)
  }

  def symmetricMatrix(rows: Int, cols: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.symmetricMatrix(rows, cols, cellFactory)
  }

  def hermitianMatrix(rows: Int, cols: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.hermitianMatrix(rows, cols, cellFactory)
  }

  def diagonalMatrix(rows: Int, cellFactory: VectorCell[Complex]): ComplexMatrix = {
    Maths.diagonalMatrix(rows, cellFactory)
  }

  def diagonalMatrix(c: Complex*): ComplexMatrix = {
    Maths.diagonalMatrix(c: _ *)
  }

  def matrix(dim: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.matrix(dim, cellFactory)
  }

  def matrix(rows: Int, columns: Int): ComplexMatrix = {
    Maths.matrix(rows, columns)
  }

  def symmetricMatrix(dim: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.symmetricMatrix(dim, cellFactory)
  }

  def hermitianMatrix(dim: Int, cellFactory: MatrixCell[Complex]): ComplexMatrix = {
    Maths.hermitianMatrix(dim, cellFactory)
  }

  def randomRealMatrix(m: Int, n: Int): ComplexMatrix = {
    Maths.randomRealMatrix(m, n)
  }

  def randomRealMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
    Maths.randomRealMatrix(m, n, min, max)
  }

  def randomRealMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
    Maths.randomRealMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
    Maths.randomImagMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
    Maths.randomImagMatrix(m, n, min, max)
  }

  def randomMatrix(m: Int, n: Int, minReal: Int, maxReal: Int, minImag: Int, maxImag: Int): ComplexMatrix = {
    Maths.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
  }

  def randomMatrix(m: Int, n: Int, minReal: Double, maxReal: Double, minImag: Double, maxImag: Double): ComplexMatrix = {
    Maths.randomMatrix(m, n, minReal, maxReal, minImag, maxImag)
  }

  def randomMatrix(m: Int, n: Int, min: Double, max: Double): ComplexMatrix = {
    Maths.randomMatrix(m, n, min, max)
  }

  def randomMatrix(m: Int, n: Int, min: Int, max: Int): ComplexMatrix = {
    Maths.randomMatrix(m, n, min, max)
  }

  def randomImagMatrix(m: Int, n: Int): ComplexMatrix = {
    Maths.randomImagMatrix(m, n)
  }

  @throws[UncheckedIOException]
  def loadTMatrix[T](componentType: TypeName[T], file: File): Matrix[T] = {
    Maths.loadTMatrix(componentType, file)
  }

  @throws[UncheckedIOException]
  def loadMatrix(file: File): ComplexMatrix = {
    Maths.loadMatrix(file)
  }

  @throws[UncheckedIOException]
  def matrix(file: File): ComplexMatrix = {
    Maths.matrix(file)
  }

  @throws[UncheckedIOException]
  def storeMatrix(m: ComplexMatrix, file: String): Unit = {
    Maths.storeMatrix(m, file)
  }

  @throws[UncheckedIOException]
  def storeMatrix(m: ComplexMatrix, file: File): Unit = {
    Maths.storeMatrix(m, file)
  }

  @throws[UncheckedIOException]
  def loadOrEvalMatrix(file: String, item: Supplier[ComplexMatrix]): ComplexMatrix = {
    Maths.loadOrEvalMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalVector(file: String, item: Supplier[Vector[Complex]]): ComplexVector = {
    Maths.loadOrEvalVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalMatrix(file: File, item: Supplier[ComplexMatrix]): ComplexMatrix = {
    Maths.loadOrEvalMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalVector(file: File, item: Supplier[Vector[Complex]]): ComplexVector = {
    Maths.loadOrEvalVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTMatrix[T](file: String, item: Supplier[Matrix[T]]): Matrix[_] = {
    Maths.loadOrEvalTMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTVector[T](file: String, item: Supplier[Vector[T]]): Vector[T] = {
    Maths.loadOrEvalTVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTMatrix[T](file: File, item: Supplier[Matrix[T]]): Matrix[T] = {
    Maths.loadOrEvalTMatrix(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEvalTVector[T](file: File, item: Supplier[Vector[T]]): Vector[_] = {
    Maths.loadOrEvalTVector(file, item)
  }

  @throws[UncheckedIOException]
  def loadOrEval[T](ofType: TypeName[T], file: File, item: Supplier[T]): T = {
    Maths.loadOrEval(ofType, file, item)
  }

  @throws[UncheckedIOException]
  def loadMatrix(file: String): ComplexMatrix = {
    Maths.loadMatrix(file)
  }

  def inv(c: ComplexMatrix): ComplexMatrix = {
    Maths.inv(c)
  }

  def tr(c: ComplexMatrix): ComplexMatrix = {
    Maths.tr(c)
  }

  def trh(c: ComplexMatrix): ComplexMatrix = {
    Maths.trh(c)
  }

  def transpose(c: ComplexMatrix): ComplexMatrix = {
    Maths.transpose(c)
  }

  def transposeHermitian(c: ComplexMatrix): ComplexMatrix = {
    Maths.transposeHermitian(c)
  }

  def rowVector(elems: Array[Complex]): ComplexVector = {
    Maths.rowVector(elems)
  }

  def constantColumnVector(size: Int, c: Complex): ComplexVector = {
    Maths.constantColumnVector(size, c)
  }

  def constantRowVector(size: Int, c: Complex): ComplexVector = {
    Maths.constantRowVector(size, c)
  }

  def zerosVector(size: Int): ComplexVector = {
    Maths.zerosVector(size)
  }

  def zerosColumnVector(size: Int): ComplexVector = {
    Maths.zerosColumnVector(size)
  }

  def zerosRowVector(size: Int): ComplexVector = {
    Maths.zerosRowVector(size)
  }

  def NaNColumnVector(dim: Int): ComplexVector = {
    Maths.NaNColumnVector(dim)
  }

  def NaNRowVector(dim: Int): ComplexVector = {
    Maths.NaNRowVector(dim)
  }

  def columnVector(expr: Expr*): Vector[Expr] = {
    Maths.columnVector(expr: _ *)
  }

  def rowVector(expr: Expr*): Vector[Expr] = {
    Maths.rowVector(expr: _ *)
  }

  def columnEVector(rows: Int, cellFactory: VectorCell[Expr]): ExprVector = {
    Maths.columnEVector(rows, cellFactory)
  }

  def rowEVector(rows: Int, cellFactory: VectorCell[Expr]): ExprVector = {
    Maths.rowEVector(rows, cellFactory)
  }

  def copyOf(ofType: Array[Array[Complex]]): Array[Array[Complex]] = {
    Maths.copyOf(ofType)
  }

  def copyOf(ofType: Array[Complex]): Array[Complex] = {
    Maths.copyOf(ofType)
  }

  def copyOf[T](vector: Vector[T]): Vector[T] = {
    Maths.copyOf(vector)
  }

  def columnTVector[T](cls: TypeName[T], cellFactory: VectorModel[T]): Vector[T] = {
    Maths.columnVector(cls, cellFactory)
  }

  def rowTVector[T](cls: TypeName[T], cellFactory: VectorModel[T]): Vector[T] = {
    Maths.rowTVector(cls, cellFactory)
  }

  def columnTVector[T](cls: TypeName[T], rows: Int, cellFactory: VectorCell[T]): Vector[T] = {
    Maths.columnVector(cls, rows, cellFactory)
  }

  def rowTVector[T](cls: TypeName[T], rows: Int, cellFactory: VectorCell[T]): Vector[T] = {
    Maths.rowTVector(cls, rows, cellFactory)
  }

  def columnVector(rows: Int, cellFactory: VectorCell[Complex]): ComplexVector = {
    Maths.columnVector(rows, cellFactory)
  }

  def rowVector(columns: Int, cellFactory: VectorCell[Complex]): ComplexVector = {
    Maths.rowVector(columns, cellFactory)
  }

  def columnVector(elems: Complex*): ComplexVector = {
    Maths.columnVector(elems: _ *)
  }

  def columnVector(elems: Array[Double]): ComplexVector = {
    Maths.columnVector(elems)
  }

  def rowVector(elems: Array[Double]): ComplexVector = {
    Maths.rowVector(elems)
  }

  def column(elems: Array[Complex]): ComplexVector = {
    Maths.column(elems)
  }

  def row(elems: Array[Complex]): ComplexVector = {
    Maths.row(elems)
  }

  def trh(c: ComplexVector): ComplexVector = {
    Maths.trh(c)
  }

  def tr(c: ComplexVector): ComplexVector = {
    Maths.tr(c)
  }

  def I(dValue: Double): Complex = {
    //    Maths.I(dValue)
    net.thevpc.scholar.hadrumaths.Complex.of(0, dValue)
    //TODO
    //    return null;
  }

  def abs(a: Complex): Complex = {
    Maths.abs(a)
  }

  def absdbl(a: Complex): Double = {
    Maths.absdbl(a)
  }

  def getColumn(a: Array[Array[Double]], index: Int): Array[Double] = {
    Maths.getColumn(a, index)
  }

  def dtimes(min: Double, max: Double, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Double] = {
    Maths.dtimes(min, max, times, maxTimes, strategy)
  }

  def dtimes(min: Double, max: Double, times: Int): Array[Double] = {
    Maths.dtimes(min, max, times)
  }

  def ftimes(min: Float, max: Float, times: Int): Array[Float] = {
    Maths.ftimes(min, max, times)
  }

  def ltimes(min: Long, max: Long, times: Int): Array[Long] = {
    Maths.ltimes(min, max, times)
  }

  def lsteps(min: Long, max: Long, step: Long): Array[Long] = {
    Maths.lsteps(min, max, step)
  }

  def itimes(min: Int, max: Int, times: Int, maxTimes: Int, strategy: IndexSelectionStrategy): Array[Int] = {
    Maths.itimes(min, max, times, maxTimes, strategy)
  }

  def dsteps(max: Int): Array[Double] = {
    Maths.dsteps(max)
  }

  def dsteps(min: Double, max: Double): Array[Double] = {
    Maths.dsteps(min, max)
  }

  def dstepsFrom(min: Double, times: Int,step: Double): Array[Double] = {
    Maths.dstepsFrom(min, times, step)
  }

  def dstepsLength(min: Double, max: Double, step: Double): Double = {
    Maths.dstepsLength(min, max, step)
  }

  def dstepsElement(min: Double, max: Double, step: Double, index: Int): Double = {
    Maths.dstepsElement(min, max, step, index)
  }

  def dsteps(min: Double, max: Double, step: Double): Array[Double] = {
    Maths.dsteps(min, max, step)
  }

  def fsteps(min: Float, max: Float, step: Float): Array[Float] = {
    Maths.fsteps(min, max, step)
  }

  def isteps(min: Int, max: Int, step: Int): Array[Int] = {
    Maths.isteps(min, max, step)
  }

  def isteps(min: Int, max: Int, step: Int, filter: IntPredicate): Array[Int] = {
    Maths.isteps(min, max, step, filter)
  }

  def itimes(min: Int, max: Int, times: Int): Array[Int] = {
    Maths.itimes(min, max, times)
  }

  def isteps(max: Int): Array[Int] = {
    Maths.isteps(max)
  }

  def isteps(min: Int, max: Int): Array[Int] = {
    Maths.isteps(min, max)
  }

  def itimes(min: Int, max: Int): Array[Int] = {
    Maths.itimes(min, max)
  }

  def hypot(a: Double, b: Double): Double = {
    Maths.hypot(a, b)
  }

  def sqr(d: Complex): Complex = {
    Maths.sqr(d)
  }

  def sqr(d: Double): Double = {
    Maths.sqr(d)
  }

  def sqr(d: Int): Int = {
    Maths.sqr(d)
  }

  def sqr(d: Long): Long = {
    Maths.sqr(d)
  }

  def sqr(d: Float): Float = {
    Maths.sqr(d)
  }

  def sincard(value: Double): Double = {
    Maths.sincard(value)
  }

  def minusOnePower(pow: Int): Int = {
    Maths.minusOnePower(pow)
  }

  def exp(c: Complex): Complex = {
    Maths.exp(c)
  }

  def sin(c: Complex): Complex = {
    Maths.sin(c)
  }

  def sinh(c: Complex): Complex = {
    Maths.sinh(c)
  }

  def cos(c: Complex): Complex = {
    Maths.cos(c)
  }

  def log(c: Complex): Complex = {
    Maths.log(c)
  }

  def log10(c: Complex): Complex = {
    Maths.log10(c)
  }

  def log10(c: Double): Double = {
    Maths.log10(c)
  }

  def log(c: Double): Double = {
    Maths.log(c)
  }

  def acotan(c: Double): Double = {
    Maths.acotan(c)
  }

  def exp(c: Double): Double = {
    Maths.exp(c)
  }

  def arg(c: Double): Double = {
    Maths.arg(c)
  }

  def argdbl(c: Complex): Double = {
    c.argdbl()
  }

  def db(c: Complex): Complex = {
    Maths.db(c)
  }

  def db2(c: Complex): Complex = {
    Maths.db2(c)
  }

  def cosh(c: Complex): Complex = {
    Maths.cosh(c)
  }

  def csum(c: Expr*): Complex = {
    Maths.csum(c: _ *)
  }

  def sum(c: Complex*): Complex = {
    Maths.sum(c: _ *)
  }

  def csum(c: VectorModel[Complex]): Complex = {
    Maths.csum(c)
  }

  def csum(size: Int, c: VectorCell[Complex]): Complex = {
    Maths.csum(size, c)
  }

  @throws[ArithmeticException]
  def chbevl(x: Double, coef: Array[Double], N: Int): Double = {
    Maths.chbevl(x, coef, N)
  }

  def pgcd(a: Long, b: Long): Long = {
    Maths.pgcd(a, b)
  }

  def pgcd(a: Int, b: Int): Int = {
    Maths.pgcd(a, b)
  }

  def toDouble(c: Array[Array[Complex]], toDoubleConverter: PlotDoubleConverter): Array[Array[Double]] = {
    Maths.toDouble(c, toDoubleConverter)
  }

  def toDouble(c: Array[Complex], toDoubleConverter: PlotDoubleConverter): Array[Double] = {
    Maths.toDouble(c, toDoubleConverter)
  }

  def rangeCC(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
    Maths.rangeCC(orderedValues, min, max)
  }

  def rangeCO(orderedValues: Array[Double], min: Double, max: Double): Array[Int] = {
    Maths.rangeCO(orderedValues, min, max)
  }

  def csqrt(d: Double): Complex = {
    Maths.csqrt(d)
  }

  def sqrt(d: Complex): Complex = {
    Maths.sqrt(d)
  }

  def dsqrt(d: Double): Double = {
    Maths.dsqrt(d)
  }

  def cotanh(c: Complex): Complex = {
    Maths.cotanh(c)
  }

  def tanh(c: Complex): Complex = {
    Maths.tanh(c)
  }

  def inv(c: Complex): Complex = {
    Maths.inv(c)
  }

  def tan(c: Complex): Complex = {
    Maths.tan(c)
  }

  def cotan(c: Complex): Complex = {
    Maths.cotan(c)
  }

  def pow(a: Complex, b: Complex): Complex = {
    Maths.pow(a, b)
  }

  def div(a: Complex, b: Complex): Complex = {
    Maths.div(a, b)
  }

  def add(a: Complex, b: Complex): Complex = {
    Maths.plus(a, b)
  }

  def sub(a: Complex, b: Complex): Complex = {
    Maths.minus(a, b)
  }

  def norm(a: ComplexMatrix): Double = {
    Maths.norm(a)
  }

  def norm(a: ComplexVector): Double = {
    Maths.norm(a)
  }

  def norm1(a: ComplexMatrix): Double = {
    Maths.norm1(a)
  }

  def norm2(a: ComplexMatrix): Double = {
    Maths.norm2(a)
  }

  def norm3(a: ComplexMatrix): Double = {
    Maths.norm3(a)
  }

  def normInf(a: ComplexMatrix): Double = {
    Maths.normInf(a)
  }

  def complex(fx: DoubleToDouble): DoubleToComplex = {
    Maths.complex(fx)
  }

  def complex(fx: DoubleToDouble, fy: DoubleToDouble): DoubleToComplex = {
    Maths.complex(fx, fy)
  }

  def randomDouble(value: Double): Double = {
    Maths.randomDouble(value)
  }

  def randomDouble(min: Double, max: Double): Double = {
    Maths.randomDouble(min, max)
  }

  def randomInt(value: Int): Int = {
    Maths.randomInt(value)
  }

  def randomInt(min: Int, max: Int): Int = {
    Maths.randomInt(min, max)
  }

  def randomComplex: Complex = {
    Maths.randomComplex
  }

  def randomBoolean: Boolean = {
    Maths.randomBoolean
  }

  def cross(x: Array[Double], y: Array[Double]): Array[Array[Double]] = {
    Maths.cross(x, y)
  }

  def cross(x: Array[Double], y: Array[Double], z: Array[Double]): Array[Array[Double]] = {
    Maths.cross(x, y, z)
  }

  def cross(x: Array[Double], y: Array[Double], z: Array[Double], filter: Double3Filter): Array[Array[Double]] = {
    Maths.cross(x, y, z, filter)
  }

  def cross(x: Array[Double], y: Array[Double], filter: Double2Filter): Array[Array[Double]] = {
    Maths.cross(x, y, filter)
  }

  def cross(x: Array[Int], y: Array[Int]): Array[Array[Int]] = {
    Maths.cross(x, y)
  }

  def cross(x: Array[Int], y: Array[Int], z: Array[Int]): Array[Array[Int]] = {
    Maths.cross(x, y, z)
  }

  def sineSeq(borders: String, m: Int, n: Int, domain: Domain): Vector[_] = {
    Maths.sineSeq(borders, m, n, domain)
  }

  def sineSeq(borders: String, m: Int, n: Int, domain: Domain, plane: PlaneAxis): Vector[_] = {
    Maths.sineSeq(borders, m, n, domain, plane)
  }

  def sineSeq(borders: String, m: DoubleParam, n: DoubleParam, domain: Domain): Expr = {
    Maths.sineSeq(borders, m, n, domain)
  }

  def rooftop(borders: String, nx: Int, ny: Int, domain: Domain): Expr = {
    Maths.rooftop(borders, nx, ny, domain)
  }

  def any(e: Double): Any = {
    Maths.any(e)
  }

  def any(e: Expr): Any = {
    Maths.any(e)
  }

  //  def any(e: Double): Any = {
  //   Maths.any(e)
  //  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double, filter: Double2Filter): ExprVector = {
    Maths.seq(pattern, m, mmax, n, nmax, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, max: Double, filter: DoublePredicate): ExprVector = {
    Maths.seq(pattern, m, max, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double, p: DoubleParam, pmax: Double, filter: Double3Filter): ExprVector = {
    Maths.seq(pattern, m, mmax, n, nmax, p, pmax, filter)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mmax: Double, n: DoubleParam, nmax: Double): ExprVector = {
    Maths.seq(pattern, m, mmax, n, nmax)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): ExprVector = {
    Maths.seq(pattern, m, mvalues, n, nvalues)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, values: Array[Array[Double]]): ExprVector = {
    Maths.seq(pattern, m, n, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, n: DoubleParam, p: DoubleParam, values: Array[Array[Double]]): ExprVector = {
    Maths.seq(pattern, m, n, p, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: Array[DoubleParam], values: Array[Array[Double]]): ExprVector = {
    Maths.seq(pattern, m, values)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, min: Double, max: Double): ExprVector = {
    Maths.seq(pattern, m, min, max)
  }

  @Deprecated
  def seq(pattern: Expr, m: DoubleParam, values: Array[Double]): ExprVector = {
    Maths.seq(pattern, m, values)
  }

  def matrix(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double]): ExprMatrix = {
    Maths.matrix(pattern, m, mvalues, n, nvalues)
  }

  def cube(pattern: Expr, m: DoubleParam, mvalues: Array[Double], n: DoubleParam, nvalues: Array[Double], p: DoubleParam, pvalues: Array[Double]): ExprCube = {
    Maths.cube(pattern, m, mvalues, n, nvalues, p, pvalues)
  }

  def derive(f: Expr, axis: Axis): Expr = {
    Maths.derive(f, axis)
  }

  def isReal(e: Expr): Boolean = {
    Maths.isReal(e)
  }

  def isImag(e: Expr): Boolean = {
    Maths.isImag(e)
  }

  def abs(e: Expr): Expr = {
    Maths.abs(e)
  }

  def db(e: Expr): Expr = {
    Maths.db(e)
  }

  def db2(e: Expr): Expr = {
    Maths.db2(e)
  }

  def complex(e: Int): Complex = {
    Maths.complex(e)
  }

  def complex(e: Double): Complex = {
    Maths.complex(e)
  }

  def complex(a: Double, b: Double): Complex = {
    Maths.complex(a, b)
  }

  def Double(e: Expr): Double = {
    Maths.Double(e)
  }

  def real(e: Expr): Expr = {
    Maths.real(e)
  }

  def imag(e: Expr): Expr = {
    Maths.imag(e)
  }

  def Complex(e: Expr): Complex = {
    Maths.Complex(e)
  }

  def complex(e: Expr): Complex = {
    Maths.complex(e)
  }

  def Complex(e: Double): Complex = {
    Maths.Complex(e)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]], axis1: Axis, axis2: Axis, axis3: Axis): CDiscrete = {
    Maths.discrete(domain, model, axis1, axis2, axis3)
  }

  def discrete(domain: Domain, model: Array[Array[Array[Complex]]]): CDiscrete = {
    Maths.discrete(domain, model)
  }

  def discrete(expr: Expr, domain: Domain, xsamples: Int, ysamples: Int, zsamples: Int): Expr = {
    Maths.discrete(expr, domain, xsamples, ysamples, zsamples)
  }

  def abssqr(e: Expr): Expr = {
    Maths.abssqr(e)
  }

  def adaptiveEval(expr: Expr, config: AdaptiveConfig): AdaptiveResult1 = {
    Maths.adaptiveEval(expr, config)
  }

  def adaptiveEval[T](expr: AdaptiveFunction1[T], distance: DistanceStrategy[T], domain: DomainX, config: AdaptiveConfig): AdaptiveResult1 = {
    Maths.adaptiveEval(expr, distance, domain, config)
  }

  def discrete(expr: Expr): CDiscrete = {
    Maths.discrete(expr)
  }

  def vdiscrete(expr: Expr): VDiscrete = {
    Maths.vdiscrete(expr)
  }

  def discrete(expr: Expr, xSamples: Int): Expr = {
    Maths.discrete(expr, xSamples)
  }

  def discrete(expr: Expr, xSamples: Int, ySamples: Int): Expr = {
    Maths.discrete(expr, xSamples, ySamples)
  }

  def discrete(expr: Expr, xSamples: Int, ySamples: Int, zSamples: Int): Expr = {
    Maths.discrete(expr, xSamples, ySamples, zSamples)
  }

  def axis(e: Axis): AxisFunction = {
    Maths.axis(e)
  }

  def sin2(d: Double): Double = {
    Maths.sin2(d)
  }

  def cos2(d: Double): Double = {
    Maths.cos2(d)
  }

  def isInt(d: Double): Boolean = {
    Maths.isInt(d)
  }

  def lcast[T](o: Any, toType: Class[T]): T = {
    Maths.lcast(o, toType)
  }

  def dump(o: scala.Any): String = {
    Maths.dump(o)
  }

  def dumpSimple(o: scala.Any): String = {
    Maths.dumpSimple(o)
  }

  def expr(value: Double, geometry: Geometry): DoubleToDouble = {
    Maths.expr(value, geometry)
  }

  def expr(value: Double, geometry: Domain): DoubleToDouble = {
    Maths.expr(value, geometry)
  }

  def expr(domain: Domain): DoubleToDouble = {
    Maths.expr(domain)
  }

  def expr(domain: Geometry): DoubleToDouble = {
    Maths.expr(domain)
  }

  def expr(a: Complex, geometry: Geometry): Expr = {
    Maths.expr(a, geometry)
  }

  def expr(a: Complex, geometry: Domain): Expr = {
    Maths.expr(a, geometry)
  }

  def abs[T](a: Vector[T]): Vector[T] = {
    Maths.abs(a)
  }

  def db[T](a: Vector[T]): Vector[T] = {
    Maths.db(a)
  }

  def db2[T](a: Vector[T]): Vector[T] = {
    Maths.db2(a)
  }

  def real[T](a: Vector[T]): Vector[T] = {
    Maths.real(a)
  }

  def imag[T](a: Vector[T]): Vector[T] = {
    Maths.imag(a)
  }

  def real(a: Complex): Double = {
    Maths.real(a)
  }

  def imag(a: Complex): Double = {
    Maths.imag(a)
  }

  def almostEqualRelative(a: Float, b: Float, maxRelativeError: Float): Boolean = {
    Maths.almostEqualRelative(a, b, maxRelativeError)
  }

  def almostEqualRelative(a: Double, b: Double, maxRelativeError: Double): Boolean = {
    Maths.almostEqualRelative(a, b, maxRelativeError)
  }

  def almostEqualRelative(a: Complex, b: Complex, maxRelativeError: Double): Boolean = {
    Maths.almostEqualRelative(a, b, maxRelativeError)
  }

//  def dtimes(param: CParam, min: Double, max: Double, times: Int): DoubleArrayParamSet = {
//    Maths.dtimes(param, min, max, times)
//  }
//
//  def dsteps(param: CParam, min: Double, max: Double, step: Double): DoubleArrayParamSet = {
//    Maths.dsteps(param, min, max, step)
//  }
//
//  def itimes(param: CParam, min: Int, max: Int, times: Int): IntArrayParamSet = {
//    Maths.itimes(param, min, max, times)
//  }
//
//  def isteps(param: CParam, min: Int, max: Int, step: Int): IntArrayParamSet = {
//    Maths.isteps(param, min, max, step)
//  }
//
//  def ftimes(param: CParam, min: Int, max: Int, times: Int): FloatArrayParamSet = {
//    Maths.ftimes(param, min, max, times)
//  }
//
//  def fsteps(param: CParam, min: Int, max: Int, step: Int): FloatArrayParamSet = {
//    Maths.fsteps(param, min, max, step)
//  }
//
//  def ltimes(param: CParam, min: Int, max: Int, times: Int): LongArrayParamSet = {
//    Maths.ltimes(param, min, max, times)
//  }
//
//  def lsteps(param: CParam, min: Int, max: Int, step: Long): LongArrayParamSet = {
//    Maths.lsteps(param, min, max, step)
//  }

  def sin(v: ComplexVector): ComplexVector = {
    Maths.sin(v)
  }

  def cos(v: ComplexVector): ComplexVector = {
    Maths.cos(v)
  }

  def tan(v: ComplexVector): ComplexVector = {
    Maths.tan(v)
  }

  def cotan(v: ComplexVector): ComplexVector = {
    Maths.cotan(v)
  }

  def tanh(v: ComplexVector): ComplexVector = {
    Maths.tanh(v)
  }

  def cotanh(v: ComplexVector): ComplexVector = {
    Maths.cotanh(v)
  }

  def cosh(v: ComplexVector): ComplexVector = {
    Maths.cosh(v)
  }

  def sinh(v: ComplexVector): ComplexVector = {
    Maths.sinh(v)
  }

  def log(v: ComplexVector): ComplexVector = {
    Maths.log(v)
  }

  def log10(v: ComplexVector): ComplexVector = {
    Maths.log10(v)
  }

  def db(v: ComplexVector): ComplexVector = {
    Maths.db(v)
  }

  def exp(v: ComplexVector): ComplexVector = {
    Maths.exp(v)
  }

  def acosh(v: ComplexVector): ComplexVector = {
    Maths.acosh(v)
  }

  def acos(v: ComplexVector): ComplexVector = {
    Maths.acos(v)
  }

  def asinh(v: ComplexVector): ComplexVector = {
    Maths.asinh(v)
  }

  def asin(v: ComplexVector): ComplexVector = {
    Maths.asin(v)
  }

  def atan(v: ComplexVector): ComplexVector = {
    Maths.atan(v)
  }

  def acotan(v: ComplexVector): ComplexVector = {
    Maths.acotan(v)
  }

  def imag(v: ComplexVector): ComplexVector = {
    Maths.imag(v)
  }

  def real(v: ComplexVector): ComplexVector = {
    Maths.real(v)
  }

  def abs(v: ComplexVector): ComplexVector = {
    Maths.abs(v)
  }

//  def abs(v: Array[Complex]): Array[Complex] = {
//    Maths.abs(v)
//  }

  def avg(v: ComplexVector): Complex = {
    Maths.avg(v)
  }

  def sum(v: ComplexVector): Complex = {
    Maths.sum(v)
  }

  def prod(v: ComplexVector): Complex = {
    Maths.prod(v)
  }

  def abs(v: ComplexMatrix): ComplexMatrix = {
    Maths.abs(v)
  }

  def sin(v: ComplexMatrix): ComplexMatrix = {
    Maths.sin(v)
  }

  def cos(v: ComplexMatrix): ComplexMatrix = {
    Maths.cos(v)
  }

  def tan(v: ComplexMatrix): ComplexMatrix = {
    Maths.tan(v)
  }

  def cotan(v: ComplexMatrix): ComplexMatrix = {
    Maths.cotan(v)
  }

  def tanh(v: ComplexMatrix): ComplexMatrix = {
    Maths.tanh(v)
  }

  def cotanh(v: ComplexMatrix): ComplexMatrix = {
    Maths.cotanh(v)
  }

  def cosh(v: ComplexMatrix): ComplexMatrix = {
    Maths.cosh(v)
  }

  def sinh(v: ComplexMatrix): ComplexMatrix = {
    Maths.sinh(v)
  }

  def log(v: ComplexMatrix): ComplexMatrix = {
    Maths.log(v)
  }

  def log10(v: ComplexMatrix): ComplexMatrix = {
    Maths.log10(v)
  }

  def db(v: ComplexMatrix): ComplexMatrix = {
    Maths.db(v)
  }

  def exp(v: ComplexMatrix): ComplexMatrix = {
    Maths.exp(v)
  }

  def acosh(v: ComplexMatrix): ComplexMatrix = {
    Maths.acosh(v)
  }

  def acos(v: ComplexMatrix): ComplexMatrix = {
    Maths.acos(v)
  }

  def asinh(v: ComplexMatrix): ComplexMatrix = {
    Maths.asinh(v)
  }

  def asin(v: ComplexMatrix): ComplexMatrix = {
    Maths.asin(v)
  }

  def atan(v: ComplexMatrix): ComplexMatrix = {
    Maths.atan(v)
  }

  def acotan(v: ComplexMatrix): ComplexMatrix = {
    Maths.acotan(v)
  }

  def imag(v: ComplexMatrix): ComplexMatrix = {
    Maths.imag(v)
  }

  def real(v: ComplexMatrix): ComplexMatrix = {
    Maths.real(v)
  }

  def real(v: Array[Complex]): Array[Complex] = {
    Maths.real(v)
  }

  def realdbl(v: Array[Complex]): Array[Double] = {
    Maths.realdbl(v)
  }

  def imag(v: Array[Complex]): Array[Complex] = {
    Maths.imag(v)
  }

  def imagdbl(v: Array[Complex]): Array[Double] = {
    Maths.imagdbl(v)
  }

  def avg(v: ComplexMatrix): Complex = {
    Maths.avg(v)
  }

  def sum(v: ComplexMatrix): Complex = {
    Maths.sum(v)
  }

  def prod(v: ComplexMatrix): Complex = {
    Maths.prod(v)
  }

  def roundEquals(a: Double, b: Double, epsilon: Double): Boolean = {
    Maths.roundEquals(a, b, epsilon)
  }

  def round(value: Double, precision: Double): Double = {
    Maths.round(value, precision)
  }

  def sqrt(v: Double, n: Int): Double = {
    Maths.sqrt(v, n)
  }

  def pow(v: Double, n: Double): Double = {
    Maths.pow(v, n)
  }

  def db(x: Double): Double = {
    Maths.db(x)
  }

  def acosh(x: Double): Double = {
    Maths.acosh(x)
  }

  def atanh(x: Double): Double = {
    Maths.atanh(x)
  }

  def acotanh(x: Double): Double = {
    Maths.acotanh(x)
  }

  def asinh(x: Double): Double = {
    Maths.asinh(x)
  }

  def db2(nbr: Double): Double = {
    Maths.db2(nbr)
  }

  def sqrt(nbr: Double): Double = {
    Maths.sqrt(nbr)
  }

  def inv(x: Double): Double = {
    Maths.inv(x)
  }

  def conj(x: Double): Double = {
    Maths.conj(x)
  }

  def sin2(x: Array[Double]): Array[Double] = {
    Maths.sin2(x)
  }

  def cos2(x: Array[Double]): Array[Double] = {
    Maths.cos2(x)
  }

  def sin(x: Array[Double]): Array[Double] = {
    Maths.sin(x)
  }

  def cos(x: Array[Double]): Array[Double] = {
    Maths.cos(x)
  }

  def tan(x: Array[Double]): Array[Double] = {
    Maths.tan(x)
  }

  def cotan(x: Array[Double]): Array[Double] = {
    Maths.cotan(x)
  }

  def sinh(x: Array[Double]): Array[Double] = {
    Maths.sinh(x)
  }

  def cosh(x: Array[Double]): Array[Double] = {
    Maths.cosh(x)
  }

  def tanh(x: Array[Double]): Array[Double] = {
    Maths.tanh(x)
  }

  def cotanh(x: Array[Double]): Array[Double] = {
    Maths.cotanh(x)
  }

  def max(a: Double, b: Double): Double = {
    Maths.max(a, b)
  }

  def max(a: Int, b: Int): Int = {
    Maths.max(a, b)
  }

  def max(a: Long, b: Long): Long = {
    Maths.max(a, b)
  }

  def min(a: Double, b: Double): Double = {
    Maths.min(a, b)
  }

  def min(arr: Array[Double]): Double = {
    Maths.min(arr)
  }

  def max(arr: Array[Double]): Double = {
    Maths.max(arr)
  }

  def avg(arr: Array[Double]): Double = {
    Maths.avg(arr)
  }

  def min(a: Int, b: Int): Int = {
    Maths.min(a, b)
  }

  def min(a: Complex, b: Complex): Complex = {
    Maths.min(a, b)
  }

  def max(a: Complex, b: Complex): Complex = {
    Maths.max(a, b)
  }

  def min(a: Long, b: Long): Long = {
    Maths.min(a, b)
  }

  def minMax(a: Array[Double]): Array[Double] = {
    Maths.minMax(a)
  }

  def minMaxAbs(a: Array[Double]): Array[Double] = {
    Maths.minMaxAbs(a)
  }

  def minMaxAbsNonInfinite(a: Array[Double]): Array[Double] = {
    Maths.minMaxAbsNonInfinite(a)
  }

  def avgAbs(arr: Array[Double]): Double = {
    Maths.avgAbs(arr)
  }

  def distances(arr: Array[Double]): Array[Double] = {
    Maths.distances(arr)
  }

  def div(a: Array[Double], b: Array[Double]): Array[Double] = {
    Maths.div(a, b)
  }

  def mul(a: Array[Double], b: Array[Double]): Array[Double] = {
    Maths.mul(a, b)
  }

  def sub(a: Array[Double], b: Array[Double]): Array[Double] = {
    Maths.minus(a, b)
  }

  def sub(a: Array[Double], b: Double): Array[Double] = {
    Maths.minus(a, b)
  }

  def add(a: Array[Double], b: Array[Double]): Array[Double] = {
    Maths.add(a, b)
  }

  def db(a: Array[Double]): Array[Double] = {
    Maths.db(a)
  }

  def sin(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.sin(c)
  }

  def sin2(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.sin2(c)
  }

  def sin(x: Double): Double = {
    Maths.sin(x)
  }

  def cos(x: Double): Double = {
    Maths.cos(x)
  }

  def tan(x: Double): Double = {
    Maths.tan(x)
  }

  def cotan(x: Double): Double = {
    Maths.cotan(x)
  }

  def sinh(x: Double): Double = {
    Maths.sinh(x)
  }

  def cosh(x: Double): Double = {
    Maths.cosh(x)
  }

  def tanh(x: Double): Double = {
    Maths.tanh(x)
  }

  def abs(a: Double): Double = {
    Maths.abs(a)
  }

  def abs(a: Int): Int = {
    Maths.abs(a)
  }

  def cotanh(x: Double): Double = {
    Maths.cotanh(x)
  }

  def acos(x: Double): Double = {
    Maths.acos(x)
  }

  def asin(x: Double): Double = {
    Maths.asin(x)
  }

  def atan(x: Double): Double = {
    Maths.atan(x)
  }

  def sum(c: Double*): Double = {
    Maths.sum(c: _ *)
  }

  def mul(a: Array[Double], b: Double): Array[Double] = {
    Maths.mul(a, b)
  }

  def mulSelf(x: Array[Double], v: Double): Array[Double] = {
    Maths.mulSelf(x, v)
  }

  def div(a: Array[Double], b: Double): Array[Double] = {
    Maths.div(a, b)
  }

  def divSelf(x: Array[Double], v: Double): Array[Double] = {
    Maths.divSelf(x, v)
  }

  def add(x: Array[Double], v: Double): Array[Double] = {
    Maths.plus(x, v)
  }

  def addSelf(x: Array[Double], v: Double): Array[Double] = {
    Maths.addSelf(x, v)
  }

  def cos(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.cos(c)
  }

  def tan(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.tan(c)
  }

  def cotan(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.cotan(c)
  }

  def sinh(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.sinh(c)
  }

  def cosh(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.cosh(c)
  }

  def tanh(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.tanh(c)
  }

  def cotanh(c: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.cotanh(c)
  }

  def add(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.plus(a, b)
  }

  def sub(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.minus(a, b)
  }

  def div(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.div(a, b)
  }

  def mul(a: Array[Array[Double]], b: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.mul(a, b)
  }

  def db(a: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.db(a)
  }

  def db2(a: Array[Array[Double]]): Array[Array[Double]] = {
    Maths.db2(a)
  }

  def If(cond: Expr, exp1: Expr, exp2: Expr): IfExpr = {
    Maths.If(cond, exp1, exp2)
  }

  def If(cond: Expr, exp1: Expr): IfExpr = {
    Maths.If(cond, exp1)
  }

  def If(cond: Expr): IfExpr = {
    Maths.If(cond)
  }

  def or(a: Expr, b: Expr): Expr = {
    Maths.or(a, b)
  }

  def and(a: Expr, b: Expr): Expr = {
    Maths.and(a, b)
  }

  def not(a: Expr): Expr = {
    Maths.not(a)
  }

  def eq(a: Expr, b: Expr): Expr = {
    Maths.eq(a, b)
  }

  def ne(a: Expr, b: Expr): Expr = {
    Maths.ne(a, b)
  }

  def gte(a: Expr, b: Expr): Expr = {
    Maths.gte(a, b)
  }

  def gt(a: Expr, b: Expr): Expr = {
    Maths.gt(a, b)
  }

  def lte(a: Expr, b: Expr): Expr = {
    Maths.lte(a, b)
  }

  def lt(a: Expr, b: Expr): Expr = {
    Maths.lt(a, b)
  }

  def cos(e: Expr): Expr = {
    Maths.cos(e)
  }

  def cosh(e: Expr): Expr = {
    Maths.cosh(e)
  }

  def sin(e: Expr): Expr = {
    Maths.sin(e)
  }

  def sincard(e: Expr): Expr = {
    Maths.sincard(e)
  }

  def sinh(e: Expr): Expr = {
    Maths.sinh(e)
  }

  def tan(e: Expr): Expr = {
    Maths.tan(e)
  }

  def tanh(e: Expr): Expr = {
    Maths.tanh(e)
  }

  def cotan(e: Expr): Expr = {
    Maths.cotan(e)
  }

  def cotanh(e: Expr): Expr = {
    Maths.cotanh(e)
  }

  def sqr(e: Expr): Expr = {
    Maths.sqr(e)
  }

  def sqrt(e: Expr): Expr = {
    Maths.sqrt(e)
  }

  def inv(e: Expr): Expr = {
    Maths.inv(e)
  }

  def neg(e: Expr): Expr = {
    Maths.neg(e)
  }

  def exp(e: Expr): Expr = {
    Maths.exp(e)
  }

  def atan(e: Expr): Expr = {
    Maths.atan(e)
  }

  def acotan(e: Expr): Expr = {
    Maths.acotan(e)
  }

  def acos(e: Expr): Expr = {
    Maths.acos(e)
  }

  def asin(e: Expr): Expr = {
    Maths.asin(e)
  }

  def integrate(e: Expr): Complex = {
    Maths.integrate(e)
  }

  def integrate(e: Expr, domain: Domain): Complex = {
    Maths.integrate(e, domain)
  }

  def esum(size: Int, f: VectorCell[Expr]): Expr = {
    Maths.esum(size, f)
  }

  def esum(size1: Int, size2: Int, e: MatrixCell[Expr]): Expr = {
    Maths.esum(size1, size2, e)
  }

  def csum(size1: Int, size2: Int, e: MatrixCell[Complex]): Complex = {
    Maths.csum(size1, size2, e)
  }

  def seq(size1: Int, f: VectorCell[Expr]): Vector[Expr] = {
    Maths.seq(size1, f)
  }

  def seq(size1: Int, size2: Int, f: MatrixCell[Expr]): Vector[Expr] = {
    Maths.seq(size1, size2, f)
  }

  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProductCache(gp, fn, monitor)
  }

  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProductCache(sp, gp, fn, monitor)
  }

  def scalarProductCache(sp: ScalarProductOperator, gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProductCache(sp, gp, fn, axis, monitor)
  }

  def scalarProductCache(gp: Array[Expr], fn: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProductCache(gp, fn, axis, monitor)
  }

  def gate(axis: Axis, a: Double, b: Double): Expr = {
    Maths.gate(axis, a, b)
  }

  def gate(axis: Expr, a: Double, b: Double): Expr = {
    Maths.gate(axis, a, b)
  }

  def gateX(a: Double, b: Double): Expr = {
    Maths.gateX(a, b)
  }

  def gateY(a: Double, b: Double): Expr = {
    Maths.gateY(a, b)
  }

  def gateZ(a: Double, b: Double): Expr = {
    Maths.gateZ(a, b)
  }

  def scalarProduct(f1: DoubleToDouble, f2: DoubleToDouble): Double = {
    Maths.scalarProduct(f1, f2)
  }

  def scalarProduct(f1: Expr, f2: Vector[Expr]): ComplexVector = {
    Maths.scalarProduct(f1, f2)
  }

  def scalarProduct(f1: Expr, f2: Matrix[Expr]): ComplexMatrix = {
    Maths.scalarProduct(f1, f2)
  }

  def scalarProduct(f2: Vector[Expr], f1: Expr): ComplexVector = {
    Maths.scalarProduct(f2, f1)
  }

  def scalarProduct(f2: Matrix[Expr], f1: Expr): ComplexMatrix = {
    Maths.scalarProduct(f2, f1)
  }

  def scalarProduct(domain: Domain, f1: Expr, f2: Expr): Expr = {
    Maths.scalarProduct(domain, f1, f2)
  }

  def scalarProduct(f1: Expr, f2: Expr): Expr = {
    Maths.scalarProduct(f1, f2)
  }

  def scalarProductMatrix(g: Vector[Expr], f: Vector[Expr]): ComplexMatrix = {
    Maths.scalarProductMatrix(g, f)
  }

  def scalarProduct(g: Vector[Expr], f: Vector[Expr]): Matrix[Complex] = {
    Maths.scalarProduct(g, f)
  }

  def scalarProduct(g: Vector[Expr], f: Vector[Expr], monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProduct(g, f, monitor)
  }

  def scalarProductMatrix(g: Vector[Expr], f: Vector[Expr], monitor: ProgressMonitor): ComplexMatrix = {
    Maths.scalarProductMatrix(g, f, monitor)
  }

  def scalarProduct(g: Vector[Expr], f: Vector[Expr], axis: AxisXY, monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProduct(g, f, axis, monitor)
  }

  def scalarProductMatrix(g: Array[Expr], f: Array[Expr]): ComplexMatrix = {
    Maths.scalarProductMatrix(g, f)
  }

  def scalarProduct(g: ComplexMatrix, f: ComplexMatrix): Complex = {
    Maths.scalarProduct(g, f)
  }

  def scalarProduct(g: ComplexMatrix, f: Vector[Expr]): Expr = {
    Maths.scalarProduct(g, f)
  }

  def scalarProductAll(g: ComplexMatrix, f: Vector[Expr]*): Expr = {
    Maths.scalarProductAll(g, f: _ *)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr]): Matrix[Complex] = {
    Maths.scalarProduct(g, f)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProduct(g, f, monitor)
  }

  def scalarProductMatrix(g: Array[Expr], f: Array[Expr], monitor: ProgressMonitor): ComplexMatrix = {
    Maths.scalarProductMatrix(g, f, monitor)
  }

  def scalarProduct(g: Array[Expr], f: Array[Expr], axis: AxisXY, monitor: ProgressMonitor): Matrix[Complex] = {
    Maths.scalarProduct(g, f, axis, monitor)
  }

  def elist(size: Int): ExprVector = {
    Maths.evector(size)
  }

  def elist(row: Boolean, size: Int): ExprVector = {
    Maths.evector(row, size)
  }

  def elist(vector: Expr*): ExprVector = {
    Maths.evector(vector: _ *)
  }

  def elist(vector: Vector[Complex]): ExprVector = {
    Maths.evector(vector)
  }

  def clist(vector: Vector[Expr]): Vector[Complex] = {
    Maths.cvector(vector)
  }

  def clist: Vector[Complex] = {
    Maths.cvector
  }

  def clist(vector: Complex*): Vector[Complex] = {
    Maths.cvector(vector: _ *)
  }

  def mlist: Vector[ComplexMatrix] = {
    Maths.mvector
  }

  def mlist(size: Int): Vector[ComplexMatrix] = {
    Maths.mvector(size)
  }

  def mlist(items: ComplexMatrix*): Vector[ComplexMatrix] = {
    Maths.mvector(items: _ *)
  }

  def clist2: Vector[Vector[Complex]] = {
    Maths.cvector2
  }

  def elist2: Vector[Vector[Expr]] = {
    Maths.evector2
  }

  def dlist2: Vector[Vector[java.lang.Double]] = {
    Maths.dvector2
  }

  def ilist2: Vector[Vector[Integer]] = {
    Maths.ivector2
  }

  def mlist2: Vector[Vector[ComplexMatrix]] = {
    Maths.mvector2
  }

  def blist2: Vector[Vector[java.lang.Boolean]] = {
    Maths.bvector2
  }

  def list[T](typeName: TypeName[T]): Vector[T] = {
    Maths.vector(typeName)
  }

  def list[T](typeName: TypeName[T], initialSize: Int): Vector[T] = {
    Maths.vector(typeName, initialSize)
  }

  def listro[T](typeName: TypeName[T], row: Boolean, model: VectorModel[T]): Vector[T] = {
    Maths.vectorro(typeName, row, model)
  }

  def list[T](typeName: TypeName[T], row: Boolean, initialSize: Int): Vector[T] = {
    Maths.vector(typeName, row, initialSize)
  }

  def list[T](vector: Vector[T]): Vector[T] = {
    Maths.list(vector)
  }

  def elist(vector: ComplexMatrix): ExprVector = {
    Maths.evector(vector)
  }

  def vscalarProduct[T](vector: Vector[T], tVectors: Vector[Vector[T]]): Vector[T] = {
    Maths.vscalarProduct(vector, tVectors)
  }

  def elist: Vector[Expr] = {
    Maths.evector
  }

  def concat[T](a: Vector[T]*): Vector[T] = {
    Maths.concat(a: _*)
  }

  def dlist: Vector[java.lang.Double] = {
    Maths.dvector
  }

  def dlist(items: ToDoubleArrayAware): Vector[java.lang.Double] = {
    Maths.dvector(items)
  }

  def dlist(items: Array[Double]): Vector[java.lang.Double] = {
    Maths.dvector(items)
  }

  def dlist(row: Boolean, size: Int): Vector[java.lang.Double] = {
    Maths.dvector(row, size)
  }

  def dlist(size: Int): Vector[java.lang.Double] = {
    Maths.dvector(size)
  }

  def slist: Vector[String] = {
    Maths.slist
  }

  def slist(items: Array[String]): Vector[String] = {
    Maths.slist(items)
  }

  def slist(row: Boolean, size: Int): Vector[String] = {
    Maths.slist(row, size)
  }

  def slist(size: Int): Vector[String] = {
    Maths.slist(size)
  }

  def blist: Vector[java.lang.Boolean] = {
    Maths.bvector
  }

  def dlist(items: Array[Boolean]): Vector[java.lang.Boolean] = {
    Maths.dvector(items)
  }

  def blist(row: Boolean, size: Int): Vector[java.lang.Boolean] = {
    Maths.bvector(row, size)
  }

  def blist(size: Int): Vector[java.lang.Boolean] = {
    Maths.bvector(size)
  }

  def ilist: IntVector = {
    Maths.ivector
  }

  def ilist(items: Array[Int]): Vector[Integer] = {
    Maths.ivector(items)
  }

  def ilist(size: Int): Vector[Integer] = {
    Maths.ivector(size)
  }

  def ilist(row: Boolean, size: Int): Vector[Integer] = {
    Maths.ivector(row, size)
  }

  def llist: LongVector = {
    Maths.lvector
  }

  def llist(items: Array[Long]): Vector[java.lang.Long] = {
    Maths.lvector(items)
  }

  def llist(size: Int): Vector[java.lang.Long] = {
    Maths.lvector(size)
  }

  def llist(row: Boolean, size: Int): Vector[java.lang.Long] = {
    Maths.lvector(row, size)
  }

  def sum[T](ofType: TypeName[T], arr: T*): T = {
    Maths.sum(ofType, arr: _ *)
  }

  def sum[T](ofType: TypeName[T], arr: VectorModel[T]): T = {
    Maths.sum(ofType, arr)
  }

  def sum[T](ofType: TypeName[T], size: Int, arr: VectorCell[T]): T = {
    Maths.sum(ofType, size, arr)
  }

  def mul[T](ofType: TypeName[T], arr: T*): T = {
    Maths.mul(ofType, arr: _ *)
  }

  def mul[T](ofType: TypeName[T], arr: VectorModel[T]): T = {
    Maths.mul(ofType, arr)
  }

  def avg(d: CDiscrete): Complex = {
    Maths.avg(d)
  }

  def vsum(d: VDiscrete): DoubleToVector = {
    Maths.vsum(d)
  }

  def vavg(d: VDiscrete): DoubleToVector = {
    Maths.vavg(d)
  }

  def avg(d: VDiscrete): Complex = {
    Maths.avg(d)
  }

  def sum(arr: Expr*): Expr = {
    Maths.sum(arr: _ *)
  }

  def esum(arr: VectorModel[Expr]): Expr = {
    Maths.esum(arr)
  }

  def mul[T](a: Matrix[T], b: Matrix[T]): Matrix[T] = {
    Maths.mul(a, b)
  }

  def mul(a: ComplexMatrix, b: ComplexMatrix): ComplexMatrix = {
    Maths.mul(a, b)
  }

  def mul(a: Expr, b: Expr): Expr = {
    Maths.mul(a, b)
  }

  def edotmul(arr: Vector[Expr]*): Vector[Expr] = {
    Maths.edotmul(arr: _ *)
  }

  def edotdiv(arr: Vector[Expr]*): Vector[Expr] = {
    Maths.edotdiv(arr: _ *)
  }

  def cmul(arr: VectorModel[Complex]): Complex = {
    Maths.cmul(arr)
  }

  def emul(arr: VectorModel[Expr]): Expr = {
    Maths.emul(arr)
  }

  def mul(e: Expr*): Expr = {
    Maths.prod(e: _ *)
  }

  def pow(a: Expr, b: Expr): Expr = {
    Maths.pow(a, b)
  }

  def sub(a: Expr, b: Expr): Expr = {
    Maths.minus(a, b)
  }

  def add(a: Expr, b: java.lang.Double): Expr = {
    Maths.plus(a, b)
  }

  def mul(a: Expr, b: java.lang.Double): Expr = {
    Maths.mul(a, b)
  }

  def sub(a: Expr, b: java.lang.Double): Expr = {
    Maths.minus(a, b)
  }

  def div(a: Expr, b: java.lang.Double): Expr = {
    Maths.div(a, b)
  }

  def add(a: Expr, b: Expr): Expr = {
    Maths.plus(a, b)
  }

  def add(a: Expr*): Expr = {
    Maths.sum(a: _ *)
  }

  def div(a: Expr, b: Expr): Expr = {
    Maths.div(a, b)
  }

  def rem(a: Expr, b: Expr): Expr = {
    Maths.rem(a, b)
  }

  def expr(value: Double): Expr = {
    Maths.expr(value)
  }

  def expr[T](vector: Vector[T]): Vector[Expr] = {
    Maths.expr(vector)
  }

  def expr(matrix: Matrix[Complex]): Matrix[Expr] = {
    Maths.expr(matrix)
  }

  def tmatrix[T](ofType: TypeName[T], model: MatrixModel[T]): Matrix[T] = {
    Maths.tmatrix(ofType, model)
  }

  def tmatrix[T](ofType: TypeName[T], rows: Int, columns: Int, model: MatrixCell[T]): Matrix[T] = {
    Maths.tmatrix(ofType, rows, columns, model)
  }

  def simplify[T](a: T): T = {
    Maths.simplify(a)
  }

  def simplify(a: Expr): Expr = {
    Maths.simplify(a)
  }

  def simplify[T](a: T, simplifyOptions: SimplifyOptions): T = {
    Maths.simplify(a, simplifyOptions)
  }

  def simplify(a: Expr, simplifyOptions: SimplifyOptions): Expr = {
    Maths.simplify(a, simplifyOptions)
  }

  def norm(a: Expr): Double = {
    Maths.norm(a)
  }

  def normalize[T](a: Vector[T]): Vector[T] = {
    Maths.normalize(a)
  }

  def normalize(a: Geometry): Expr = {
    Maths.normalize(a)
  }

  def normalize(a: Expr): Expr = {
    Maths.normalize(a)
  }

  def vector(fx: Expr, fy: Expr): Expr = {
    Maths.vector(fx, fy)
  }

  def vector(fx: Expr): Expr = {
    Maths.vector(fx)
  }

  def vector(fx: Expr, fy: Expr, fz: Expr): Expr = {
    Maths.vector(fx, fy, fz)
  }

  def cos[T](a: Vector[T]): Vector[T] = {
    Maths.cos(a)
  }

  def cosh[T](a: Vector[T]): Vector[T] = {
    Maths.cosh(a)
  }

  def sin[T](a: Vector[T]): Vector[T] = {
    Maths.sin(a)
  }

  def sinh[T](a: Vector[T]): Vector[T] = {
    Maths.sinh(a)
  }

  def tan[T](a: Vector[T]): Vector[T] = {
    Maths.tan(a)
  }

  def tanh[T](a: Vector[T]): Vector[T] = {
    Maths.tanh(a)
  }

  def cotan[T](a: Vector[T]): Vector[T] = {
    Maths.cotan(a)
  }

  def cotanh[T](a: Vector[T]): Vector[T] = {
    Maths.cotanh(a)
  }

  def sqr[T](a: Vector[T]): Vector[T] = {
    Maths.sqr(a)
  }

  def sqrt[T](a: Vector[T]): Vector[T] = {
    Maths.sqrt(a)
  }

  def inv[T](a: Vector[T]): Vector[T] = {
    Maths.inv(a)
  }

  def neg[T](a: Vector[T]): Vector[T] = {
    Maths.neg(a)
  }

  def exp[T](a: Vector[T]): Vector[T] = {
    Maths.exp(a)
  }

  def simplify[T](a: Vector[T]): Vector[T] = {
    Maths.simplify(a)
  }

  def addAll[T](e: Vector[T], expressions: T*): Vector[T] = {
    Maths.addAll(e, expressions: _ *)
  }

  def mulAll[T](e: Vector[T], expressions: T*): Vector[T] = {
    Maths.mulAll(e, expressions: _ *)
  }

  def pow[T](a: Vector[T], b: T): Vector[T] = {
    Maths.pow(a, b)
  }

  def sub[T](a: Vector[T], b: T): Vector[T] = {
    Maths.minus(a, b)
  }

  def div[T](a: Vector[T], b: T): Vector[T] = {
    Maths.div(a, b)
  }

  def rem[T](a: Vector[T], b: T): Vector[T] = {
    Maths.rem(a, b)
  }

  def add[T](a: Vector[T], b: T): Vector[T] = {
    Maths.add(a, b)
  }

  def mul[T](a: Vector[T], b: T): Vector[T] = {
    Maths.mul(a, b)
  }

  def loopOver(values: Array[Array[AnyRef]], action: LoopAction): Unit = {
    Maths.loopOver(values, action)
  }

  def loopOver(values: Array[Loop], action: LoopAction): Unit = {
    Maths.loopOver(values, action)
  }

  def formatMemory: String = {
    Maths.formatMemory
  }

  def formatMetric(value: Double): String = {
    Maths.formatMetric(value)
  }

  def memoryInfo: MemoryInfo = {
    Maths.memoryInfo
  }

  def memoryMeter: MemoryMeter = {
    Maths.memoryMeter
  }

  def inUseMemory: Long = {
    Maths.inUseMemory
  }

  def maxFreeMemory: Long = {
    Maths.maxFreeMemory
  }

  def formatMemory(bytes: Long): String = {
    Maths.formatMemory(bytes)
  }

  def formatFrequency(frequency: Double): String = {
    Maths.formatFrequency(frequency)
  }

  def formatDimension(dimension: Double): String = {
    Maths.formatDimension(dimension)
  }

  def formatPeriodNanos(period: Long): String = {
    Maths.formatPeriodNanos(period)
  }

  def formatPeriodMillis(period: Long): String = {
    Maths.formatPeriodMillis(period)
  }

  def sizeOf(src: Class[_]): Int = {
    Maths.sizeOf(src)
  }

  def invokeMonitoredAction[T](mon: ProgressMonitor, messagePrefix: String, run: MonitoredAction[T]): T = {
    Maths.invokeMonitoredAction(mon, messagePrefix, run)
  }

  def chrono(): Chronometer = {
    Maths.chrono
  }

  def chrono(name: String): Chronometer = {
    Maths.chrono(name)
  }

  def chrono(name: String, r: Runnable): Chronometer = {
    Maths.chrono(name, r)
  }

  def chrono[V](name: String, r: Callable[V]): V = {
    Maths.chrono(name, r)
  }

  def solverExecutorService(threads: Int): SolverExecutorService = {
    Maths.solverExecutorService(threads)
  }

  def chrono(r: Runnable): Chronometer = {
    Maths.chrono(r)
  }

  def percentFormat: DoubleFormat = {
    Maths.percentFormat
  }

  def frequencyFormat: DoubleFormat = {
    Maths.frequencyFormat
  }

  def metricFormat: DoubleFormat = {
    Maths.metricFormat
  }

  def memoryFormat: DoubleFormat = {
    Maths.memoryFormat
  }

  def dblformat(format: String): DoubleFormat = {
    Maths.dblformat(format)
  }

  def resizePickFirst(array: Array[Double], newSize: Int): Array[Double] = {
    Maths.resizePickFirst(array, newSize)
  }

  def resizePickAverage(array: Array[Double], newSize: Int): Array[Double] = {
    Maths.resizePickAverage(array, newSize)
  }

  def toArray[T](t: java.lang.Class[T], coll: java.util.Collection[T]): Array[_ <: T] = {
    Maths.toArray[T](t, coll);
  }

  def toArray[T](t: TypeName[T], coll: java.util.Collection[_ >: T]): Array[T] = {
    //   Maths.toArray(t, coll)
    Maths.toArray(t, null).asInstanceOf[Array[T]]
  }

  def rerr(a: Double, b: Double): Double = {
    Maths.rerr(a, b)
  }

  def rerr(a: Complex, b: Complex): Double = {
    Maths.rerr(a, b)
  }

  def define(name: String, f: FunctionCCX): CustomCCFunctionXExpr = {
    Maths.define(name, f)
  }

  def define(name: String, f: FunctionDCX): CustomDCFunctionXExpr = {
    Maths.define(name, f)
  }

  def define(name: String, f: FunctionDDX): CustomDDFunctionXExpr = {
    Maths.define(name, f)
  }

  def define(name: String, f: FunctionDDXY): CustomDDFunctionXYExpr = {
    Maths.define(name, f)
  }

  def define(name: String, f: FunctionDCXY): CustomDCFunctionXYExpr = {
    Maths.define(name, f)
  }

  def define(name: String, f: FunctionCCXY): CustomCCFunctionXYExpr = {
    Maths.define(name, f)
  }

  def rerr(a: ComplexMatrix, b: ComplexMatrix): Double = {
    Maths.rerr(a, b)
  }

  def toDoubleArray[T <: Expr](c: Vector[T]): DoubleVector = {
    Maths.toDoubleArray(c)
  }

  def toDouble(c: Complex, d: ToDoubleFunction[AnyRef]): Double = {
    Maths.toDouble(c, d)
  }

  def conj(e: Expr): Expr = {
    Maths.conj(e)
  }

  def complex(t: Matrix[_]): Complex = {
    Maths.complex(t)
  }

  def matrix(t: Matrix[_]): ComplexMatrix = {
    Maths.matrix(t)
  }

  def ematrix(t: Matrix[_]): Matrix[Expr] = {
    Maths.ematrix(t)
  }

  def getVectorSpace[T](cls: TypeName[T]): VectorSpace[T] = {
    Maths.getVectorSpace(cls)
  }

  def refineSamples(values: Vector[java.lang.Double], n: Int): DoubleVector = {
    Maths.refineSamples(values, n)
  }

  def refineSamples(values: Array[Double], n: Int): Array[Double] = {
    Maths.refineSamples(values, n)
  }

  def getHadrumathsVersion: String = {
    Maths.getHadrumathsVersion
  }

  def expandComponentDimension(d1: ComponentDimension, d2: ComponentDimension): ComponentDimension = {
    Maths.expandComponentDimension(d1, d2)
  }

  def expandComponentDimension(e: Expr, d: ComponentDimension): Expr = {
    Maths.expandComponentDimension(e, d)
  }

  def atan2(y: Double, x: Double): Double = {
    Maths.atan2(y, x)
  }

  def ceil(a: Double): Double = {
    Maths.ceil(a)
  }

  def floor(a: Double): Double = {
    Maths.floor(a)
  }

  def round(a: Double): Long = {
    Maths.round(a)
  }

  def round(a: Float): Int = {
    Maths.round(a)
  }

  def random: Double = {
    Maths.random
  }

  def rightArrow[A, B](a: A, b: B): RightArrowUplet2[A, B] = {
    Maths.rightArrow(a, b)
  }

  def rightArrow(a: Double, b: Double): RightArrowUplet2.Double = {
    Maths.rightArrow(a, b)
  }

  def rightArrow(a: Complex, b: Complex): RightArrowUplet2.Complex = {
    Maths.rightArrow(a, b)
  }

  def rightArrow(a: Expr, b: Expr): RightArrowUplet2.Expr = {
    Maths.rightArrow(a, b)
  }

  def parseExpression(expression: String): Expr = {
    Maths.parseExpression(expression)
  }

  def createExpressionEvaluator: JContext = {
    Maths.createExpressionEvaluator
  }

  def createExpressionParser: JContext = {
    Maths.createExpressionParser
  }

  def evalExpression(expression: String): Expr = {
    Maths.evalExpression(expression)
  }

  def toRadians(a: Double): Double = {
    Maths.toRadians(a)
  }

  def toRadians(a: Array[Double]): Array[Double] = {
    Maths.toRadians(a)
  }

  def det(m: ComplexMatrix): Complex = {
    Maths.det(m)
  }

  def toInt(o: Any): Int = {
    Maths.toInt(o)
  }

  def toInt(o: Any, defaultValue: Integer): Int = {
    Maths.toInt(o, defaultValue)
  }

  def toLong(o: Any): Long = {
    Maths.toLong(o)
  }

  def toLong(o: Any, defaultValue: Long): Long = {
    Maths.toLong(o, defaultValue)
  }

  def toDouble(o: Any): Double = {
    Maths.toDouble(o)
  }

  def toDouble(o: Any, defaultValue: Double): Double = {
    Maths.toDouble(o, defaultValue)
  }

  def toFloat(o: Any): Float = {
    Maths.toFloat(o)
  }

  def toFloat(o: Any, defaultValue: Float): Float = {
    Maths.toFloat(o, defaultValue)
  }

  def DC(e: Expr): DoubleToComplex = {
    Maths.DC(e)
  }

  def DD(e: Expr): DoubleToDouble = {
    Maths.DD(e)
  }

  def DV(e: Expr): DoubleToVector = {
    Maths.DV(e)
  }

  def DM(e: Expr): DoubleToMatrix = {
    Maths.DM(e)
  }

  def matrix(e: Expr): ComplexMatrix = {
    Maths.matrix(e)
  }

  def solvePoly2dbl(a: Double,b: Double,c: Double): Array[Double] = {
    Maths.solvePoly2dbl(a,b,c)
  }

  def solvePoly2(a: Double,b: Double,c: Double): Array[Complex] = {
    Maths.solvePoly2(a,b,c)
  }

  ///////////////////////////////////////////

  def absoluteSamples(x: Array[Double], y: Array[Double], z: Array[Double]) = Samples.absolute(x, y, z)

  def absoluteSamples(x: Array[Double], y: Array[Double]) = Samples.absolute(x, y)

  def absoluteSamples(x: Array[Double]) = Samples.absolute(x)

  def relativeSamples(x: Array[Double], y: Array[Double], z: Array[Double]) = Samples.relative(x, y, z)

  def relativeSamples(x: Array[Double], y: Array[Double]) = Samples.relative(x, y)

  def relativeSamples(x: Array[Double]) = Samples.relative(x)

  def relativeSamples(x: Int, y: Int, z: Int) = Samples.relative(x, y, z)

  def relativeSamples(x: Int, y: Int) = Samples.relative(x, y)

  def relativeSamples(x: Int) = Samples.relative(x)

  def adaptiveSamples() = Samples.adaptive()

  def adaptiveSamples(min: Int, max: Int) = Samples.adaptive(min, max)

  def persistenceCache: PersistenceCacheBuilder = Maths.persistenceCache


}

