package com.sbuljat.mailgunner

import java.util.concurrent.TimeUnit

import com.sbuljat.mailgunner.json.SendMessageRequestJsonProtocol
import com.sbuljat.mailgunner.model.{SendMessageResponse, SendMessageRequest}
import com.sbuljat.mailgunner.service.{ActorService, MailgunService}
import spray.json._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success, Try}

/**
  * Created by stipe on 21.1.2016..
  */
object MailgunnerCli extends SendMessageRequestJsonProtocol{

  def main (args: Array[String]): Unit = {
    println("Mailgunner: Simple Mailgun tool")

    getSendInstruction(args) match {
      case Success(payload) =>

        val service = new MailgunService()
        Await.result(service.send(payload), atMost = Duration(10, TimeUnit.SECONDS)) match {
          case SendMessageResponse(true, _) =>
            println(s"SUCCESS: Message sent to ${payload.to}")
          case SendMessageResponse(false, msg) =>
            println(s"ERROR: $msg")
        }

        service.shutdown

      case Failure(ex) =>
        println("ERROR: " + ex.getMessage)
        println(usage)
    }


  }

  // parse out SendMessageRequest from input arguments
  private def getSendInstruction(args:Array[String]):Try[SendMessageRequest] = Try{
    args match {
      case Array("-json", json @ _*) =>
        json.mkString("").replaceAll("'", "\"").parseJson.convertTo[SendMessageRequest]
      case _ =>
        throw new Exception("Missing arguments")
    }
  }

  private def usage = {
    """USAGE: -json { 'to':'email','subject':'message title','body':'message content' }""".stripMargin
  }

}
