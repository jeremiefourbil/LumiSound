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
import java.io.ByteArrayOutputStream

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
import main.java.PrincetonComplex

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
      println("in thread")
      line.start()
      //AudioSystem.write(new AudioInputStream(line), targetType, outputArray)
      while (!stopped) {
        numBytesRead =  line.read(data, 0, data.length)
        if( numBytesRead > 0 ) {
          outputArray.write(data, 0, numBytesRead)
        }
      }
      println("out of thread")
    }
  }

  def FFT(baos: ByteArrayOutputStream) = {

      var audio = baos.toByteArray()

      val totalSize = audio.length

      var amountPossible = totalSize/4096

      //When turning into frequency domain we'll need complex numbers:
      var results = new Array[PrincetonComplex](amountPossible)()

      // JHB: need doubles as well
      //double[][] r = new double[amountPossible][];

      //For all the chunks:
      for(times <- 0 to (amountPossible-1)) {
        var complex = new Array[PrincetonComplex](4096)

        // JHB: need 2 array of doubles, real[], imag[] for FFT algorithm
        //double[] real = new double[CHUNK_SIZE];
        //double[] imag = new double[CHUNK_SIZE];

        for(i <- 0 to 4095) {
          //Put the time domain data into a complex number with imaginary part as 0:
          complex(i) = new PrincetonComplex(audio((times*4096)+i), 0)
          //real[i] = audio[(times*CHUNK_SIZE)+i];
          //imag[i] = 0;
        }

        //Perform FFT analysis on the chunk:
        results(times) = PrincetonFFT.fft(complex)
        //r[times] = OrlandoSelenuFFT.fft(real, imag, true);
      }

      results
      //Done!
  }

  // initializing the audio capture thread
  override def preStart() = {
    audioThread.start()
  }

  def receive() = {
    case Greeting => {

      println("tap enter")

      System.in.read()

      println("tap to stop")

      System.in.read()
      stopped = true
      println("This is what the array contains : ")
      line.stop()
      line.close()

      //println(outputArray.toByteArray().map("%02X" format _).mkString)
      println(outputArray.toByteArray().length)
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