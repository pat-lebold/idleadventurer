package com.salad.idlehero.game

import com.salad.idlehero.model.{Element, Enemy, EnemyClass, Rarity}

import scala.util.Random

class EnemyGenerator(enemyClassPool: Map[EnemyClass, Double],
                     elementPool: Map[Element, Double],
                     rarityPool: Map[Rarity, Double]) {

  private val enemies = explodeOptionPool(enemyClassPool)
  private val elements = explodeOptionPool(elementPool)
  private val rarities = explodeOptionPool(rarityPool)

  def generateEnemy(): Enemy = {
    val enemyClass = enemies(Random.nextInt(enemies.size))
    val element = elements(Random.nextInt(elements.size))
    val rarity = rarities(Random.nextInt(rarities.size))

    val willDrop = (enemyClass.dropRate * element.dropChanceMultiplier * rarity.dropRateMultiplier) <= Math.random()
    val drop = if (willDrop) {
      val drops = explodeOptionPool(enemyClass.drops)
      Some(drops(Random.nextInt(drops.size)))
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
