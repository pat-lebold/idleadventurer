package com.salad.idlehero.game

import com.salad.idlehero.game.ResourceTypes.ResourceType

import scala.collection.mutable
import scala.math.floor

class ResourceManager {

  private val resources: mutable.Map[ResourceType, Double] = mutable.Map[ResourceType, Double]()

  def init(): ResourceManager = {
    resources.put(ResourceTypes.Gold, 0)
    resources.put(ResourceTypes.Mana, 0)
    this
  }

  def add(resourceType: ResourceType, amount: Double): Unit = {
    resources(resourceType) += amount
  }

  def get(resourceType: ResourceType): Long = {
    floor(resources(resourceType)).toLong
  }

  def has(resourceType: ResourceType, amount: Long): Boolean = {
    resources(resourceType) >= amount
  }

  def subtract(resourceType: ResourceType, amount: Long): Unit = {
    resources(resourceType) -= amount
  }

  override def toString: String = {
    resources.map(pair => s"\t- ${pair._1.toString}: ${floor(pair._2).toLong}").mkString("Resources:\n","\n","")
  }
}
