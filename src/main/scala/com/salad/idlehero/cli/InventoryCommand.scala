package com.salad.idlehero.cli
import com.salad.idlehero.model.InventoryManager
import scopt.OptionParser

class InventoryCommand(inventoryManager: InventoryManager) extends AbstractCliCommand[InventoryCommandArgs] {
  override val commandName: String = "/inventory"
  override val helpText: String =
    """
      | Help:
      | - /inventory -> View your inventory
      |""".stripMargin

  override def parser(): OptionParser[InventoryCommandArgs] = {
    new scopt.OptionParser[InventoryCommandArgs](helpText) {
      head(helpText)
    }
  }

  override def execute(args: Array[String]): Unit = {
    println(inventoryManager.toString)
  }
}

case class InventoryCommandArgs()
