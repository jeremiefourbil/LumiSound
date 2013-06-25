package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class Machine2 extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("machine2"))
	val greeter = system.actorOf(Props[GreetingActor], name = "greeter")


	def startup() = {}
	def shutdown() = {}
}


object Machine2App {
  def main(args: Array[String]) {
    println("Machine2App")

    new Machine2

  }
}