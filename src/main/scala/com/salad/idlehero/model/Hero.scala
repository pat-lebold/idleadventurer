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
           items: mutable.Map[ItemSlot, Option[Item]]) {

  var cooldown = 0

  def attack(): Option[Attack] = {
    if (cooldown != 0) {
      cooldown -= 1
      return None
    }

    val equippedItems = items.values.toSeq.flatten

    val finalAttackDamage = equippedItems.foldLeft(attackDamage) { case (ad, item) => ad + item.attackDamage }
    val finalMagicDamage = equippedItems.foldLeft(magicDamage) { case (md, item) => md + item.magicDamage }
    val finalCritChance = equippedItems.foldLeft(critChance) { case (cc, item) => cc + item.critChance}
    val finalDropRateMulti = equippedItems.foldLeft(0.0) { case (dr, item) => dr + item.dropRate }

    val attackSpeedMulti = equippedItems.foldLeft(0.0) { case (as, item) => as + item.attackSpeed }

    cooldown = Math.max(1, attackSpeed.toDouble / attackSpeedMulti).toInt
    Some(Attack(finalAttackDamage, finalMagicDamage, finalCritChance <= Math.random(), finalDropRateMulti ))
  }

  override def toString: String = {
    s"""
      |Hero:
      |\t- class: ${heroClass.name}
      |\t- rarity: ${rarity.name}
      |\t- attack speed: $attackSpeed
      |\t- attack damage: $attackDamage
      |\t- magic damage: $magicDamage
      |\t- crit chance: $critChance
      |""".stripMargin
  }
}
