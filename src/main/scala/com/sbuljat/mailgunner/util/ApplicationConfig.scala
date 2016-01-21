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

}
