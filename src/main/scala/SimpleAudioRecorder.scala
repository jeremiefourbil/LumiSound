package fr.lumisound

/**
 * Created with IntelliJ IDEA.
 * User: jeremiefourbil
 * Date: 14/06/13
 * Time: 14:49
 * To change this template use File | Settings | File Templates.
 */
// java imports

import java.io.IOException
import java.io.File
import java.io.ByteArrayOutputStream
import javax.sound.sampled.DataLine
import javax.sound.sampled.TargetDataLine
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.LineUnavailableException
import javax.sound.sampled.AudioFileFormat

// scala imports
import fr.lumisound.FFT.myComplex
import akka.kernel.Bootable
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import scala.math._
import scala.concurrent.duration._

class SimpleAudioRecorder extends Actor {
  // actor attributes
  var outputFile = new File("test.wav")
  var outputArray = new ByteArrayOutputStream()
  var audioFormat = new AudioFormat(
    AudioFormat.Encoding.PCM_SIGNED
    ,44100.0F
    ,16
    ,2
    ,4
    ,44100.0F
    ,false)
  var info = new DataLine.Info(classOf[TargetDataLine], audioFormat)
  var line = AudioSystem.getLine(info).asInstanceOf[TargetDataLine]
  line.open(audioFormat, line.getBufferSize())
  var targetType = AudioFileFormat.Type.WAVE
  var numBytesRead = 0
  var size = line.getBufferSize() / 5
  var data = new Array[Byte](size)
  var stopped = false

  val audioThread = new Thread() {
    override def run() {
      println("starting audio recording...")
      line.start()
      while (!stopped) {
        numBytesRead =  line.read(data, 0, data.length)
//        println("numBytesRead :" ++ numBytesRead.toString)
//        println("outputArray :" ++ outputArray.toByteArray.length.toString)
        if( numBytesRead > 0 ) {
          outputArray.write(data, 0, numBytesRead)
        }
      }
      println("out of thread")
    }
  }
  var connectedClients = List[ActorRef]()

  // custom ByteArrayOutputStream FFT
  def myFFT(baos: ByteArrayOutputStream) = {
      val CHUNK_SIZE: Int = 4096
      val audio = baos.toByteArray
      val totalSize = pow(2, floor(log(audio.length.toDouble)/log(2))) toInt
      val amountPossible = totalSize/CHUNK_SIZE
      // dropRight in order to have an audio array of size 2^N and apply Cooley-Tukey FFT
      audio dropRight audio.length - totalSize
      // shift operator
      def shiftOp = { x: Int => y: Int => y*CHUNK_SIZE + x}
      // compute FFT
      0 until CHUNK_SIZE map
        { x => FFT.fft( 0 until amountPossible map { y => myComplex(audio(shiftOp(x)(y)))} toList)}
  }

  // initializing the audio capture thread
  override def preStart() = {
    //audioThread.start()
  }

  // scheduler
  import context.dispatcher
  val tick = context.system.scheduler.schedule(500 millis, 1000 millis, self, "tick")

  def receive() = {
    case Start() => {
      println("tap enter")
      println("tap to stop")
      System.in.read()
      stopped = true
      println("This is what the array contains : ")
      line.stop()
      line.close()

      //println(outputArray.toByteArray().map("%02X" format _).mkString)
      println(myFFT(outputArray).length)
      //println(myFFT(outputArray))
      val test = myFFT(outputArray).map( x => x.map(y => sqrt(y.re*y.re + y.im*y.im)) )
    }
    case Stop() => {
      println("stopping system ...")
      context.system.shutdown
    }
    case NewClient => {
      println("new client ...")
      connectedClients = sender :: connectedClients
      sender ! Connected
    }
    case "tick" => { println("test") }
  }
}

class SimpleAudioRecorderApplication extends Bootable {
  val system = ActorSystem("SimpleAudioRecorderApplication",ConfigFactory.load.getConfig("audioRecorder"))
  val localActor = system.actorOf(Props[SimpleAudioRecorder], "simpleAudioRecorder")
  localActor ! Start()

  def startup() {
  }

  def shutdown() {
    system.shutdown()
  }
}

object SimpleAudioRecorderApp {
  def main(args: Array[String]) {

    new SimpleAudioRecorderApplication

  }
}

case class Greeting1

case class Initialize(who: String) extends Serializable

case class Start() extends  Serializable

case class Stop() extends Serializable

case class NewClient extends Serializable

case class Connected extends Serializable