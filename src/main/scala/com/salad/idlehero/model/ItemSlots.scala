package com.salad.idlehero.model

object ItemSlots extends Enumeration {
  type ItemSlot = Value

  val Hand, Feet, Head = Value

  def fromString(name: String): ItemSlot = {
    name.toLowerCase() match {
      case "hand" => ItemSlots.Hand
      case "feet" => ItemSlots.Feet
      case "head" => ItemSlots.Head
      case _ => throw new Exception(s"Unknown item slot: $name")
    }
  }
}
