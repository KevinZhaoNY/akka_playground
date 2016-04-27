package airplane.avionics

import airplane.avionics.ControlSurfaces.StickBack
import akka.pattern.ask
import akka.util.Timeout
import concurrent.duration._
import akka.actor.{ActorRef, Props, ActorSystem}
import scala.concurrent.Await
// The futures created by the ask syntax need an
// execution context on which to run, and we will use the
// default global instance for that context
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by yuanzhao on 4/27/16.
  */
object Avionics {
  val system = ActorSystem("PlaneSimulation")
  val plane = system.actorOf(Props[Plane],"Plane")

  def main(args: Array[String]) {
    // needed for "?"
    implicit val timeout = Timeout(5.seconds)
    val control = Await.result((plane ? Plane.GiveMeControl).mapTo[ActorRef],5.seconds)

    // Takeoff!
    system.scheduler.scheduleOnce(1.seconds){control ! StickBack(0.5f)}
    // Level out
    system.scheduler.scheduleOnce(4.seconds){control ! StickBack(0.0f)}
    // Shut down
    system.scheduler.scheduleOnce(5.seconds) {
      system.terminate()
    }
  }
}
