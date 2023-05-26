package com.salad.idlehero.cli

import com.salad.idlehero.model.{Element, InventoryManager, Item, ItemStack}
import scopt.OptionParser

import scala.collection.IterableOnce.iterableOnceExtensionMethods

class ForgeCommand(inventoryManager: InventoryManager,
                   items: Map[String, Item],
                   elements: Map[String, Element]) extends AbstractCliCommand[ForgeCommandArgs] {
  override val commandName: String = "/forge"
  override val helpText: String =
    """
      | Help:
      | - /forge --options available
      | - /forge --options all
      | - /forge --item <item>
      | - /forge --item <item> --element <element
      |""".stripMargin

  private val forgeableItems = items.filter { case(name, item) => item.components.nonEmpty }

  override def parser(): OptionParser[ForgeCommandArgs] = {
    new scopt.OptionParser[ForgeCommandArgs](helpText) {
      head(helpText)

      opt[String]("options").optional().action { (x, c) =>
        c.copy(option = Some(x))
      } text "Options: [available, all]"

      opt[String]("item").optional().action { (x, c) =>
        c.copy(item = Some(x))
      } text "Name of item you wish to forge"

      opt[String]("element").optional().action { (x, c) =>
        c.copy(element = Some(x))
      } text "The element of the item you wish to forge. (if applicable)"
    }
  }

  override def execute(args: Array[String]): Unit = {
    if (args.length == 1) {
      println(helpText)
    }

    val forgeArgs = parser().parse(args, ForgeCommandArgs()).get

    if (forgeArgs.option.isDefined) {
      forgeArgs.option.get.toLowerCase() match {
        case "available" => {
          val availableItems = items.values.filter { item =>
            inventoryManager.canForge(item)
          }

          if (availableItems.isEmpty) {
            println("You can't forge anything you poor shmuck.")
          } else {
            availableItems.foreach(println)
          }
        }
        case "all" => {
          println("Forgeable Items:")
          forgeableItems.values.foreach(println)
        }
        case option => {
          println(s"${option} is not a valid option...\n")
          Thread.sleep(1000)
          println(helpText)
        }
      }
    } else if (forgeArgs.item.isDefined && (forgeArgs.element.isEmpty || (forgeArgs.element.isDefined && elements.contains(forgeArgs.element.get)))) {
      val itemArg = forgeArgs.item.get match {
        case success => success
        case _ => {
          println(helpText)
          return
        }
      }

      // If requested item is a forgable item
      if (forgeableItems.contains(itemArg)) {
        val item = forgeableItems(itemArg)
        val components = item.components.map { case (name, quantity) =>
          name -> new ItemStack(items(name), quantity)
        }.toMap

        // Check if resources exist
        components.values.foreach { itemStack =>
          if (!inventoryManager.has(itemStack)) {
            println(s"You need ${itemStack.quantity} ${itemStack.item.name} to forge this item.")
            println(s"You only have ${inventoryManager.balance(item.name)} ${itemStack.item.name}.")
            Thread.sleep(500)
            println("Yikes")
            return
          }
        }

        // If elemental, check if you have the gem
        if (!item.elemental && forgeArgs.element.isDefined && inventoryManager.hasGem(forgeArgs.element.get)) {
          println("You don't have the correct elemental gem to forge this elemental item...")
          return
        }

        // Spend resources
        components.values.foreach { componentStack =>
          if (item.elemental && componentStack.item.name.equals("gem")) {
            inventoryManager.withdrawalGem(elements(forgeArgs.element.get).name)
          }
          inventoryManager.withdrawal(componentStack)
        }

        val forgedItem = items(itemArg)

        val finalItem = if (forgedItem.elemental) {
          forgedItem.copy(element = Some(elements(forgeArgs.element.get)))
        } else {
          forgedItem
        }

        println(s"You forged a ${finalItem.id()}")

        inventoryManager.deposit(new ItemStack(finalItem, 1))
      } else {
        println(s"You can't forge a that... yikes.")
      }
    } else {
      println(s"\n$helpText")
    }
  }
}

case class ForgeCommandArgs(option: Option[String] = None,
                            item: Option[String] = None,
                            element: Option[String] = None)