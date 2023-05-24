package com.salad.idlehero.resource

import com.salad.idlehero.model.{HeroClass, ItemSlots}

import java.nio.file.FileSystems
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

object JsonHeroClassLoader extends AbstractJsonResourceLoader {

  def loadHeroClasses(): Map[String, HeroClass] = {
    val heroClassPath = FileSystems.getDefault.getPath(s"${BASE_PATH}heroclasses.json")

    val jsonTree = objectMapper.readTree(heroClassPath.toFile)
    jsonTree.map { jsonNode =>
      val name = jsonNode.get("name").asText()
      val heroClass = HeroClass(
        name = name,
        itemSlots = jsonNode.get("itemSlots").map { itemNode => ItemSlots.fromString(itemNode.asText()) }.toSeq
      )
      name -> heroClass
    }.toMap
  }
}
