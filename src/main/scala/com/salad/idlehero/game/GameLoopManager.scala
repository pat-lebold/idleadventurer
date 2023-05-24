package com.salad.idlehero.game

class GameLoopManager(resourceGrowthTaskManager: ResourceGrowthTaskManager) extends Runnable {

  override def run(): Unit = resourceGrowthTaskManager.executeAllTasks()
}
