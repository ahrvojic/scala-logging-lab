import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import org.tinylog.{Logger, Supplier}

import java.time.Instant

object Utils {
  def timer(body: => Unit): Unit = {
    val t1 = System.nanoTime()
    body
    val t2 = System.nanoTime()
    println(s"Elapsed time: ${(t2 - t1) / 1000} us")
  }
}

object MyLogger {
  import Utils._

  private val mapper = JsonMapper.builder()
    .addModule(new JavaTimeModule)
    .addModule(DefaultScalaModule)
    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    .build()

  private case class Message(
    date: Instant,
    level: String,
    message: String,
    data: Map[String, Any],
  )

  def info(message: String, fields: (String, Any)*): Unit = timer {
    Logger.info(new Supplier[String] {
      override def get(): String =
        mapper.writeValueAsString(Message(Instant.now, "info", message, fields.toMap))
    })
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
