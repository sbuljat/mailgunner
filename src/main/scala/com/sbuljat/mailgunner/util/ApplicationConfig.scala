package com.sbuljat.mailgunner.util

import com.typesafe.config.ConfigFactory

/**
  * Created by stipe on 20.1.2016.
  *
  * Application configuration singleton, all config parameters should go here.
  */
object ApplicationConfig {
  private val config = ConfigFactory.load.getConfig("application")

  object Mailgun{
    val apiKey = config.getString("mailgun.api-key")
    val endpoint = config.getString("mailgun.endpoint")
    val from = config.getString("mailgun.from")
  }

  object Template{

    def template(templateName:String, params:Map[String,String]):String = {
      val content = config.getConfig("template").getString(templateName)
      params.foldLeft(content){ case (agg,next) => agg.replaceAll( s"\\{${next._1}\\}", next._2) }
    }

  }

}
