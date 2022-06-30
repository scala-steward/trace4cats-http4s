package trace4cats.http4s.client

import org.http4s.Uri
import trace4cats.http4s.common.Request_
import trace4cats.model.AttributeValue.{LongValue, StringValue}
import trace4cats.model.{AttributeValue, SemanticAttributeKeys}

object Http4sClientRequest {
  def toAttributes(req: Request_): Map[String, AttributeValue] =
    Map[String, AttributeValue](
      SemanticAttributeKeys.httpFlavor -> s"${req.httpVersion.major}.${req.httpVersion.minor}",
      SemanticAttributeKeys.httpMethod -> req.method.name,
      SemanticAttributeKeys.httpUrl -> req.uri.toString
    ) ++ req.uri.host.map { host =>
      val key = host match {
        case _: Uri.Ipv4Address => SemanticAttributeKeys.remoteServiceIpv4
        case _: Uri.Ipv6Address => SemanticAttributeKeys.remoteServiceIpv6
        case _: Uri.RegName => SemanticAttributeKeys.remoteServiceHostname
      }
      key -> StringValue(host.value)
    }.toMap ++ req.uri.port.map(port => SemanticAttributeKeys.remoteServicePort -> LongValue(port.toLong))
}
