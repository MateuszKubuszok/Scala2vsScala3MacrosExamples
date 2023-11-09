//> using scala 2.13.12
//> using dep org.scala-lang:scala-reflect:2.13.12

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

class Scala2Example {
  def macroMethod: String =
    macro Scala2Example.macroMethodImpl
}
object Scala2Example {
  def macroMethodImpl(c: blackbox.Context): c.Expr[String] = {
  	import c.universe._
    c.Expr(q""" "some constant string" """)
  }
}
