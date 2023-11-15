//> using scala 3.3.1

import scala.quoted.{ Quotes, Expr, Type }

class Scala3Example[A] {
  inline def macroMethod: List[A] =
    ${ Scala3Example.macroMethodImpl[A] }
}
object Scala3Example {
  def macroMethodImpl[A: Type](using quotes: Quotes): Expr[List[A]] =
    import quotes.*, quotes.reflect.*
    val A: TypeRepr = TypeRepr.of[A]
    val ASymbol: Symbol = A.typeSymbol

    def isSealed(symbol: Symbol): Boolean =
      val flags = symbol.flags
      flags.is(Flags.Enum) || flags.is(Flags.Sealed)
    
    def isEnumWithoutParams(symbol: Symbol): Boolean =
      symbol.flags.is(Flags.Case | Flags.Enum | Flags.JavaStatic)

    def enumWithoutParamsExpr(symbol: Symbol): Expr[A] =
      Ref(symbol).asExprOf[A]

    def isCaseClass(symbol: Symbol): Boolean =
      symbol.isClassDef && symbol.flags.is(Flags.Case) &&
        !symbol.flags.is(Flags.Abstract) && !symbol.flags.is(Flags.Trait)
     
                           // <init>$default$ 
    val initDefaultPrefix = "$lessinit$greater$default$"

    def caseClassExpr(symbol: Symbol): Expr[A] = {
      val ctor = symbol.primaryConstructor
      val paramLists = ctor.paramSymss
      val paramToIdx = paramLists.flatten.zipWithIndex
                         .toMap.view.mapValues(_ + 1).toMap
      
      val companion = symbol.companionModule
      val companionMethods = companion.declaredMethods
          .map(m => m -> m.name)
      val defaults = companionMethods.collect {
        case (method, name) if method.isDefDef && name.startsWith(initDefaultPrefix) =>
          name.drop(initDefaultPrefix.length).toInt -> Ref(companion).select(method)
      }.toMap
      
      val args = paramLists.map { paramList =>
        paramList.map { param =>
          defaults(paramToIdx(param))
        }
      }

      New(TypeTree.ref(symbol)).select(ctor).appliedToArgss(args).asExprOf[A]
    }


    val values =
      if (isSealed(ASymbol))
        ASymbol.children.flatMap { subtypeSymbol =>
          if (isEnumWithoutParams(subtypeSymbol)) List(enumWithoutParamsExpr(subtypeSymbol))
          else if (isCaseClass(subtypeSymbol)) List(caseClassExpr(subtypeSymbol))
          else List()
        }
      else if (isCaseClass(ASymbol)) List(caseClassExpr(ASymbol))
      else List()

    val result = '{ List[A](${ quoted.Varargs(values.toSeq) }*) }
    report.info(result.asTerm.show(using Printer.TreeAnsiCode),
                Position.ofMacroExpansion)
    result
}
