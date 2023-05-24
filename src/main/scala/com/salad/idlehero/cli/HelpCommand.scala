package com.salad.idlehero.cli
import scopt.OptionParser

class HelpCommand() extends AbstractCliCommand[HelpCommandArgs]() {

  override val commandName: String = "/help"

  override val helpText: String =
    """
      | IdleHero Available Commands:
      | - /mine
      | - /purchase
      | - /stats
      |""".stripMargin

  override def parser(): OptionParser[HelpCommandArgs] = {
    new scopt.OptionParser[HelpCommandArgs](commandName) {
      head(helpText)
    }
  }

  override def execute(args: Array[String]): Unit = {
    println(helpText)
  }
}

case class HelpCommandArgs()