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
  import com.github.plokhotnyuk.jsoniter_scala.core._
  import com.github.plokhotnyuk.jsoniter_scala.macros._

  private sealed trait Value
  private case class IntValue(value: Int) extends Value
  private case class StrValue(value: String) extends Value
  private case class MapValue(value: Map[String, Value]) extends Value
  private case class SeqValue(value: Seq[Value]) extends Value

  private def toValue(v: Any): Value = v match {
    case i: Int => IntValue(i)
    case s: String => StrValue(s)
    case m: Map[_, _] => MapValue(m.asInstanceOf[Map[String, Any]].mapValues(toValue))
    case seq: Seq[_] => SeqValue(seq.map(toValue))
    case _ => StrValue(v.toString)
  }

  private case class Message(
    date: Instant,
    level: String,
    message: String,
    data: Map[String, Value],
  )

  private implicit val messageCodec: JsonValueCodec[Message] = JsonCodecMaker.make(
    CodecMakerConfig.withAllowRecursiveTypes(true)
  )

  private implicit val messageValueCodec: JsonValueCodec[Value] = new JsonValueCodec[Value] {
    def decodeValue(in: JsonReader, default: Value): Value =
      throw new NotImplementedError()

    def encodeValue(x: Value, out: JsonWriter): Unit = x match {
      case IntValue(v) => out.writeVal(v)
      case StrValue(v) => out.writeVal(v)
      case SeqValue(vs) =>
        out.writeArrayStart()
        vs.foreach(encodeValue(_, out))
        out.writeArrayEnd()
      case MapValue(m) =>
        out.writeObjectStart()
        m.foreach { case (k, v) => out.writeKey(k); encodeValue(v, out) }
        out.writeObjectEnd()
    }

    def nullValue: Value = StrValue("")
  }

  def info(message: String, fields: (String, Any)*): Unit = timer {
    Logger.info(new Supplier[String] {
      override def get(): String = writeToString(
        Message(Instant.now, "info", message, fields.toMap.mapValues(toValue))
      )
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
