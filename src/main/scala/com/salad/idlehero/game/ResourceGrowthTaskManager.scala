package com.salad.idlehero.game

import com.salad.idlehero.Constants
import com.salad.idlehero.tasks.ResourceGrowthTask

import scala.math.floor

class ResourceGrowthTaskManager(resourceGrowthTasks: Map[String, ResourceGrowthTask],
                                resourceManager: ResourceManager) {

  val STRENGTH_INCREASE_RATE: Double = 2
  val LEVEL_INCREASE_COST_RATE: Double = 5

  def executeAllTasks(): Unit = {
    resourceGrowthTasks.values.foreach { resourceGrowthTask =>
      resourceManager.add(resourceGrowthTask.resourceType, resourceGrowthTask.collect())
    }
  }

  def hasTask(taskName: String): Boolean = resourceGrowthTasks.contains(taskName)

  def listAllTasks(): Seq[ResourceGrowthTask] = resourceGrowthTasks.values.toSeq

  def getTaskLevel(taskName: String): Int = resourceGrowthTasks(taskName).level

  def levelUpTask(taskName: String): Boolean = {
    val task = resourceGrowthTasks(taskName)

    // Ensure Funds Exist
    if (!resourceManager.has(task.levelUpResourceType, task.levelUpCost)) {
      val difference = task.levelUpCost - resourceManager.get(task.levelUpResourceType)
      println(s"You need $difference more ${task.levelUpResourceType} to purchase this item.")
      return false
    }

    // Charge Player for Level Up
    resourceManager.subtract(task.levelUpResourceType, task.levelUpCost)

    // Level Up
    if (!task.active)
      task.active = true
    else {
      task.level += 1
      task.strength *= STRENGTH_INCREASE_RATE
    }

    // Increase Cost for Next Level Up
    task.levelUpCost = floor(task.levelUpCost * LEVEL_INCREASE_COST_RATE).toLong

    true
  }

  override def toString: String = {
    resourceGrowthTasks.values
      .filter { growthTask => growthTask.active }
      .map { growthTask =>
        s"\t- ${growthTask.name}\n\t\t- Level: ${growthTask.level}\n\t\t- Strength: ${(growthTask.strength * Constants.TICK_SPEED).toLong}\n\t\t- Cost For Next Level: ${growthTask.levelUpCost}"
      }.mkString("Buildings:\n", "\n", "")
  }
}
