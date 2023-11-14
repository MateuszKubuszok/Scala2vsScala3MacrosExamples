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
    val A2B: TypeRepr = TypeRepr.of[A]
    val A2BSymbol: Symbol = A2B.typeSymbol
    val methods: List[Symbol] = A2BSymbol.declaredMethods
    def asLambda: Symbol => Option[LambdaType] = m => A2B.memberType(m) match
      case l: LambdaType => Some(l)
      case _             => None
    report.info(s"""Types:
                   |          
                   |A => B = $A2B:
                   |  - symbol:                 ${A2BSymbol}
                   |  - full name:              ${A2BSymbol.fullName}
                   |  - Type/Class/Method/Term: ${A2BSymbol.isType}/${A2BSymbol.isClassDef}/${A2BSymbol.isDefDef}/${A2BSymbol.isTerm}
                   |  - declared methods:       ${methods.mkString(", ")}
                   |  - paramSymss:             ${methods.map(m => s"${m.name} -> ${m.paramSymss}").mkString(", ")}
                   |  - return type:            ${methods.map(m => s"${m.name} -> ${asLambda(m).map(_.resType)}").mkString(", ")}
                   |""".stripMargin,
                Position.ofMacroExpansion)
    '{ ${ fun }(${ example }.value) }
}
