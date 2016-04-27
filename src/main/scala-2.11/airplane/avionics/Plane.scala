package airplane.avionics

import akka.actor.Actor.Receive
import akka.actor.{Props, ActorRef, ActorLogging, Actor}

/**
  * Created by yuanzhao on 4/27/16.
  */

object Plane{

  case object GiveMeControl

}
class Plane extends Actor with ActorLogging{
  import Plane._
  import Altimeter._
  val altimeter = context.actorOf(Props[Altimeter],"Altimeter")
  val controls = context.actorOf(Props(new ControlSurfaces(altimeter)), "ControlSurfaces")
  override def receive: Receive = {
    case GiveMeControl =>
      log info "Plane giving control."
      sender ! controls
    case AltitudeUpdate(altitude) =>
      log info s"Altitude is now: $altitude"
  }

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    import EventSource._
    altimeter ! Register(self)
  }
}
