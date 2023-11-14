//> using scala 2.13.12
//> using dep org.scala-lang:scala-reflect:2.13.12
package example

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

class Scala2Example[A](val value: A) {
  def macroMethod[B](arg: A => B): B =
    macro Scala2Example.macroMethodImpl[A, B]
}
object Scala2Example {
  def macroMethodImpl[A: c.WeakTypeTag, B: c.WeakTypeTag](
    c: blackbox.Context
  )(arg: c.Expr[A => B]): c.Expr[B] = {
    import c.universe._
    val A2B: Type = weakTypeOf[A => B]
    val A2BSymbol: Symbol = A2B.typeSymbol
    val methods: List[MethodSymbol] = A2B.decls.to(List).filter(_.isMethod).map(_.asMethod)
    c.echo(c.enclosingPosition,
           s"""Type:
              |          
              |A => B = $A2B:
              |  - symbol:                 ${A2BSymbol}
              |  - full name:              ${A2BSymbol.fullName}
              |  - Type/Class/Method/Term: ${A2BSymbol.isType}/${A2BSymbol.isClass}/${A2BSymbol.isMethod}/${A2BSymbol.isTerm}
              |  - declared methods:       ${methods.mkString(", ")}
              |  - typeParams:             ${methods.map(m => s"${m.name} -> ${m.typeParams}").mkString(", ")}
              |  - paramLists (paramss):   ${methods.map(m => s"${m.name} -> ${m.paramLists}").mkString(", ")}
              |  - returnType:             ${methods.map(m => s"${m.name} -> ${m.returnType}").mkString(", ")}
              |""".stripMargin)
    c.Expr(q""" $arg(${c.prefix}.value) """)
  }
}
