package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class GreetingActor extends Actor {
  def receive = {
    //case Greeting(who) => println("Hello " + who)
  	case _ => println("AAAAAAAAAAAAAAAA")
  }
}