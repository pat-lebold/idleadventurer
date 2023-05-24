package com.salad.idlehero.cli
import scopt.OptionParser

class HelpCommand() extends AbstractCliCommand[HelpCommandArgs]() {

  override val commandName: String = "/help"

  override val helpText: String =
    """
      | IdleHero Available Commands:
      |	- /campaign <start | stop>
      |	- /stats --hero <hero name>
      |	- /stats --item <item name>
      | - /inventory
      | - /shop
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