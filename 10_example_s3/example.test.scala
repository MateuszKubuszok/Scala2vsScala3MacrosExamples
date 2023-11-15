//> using test.dep org.scalameta::munit::1.0.0-M10

case class CaseClass(foo: Int = 127, bar: String = "value", baz: Double = 1.0)
enum SealedHierarchy:
  case ClassSubtype(foobar: Float = 2.0f)
  case ObjectSubtype

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result1 = new Scala3Example[CaseClass].macroMethod
    assert(result1 == List(CaseClass()))
    val result2 = new Scala3Example[SealedHierarchy].macroMethod
    assert(result2 == List(SealedHierarchy.ClassSubtype(), SealedHierarchy.ObjectSubtype))
  }
}
