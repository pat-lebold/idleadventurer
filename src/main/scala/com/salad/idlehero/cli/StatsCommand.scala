package com.salad.idlehero.cli

import com.salad.idlehero.model.{Hero, HeroClass, Item}
import scopt.OptionParser

class StatsCommand(private val items: Map[String, Item],
                   private val heroes: Map[String, Hero],
                   private val hero: Hero) extends AbstractCliCommand[StatsCommandArgs]() {

  override val commandName: String = "/stats"
  override val helpText: String =
    """
      | Stats Command:
      | - /stats -> Your Stats
      | - /stats --item <item name> -> Item Stats
      | - /stats --hero <hero name> -> Hero Stats
      |""".stripMargin

  override def parser(): OptionParser[StatsCommandArgs] = {
    new scopt.OptionParser[StatsCommandArgs](helpText) {
      head(helpText)

      opt[String]("hero").optional().action { (x, c) =>
        c.copy(hero = Some(x))
      } text "Name of hero who's stats you desire."

      opt[String]("item").optional().action { (x, c) =>
        c.copy(item = Some(x))
      } text "Name of item who's stats you desire."
    }
  }

  override def execute(args: Array[String]): Unit = {
    val statsArgs = parser().parse(args, StatsCommandArgs()).get match {
      case success => success
      case _ => {
        println(helpText)
        return
      }
    }

    if (statsArgs.hero.isDefined) {
      if (heroes.contains(statsArgs.hero.get)) {
        println(heroes(statsArgs.hero.get).toString)
      } else {
        println(s"${statsArgs.hero.get} is not a hero.")
      }
    } else if (statsArgs.item.isDefined) {
      if (items.contains(statsArgs.item.get)) {
        println(items(statsArgs.item.get).toString)
      } else {
        println(s"${statsArgs.item.get} is not an item.")
      }
    } else {
      println(hero.toString)
    }
  }
}

case class StatsCommandArgs(hero: Option[String] = None,
                            item: Option[String] = None)