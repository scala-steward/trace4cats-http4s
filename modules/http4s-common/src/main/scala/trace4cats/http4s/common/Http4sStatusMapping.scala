package trace4cats.http4s.common

import org.http4s.Status
import trace4cats.model.SpanStatus

object Http4sStatusMapping {
  val toSpanStatus: Status => SpanStatus = {
    case status @ Status.BadRequest => SpanStatus.Internal(status.reason)
    case Status.Unauthorized => SpanStatus.Unauthenticated
    case Status.Forbidden => SpanStatus.PermissionDenied
    case Status.NotFound => SpanStatus.NotFound
    case Status.TooManyRequests => SpanStatus.Unavailable
    case Status.BadGateway => SpanStatus.Unavailable
    case Status.ServiceUnavailable => SpanStatus.Unavailable
    case Status.GatewayTimeout => SpanStatus.Unavailable
    case status if status.isSuccess => SpanStatus.Ok
    case _ => SpanStatus.Unknown
  }
}
