package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class CreationActor extends Actor {
  def receive = {
    case (actor: ActorRef, op: MathOp) ⇒ actor ! op
    case result: MathResult ⇒ result match {
      case MultiplicationResult(n1, n2, r) ⇒
      println("Mul result: %d * %d = %d".format(n1, n2, r))
      case DivisionResult(n1, n2, r) ⇒
      println("Div result: %.0f / %d = %.2f".format(n1, n2, r))
  }
}
}

class CreationApplication extends Bootable {
	val system =
    ActorSystem("RemoteCreation", ConfigFactory.load.getConfig("remotecreation"))
    val localActor = system.actorOf(Props[CreationActor], "creationActor")
    val remoteActor =
    system.actorOf(Props[AdvancedCalculatorActor], "advancedCalculator")

    def doSomething(op: MathOp) = {
        localActor ! (remoteActor, op)
    }

    def startup() {
    }

    def shutdown() {
      system.shutdown()
  }
}

object CreationApp {
  def main(args: Array[String]) {
    val app = new CreationApplication
    println("Started Creation Application")
    while (true) {
      if (Random.nextInt(100) % 2 == 0)
      app.doSomething(Multiply(Random.nextInt(20), Random.nextInt(20)))
      else
      app.doSomething(Divide(Random.nextInt(10000), (Random.nextInt(99) + 1)))

      Thread.sleep(200)
  }
}
}