//> using test.dep org.scalameta::munit::1.0.0-M10
package example

class Tests extends munit.FunSuite {
  test("macro works OK") {
    val result = Scala3Example.macroMethod(1 -> "test")
    println(s"result:\n$result")
    import Console.*
    assert(result == s"""a$$proxy1
                        |${MAGENTA}a$$proxy1${RESET}
                        |Inlined(None, Nil, Ident("a$$proxy1"))
                        |scala.Tuple2[scala.Int, java.lang.String]
                        |${MAGENTA}scala${RESET}.${MAGENTA}Tuple2${RESET}[${MAGENTA}scala${RESET}.${MAGENTA}Int${RESET}, java.${MAGENTA}lang${RESET}.${MAGENTA}String${RESET}]
                        |AppliedType(TypeRef(ThisType(TypeRef(NoPrefix(), "scala")), "Tuple2"), List(TypeRef(ThisType(TypeRef(NoPrefix(), "scala")), "Int"), TypeRef(ThisType(TypeRef(NoPrefix(), "lang")), "String")))""".stripMargin)
  }
}
