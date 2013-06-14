package fr.lumisound

import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class SimpleCalculatorActor extends Actor {
  def receive = {
    case Add(n1, n2) ⇒
      println("Calculating %d + %d".format(n1, n2))
      sender ! AddResult(n1, n2, n1 + n2)
    case Subtract(n1, n2) ⇒
      println("Calculating %d - %d".format(n1, n2))
      sender ! SubtractResult(n1, n2, n1 - n2)
  }
}

class CalculatorApplication extends Bootable {
	val system = ActorSystem("CalculatorApplication",
  	ConfigFactory.load.getConfig("calculator"))
	val actor = system.actorOf(Props[SimpleCalculatorActor], "simpleCalculator")

	def startup() {
	}

	def shutdown() {
		system.shutdown()
	}
}

object CalcApp {
  	def main(args: Array[String]) {
    	new CalculatorApplication
    	println("Started Calculator Application - waiting for messages")
  	}
}