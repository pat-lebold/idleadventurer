package com.salad.idlehero.model

import com.salad.idlehero.model.ItemSlots.ItemSlot

case class Item(name: String,
                sellValue: Long,
                buyValue: Long,
                elemental: Boolean,
                element: Option[Element],
                components: Seq[String],
                itemSlot: Option[ItemSlot],
                attackDamage: Long = 0,
                magicDamage: Long = 0,
                attackSpeed: Long = 0,
                critChance: Double = 0,
                dropRate: Double = 0) {

  def id(): String = {
    val sb = new StringBuilder()
    sb.append(name)
    if (elemental) {
      sb.append(" - ")
      sb.append(s"element(${element.get.name})")
    }

    sb.toString()
  }

  override def toString: String = {
    s"""
      | Item:
      | \t- name: $name
      | \t- sell value: $sellValue
      | \t- buy value: $buyValue
      | \t- elemental: $elemental
      | \t- itemSlot: ${if (itemSlot.isDefined) itemSlot else None}
      |""".stripMargin
  }
}
