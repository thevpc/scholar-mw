package net.thevpc.scholar.mentoring.ch01_scala

object T08_ScalaFunctionalCollections extends App{
  class Person(var name:String,var age:Int){
    override def toString = s"La Personne($name, $age)"
  }
//  public class Person{
//    String name;
//    int age;
//    Person(String name, int age){
//      //
//    }
//    getNAme
//    getAge
//    setName
//    setAge
//  }

  var seif=List(
    new Person("alia",15),
    new Person("hammadi",20),
    new Person("aguerbi",17),
    new Person("belleaid",20),
  )
  println(seif)
  println(seif.sortBy(x=>x.age))
  println(seif.filter(x=>x.age>17))
  println(seif.forall(x=>x.age>10))
  println(seif.forall(x=>x.age>18))
  println(seif.exists(x=>x.age>18))
  println(seif.exists(x=>x.age>65))
  println(seif.groupBy(x=>x.age))
  println(seif.flatMap(x=>List(x.name,x.age)))
  println(seif.map(x=>x.age))
  println(seif.map(x=>x.age).reduce((x, y)=>x+y))
}
