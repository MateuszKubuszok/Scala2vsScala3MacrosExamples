//> using test.dep org.scalameta::munit::1.0.0-M10

class Tests extends munit.FunSuite {
  test("macro works OK") {
    case class Foo(foo: Int)
    case class Bar(bar: String)
    val result = new Scala3Example(Foo(10)).macroMethod(foo => Bar(foo.toString))
    assert(result == Bar("Foo(10)"))
  }
}
