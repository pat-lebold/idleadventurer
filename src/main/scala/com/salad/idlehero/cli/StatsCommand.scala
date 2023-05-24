package com.salad.idlehero.cli

import com.salad.idlehero.game.{GameLoopManager, ResourceGrowthTaskManager, ResourceManager}
import scopt.OptionParser

class StatsCommand(private val resourceManager: ResourceManager,
                   private val resourceGrowthTaskManager: ResourceGrowthTaskManager) extends AbstractCliCommand[StatsCommandArgs]() {
  override val commandName: String = "/stats"
  override val helpText: String =
    """
      | Stats Command:
      | \t- This command will show you your stats!
      |""".stripMargin

  override def parser(): OptionParser[StatsCommandArgs] = {
    new scopt.OptionParser[StatsCommandArgs](helpText) {
      head(helpText)
    }
  }

  override def execute(args: Array[String]): Unit = {
    println(resourceManager.toString)
    println(resourceGrowthTaskManager.toString)
  }
}

case class StatsCommandArgs()