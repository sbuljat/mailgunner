package com.sbuljat.mailgunner.service

import akka.actor.ActorSystem

/**
  * Created by stipe on 21.1.2016..
  */
class ActorService {
  val system = ActorSystem("mailgunner-system")


  def shutdown(): Unit = {
    system.terminate()
  }

}
