package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class ClientLumisound extends Bootable {
	val system = ActorSystem("MySystem", ConfigFactory.load.getConfig("client"))
  val visualizer = system.actorOf(Props[VisualizerActor], name = "visualizer")
  val remoteAudioRecorder = system.actorFor("akka://MySystem@127.0.0.1:2552/user/audiorecorder")
	def startup() = {
    println("Initializing client ...")
    remoteAudioRecorder.tell(NewClient, visualizer)
  }
	def shutdown() = system.shutdown
}

object ClientLumisoundApp {
  def main(args: Array[String]) {

    val client = new ClientLumisound
    client.startup()
  }
}
