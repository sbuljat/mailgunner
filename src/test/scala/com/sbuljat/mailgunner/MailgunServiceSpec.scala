package com.sbuljat.mailgunner

import java.util.concurrent.TimeUnit

import com.sbuljat.mailgunner.service.MailgunService
import org.specs2.mutable.Specification

import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by stipe on 20.1.2016..
  */
class MailgunServiceSpec extends Specification{
  val BODY_HELLOWORLD = "<html><body><h1>Hello World!</b></body></html>"

  "MailgunService" should {

    "send message to 'sbuljat@gmail.com' without issues" in {
      Await.result( new MailgunService().send("sbuljat@gmail.com", "Mailgun TEST", BODY_HELLOWORLD), atMost = Duration.apply(10, TimeUnit.SECONDS) ).success must beTrue
    }

  }

}
