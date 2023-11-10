//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    println(Scala2Example)
    val result = Scala2Example.macroMethod
    println(s"result: $result")
    assert(result == Scala2Example.normalMethod)
  }
}
