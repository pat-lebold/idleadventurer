package com.salad.idlehero.game

import com.salad.idlehero.model.{Element, Enemy, EnemyClass, ItemStack, Rarity}

import scala.util.Random

class EnemyGenerator(enemyClassPool: Map[EnemyClass, Double],
                     elementPool: Map[Element, Double],
                     rarityPool: Map[Rarity, Double]) {

  private val enemies = explodeOptionPool(enemyClassPool)
  private val elements = explodeOptionPool(elementPool)
  private val rarities = explodeOptionPool(rarityPool)

  def generateEnemy(): Enemy = {
    val enemyClass = enemies(Random.nextInt(enemies.size))
    val element = if (enemyClass.elemental) elements(Random.nextInt(elements.size)) else Element(name = "no-element",1,1,1,1,1,1,1,1,1)
    val rarity = rarities(Random.nextInt(rarities.size))

    val dropRatePercentage = enemyClass.dropRate * element.dropChanceMultiplier * rarity.dropRateMultiplier
    val willDrop = Math.random() <= dropRatePercentage

    val drop = if (willDrop) {
      val drops = explodeOptionPool(enemyClass.drops)
      val drop = drops(Random.nextInt(drops.size))
      if (enemyClass.elemental && drop.item.elemental)
        Some (new ItemStack(drop.item.clone(Some(element)), drop.quantity))
      else
        Some (new ItemStack(drop.item.clone(None), drop.quantity) )
    } else None


    Enemy(
      name = enemyClass.name,
      rarity = rarity,
      element = if (enemyClass.elemental) Some(element) else None,
      health = (enemyClass.health * element.healthMultiplier * rarity.healthMultiplier).toLong,
      armor = (enemyClass.armor * element.armorMultiplier * rarity.armorMultiplier).toLong,
      magicResist = (enemyClass.magicResist * element.magicResistMultiplier * rarity.magicResistMultiplier).toLong,
      dodgeChance = (enemyClass.dodgeChance * element.dodgeChanceMultiplier * rarity.dodgeChanceMultiplier),
      drop = drop
    )
  }

  private def explodeOptionPool[T](pool: Map[T, Double]) = pool.flatMap { option => (0 until (option._2 * 100).toInt).map { num => option._1 } }.toSeq
}
