//> using scala 2.13.12
//> using dep org.scala-lang:scala-reflect:2.13.12
package example

import scala.reflect.macros.blackbox
import scala.language.experimental.macros

class Scala2Example[A] {
  def macroMethod: List[A] =
    macro Scala2Example.macroMethodImpl[A]
}
object Scala2Example {
  def macroMethodImpl[A: c.WeakTypeTag](c: blackbox.Context): c.Expr[List[A]] = {
    import c.universe._
    val A: Type = weakTypeOf[A]
    val ASymbol: Symbol = A.typeSymbol    

    def isSealed(symbol: Symbol): Boolean =
      symbol.isClass && symbol.asClass.isSealed
    
    def isCaseObject(symbol: Symbol): Boolean =
      symbol.isModuleClass && symbol.asClass.isCaseClass

    def caseObjectExpr(classSymbol: ClassSymbol): c.Expr[A] =
      c.Expr[A](q"${classSymbol.module}")

    def isCaseClass(symbol: Symbol): Boolean =
     symbol.isClass && symbol.asClass.isCaseClass &&
       symbol.asClass.primaryConstructor.asMethod.paramLists
         .flatten.forall(_.asTerm.isParamWithDefault)

    val applyDefaultPrefix = "apply$default$"

    def caseClassExpr(classSymbol: ClassSymbol): c.Expr[A] = {
      val ctor = classSymbol.primaryConstructor
      val paramLists = ctor.asMethod.paramLists
      val paramToIdx = paramLists.flatten.zipWithIndex
                         .toMap.view.mapValues(_ + 1).toMap
      
      val companion = classSymbol.companion
      val companionMethods = companion.typeSignature.decls.to(List)
          .map(m => m -> m.name.decodedName.toString)
      val defaults = companionMethods.collect {
        case (method, name) if method.isMethod && name.startsWith(applyDefaultPrefix) =>
          name.drop(applyDefaultPrefix.length).toInt -> q"$companion.$method"
      }.toMap
      
      val args = paramLists.map { paramList =>
        paramList.map { param =>
          defaults(paramToIdx(param))
        }
      }

      c.Expr[A](q"new $classSymbol(...$args)")
    }


    val values =
      if (isSealed(ASymbol))
        ASymbol.asClass.knownDirectSubclasses.toList.flatMap { subtypeSymbol =>
          if (isCaseObject(subtypeSymbol)) List(caseObjectExpr(subtypeSymbol.asClass))
          else if (isCaseClass(subtypeSymbol)) List(caseClassExpr(subtypeSymbol.asClass))
          else List()
        }
      else if (isCaseClass(ASymbol)) List(caseClassExpr(ASymbol.asClass))
      else List()
    
    val result = c.Expr[List[A]](q""" List(..$values) """)
    c.echo(c.enclosingPosition, showCode(result.tree))
    result
  }
}
