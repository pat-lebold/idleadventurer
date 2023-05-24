package com.salad.idlehero.cli

abstract class AbstractCliCommand[T]() {

  val commandName: String
  val helpText: String

  def parser(): scopt.OptionParser[T]

  def execute(args: Array[String]): Unit
}
