package org.nextprot.parser.core.actor
import akka.actor.{ ActorRef, ActorSystem, Props, Actor, Inbox }
import java.io.File
import java.io.OutputStream
import scala.Array.canBuildFrom
import org.nextprot.parser.core.datamodel.AnnotationListWrapper
import java.io.FileWriter
import scala.collection.mutable.ArrayBuffer
import org.nextprot.parser.core.datamodel.AnnotationListWrapper
import akka.routing.RoundRobinLike
import akka.routing.RoundRobinRouter
import java.util.Collection
import org.nextprot.parser.core.actor.message.EndActorSystemMSG
import org.nextprot.parser.core.exception.NXException
import org.nextprot.parser.core.exception.NXException
import org.nextprot.parser.core.NXProperties

/**
 * Actor responsible responsible to print a final report and shutdown the actor system.
 * @author Daniel Teixeira
 */

class NXListener extends Actor {

  val time = System.nanoTime();

  def receive = {
    case m: EndActorSystemMSG => {
      printStats(m.success, m.errors, m.files);
      context.system.shutdown()
    }
    case _ => {
      println("Unexpected message received ")
    }
  }

  def printStats(success: Int, errors: Traversable[NXException], files: List[File]) = {
    var existError = false;
    println
    println("############################################## Parsing statistics #########################################################")
    println("From a total of " + files.size + " entries: " + success + " entries were successfully parsed and " + errors.size + " discarded.");
    println("----------------------------------------------------------------------------------------------------------------------------")
    println("Discard / Error cases: ")
    errors.groupBy(e => e.getNXExceptionType).toList.sortWith(_._2.size > _._2.size).map(
      f => {
        if (f._1.isError) {
          existError = true;
          println("\tERROR - Found " + f._2.size + " errors (" + f._1.getClass().getSimpleName() + "): " + f._1.description)
        } else {
          println("\tINFO - Found " + f._2.size + " discarded cases (" + f._1.getClass().getSimpleName() + "): " + f._1.description)
        }
      })
    println("----------------------------------------------------------------------------------------------------------------------------")
    println("Finished in " + (System.nanoTime - time) / 1e9 + " seconds")
    println("----------------------------------------------------------------------------------------------------------------------------")
    println("Ouput file: " + System.getProperty(NXProperties.outputFileProperty))
    if (existError) {
      println("Some errors occured, quelle misere...: " + System.getProperty(NXProperties.failedFileProperty));

    } else {
      println("Parsing fully successful. Bravo!")
    } 
  }

}

