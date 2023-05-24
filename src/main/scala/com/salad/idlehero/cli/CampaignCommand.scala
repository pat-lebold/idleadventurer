package com.salad.idlehero.cli
import com.salad.idlehero.game.GameLoopManager
import scopt.OptionParser

class CampaignCommand(gameLoopManager: GameLoopManager) extends AbstractCliCommand[CampaignCommandArgs] {
  override val commandName: String = "/campaign"
  override val helpText: String =
    """ Campaign Help:
      | - start (start a compaign)
      | - stop (stop a campaign)
      |
      |""".stripMargin

  override def parser(): scopt.OptionParser[CampaignCommandArgs] = {
    new scopt.OptionParser[CampaignCommandArgs](helpText) {
      head(helpText)
    }
  }

  override def execute(args: Array[String]): Unit = {
    args(0) match {
      case "start" => {
        if (gameLoopManager.active)
          println("Campaign currently ongoing.")
        else
          gameLoopManager.startCampaign()
      }
      case "stop" => {
        if (!gameLoopManager.active)
          println("You aren't currently campaigning you bum.")
        else
          gameLoopManager.pauseCampaign()
      }
      case _ => println(helpText)
    }
  }
}

case class CampaignCommandArgs()