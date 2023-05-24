package com.salad.idlehero.model

case class Rarity(name: String,
                  chance: Double,
                  attackDamageMultiplier: Double,
                  magicDamageMultiplier: Double,
                  critChanceMultiplier: Double,
                  healthMultiplier: Double,
                  armorMultiplier: Double,
                  magicResistMultiplier: Double,
                  dodgeChanceMultiplier: Double,
                  dropRateMultiplier: Double)
