package com.salad.idlehero.model

import com.salad.idlehero.model.ItemSlots.ItemSlot

import scala.collection.mutable

class Hero(val name: String,
           heroClass: HeroClass,
           rarity: Rarity,
           attackSpeed: Int,
           attackDamage: Long,
           magicDamage: Long,
           critChance: Double,
           val items: mutable.Map[ItemSlot, Option[Item]]) {

  var cooldown = attackSpeed

  def attack(): Option[Attack] = {
    if (cooldown > 0) {
      cooldown -= 1
      return None
    }

    val equippedItems = items.values.toSeq.flatten

    val itemizedAttackDamage = equippedItems.foldLeft(attackDamage) { case (ad, item) => ad + item.attackDamage }
    val finalAttackDamage = equippedItems.foldLeft(itemizedAttackDamage) { case (ad, item) =>
      if (item.elemental) (ad * item.element.get.attackDamageMultiplier).toLong else ad
    }
    val itemizedMagicDamage = equippedItems.foldLeft(magicDamage) { case (md, item) => md + item.magicDamage }
    val finalMagicDamage = equippedItems.foldLeft(itemizedMagicDamage) { case (md, item) =>
      if (item.elemental) (md * item.element.get.magicDamageMultiplier).toLong else md
    }

    val itemizedCritChance = equippedItems.foldLeft(critChance) { case (cc, item) => cc + item.critChance}
    val finalCritChance = equippedItems.foldLeft(itemizedCritChance) { case (cc, item) =>
      if (item.elemental) (cc * item.element.get.critChanceMultiplier).toLong else cc
    }

    val itemizedDropRate = equippedItems.foldLeft(0.0) { case (dr, item) => dr + item.dropRate }
    val finalDropRate = equippedItems.foldLeft(itemizedDropRate) { case (dr, item) =>
      if (item.elemental) (dr * item.element.get.dropChanceMultiplier).toLong else dr
    }

    val itemizedAttackSpeed = Math.max(1, equippedItems.foldLeft(0.0) { case (as, item) => as + item.attackSpeed })
    val finalAttackSpeedMulti = equippedItems.foldLeft(itemizedAttackSpeed) { case (as, item) =>
      if (item.elemental) (as * item.element.get.attackSpeedMultiplier).toLong else as
    }

    cooldown = Math.max(1, attackSpeed.toDouble / finalAttackSpeedMulti).toInt
    Some(Attack(finalAttackDamage, finalMagicDamage, Math.random() <= finalCritChance, finalDropRate ))
  }

  override def toString: String = {
    val sb = new StringBuilder()

    val baseText = s"""
      |Hero:
      |\t- name: $name
      |\t- class: ${heroClass.name}
      |\t- rarity: ${rarity.name}
      |\t- attack speed: $attackSpeed
      |\t- attack damage: $attackDamage
      |\t- magic damage: $magicDamage
      |\t- crit chance: $critChance
      |""".stripMargin

    sb.append(baseText)
    if (items.nonEmpty && !items.values.forall(_.isEmpty)) {
      println("Items Held:")
      items.foreach { case (slot, item) =>
        if (item.isDefined) {
          println(s"${slot.toString}:")
          sb.append(s"${item.get.toString}")
        }
        else ""
      }
    }

    sb.toString()
  }
}
