package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }


class ClientLumisound extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("client"))
  val greeter = system.actorOf(Props[GreetingActor], name = "greeter")
  val remoteAudioRecorder = system.actorFor("akka://MySystem@127.0.0.1:2552/user/audiorecorder")
	def startup() = {
    println("Initializing server ...")
    remoteAudioRecorder.tell(NewClient, greeter)
    println(greeter)
    println(system)
  }
	def shutdown() = system.shutdown
}

object ClientLumisoundApp {
  def main(args: Array[String]) {

    val client = new ClientLumisound
    client.startup()

  }
}
