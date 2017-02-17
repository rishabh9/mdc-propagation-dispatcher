package zyx.cba;


import org.slf4j.MDC;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

/**
 * @author rishabh
 */
public class MappedDiagnosticContextAction extends Action<EnableMDC> {
    public CompletionStage<Result> call(Http.Context ctx) {
        if (configuration.value()) {
            MDC.put("X-UUID", UUID.randomUUID().toString());
        }
        return delegate.call(ctx).whenComplete((result, throwable) -> {
            if (configuration.value()) {
                MDC.remove("X-UUID");
            }
        });
    }
}
