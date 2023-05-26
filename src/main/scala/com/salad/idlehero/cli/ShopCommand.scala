package com.salad.idlehero.cli
import com.salad.idlehero.model.{InventoryManager, Item, ItemStack}

import scala.util.{Failure, Success}

class ShopCommand(inventoryManager: InventoryManager,
                  items: Map[String, Item]) extends AbstractCliCommand[ShopCommandArgs] {
  override val commandName: String = "/shop"
  override val helpText: String =
    """
      | Help:
      | - /shop help
      | - /shop --purchase <item> --quantity <number>
      | - /shop --sell <item> --quantity <number>
      | - /shop --info <item>
      |""".stripMargin

  override def parser(): scopt.OptionParser[ShopCommandArgs] = {
    new scopt.OptionParser[ShopCommandArgs](helpText) {
      head(helpText)


      opt[String]("purchase").optional().action { (x, c) =>
        c.copy(purchaseItem = Some(x))
      } text "The name of the item you wish to purchase."

      opt[String]("sell").optional().action { (x, c) =>
        c.copy(sellItem = Some(x))
      } text "The name of the item you wish to sell."

      opt[Int]("quantity").optional().action { (x, c) =>
        c.copy(quantity = Some(x))
      } text "The quantity of items you with to purchase/sell."

      opt[String]("info").optional().action { (x, c) =>
        c.copy(info = Some(x))
      } text "The item you desire info about."
    }
  }
  override def execute(args: Array[String]): Unit = {
    val shopArgs = parser().parse(args, ShopCommandArgs()).get match {
      case success => success
      case _ => {
        println(helpText)
        return
      }
    }

    if (shopArgs.info.isDefined)
      infoCommand(shopArgs)
    else if (shopArgs.purchaseItem.isDefined)
      purchaseCommand(shopArgs)
    else if (shopArgs.sellItem.isDefined)
      sellCommand(shopArgs)
    else
      println(helpText)
  }

  private def infoCommand(args: ShopCommandArgs): Unit = {
    val itemName = args.info.get

    if (items.contains(itemName)){
      val item = items(itemName)
      println(s"Item Name: $itemName")

      if (item.sellable)
        println(s"\t- Item can be sold for ${item.sellValue} gold.")
      else println(s"\t- Item can not be sold.")

      if (item.purchasable)
        println(s"\t- Item can be purchased for ${item.buyValue} gold.")
      else println(s"\t- Item can not be purchased.")
    } else {
      println(s"Sorry mate, got no clue what a $itemName is...\n")
    }
  }

  private def purchaseCommand(args: ShopCommandArgs): Unit = {
    val itemName = args.purchaseItem.get
    if (args.quantity.isEmpty) {
      println("You must provide the quantity of this item you wish to purchase...\n")
      println(helpText)
      println("\n")
    }
    val quantity = args.quantity.get

    val cost = items(itemName).buyValue * quantity
    if (inventoryManager.canPurchase(cost)) {
      val purchasedStack = new ItemStack(items(itemName), quantity)
      inventoryManager.withdrawalGold(cost)
      inventoryManager.deposit(purchasedStack)
      println(s"You purchased $quantity $itemName.")
    } else {
      println("You're too poor.")
      Thread.sleep(500)
      println(s"You need $cost to purchase $quantity $itemName...")
      Thread.sleep(500)
      println(s"You only have ${inventoryManager.balance("gold")} gold.\n")
    }
  }

  private def sellCommand(args: ShopCommandArgs): Unit = {
    val itemName = args.sellItem.get
    if (args.quantity.isEmpty) {
      println("You must provide the quantity of this item you wish to purchase...\n")
      println(helpText)
      println("\n")
    }
    val quantity = args.quantity.get

    if (inventoryManager.has(itemName, quantity)) {
      val depositedItemStack = new ItemStack(items(itemName), quantity)
      inventoryManager.withdrawal(depositedItemStack) match {
        case Success(depositedItems) => {
          val earnedGold = new ItemStack(items("gold"), items(itemName).sellValue * quantity)
          inventoryManager.deposit(earnedGold)
          println(s"You just sold $quantity $itemName for ${earnedGold.quantity} gold!")
        }
        case Failure(e) => println("That didn't work...")
      }
    } else {
      println(s"You're trying to sell me $quantity $itemName...")
      Thread.sleep(500)
      println(s"...")
      Thread.sleep(500)
      println(s"You don't have $quantity $itemName")
      Thread.sleep(500)
      println("...")
      Thread.sleep(500)
      println("Nice try...")
    }
  }
}

case class ShopCommandArgs(purchaseItem: Option[String] = None,
                           sellItem: Option[String] = None,
                           quantity: Option[Int] = None,
                           info: Option[String] = None)
