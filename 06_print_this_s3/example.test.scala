//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = Scala3Example.macroMethod
    println(s"result:\n$result")
    assert(result == """this
                       |this
                       |Inlined(None, Nil, This(None))
                       |example.Scala3Example""".stripMargin)
  }
}
