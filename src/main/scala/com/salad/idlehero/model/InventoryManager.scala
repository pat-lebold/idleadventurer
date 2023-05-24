package com.salad.idlehero.model

import scala.collection.mutable
import scala.util.Try

class InventoryManager(private val inventory: mutable.Map[String, ItemStack]) {

  def deposit(itemStack: ItemStack): Unit = {
    if (!inventory.contains(itemStack.item.id()))
      inventory.put(itemStack.item.id(), itemStack)
    else
      inventory(itemStack.item.id()).quantity += itemStack.quantity
  }

  def has(itemStack: ItemStack): Boolean = {
    inventory.contains(itemStack.item.id()) && inventory(itemStack.item.id()).quantity >= itemStack.quantity
  }

  def withdrawal(itemStack: ItemStack): Try[ItemStack] = Try {
    if (this.has(itemStack)) {
      inventory(itemStack.item.id()).quantity -= itemStack.quantity
      return Try(itemStack)
    }

    throw new Exception(s"You don't have enough \"${itemStack.item.id()}\"")
  }
}
