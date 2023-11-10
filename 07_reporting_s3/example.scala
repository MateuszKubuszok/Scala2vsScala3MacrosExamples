//> using scala 3.3.1
package example

import scala.quoted.{ Quotes, Expr, Type }

object Scala3Example {
  inline def macroMethod[A](a: A): String =
    ${ Scala3Example.macroMethodImpl[A]('{ a }) }

  def macroMethodImpl[A: Type](
    expr: Expr[A]
  )(using quotes: Quotes): Expr[String] =
    import quotes.*, quotes.reflect.*
    report.info(
      s"""${expr.asTerm.show}
         |${expr.asTerm.show(using Printer.TreeAnsiCode)}
         |${expr.asTerm.show(using Printer.TreeStructure)}
         |${TypeRepr.of[A].show}
         |${TypeRepr.of[A].show(using Printer.TypeReprAnsiCode)}
         |${TypeRepr.of[A].show(using Printer.TypeReprStructure)}""".stripMargin,
      Position.ofMacroExpansion
    )
    report.warning("example warning")
    report.errorAndAbort("example error message", Position.ofMacroExpansion)
}
