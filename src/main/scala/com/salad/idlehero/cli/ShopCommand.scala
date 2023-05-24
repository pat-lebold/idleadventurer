package com.salad.idlehero.cli
import com.salad.idlehero.model.InventoryManager

class ShopCommand(inventoryManager: InventoryManager) extends AbstractCliCommand[ShopCommandArgs] {
  override val commandName: String = "/shop"
  override val helpText: String =
    """
      | Help:
      | - Shop currently closed
      |""".stripMargin

  override def parser(): scopt.OptionParser[ShopCommandArgs] = {
    new scopt.OptionParser[ShopCommandArgs](helpText) {
      head(helpText)
    }
  }
  override def execute(args: Array[String]): Unit = {
    println(helpText)
  }
}

case class ShopCommandArgs()
