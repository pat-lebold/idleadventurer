package com.salad.idlehero.resource

import com.salad.idlehero.model.Rarity

import java.nio.file.FileSystems
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

object JsonRarityLoader extends AbstractJsonResourceLoader {

  def loadRarities(): Map[String, Rarity] = {
    val rarityPath = FileSystems.getDefault.getPath(s"${BASE_PATH}rarity.json")

    val jsonTree = objectMapper.readTree(rarityPath.toFile)
    jsonTree.map { jsonNode =>
      val name = jsonNode.get("name").asText()
      val rarity = Rarity(
        name = name,
        chance = jsonNode.get("chance").asDouble(),
        attackDamageMultiplier = jsonNode.get("attackDamageMultiplier").asDouble(),
        magicDamageMultiplier = jsonNode.get("magicDamageMultiplier").asDouble(),
        critChanceMultiplier = jsonNode.get("critChanceMultiplier").asDouble(),
        healthMultiplier = jsonNode.get("healthMultiplier").asDouble(),
        armorMultiplier = jsonNode.get("armorMultiplier").asDouble(),
        magicResistMultiplier = jsonNode.get("magicResistMultiplier").asDouble(),
        dodgeChanceMultiplier = jsonNode.get("dodgeChanceMultiplier").asDouble(),
        dropRateMultiplier = jsonNode.get("dropRateMultiplier").asDouble()
      )
      name -> rarity
    }.toMap
  }

}
