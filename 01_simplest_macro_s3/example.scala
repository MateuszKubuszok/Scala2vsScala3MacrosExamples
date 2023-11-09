//> using scala 3.3.1
package example

import scala.quoted.{ Quotes, Expr }

object Scala3Example {
  inline def macroMethod: String =
    ${ Scala3Example.macroMethodImpl }

  def macroMethodImpl(using Quotes): Expr[String] =
    '{ "some constant string" }
}
