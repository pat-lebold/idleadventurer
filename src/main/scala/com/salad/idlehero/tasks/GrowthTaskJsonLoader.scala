package com.salad.idlehero.tasks


import com.fasterxml.jackson.databind.ObjectMapper
import com.salad.idlehero.Constants
import com.salad.idlehero.game.ResourceTypes

import java.nio.file.{FileSystems, Files}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object GrowthTaskJsonLoader {

  def loadGrowthTasks(): Seq[ResourceGrowthTask] = {
    val growthTaskFileDir = FileSystems.getDefault.getPath("src/main/resources/com/salad/idlehero/tasks")
    val growthTaskFiles = Files.list(growthTaskFileDir).iterator().asScala.filter(Files.isRegularFile(_)).toSeq

    val jsonMapper = new ObjectMapper()
    growthTaskFiles.map { path =>
      val jsonTree = jsonMapper.readTree(path.toFile)
      new ResourceGrowthTask(
        name = jsonTree.get("name").asText(),
        resourceType = ResourceTypes.fromString(jsonTree.get("resourceType").asText()),
        strength = jsonTree.get("strength").asDouble() / Constants.TICK_SPEED,
        levelUpCost = jsonTree.get("levelUpCost").asLong(),
        levelUpResourceType = ResourceTypes.fromString(jsonTree.get("levelUpResourceType").asText())
      )
    }
  }
}
