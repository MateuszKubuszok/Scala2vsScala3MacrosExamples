//> using scala 3.3.1

import scala.quoted.{ Quotes, Expr }

class Scala3Example {
  inline def macroMethod: String =
    ${ Scala3Example.macroMethodImpl }
}
object Scala3Example {
  def macroMethodImpl(using Quotes): Expr[String] =
    '{ "some constant string" }
}
