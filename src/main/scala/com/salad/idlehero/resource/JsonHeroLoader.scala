package com.salad.idlehero.resource

import com.salad.idlehero.model.ItemSlots.ItemSlot
import com.salad.idlehero.model.{Hero, Item}

import java.nio.file.{FileSystems, Files}
import scala.collection.mutable
import scala.jdk.CollectionConverters.IteratorHasAsScala

object JsonHeroLoader extends AbstractJsonResourceLoader {

  def loadStarterHeroes(): Map[String, Hero] = loadHeroes("heroes/starters/")

  def loadHeroes(): Map[String, Hero] = loadHeroes("heroes/")

  private def loadHeroes(path: String): Map[String, Hero] = {
    val heroesTaskFileDir = FileSystems.getDefault.getPath(s"$BASE_PATH$path")
    val heroesTaskFiles = Files.list(heroesTaskFileDir).iterator().asScala.filter(Files.isRegularFile(_)).toSeq

    val heroClasses = JsonHeroClassLoader.loadHeroClasses()
    val rarities = JsonRarityLoader.loadRarities()

    heroesTaskFiles.map { path =>
      val jsonTree = objectMapper.readTree(path.toFile)
      val name = jsonTree.get("name").asText()
      val heroClass = heroClasses(jsonTree.get("class").asText())

      val mutableItems: mutable.Map[ItemSlot, Option[Item]] = mutable.Map()
      heroClass.itemSlots.foreach { itemSlot =>
        mutableItems.put(itemSlot, None)
      }

      val hero = new Hero(
        name = jsonTree.get("name").asText(),
        heroClass = heroClass,
        rarity = rarities(jsonTree.get("rarity").asText()),
        attackSpeed = jsonTree.get("attackSpeed").asInt(),
        attackDamage = jsonTree.get("attackDamage").asLong(),
        magicDamage = jsonTree.get("magicDamage").asLong(),
        critChance = jsonTree.get("critChance").asDouble(),
        items = collection.mutable.Map()
      )
      name -> hero
    }.toMap
  }
}
