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

  def receive() = {
    case Greeting => {
        var outputFile = new File("test.wav")

        var audioFormat = new AudioFormat(
            AudioFormat.Encoding.PCM_SIGNED,
            44100.0F, 16, 2, 4, 44100.0F, false)

        var info = new DataLine.Info(classOf[TargetDataLine], audioFormat)

        var line = AudioSystem.getLine(info).asInstanceOf[TargetDataLine]
        line.open(audioFormat)

        var targetType = AudioFileFormat.Type.WAVE

        println("tap enter")

        System.in.read()

        val th = new Thread() {
          override def run() {
                println("in thread")
                line.start()
                AudioSystem.write(new AudioInputStream(line), targetType, outputFile)
                println("out of thread")
            }
        }

        th.start()

        println("tap to stop")

        System.in.read()

        line.stop()
        line.close()

        th.stop()
    }
}
}

class SimpleAudioRecorderApplication extends Bootable {
  val system = ActorSystem("SimpleAudioRecorderApplication")
  val localActor = system.actorOf(Props[SimpleAudioRecorder], "simpleAudioRecorder")
  localActor ! Greeting

  def startup() {
  }

  def shutdown() {
    system.shutdown()
}
}

object SimpleAudioRecorderApp {
  def main(args: Array[String]) {
    println("coucou")

    new SimpleAudioRecorderApplication
    
}
}

case class Greeting