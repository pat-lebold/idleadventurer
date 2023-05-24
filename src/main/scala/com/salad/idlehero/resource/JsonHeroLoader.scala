package com.salad.idlehero.resource

import com.salad.idlehero.model.ItemSlots.ItemSlot
import com.salad.idlehero.model.{Hero, Item}

import java.nio.file.{FileSystems, Files}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object JsonHeroLoader extends AbstractJsonResourceLoader {

  def loadStarterHeroes(): Seq[Hero] = loadHeroes("heroes/starters/")

  def loadHeroes(): Seq[Hero] = loadHeroes("heroes/")

  private def loadHeroes(path: String): Seq[Hero] = {
    val heroesTaskFileDir = FileSystems.getDefault.getPath(s"$BASE_PATH$path")
    val heroesTaskFiles = Files.list(heroesTaskFileDir).iterator().asScala.filter(Files.isRegularFile(_)).toSeq

    val heroClasses = JsonHeroClassLoader.loadHeroClasses()
    val rarities = JsonRarityLoader.loadRarities()

    heroesTaskFiles.map { path =>
      val jsonTree = objectMapper.readTree(path.toFile)
      val heroClass = heroClasses(jsonTree.get("class").asText())

      new Hero(
        name = jsonTree.get("name").asText(),
        heroClass = heroClasses(jsonTree.get("class").asText()),
        rarity = rarities(jsonTree.get("rarity").asText()),
        attackSpeed = jsonTree.get("attackSpeed").asInt(),
        attackDamage = jsonTree.get("attackDamage").asLong(),
        magicDamage = jsonTree.get("magicDamage").asLong(),
        critChance = jsonTree.get("critChance").asDouble(),
        items = Map[ItemSlot, Option[Item]](heroClass.itemSlots.map { _ -> None })
      )
    }
  }
}
