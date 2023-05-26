package com.salad.idlehero.model

import scala.collection.mutable
import scala.util.Try

class InventoryManager() {

  private val inventory: mutable.Map[String, ItemStack] = mutable.Map()

  def balance(itemName: String): Long = {
    if (inventory.contains(itemName))
      inventory(itemName).quantity
    else
      0
  }

  def canForge(item: Item): Boolean = {
    if (item.components.isEmpty)
      return false

    item.components.foreach { case (name, quantity) =>
      if (!inventory.contains(name) || inventory(name).quantity < quantity)
        return false
    }

    true
  }

  def canPurchase(gold: Long): Boolean = {
    inventory.contains("gold") && inventory("gold").quantity >= gold
  }
  def deposit(itemStack: ItemStack): Unit = {
    if (!inventory.contains(itemStack.item.id()))
      inventory.put(itemStack.item.id(), itemStack.clone())
    else
      inventory(itemStack.item.id()).quantity += itemStack.quantity
  }

  def hasGem(elementName: String): Boolean = {
    inventory.contains(s"gem - ${elementName}")
  }

  def has(itemName: String, quantity: Long): Boolean = {
    inventory.contains(itemName) && inventory(itemName).quantity >= quantity
  }

  def has(itemStack: ItemStack): Boolean = {
    inventory.contains(itemStack.item.id()) && inventory(itemStack.item.id()).quantity >= itemStack.quantity
  }

  def withdrawalGem(elementName: String): Try[ItemStack] = Try {
    val id = s"gem - element($elementName)"
    if (inventory.contains(id)) {
      inventory(id).quantity -= 1
      return Try(new ItemStack(inventory(id).item.clone(inventory(id).item.element), 1))
    }

    throw new Exception(s"You don't have a \"$id\"")
  }

  def withdrawalGold(quantity: Long): Try[ItemStack] = Try {
    if (inventory.contains("gold") && inventory("gold").quantity >= quantity) {
      inventory("gold").quantity -= quantity
      return Try(new ItemStack(inventory("gold").item.clone(None), quantity))
    }

    throw new Exception(s"You don't have $quantity gold.")
  }

  def withdrawal(itemStack: ItemStack): Try[ItemStack] = Try {
    if (this.has(itemStack)) {
      inventory(itemStack.item.id()).quantity -= itemStack.quantity
      return Try(itemStack.clone())
    }

    throw new Exception(s"You don't have enough \"${itemStack.item.id()}\"")
  }

  override def toString: String = {
    inventory.values.map { itemStack =>
      s"\t- ${itemStack.item.id()}: ${itemStack.quantity}"
    }.mkString("Inventory:\n","\n","\n")
  }
}
