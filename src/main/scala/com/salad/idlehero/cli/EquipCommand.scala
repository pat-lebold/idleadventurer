package com.salad.idlehero.cli

import com.salad.idlehero.model.{Hero, InventoryManager, ItemStack}
import scopt.OptionParser

import scala.None
import scala.util.{Failure, Success}

class EquipCommand(hero: Hero, inventoryManager: InventoryManager) extends AbstractCliCommand[EquipCommandArgs] {
  override val commandName: String = "/equip"
  override val helpText: String =
    """
      | Help:
      | - /equip --item <item name>
      |
      |""".stripMargin

  override def parser(): OptionParser[EquipCommandArgs] = {
    new scopt.OptionParser[EquipCommandArgs](helpText) {
      head(helpText)


      opt[String]("item").optional().action { (x, c) =>
        c.copy(item = Some(x))
      } text "The name of the item you wish to equip."
    }
  }

  override def execute(args: Array[String]): Unit = {
    val equipArgs = parser().parse(args, EquipCommandArgs()).get match {
      case success => success
      case _ => {
        println(helpText)
        return
      }
    }

    if (equipArgs.item.isDefined) {
      val requestedItem = equipArgs.item.get
      if (inventoryManager.has(requestedItem, 1)) {
        val confirmedItem = inventoryManager.withdrawal(requestedItem, 1)
        val confirmedItemSlot = inventoryManager.peek(requestedItem).get.item.itemSlot
        if (confirmedItemSlot.isEmpty) {
          println(s"$confirmedItem can not be equipped you dingus.")
        } else {
          // Unequip existing item
          if (hero.items.contains(confirmedItemSlot.get)) {
            val equippedItem = hero.items(confirmedItemSlot.get).get
            inventoryManager.deposit(new ItemStack(equippedItem.clone(equippedItem.element), 1))
            println(s"You have unequipped your ${hero.items(confirmedItemSlot.get).get.name}")
          }

          // Equip new item
          hero.items(confirmedItemSlot.get) = Some(confirmedItem.get.item)
          println(s"You have equipped a ${confirmedItem.get.item.id()}")
        }
      }
    } else {
      println(helpText)
    }
  }
}

case class EquipCommandArgs(item: Option[String] = None)