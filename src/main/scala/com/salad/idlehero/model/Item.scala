package com.salad.idlehero.model

import com.salad.idlehero.model.ItemSlots.ItemSlot

case class Item(name: String,
                sellable: Boolean,
                purchasable: Boolean,
                sellValue: Long,
                buyValue: Long,
                elemental: Boolean,
                element: Option[Element],
                components: Map[String, Int],
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

  def clone(elementOption: Option[Element]): Item = {
    Item(name, sellable, purchasable, sellValue, buyValue, elemental, elementOption, components, itemSlot, attackDamage, magicDamage, attackSpeed, critChance, dropRate)
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
