package com.salad.idlehero.cli

import com.salad.idlehero.game.{GameLoopManager, ResourceManager, ResourceTypes}

class MineCommand(private val resourceManager: ResourceManager) extends AbstractCliCommand[MineCommandArgs]() {
  override val commandName: String = "/mine"
  override val helpText: String =
    s"""
      | Mine Command:
      | \t- This command will add ${STRENGTH} gold to your reserves.
      |""".stripMargin

  val STRENGTH: Long = 100

  override def parser(): scopt.OptionParser[MineCommandArgs] = {
    new scopt.OptionParser[MineCommandArgs](helpText) {
      head(helpText)
    }
  }

  override def execute(args: Array[String]): Unit = {
    resourceManager.add(ResourceTypes.Gold, STRENGTH)
    println(s"Diggy Diggy Hole. You now have ${resourceManager.get(ResourceTypes.Gold)} gold.")
  }
}

case class MineCommandArgs()
