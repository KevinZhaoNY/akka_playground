package airplane.avionics

import akka.actor.{Actor, ActorRef}

/**
  * Created by yuanzhao on 4/27/16.
  */

object ControlSurfaces{
  // amount is a value between -1 and 1.  The altimeter
  // ensures that any value outside that range is truncated
  // to be within it.
  case class StickBack(amount: Float)
  case class StickForward(amount: Float)
}
// Pass in the Altimeter as an ActorRef so that we can send
// messages to it
class ControlSurfaces(altimeter: ActorRef) extends Actor {
  import ControlSurfaces._
  import Altimeter._
  override def receive: Receive = {
    // Pilot pulled the stick back by a certain amount,
    // and we inform the Altimeter that we're climbing
    case StickBack(amount) =>
      altimeter ! RateChange(amount)
    case StickForward(amount) =>
      altimeter ! RateChange(-amount)
  }
}
