//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = Scala2Example.macroMethod(1 -> "test")
    println(s"result:\n$result")
    assert(result == """scala.Predef.ArrowAssoc[Int](1).->[String]("test")
                       |scala.Predef.ArrowAssoc[Int](1).->[String]("test")
                       |Apply(TypeApply(Select(Apply(TypeApply(Select(Select(Ident(scala), scala.Predef), TermName("ArrowAssoc")), List(TypeTree())), List(Literal(Constant(1)))), TermName("$minus$greater")), List(TypeTree())), List(Literal(Constant("test"))))
                       |WeakTypeTag[(Int, String)]
                       |(Int, String)
                       |TypeRef(ThisType(scala), scala.Tuple2, List(TypeRef(ThisType(scala), scala.Int, List()), TypeRef(ThisType(java.lang), java.lang.String, List())))""".stripMargin)
  }
}
