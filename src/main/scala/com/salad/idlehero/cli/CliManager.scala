package com.salad.idlehero.cli

import com.salad.idlehero.game.{GameLoopManager, ResourceGrowthTaskManager, ResourceManager}

import scala.collection.mutable

class CliManager(resourceManager: ResourceManager,
                 resourceGrowthTaskManager: ResourceGrowthTaskManager) {

  val cliCommands = mutable.Map[String, AbstractCliCommand[_]]()

  def init(): CliManager = {
    cliCommands.put("/help", new HelpCommand())
    cliCommands.put("/mine", new MineCommand(resourceManager))
    cliCommands.put("/purchase", new PurchaseCommand(resourceGrowthTaskManager))
    cliCommands.put("/stats", new StatsCommand(resourceManager, resourceGrowthTaskManager))
    this
  }

}
