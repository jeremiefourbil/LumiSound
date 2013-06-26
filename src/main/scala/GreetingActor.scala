package fr.lumisound

import akka.kernel.Bootable
import scala.util.Random
import com.typesafe.config.ConfigFactory
import akka.actor.{ ActorRef, Props, Actor, ActorSystem }
import scala.collection.immutable.IndexedSeq

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general._
import org.jfree.data._
import org.jfree.chart.ChartPanel
import org.jfree.data.category._
import org.jfree.chart.plot._

import javax.swing.JFrame
import javax.swing.JPanel
import java.awt.event._

class VisualizerActor extends Actor {

	// Create a simple bar chart
  var barDataset = new DefaultCategoryDataset()
  val chart = ChartFactory.createBarChart(
          "LumiSound",
          null,
          null,
          barDataset,
          PlotOrientation.VERTICAL,
          false,
          false,
          false)

  // Add some parameters to have a "beautiful" visualization
  val plot = chart.getPlot().asInstanceOf[CategoryPlot]
  val axis = plot.getRangeAxis()
  axis.setAutoRange(false)
  axis.setVisible(false)
  axis.setUpperBound(1.5)
  
  val yAxis = plot.getDomainAxis()
  yAxis.setVisible(false)

  val frame = new JFrame("LumiSound")
  frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )

  val remoteAudioRecorder = context.system.actorFor("akka://MySystem@127.0.0.1:2552/user/audiorecorder")
  val windowList = new MyWindowListener(remoteAudioRecorder, self)
  frame.addWindowListener(windowList)

  frame.setSize(640,420)
  frame.add( new ChartPanel(chart) )
  frame.pack()
  frame.setVisible(true)

  def receive = {
    case Stop => { 
      println("stopping client ...")
      context.system.shutdown
    }
    case Connected => { println("Connected !!!") }
	  case Result(fft) => { 

      barDataset.clear()

      fft.zipWithIndex.map( x => barDataset.addValue(x._1/500,"",""+x._2))
		  frame.repaint()
	  }
  }
}

class MyWindowListener(actorServer: ActorRef, thisActor: ActorRef) extends WindowListener {
  
  def windowClosing(x: WindowEvent) {
    actorServer.tell(Disconnected, thisActor)
    thisActor ! Stop
  }

  def windowActivated(x: WindowEvent) {}
  def windowClosed(x: WindowEvent) {}
  def windowDeactivated(x: WindowEvent) {}
  def windowDeiconified(x: WindowEvent) {}
  def windowIconified(x: WindowEvent) {}
  def windowOpened(x: WindowEvent) {}
}