package com.salad.idlehero.model

case class Enemy(name: String,
                 rarity: Rarity,
                 element: Option[Element],
                 health: Long,
                 armor: Long,
                 magicResist: Long,
                 dodgeChance: Double,
                 drop: Option[ItemStack])
