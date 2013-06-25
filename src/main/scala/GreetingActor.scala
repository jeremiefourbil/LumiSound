package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import scala.collection.immutable.IndexedSeq

class GreetingActor extends Actor {
  def receive = {
    case Greeting(who) => { println("Hello " + who); context.system.shutdown() }
    case Connected => { println("Connected !!!") }
	case Result(fft) => { println(fft)}
  }
}