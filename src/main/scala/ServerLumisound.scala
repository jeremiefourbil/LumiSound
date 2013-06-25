package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class ServerLumisound extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("machine1"))
//	val greeter = system.actorOf(Props[GreetingActor], name = "greeter")
  val audioRecorder = system.actorOf(Props[SimpleAudioRecorder], name = "audiorecorder")

	def startup() = {
    println("Initializing server ...")
  }
	def shutdown() = system.shutdown()
}


object ServerLumisoundApp {
  def main(args: Array[String]) {
    val server = new ServerLumisound
    server.startup()
  }
}