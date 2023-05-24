package com.salad.idlehero.game

object ResourceTypes extends Enumeration {
  type ResourceType = Value

  val Gold, Mana = Value

  def fromString(name: String): ResourceType = {
    name match {
      case "gold" => ResourceTypes.Gold
      case "mana" => ResourceTypes.Mana
      case _ => throw new Exception(s"Unknown resource type: $name")
    }
  }
}
