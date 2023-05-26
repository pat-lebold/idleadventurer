package com.salad.idlehero.cli

import com.salad.idlehero.game.GameLoopManager
import com.salad.idlehero.model.{Element, Hero, HeroClass, InventoryManager, Item}

import scala.collection.mutable

class CliManager(inventoryManager: InventoryManager,
                 gameLoopManager: GameLoopManager,
                 elements: Map[String, Element],
                 items: Map[String, Item],
                 heroes: Map[String, Hero],
                 userHero: Hero) {

  val cliCommands = mutable.Map[String, AbstractCliCommand[_]]()

  def init(): CliManager = {
    cliCommands.put("/help", new HelpCommand())
    cliCommands.put("/campaign", new CampaignCommand(gameLoopManager))
    cliCommands.put("/inventory", new InventoryCommand(inventoryManager))
    cliCommands.put("/shop", new ShopCommand(inventoryManager, items))
    cliCommands.put("/stats", new StatsCommand(items, heroes, userHero))
    cliCommands.put("/forge", new ForgeCommand(inventoryManager, items, elements))
    this
  }

}
