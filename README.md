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

- **[application/json] POST  /send**
  Regular send. Doesn't keep track of requests in the case of failure.

- **[application/json] POST  /qsend**
  Persisted queue send
