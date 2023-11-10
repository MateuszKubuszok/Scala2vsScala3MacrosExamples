//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = Scala2Example.macroMethod
    println(s"result:\n$result")
    assert(result == """Scala2Example
                       |Scala2Example
                       |Ident(example.Scala2Example)
                       |Nothing""".stripMargin)
  }
}
