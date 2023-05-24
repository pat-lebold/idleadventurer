package com.salad.idlehero.cli

import com.salad.idlehero.game.{GameLoopManager, ResourceGrowthTaskManager}
import scopt.OptionParser

class PurchaseCommand(resourceGrowthTaskManager: ResourceGrowthTaskManager) extends AbstractCliCommand[PurchaseCommandArgs]() {
  override val commandName: String = "/purchase"
  override val helpText: String =
    s"""
      | Purchase Command:
      |
      | /purchase --item <item>
      |
      | \t- Items available for purchase:
      | ${resourceGrowthTaskManager.listAllTasks().map { it => s"\t\t- ${it.name}"}.mkString("","\n","")}
      |""".stripMargin


  override def parser(): OptionParser[PurchaseCommandArgs] = {
    new OptionParser[PurchaseCommandArgs](helpText) {
      head(helpText)

      opt[String]("item").optional().action { (x, c) =>
        c.copy(item = x)
      } text "Item to be purchased"
    }
  }

  override def execute(args: Array[String]): Unit = {
    val commandArgs = parser().parse(args, PurchaseCommandArgs()) match {
      case Some(value) => value
      case None => return
    }
    val item = commandArgs.item

    if (resourceGrowthTaskManager.hasTask(item)) {
      if (resourceGrowthTaskManager.levelUpTask(item))
        println(s"$item upgraded to level ${resourceGrowthTaskManager.getTaskLevel(item)}.")
    }
    else {
      println(s"\"${item}\" is not a purchasable item.\n\n$helpText")
    }
  }
}

case class PurchaseCommandArgs(item: String = "")
