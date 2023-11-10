//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    Scala3Example.macroMethod(1 -> "test")
  }
}
