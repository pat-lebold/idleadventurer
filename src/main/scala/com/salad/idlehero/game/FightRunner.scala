package com.salad.idlehero.game

import com.salad.idlehero.model.{EnemyClass, Hero, InventoryManager}

class FightRunner(userHero: Hero,
                  campaignStatus: CampaignStatus,
                  inventoryManager: InventoryManager,
                  enemyDistribution: Map[EnemyClass, Double]) extends Runnable {

  private var enemyGenerator: EnemyGenerator = new EnemyGenerator(
    enemyDistribution,
    campaignStatus.currentCampaign.elementDistribution,
    campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex).rarityDistribution
  )

  private var enemy = enemyGenerator.generateEnemy()

  println(s"${enemy.displayName()} appeared!")
  override def run(): Unit = {
    val currentStage = campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex)

    val attackAttempt = userHero.attack()

    if (attackAttempt.isDefined) {
      val attack = attackAttempt.get

      val remainingHealth = enemy.attack(attack.attackDamage, attack.magicDamage, attack.isCriticalStrike)

      if (remainingHealth <= 0) {
        campaignStatus.stageEnemiesDefeated += 1
        println(s"You killed the ${enemy.displayName()}!")

        if (enemy.drop.isDefined) {
          val drop = enemy.drop.get
          inventoryManager.deposit(drop)
          println(s"The ${enemy.displayName()} dropped ${drop.quantity} ${drop.item.name}!\n")
        } else {
          println(s"The ${enemy.displayName()} dropped nothing...\n")
        }

        println(s"Stage progress ${campaignStatus.stageEnemiesDefeated}/${currentStage.numEnemies}")

        if (campaignStatus.stageEnemiesDefeated >= currentStage.numEnemies) {
          campaignStatus.currentStageIndex += 1
          campaignStatus.stageEnemiesDefeated = 0
          if (campaignStatus.currentStageIndex >= campaignStatus.currentCampaign.stages.size) {
            println("You completed the campaign!") // TODO: next campaign
          } else {
            println(s"You've progressed to stage ${campaignStatus.currentStageIndex}")
            println(s"You must defeat ${campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex).numEnemies} enemies to advance.")
            val elementDistribution = campaignStatus.currentCampaign.elementDistribution
            val rarityDistribution = campaignStatus.currentCampaign.stages(campaignStatus.currentStageIndex).rarityDistribution
            enemyGenerator = new EnemyGenerator(enemyDistribution, elementDistribution, rarityDistribution)
          }
        }

        enemy = enemyGenerator.generateEnemy()
        println(s"${enemy.displayName()} appeared!")
      }
      else {
        println(s"The ${enemy.displayName()} now has $remainingHealth health.\n")
      }
    }
  }
}
