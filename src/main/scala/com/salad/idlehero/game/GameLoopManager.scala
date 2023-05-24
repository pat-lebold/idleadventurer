package com.salad.idlehero.game

import com.salad.idlehero.model.{Campaign, InventoryManager, Stage}

class GameLoopManager(inventoryManager: InventoryManager,
                      campaigns: Seq[Campaign]) {

  var currentCampaign: Campaign = null
  val currentStageIndex: Int = 0
  val stageEnemiesDefeated: Int = 0

  def startCampaign(): Unit = {

  }

  def pauseCampaign(): Unit = {

  }
}
