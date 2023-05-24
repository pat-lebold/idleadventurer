package com.salad.idlehero.tasks

import com.salad.idlehero.game.ResourceManager
import com.salad.idlehero.game.ResourceTypes.ResourceType

import scala.math.floor

class ResourceGrowthTask(val name: String,
                         val resourceType: ResourceType,
                         var strength: Double,
                         var levelUpCost: Long,
                         val levelUpResourceType: ResourceType) {

  var active: Boolean = false
  var level: Int = 1

  def collect(): Double = {
    if (active) strength else 0
  }

  def boostStrength(strengthIncrease: Double): Unit = {
    strength = floor(strength * strengthIncrease).toLong
  }
}
