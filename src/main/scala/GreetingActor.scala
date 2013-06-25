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
import org.jfree.chart.plot.PlotOrientation

import javax.swing.JFrame
import javax.swing.JPanel

class GreetingActor extends Actor {

	// Create a simple pie chart
    var pieDataset = new DefaultCategoryDataset();
    val chart = ChartFactory.createBarChart(
           "Hello World",
           "cat",
           "val",
            pieDataset,
            PlotOrientation.VERTICAL,
            false,
            false,
            false);
 
        println("Hello world");
 
        val frame = new JFrame("Hello Pie World")
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE )
 
        frame.setSize(640,420)
        frame.add( new ChartPanel(chart) )
        frame.pack()
        frame.setVisible(true)

  def receive = {
    case Greeting(who) => { println("Hello " + who); context.system.shutdown() }
    case Connected => { println("Connected !!!") }
	case Result(fft) => { //println(fft) 

		fft.zipWithIndex.map( x => pieDataset.addValue(x._1/500,"lol",""+x._2))
		frame.repaint()
	}
  }
}