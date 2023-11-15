//> using test.dep org.scalameta::munit::1.0.0-M10
package example

case class CaseClass(foo: Int = 127, bar: String = "value", baz: Double = 1.0)
sealed trait SealedHierarchy
object SealedHierarchy {
  case class ClassSubtype(foobar: Float = 2.0f) extends SealedHierarchy
  case object ObjectSubtype extends SealedHierarchy
}

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result1 = new Scala2Example[CaseClass].macroMethod
    assert(result1 == List(CaseClass()))
    val result2 = new Scala2Example[SealedHierarchy].macroMethod
    assert(result2 == List(SealedHierarchy.ClassSubtype(), SealedHierarchy.ObjectSubtype))
  }
}
