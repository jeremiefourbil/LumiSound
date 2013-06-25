package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }


class Machine3 extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("machine3"))
	val greeter = system.actorFor("akka://MySystem@127.0.0.1:2552/user/greeter")
	greeter ! Greeting("Sonny Rollins")

	def startup() = {}
	def shutdown() = {}
}

object Machine3App {
  def main(args: Array[String]) {
    println("Machine3App")

    new Machine3

  }
}