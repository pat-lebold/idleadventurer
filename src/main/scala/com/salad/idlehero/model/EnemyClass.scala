package com.salad.idlehero.model

case class EnemyClass(name: String,
                      elemental: Boolean,
                      health: Long,
                      armor: Long,
                      magicResist: Long,
                      dodgeChance: Double,
                      dropRate: Double,
                      drops: Map[ItemStack, Double])
