package fr.lumisound

/**
 * Created with IntelliJ IDEA.
 * User: jeremiefourbil
 * Date: 14/06/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */

import java.io.IOException
import java.io.File

import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.AudioFileFormat

import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }

class SimpleAudioRecorder extends Actor {
  var m_line : TargetDataLine
  var m_targetType : AudioFileFormat.Type
  var m_audioInputStream : AudioInputStream
  var m_outputFile : File

  def receive() = {
  }
}

class SimpleAudioRecorderApplication extends Bootable {
  val system = ActorSystem("SimpleAudioRecorderApplication",
    ConfigFactory.load.getConfig("audiorecorder"))
  val actor = system.actorOf(Props[SimpleCalculatorActor], "simpleRecorder")

  def startup() {
  }

  def shutdown() {
    system.shutdown()
  }
}

object SimpleAudioRecorderApp {
  def main(args: Array[String]) {
    println("coucou")
  }
}