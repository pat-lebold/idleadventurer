package com.salad.idlehero.resource

import com.salad.idlehero.model.{Campaign, Rarity, Stage}

import java.nio.file.FileSystems
import scala.collection.convert.ImplicitConversions.`iterable AsScalaIterable`

object JsonCampaignLoader extends AbstractJsonResourceLoader {

  def loadCampaigns(): Seq[Campaign] = {
    val campaignPath = FileSystems.getDefault.getPath(s"${BASE_PATH}campaigns/campaigns.json")

    val elements = JsonElementsLoader.loadElements()
    val enemies = JsonEnemyLoader.loadEnemies()
    val rarities = JsonRarityLoader.loadRarities()

    val jsonTree = objectMapper.readTree(campaignPath.toFile)
    jsonTree.map { jsonNode =>
      val name = jsonNode.get("name").asText()

      val elementDistribution = jsonNode.get("elementDistribution").map { elementNode =>
        elements(elementNode.get("name").asText()) -> elementNode.get("frequency").asDouble()
      }.toMap

      val enemyDistribution = jsonNode.get("enemyDistribution").map { enemyNode =>
        enemies(enemyNode.get("name").asText()) -> enemyNode.get("frequency").asDouble()
      }.toMap

      val stages = jsonNode.get("stages").map { stageNode =>
        val rarityDistribution = stageNode.get("rarityDistribution").map { rarityNode =>
          rarities(rarityNode.get("name").asText()) -> rarityNode.get("frequency").asDouble()
        }.toMap
        Stage(stageNode.get("numEnemies").asInt(), rarityDistribution)
      }.toSeq

      Campaign(
        name = name,
        enemyDistribution = enemyDistribution,
        elementDistribution = elementDistribution,
        stages = stages
      )
    }.toSeq
  }
}
