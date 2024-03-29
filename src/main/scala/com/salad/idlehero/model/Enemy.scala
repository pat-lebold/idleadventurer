package com.salad.idlehero.model

case class Enemy(name: String,
                 rarity: Rarity,
                 element: Option[Element],
                 var health: Long,
                 armor: Long,
                 magicResist: Long,
                 dodgeChance: Double,
                 drop: Option[ItemStack]) {

  def attack(attackDamage: Long, magicDamage: Long, crit: Boolean): Long = {
    if (Math.random() <= dodgeChance) {
      println(s"${displayName()} dodged your attack!")
      return health
    }

    val multiplier = if (crit) 2 else 1
    if (crit)
      println("It's a critical strike!")

    val attackNet = Math.max(attackDamage * multiplier - armor, 0)
    val magicNet = Math.max(magicDamage * multiplier - magicResist, 0)
    health = health - attackNet - magicNet
    println(s"You hit the ${displayName()} for $attackNet attack damage and $magicNet magic damage!")

    health
  }

  def displayName(): String = {
    val sb = new StringBuilder()
    sb.append(rarity.name)
    sb.append(" ")
    if (element.isDefined) {
      sb.append(element.get.name)
      sb.append(" ")
    }
    sb.append(name)
    sb.toString()
  }
}
