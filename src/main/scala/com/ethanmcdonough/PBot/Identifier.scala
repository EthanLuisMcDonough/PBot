package com.ethanmcdonough.PBot

class InvalidIdentifierException(message: String) extends Exception(message)

class Identifier(val identifier: String) {
  val kind: IdentifierTypes = IdentifierTypes.values
    .find(identifier matches _.regex.regex)
    .getOrElse(throw new InvalidIdentifierException(""))
  override def toString: String = identifier
  override def equals(o: Any): Boolean = o.isInstanceOf[Identifier] && o.asInstanceOf[Identifier].identifier == identifier
  override def hashCode: Int = identifier.hashCode
}

object Identifier {
  def apply(identifier: String): Identifier = new Identifier(identifier)
}