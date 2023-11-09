//> using scala 2.13.12
//> using dep org.scala-lang:scala-reflect:2.13.12
package example

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

object Scala2Example {
  def macroMethod: String =
    macro Scala2Example.macroMethodImpl

  def macroMethodImpl(c: blackbox.Context): c.Expr[String] = {
  	import c.universe._
    c.Expr(q""" "some constant string" """)
  }
}
