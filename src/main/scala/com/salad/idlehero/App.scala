package com.salad.idlehero

import com.salad.idlehero.cli.{CampaignCommand, CliManager}
import com.salad.idlehero.game.{EnemyGenerator, GameLoopManager}
import com.salad.idlehero.model.{Hero, InventoryManager}
import com.salad.idlehero.resource.{JsonCampaignLoader, JsonElementsLoader, JsonEnemyLoader, JsonHeroLoader, JsonItemLoader}

import scala.io.StdIn.readLine

object App {


  def main(args: Array[String]): Unit = {
    /*
    val userHero: Hero = userSelectsHero()

    inventoryIntro()
    gotoIntro()
    */

    val campaigns = JsonCampaignLoader.loadCampaigns()
    val elements = JsonElementsLoader.loadElements()
    val enemies = JsonEnemyLoader.loadEnemies()
    val items = JsonItemLoader.loadItems()
    val heroes = JsonHeroLoader.loadStarterHeroes()

    val userHero = heroes("sorcerer")

    val inventoryManager = new InventoryManager()
    val gameLoopManager = new GameLoopManager(inventoryManager, campaigns, userHero, enemies)

    val cliManager = new CliManager(inventoryManager, gameLoopManager, elements, items, heroes, userHero).init()

    while(true) {
      val input = readLine().split(" ")
      if (input.nonEmpty) {
        val command = input(0)
        val args = input.drop(1)

        if (gameLoopManager.active && command != new CampaignCommand(gameLoopManager).commandName) {
          println("You're on a campaign! You have no time for that!\n\nYou can leave the campaign with the following command:\n\t- /campaign stop")
        } else if (cliManager.cliCommands.contains(command))
          cliManager.cliCommands(command).execute(args)
        else {
          cliManager.cliCommands("/help").execute(args)
        }
      }
    }
  }

  private def userSelectsHero(): Hero = {
    val startingHeroes = JsonHeroLoader.loadStarterHeroes()

    println("... Is that ... a hero??")
    Thread.sleep(1000)
    println("...")
    Thread.sleep(200)
    println("..")
    Thread.sleep(200)
    println("..")
    Thread.sleep(1000)
    println("And what kind of hero are you?")
    println("------------------------------")

    val helpText =
      """
        | Help:
        | - /list heroes
        | - /select --hero <heroname>
        | - /stats --hero <heroname>\n\n
        |""".stripMargin

    println(helpText)

    var heroSelection: Hero = null
    while (heroSelection == null) {
      val input = readLine().split(" ")
      input(0) match {
        case "/list" => {
          if (!input(1).equals("heroes")) {
            println("Invalid command :(\n")
            println(helpText)
          } else {
            println("Heroes:")
            startingHeroes.keys.foreach { heroName => println(s"\t- $heroName") }
            println()
          }
        }
        case "/select" => {
          if (!input(1).equals("--hero")) {
            println("Invalid command :(\n")
            println(helpText)
          } else if (!startingHeroes.contains(input(2))) {
            println(s"\"${input(2)}\" is not a hero you fool!")
            println(helpText)
          } else {
            heroSelection = startingHeroes(input(2))
          }
        }
        case "/stats" => {
          if (!input(1).equals("--hero")) {
            println("Invalid command :(\n")
            println(helpText)
          } else if (!startingHeroes.contains(input(2))) {
            println(s"\"${input(2)}\" is not a hero you fool!")
            println(helpText)
          } else {
            println(startingHeroes(input(2)).toString)
            println()
          }
        }
        case _ => {
          println("Invalid command :(\n")
          println(helpText)
        }
      }
    }

    Thread.sleep(1000)
    println("...")
    Thread.sleep(500)
    println("oh")
    Thread.sleep(500)
    println("..")
    Thread.sleep(500)
    println(s"A ${heroSelection.name} are you?")
    Thread.sleep(2000)
    println(s"Well you have some options then considering you're a big bad ${heroSelection.name}...")
    Thread.sleep(500)
    println("...")

    heroSelection
  }

  private def inventoryIntro(): Unit = {
    Thread.sleep(1500)
    println("Here. Take this as a token of my favor...")
    Thread.sleep(2000)
    println("<You received a big bad inventory from the random mistress god damn!!!>")
    Thread.sleep(1000)
    println("\nWhen you're at home you can check your inventory.")
    Thread.sleep(3000)
    println("You're currently at home.")
    Thread.sleep(3000)
    println("You can check your inventory with the following command:")
    println("\t- /inventory\n")
    Thread.sleep(1000)
    println("Give it a go!")

    var viewedInventory = false
    while (!viewedInventory) {
      val input = readLine()
      if (input.equals("/inventory")) {
        println("It's empty you nut.\n")
        viewedInventory = true
      } else {
        println("Invalid command :(\n")
        println("Check your inventory with \"/inventory\"\n")
      }
    }
  }

  private def gotoIntro(): Unit = {
    Thread.sleep(2000)
    println("...")
    Thread.sleep(2000)
    println(s"Well as a big bad adventurer you have better things to do than stay here with me...")
    Thread.sleep(2000)
    println(s"...")
    Thread.sleep(2000)
    println(s"unless...")
    Thread.sleep(4000)
    println("oh nevermind.")
    Thread.sleep(2000)
    println("You have a few places you can go! The shops! The ...")
    Thread.sleep(2000)
    println("...")
    Thread.sleep(2000)
    println("well actually that's it...")
    Thread.sleep(2000)
    println("well actually there's the forge now that I think about it")
    Thread.sleep(500)
    println("but you don't have anythign to forge...")
    Thread.sleep(500)
    println("...")
    Thread.sleep(500)
    println("but if you did you could create new items there... weapons even")
    Thread.sleep(2000)
    println("and also you could go on a campaign as well.")
    Thread.sleep(1000)
    println("I'll leave it up to you.\n")
    Thread.sleep(2000)
    println("Help:")
    println("\t- /campaign <start | stop>")
    println("\t- /stats --hero <hero name>")
    println("\t- /stats --item <item name>")
    println("\t- /equip")
    println("\t- /forge")
    println("\t- /inventory")
    println("\t- /shop")
    Thread.sleep(4000)
    println("And you can always type /help for help")
  }
}