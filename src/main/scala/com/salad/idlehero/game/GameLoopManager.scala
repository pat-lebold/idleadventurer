package com.salad.idlehero.game

import com.salad.idlehero.model.{Campaign, EnemyClass, Hero, InventoryManager}

import java.util.concurrent.{Executors, ScheduledFuture, TimeUnit}

class GameLoopManager(inventoryManager: InventoryManager,
                      campaigns: Seq[Campaign],
                      userHero: Hero,
                      enemies: Map[String, EnemyClass]) {

  val TICK_RATE = 1000L // ms

  var active = false

  val campaignStatus = new CampaignStatus()

  private val ex = Executors.newScheduledThreadPool(1)
  private var fightRunnerHandle: ScheduledFuture[?] = null
  def startCampaign(): Unit = {
    if (campaignStatus.currentCampaign == null) {
      campaignStatus.currentCampaign = campaigns.head

      println(s"You've begun the campaign: ${campaignStatus.currentCampaign.name}")
      println(s"You are on stage ${campaignStatus.currentStageIndex + 1} of ${campaignStatus.currentCampaign.stages.size} stages.")
      println(s"You need to defeat ${campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex).numEnemies - campaignStatus.stageEnemiesDefeated} enemies to progress...")
      Thread.sleep(2000)
    }

    active = true
    val enemyDistro = campaignStatus.currentCampaign.enemyDistribution

    val fightRunner = new FightRunner(userHero, campaigns, campaignStatus, inventoryManager, enemyDistro)
    fightRunnerHandle = ex.scheduleAtFixedRate(fightRunner, 0L, TICK_RATE, TimeUnit.MILLISECONDS)
  }

  def pauseCampaign(): Unit = {
    if (active) {
      fightRunnerHandle.cancel(true)
      active = false
      println("You have left the campaign!")
      Thread.sleep(500)
      println("...")
      Thread.sleep(500)
      println("You wimp.")
    }
  }
}
