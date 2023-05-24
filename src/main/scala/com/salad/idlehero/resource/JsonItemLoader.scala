package com.salad.idlehero.resource

import com.fasterxml.jackson.databind.JsonNode
import com.salad.idlehero.model.{Item, ItemSlots}

import java.nio.file.{FileSystems, Files}
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`
import scala.jdk.CollectionConverters.IteratorHasAsScala

object JsonItemLoader extends AbstractJsonResourceLoader {

  def loadItems(): Map[String, Item] = {
    val itemsFileDir = FileSystems.getDefault.getPath(s"${BASE_PATH}items/")
    val itemFiles = Files.list(itemsFileDir).iterator().asScala.filter(Files.isRegularFile(_)).toSeq

    itemFiles.map { path =>
      val jsonTree = objectMapper.readTree(path.toFile)
      val name = jsonTree.get("name").asText()
      val itemSlotNode = jsonTree.get("itemSlot")

      val item = Item(
        name = name,
        sellValue = jsonTree.get("sellValue").asLong(),
        buyValue = jsonTree.get("buyValue").asLong(),
        elemental = jsonTree.get("elemental").asBoolean(),
        element = None,
        components = jsonTree.get("components").map { _.asText() }.toSeq,
        itemSlot = if (itemSlotNode == null) None else Some(ItemSlots.fromString(itemSlotNode.asText())),
        attackDamage = if(jsonTree.has("attackDamage")) jsonTree.get("attackDamage").asLong() else 0,
        magicDamage = if(jsonTree.has("magicDamage")) jsonTree.get("magicDamage").asLong() else 0,
        attackSpeed = if(jsonTree.has("attackSpeed")) jsonTree.get("attackSpeed").asLong() else 0,
        critChance = if(jsonTree.has("critChance")) jsonTree.get("critChance").asDouble() else 0,
        dropRate = if(jsonTree.has("dropRate")) jsonTree.get("dropRate").asDouble() else 0
      )
      name -> item
    }.toMap
  }
}
