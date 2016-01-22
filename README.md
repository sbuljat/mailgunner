# mailgunner
Mailgun integration

### Prerequisites

- [Git](https://git-scm.com/)
- [Java Development Kit](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Scala Build Tool](http://www.scala-sbt.org/download.html)

### How to run it?

    $ git clone https://github.com/sbuljat/mailgunner.git
    $ cd mailgunner
    $ sbt
    $ run
    
    [1] com.sbuljat.mailgunner.MailgunnerCli
    [2] com.sbuljat.mailgunner.RestApplication
    
### Command-line interface
    
    $ run -file [/path/payload.json]
    $ run -json {'to':'john@me.com', 'subject':'TEST', 'body':'<html>blabla</html>' }
    
### REST interface

#### [application/json] POST  /send
Authentication: HEADER X-API-Token (see application.conf for valid tokens)

Regular send. Doesn't keep track of requests in the case of failure.
Works async, returns response as soon it gets available from Mailgun.

#### [application/json] POST  /qsend
Authentication: HEADER X-API-Token  (see application.conf for valid tokens)

Persistent queue send. All requests get persisted to a file store prior to Mailgun send.
This makes system fault-tolerant and on restart replays all unprocessed events.
When a request is persisted user gets information that his request has been queued under given ID.

App would be complete if user can query status of his request by ID.
Another approach is to enable inputting callback url which gets called on successful request.
