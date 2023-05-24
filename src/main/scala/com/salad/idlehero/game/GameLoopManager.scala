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
    }

    active = true
    val enemyDistro = Map(enemies("blob") -> 1.00)
    val enemyGenerator = new EnemyGenerator(enemyDistro, campaignStatus.currentCampaign.elementDistribution, campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex).rarityDistribution)

    val fightRunner = new FightRunner(userHero, campaignStatus, inventoryManager, enemyDistro)
    fightRunnerHandle = ex.scheduleAtFixedRate(fightRunner, 0L, TICK_RATE, TimeUnit.MILLISECONDS)
  }

  def pauseCampaign(): Unit = {
    if (active) {
      fightRunnerHandle.cancel(true)
      active = false
      println("You have left the campaign!")
      Thread.sleep(2000)
      println("...")
      Thread.sleep(1000)
      println("You wimp.")
    }
  }
}
