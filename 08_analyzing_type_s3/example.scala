//> using scala 3.3.1

import scala.quoted.{ Quotes, Expr, Type }

class Scala3Example[A](val value: A) {
  inline def macroMethod[B](arg: A => B): B =
    ${ Scala3Example.macroMethodImpl('{ this }, '{ arg }) }
}
object Scala3Example {
  def macroMethodImpl[A: Type, B: Type](
    using quotes: Quotes
  )(example: Expr[Scala3Example[A]], fun: Expr[A => B]): Expr[B] =
    import quotes.*, quotes.reflect.*
    val A: TypeRepr = TypeRepr.of[A]
    val ASymbol: Symbol = A.typeSymbol
    val B: TypeRepr = TypeRepr.of[A]
    val BSymbol: Symbol = A.typeSymbol
    report.info(s"""Types:
                   |          
                   |A = $A:
                   |  - symbol:                 ${ASymbol}
                   |  - full name:              ${ASymbol.fullName}
                   |  - Type/Class/Term:        ${ASymbol.isType}/${ASymbol.isClassDef}/${ASymbol.isTerm}
                   |  - primary constructor:    ${ASymbol.primaryConstructor}
                   |  - declared fields:        ${ASymbol.declaredFields.mkString(", ")}
                   |  - declared methods:       ${ASymbol.declaredMethods.mkString(", ")}
                   |  - field methods:          ${ASymbol.fieldMembers.mkString(", ")}
                   |  - member methods:         ${ASymbol.methodMembers.mkString(", ")}
                   |
                   |B = $B:
                   |  - symbol:                 ${BSymbol}
                   |  - full name:              ${BSymbol.fullName}
                   |  - Type/Class/Term:        ${BSymbol.isType}/${BSymbol.isClassDef}/${BSymbol.isTerm}
                   |  - primary constructor:    ${BSymbol.primaryConstructor}
                   |  - declared fields:        ${BSymbol.declaredFields.mkString(", ")}
                   |  - declared methods:       ${BSymbol.declaredMethods.mkString(", ")}
                   |  - field methods:          ${BSymbol.fieldMembers.mkString(", ")}
                   |  - member methods:         ${BSymbol.methodMembers.mkString(", ")}
                   |""".stripMargin,
                Position.ofMacroExpansion)
    '{ ${ fun }(${ example }.value) }
}
