import org.apache.logging.log4j.message.MapMessage
import org.apache.logging.log4j.scala.Logging

import scala.collection.JavaConverters._

object Utils {
  def timer(body: => Unit): Unit = {
    val t1 = System.nanoTime()
    body
    val t2 = System.nanoTime()
    println(s"Elapsed time: ${(t2 - t1) / 1000} us")
  }
}

object MyLogger extends Logging {
  import Utils._

  private def toJava(v: Any): Any = v match {
    case m: Map[_, _] => m.mapValues(toJava).asJava
    case seq: Seq[_] => seq.map(toJava).asJava
    case _ => v
  }

  def info(message: String, fields: (String, Any)*): Unit = timer {
    logger.info(new MapMessage(
      (("message" -> message) +: fields).toMap.mapValues(toJava).asJava
    ))
  }
}

object Main {
  import MyLogger._

  def main(args: Array[String]): Unit = {
    for (_ <- 0 until 100) {
      info("Hello world!", "a" -> "foo", "b" -> 10, "c" -> Seq(1, 2, 3), "d" -> Map("a" -> 1, "b" -> 2))
    }
  }
}
