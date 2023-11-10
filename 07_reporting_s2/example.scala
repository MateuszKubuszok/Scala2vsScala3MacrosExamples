//> using scala 2.13.12
//> using dep org.scala-lang:scala-reflect:2.13.12
package example

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

object Scala2Example {
  def macroMethod[A](a: A): String =
    macro Scala2Example.macroMethodImpl[A]

  def macroMethodImpl[A: c.WeakTypeTag](
    c: blackbox.Context
  )(a: c.Expr[A]): c.Expr[String] = {
    import c.universe._
    val output = s"""${show(a.tree)}
                    |${showCode(a.tree)}
                    |${showRaw(a.tree)}
                    |${weakTypeTag[A]}
                    |${weakTypeOf[A]}
                    |${showRaw(weakTypeOf[A])}""".stripMargin
    c.echo(c.enclosingPosition, output)
    c.warning(c.enclosingPosition, "example warning message")
    c.abort(c.enclosingPosition, "example error message")
  }
}
