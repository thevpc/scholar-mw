package net.thevpc.scholar.mentoring.ch01_scala

object T08_ScalaFunctionalCollections extends App{
  class Person(var name:String,var age:Int){
    override def toString =
      s"La Personne($name, $age)"
//      "La Personne("+name+", "+age+")"
//    override def toString = "La Personne("+name+", "+age+")"
  }
//  class Person2{
//    var name:String;
//    var age:Int;
//    Person2(name:String,age:Int){
//      //this.aa=aa;
//    }
//  }
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

  var yasmina=List(
    new Person("alia",15),
    new Person("hammadi",20),
    new Person("aguerbi",17),
    new Person("belleaid",20),
  )
  println(yasmina)
  println(yasmina.sortBy(x=>x.age))
  println(yasmina.filter(x=>x.age>17).sortBy(x=>x.age))
  println(yasmina.forall(x=>x.age>10))
  println(yasmina.forall(x=>x.age>18))
  println(yasmina.exists(x=>x.age>18))
  println(yasmina.exists(x=>x.age>65))
  println(yasmina.groupBy(x=>x.age))
  println(yasmina.flatMap(x=>List(x.name,x.age)))
  println(yasmina.map(x=>x.age))
  println(yasmina.map(x=>x.age).reduce((x, y)=>x+y))
}
