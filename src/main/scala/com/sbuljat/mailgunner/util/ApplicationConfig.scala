package com.sbuljat.mailgunner.util

import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

import scala.util.Try

/**
  * Created by stipe on 20.1.2016.
  *
  * Application configuration singleton, all config parameters should go here.
  */
object ApplicationConfig {
  private val config = ConfigFactory.load.getConfig("application")

  val timeout = config.getInt("timeout.seconds").seconds
  val httpInterface = config.getString("http.interface")
  val httpPort = config.getInt("http.port")

  object Mailgun{
    val apiKey = config.getString("mailgun.api-key")
    val endpoint = config.getString("mailgun.endpoint")
    val from = config.getString("mailgun.from")
  }

  object Template{

    // try to locate template in application configuration, if found replace all placeholders {name} with given vars, if any.
    def template(templateName:String, params:Map[String,String]):Try[String] = Try{
      val content = config.getConfig("template").getString(templateName)
      params.foldLeft(content){ case (agg,next) => agg.replaceAll( s"\\{${next._1}\\}", next._2) }
    }

  }

}
