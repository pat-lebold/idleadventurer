package com.salad.idlehero.resource

import com.salad.idlehero.model.Element
import com.salad.idlehero.resource.JsonRarityLoader.BASE_PATH

import java.nio.file.FileSystems
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

object JsonElementsLoader extends AbstractJsonResourceLoader {

  def loadElements(): Map[String, Element] = {
    val elementsPath = FileSystems.getDefault.getPath(s"${BASE_PATH}elements.json")

    val jsonTree = objectMapper.readTree(elementsPath.toFile)
    jsonTree.map { jsonNode =>
      val name = jsonNode.get("name").asText()
      val element = Element(
        name = name,
        healthMultiplier = jsonNode.get("healthMultiplier").asDouble(),
        armorMultiplier = jsonNode.get("armorMultiplier").asDouble(),
        magicResistMultiplier = jsonNode.get("magicResistMultiplier").asDouble(),
        dodgeChanceMultiplier = jsonNode.get("dodgeChanceMultiplier").asDouble(),
        attackDamageMultiplier = jsonNode.get("attackDamageMultiplier").asDouble(),
        magicDamageMultiplier = jsonNode.get("magicDamageMultiplier").asDouble(),
        dropChanceMultiplier = jsonNode.get("dropChanceMultiplier").asDouble(),
        attackSpeedMultiplier = jsonNode.get("attackSpeedMultiplier").asDouble(),
        critChanceMultiplier = jsonNode.get("critChanceMultiplier").asDouble()
      )
      name -> element
    }.toMap
  }
}
