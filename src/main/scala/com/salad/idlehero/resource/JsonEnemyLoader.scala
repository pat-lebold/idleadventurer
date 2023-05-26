package com.salad.idlehero.resource

import com.salad.idlehero.model.{EnemyClass, ItemStack}

import java.nio.file.{FileSystems, Files}
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.jdk.CollectionConverters.IteratorHasAsScala

object JsonEnemyLoader extends AbstractJsonResourceLoader {

  def loadEnemies(): Map[String, EnemyClass] = {
    val enemiesFilesDir = FileSystems.getDefault.getPath(s"${BASE_PATH}enemies/")
    val enemyFiles = Files.list(enemiesFilesDir).iterator().asScala.filter(Files.isRegularFile(_)).toSeq

    val items = JsonItemLoader.loadItems()

    enemyFiles.map { path =>
      val jsonTree = objectMapper.readTree(path.toFile)
      val name = jsonTree.get("name").asText()

      val dropsNode = jsonTree.get("drops")
      val drops = dropsNode.map { dropItem =>
        val name = dropItem.get("name").asText()
        val chance = dropItem.get("chance").asDouble()
        val quantity = dropItem.get("quantity").asLong()
        val itemStack = new ItemStack(items(name), quantity)
        itemStack -> chance
      }.toMap

      val enemy = EnemyClass(
        name = name,
        elemental = jsonTree.get("elemental").asBoolean(),
        health = jsonTree.get("health").asLong(),
        armor = jsonTree.get("armor").asLong(),
        magicResist = jsonTree.get("magicResist").asLong(),
        dodgeChance = jsonTree.get("dodgeChance").asLong(),
        dropRate = jsonTree.get("dropRate").asDouble(),
        drops = drops
      )
      name -> enemy
    }.toMap
  }
}
