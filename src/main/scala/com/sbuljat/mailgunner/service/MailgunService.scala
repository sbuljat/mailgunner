package com.sbuljat.mailgunner.service

import akka.actor.ActorSystem
import akka.event.Logging
import spray.http._
import spray.client.pipelining._
import com.sbuljat.mailgunner.util.ApplicationConfig._
import scala.concurrent.Future

/**
  * Created by stipe on 20.1.2016..
  */

case class MailgunResponse(success:Boolean, msg:String)

trait MailgunApi {

  def send(to:String, subject:String, html:String):Future[MailgunResponse]

}

class MailgunService extends MailgunApi{
  private implicit val system = ActorSystem("mailgun-client")
  import system.dispatcher
  val log = Logging(system, getClass)

  private val pipeline: HttpRequest => Future[HttpResponse] = ( addCredentials(BasicHttpCredentials("api", Mailgun.apiKey)) ~> sendReceive )

  def send(to:String, subject:String, html:String):Future[MailgunResponse] = {
    val payload = FormData(Seq("from" -> Mailgun.from, "to" -> to, "subject" -> subject, "html" -> html))

    pipeline(Post(Mailgun.endpoint, payload)).map{response =>
      log.info(s"Message send status to $to => ${response.status.toString()} ${response.entity.asString}")
      MailgunResponse(response.status.isSuccess, response.entity.asString)
    }
  }
}
