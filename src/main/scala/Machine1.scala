package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class Machine1 extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("machine1"))
	val greeter = system.actorOf(Props[GreetingActor], name = "greeter")
	greeter ! Greeting("test")

	def startup() = {}
	def shutdown() = {}
}


object Machine1App {
  def main(args: Array[String]) {
    println("Machine1App")

    new Machine1

  }
}