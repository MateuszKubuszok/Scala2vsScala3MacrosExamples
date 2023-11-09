//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = new Scala2Example().macroMethod
    println(s"result: $result")
    assert(result == "some constant string")
  }
}
