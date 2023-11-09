//> using test.dep org.scalameta::munit::1.0.0-M10

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = Scala3Example.macroMethod
    println(result)
    assert(result == Scala3Example.normalMethod)
  }
}
