package com.salad.idlehero.model

class ItemStack(val item: Item, var quantity: Long = 0) {

  override def clone(): ItemStack = {
    val itemClone = item.clone(item.element)
    new ItemStack(itemClone, quantity)
  }

}
