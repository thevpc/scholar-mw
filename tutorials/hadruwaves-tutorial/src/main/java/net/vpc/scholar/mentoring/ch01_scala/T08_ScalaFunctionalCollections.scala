package net.vpc.scholar.mentoring.ch01_scala

object T08_ScalaFunctionalCollections extends App{
  class Person(var name:String,var age:Int){
    override def toString = s"La Personne($name, $age)"
  }
  var all=List(
    new Person("alia",15),
    new Person("hammadi",20),
    new Person("aguerbi",17),
    new Person("belleaid",20),
  )
  println(all)
  println(all.sortBy(x=>x.age))
  println(all.filter(x=>x.age>17))
  println(all.forall(x=>x.age>10))
  println(all.forall(x=>x.age>18))
  println(all.exists(x=>x.age>18))
  println(all.exists(x=>x.age>65))
  println(all.groupBy(x=>x.age))
  println(all.flatMap(x=>List(x.name,x.age)))
  println(all.map(x=>x.age))
  println(all.map(x=>x.age).reduce((x,y)=>x+y))
}
