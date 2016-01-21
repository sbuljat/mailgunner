package com.sbuljat.mailgunner.service

import akka.event.Logging
import com.sbuljat.mailgunner.model.{SendMessageResponse, SendMessageRequest}
import spray.http._
import spray.client.pipelining._
import com.sbuljat.mailgunner.util.ApplicationConfig._
import scala.concurrent.Future

/**
  * Created by stipe on 20.1.2016..
  */
trait MailgunApi {

  def send(payload:SendMessageRequest):Future[SendMessageResponse]

}

class MailgunService(actorService:ActorService = new ActorService) extends MailgunApi{
  implicit val sys = actorService.system

  import sys.dispatcher
  val log = Logging(sys, getClass)

  private val pipeline: HttpRequest => Future[HttpResponse] = ( addCredentials(BasicHttpCredentials("api", Mailgun.apiKey)) ~> sendReceive )

  def send(request:SendMessageRequest):Future[SendMessageResponse] = {

    val payload = FormData(Seq("from" -> Mailgun.from, "to" -> request.to, "subject" -> request.subject, "html" -> request.body))

    pipeline(Post(Mailgun.endpoint, payload)).map{response =>
      //log.info(s"Message send status to ${request.to} => ${response.status.toString()} ${response.entity.asString}")
      SendMessageResponse(response.status.isSuccess, response.entity.asString)
    }
  }

  def shutdown = {
    actorService.shutdown()
  }

}
