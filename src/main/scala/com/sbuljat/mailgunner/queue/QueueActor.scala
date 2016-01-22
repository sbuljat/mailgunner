package com.sbuljat.mailgunner.queue

import java.util.UUID

import akka.actor.{ActorLogging, Actor}
import akka.persistence.{RecoveryCompleted, PersistentActor}
import com.sbuljat.mailgunner.model.{SendMessageResponse, SendMessageRequest}
import com.sbuljat.mailgunner.service.MailgunService

/**
  * Created by stipe on 22.1.2016.
  *
  * Actor which takes care of queue of requests. Queue is persistent using LevelDB.
  * When an event is persisted then it gets processed. On successful processing new event with status done=true gets persisted.
  * On recovery only events, same id, that haven't been processed are reprocessed.
  *
  * To handle proper notification we should implement some kind of callback, e.g. give users possibility to input callback url which gets called
  * on every successful request.
  *
  */

case class Persist(event:Event)
case class Event(request:SendMessageRequest, response:Option[SendMessageResponse]=None)

class QueueActor extends PersistentActor with ActorLogging{
  override def persistenceId = "mail-queue"

  implicit def executionContext = context.dispatcher

  val mailgun = new MailgunService()

  var state = scala.collection.mutable.MutableList[Event]()

  val receiveRecover:Receive = {
    case event:Event =>
      state += event
    case RecoveryCompleted =>
      state.groupBy(e => e.request.id).foreach{ case (id,events) =>
        events.toList match {
          case first :: second :: Nil if(second.response.nonEmpty || first.response.nonEmpty) =>
            log.info(s"[RECOVERY] Fully processed event ${first.request.id}")
          case first :: Nil =>
            log.info(s"[RECOVERY] [${first.request.id}] Unprocessed request ${first.request}")
            mailgun.send(first.request).map(res =>
              self ! Persist(Event(res.request, Some(res)))
            )
          case _ =>
        }
      }

  }

  val receiveCommand:Receive = {
    case cmd:Persist =>
      persistAsync(cmd.event){e =>
        log.info(s"[COMMAND] Persisted ${e.request.id}")
      }
    case event:Event =>
      persistAsync(event){e =>
        sender() ! SendMessageResponse(true, s"Message has been queued under id ${e.request.id}", event.request)
        mailgun.send(event.request).map(res =>
          if(res.success){
            self ! Persist(Event(res.request, Some(res)))
          }else{
            log.warning(s"[COMMAND] Mailgun request ${res.request.id} failed")
          }
        )
      }
  }

}
