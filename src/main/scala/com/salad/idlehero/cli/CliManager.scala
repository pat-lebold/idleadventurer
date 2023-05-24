package com.salad.idlehero.cli

import com.salad.idlehero.game.{GameLoopManager, ResourceGrowthTaskManager, ResourceManager}
import com.salad.idlehero.model.{Hero, HeroClass, InventoryManager, Item}

import scala.collection.mutable

class CliManager(inventoryManager: InventoryManager,
                 gameLoopManager: GameLoopManager,
                 items: Map[String, Item],
                 heroes: Map[String, Hero],
                 userHero: Hero) {

  val cliCommands = mutable.Map[String, AbstractCliCommand[_]]()

  def init(): CliManager = {
    cliCommands.put("/help", new HelpCommand())
    cliCommands.put("/campaign", new CampaignCommand(gameLoopManager))
    cliCommands.put("/inventory", new InventoryCommand(inventoryManager))
    cliCommands.put("/shop", new ShopCommand(inventoryManager))
    cliCommands.put("/stats", new StatsCommand(items, heroes, userHero))
    this
  }

}
