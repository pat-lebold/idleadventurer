package com.salad.idlehero

import com.salad.idlehero.cli.CliManager
import com.salad.idlehero.game.{GameLoopManager, ResourceGrowthTaskManager, ResourceManager}
import com.salad.idlehero.tasks.GrowthTaskJsonLoader

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}
import scala.io.StdIn.readLine

object App {

  def main(args: Array[String]): Unit = {

    println("... Is that ... a hero??")
    Thread.sleep(1000)
    println("...")
    Thread.sleep(200)
    println("..")
    Thread.sleep(200)
    println("..")
    Thread.sleep(1000)
    println("And what kind of hero are you?")


    println("Welcome to Idle Adventurer")
    println(":D :D :D :D :D :D :D :D :D \n")
    Thread.sleep(1000)
    println("This is a trial of an idle game.....")
    Thread.sleep(1000)
    println("You are dwarf. Go get gold you underground god. slayyyyy")
    Thread.sleep(1000)
    println("Type /help for help")

    val resourceManager = new ResourceManager().init()

    val resourceGrowthTasks = GrowthTaskJsonLoader.loadGrowthTasks().map { resourceGrowthTask => resourceGrowthTask.name -> resourceGrowthTask }.toMap
    val resourceGrowthTaskManager = new ResourceGrowthTaskManager(resourceGrowthTasks, resourceManager)

    val gameLoopManager = new GameLoopManager(resourceGrowthTaskManager)

    val cliManager = new CliManager(resourceManager, resourceGrowthTaskManager).init()

    val ex = new ScheduledThreadPoolExecutor(1)
    ex.scheduleAtFixedRate(gameLoopManager, 0L, Constants.TICK_SPEED, TimeUnit.MILLISECONDS)

    while(true){
      val input = readLine().split(" ")
      if (input.nonEmpty) {
        val command = input(0)
        val args = input.drop(1)

        if (cliManager.cliCommands.contains(command))
          cliManager.cliCommands(command).execute(args)
        else {
          cliManager.cliCommands("/help").execute(args)
        }
      }
    }
  }

}