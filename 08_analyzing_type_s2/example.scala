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
    val A: Type = weakTypeOf[A]
    val ASymbol: Symbol = A.typeSymbol
    val B: Type = weakTypeOf[B]
    val BSymbol: Symbol = B.typeSymbol
    c.echo(c.enclosingPosition,
           s"""Types:
              |          
              |A = $A:
              |  - symbol:                 ${ASymbol}
              |  - full name:              ${ASymbol.fullName}
              |  - Type/Class/Module/Term: ${ASymbol.isType}/${ASymbol.isClass}/${ASymbol.isModule}/${ASymbol.isTerm}
              |  - primary constructor:    ${ASymbol.asClass.primaryConstructor}
              |  - declared definitions:   ${A.decls.to(List).mkString(", ")}
              |  - member definitions:     ${A.members.to(List).mkString(", ")}
              |
              |B = $B:
              |  - symbol:                 ${BSymbol}
              |  - full name:              ${BSymbol.fullName}
              |  - Type/Class/Module/Term: ${BSymbol.isType}/${BSymbol.isClass}/${BSymbol.isModule}/${BSymbol.isTerm}
              |  - primary constructor:    ${BSymbol.asClass.primaryConstructor}
              |  - declared definitions:   ${B.decls.to(List).mkString(", ")}
              |  - member definitions:     ${B.members.to(List).mkString(", ")}
              |""".stripMargin)
    c.Expr(q""" $arg(${c.prefix}.value) """)
  }
}
