package airplane.avionics

import akka.actor.{Actor, ActorRef}

/**
  * Created by yuanzhao on 4/27/16.
  */

object EventSource{
  case class Register(listener:ActorRef)
  case class Deregister(listener:ActorRef)
}
trait EventSource extends Actor{
  import EventSource._
  // We're going to use a Vector but many structures would be
  // adequate
  var listeners = Vector.empty[ActorRef]
  // Sends the event to all of our listeners
  def sendEvent[T](event:T) = {
    listeners foreach(_ ! event)
  }

  def eventSourceReceive : Receive = {
    case Register(listener) =>
      listeners = listeners :+ listener
    case Deregister(listener) =>
      listeners = listeners filter(_ != listener)
  }
}
