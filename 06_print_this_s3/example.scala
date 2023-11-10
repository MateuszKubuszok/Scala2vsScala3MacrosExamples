//> using scala 3.3.1
package example

import scala.quoted.{ Quotes, Expr, Type }

object Scala3Example {
  inline def macroMethod: String =
    ${ Scala3Example.macroMethodImpl('{ this }) }

  def macroMethodImpl[A: Type](
    expr: Expr[Scala3Example.type]
  )(using quotes: Quotes): Expr[String] =
    import quotes.*, quotes.reflect.*
    Expr(
      s"""${expr.asTerm.show(using Printer.TreeCode)}
         |${expr.asTerm.show(using Printer.TreeAnsiCode)}
         |${expr.asTerm.show(using Printer.TreeStructure)}
         |${TypeRepr.of[Scala3Example.type].show}""".stripMargin
    )
}
