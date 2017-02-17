package zyx.cba;

import akka.stream.Materializer;
import org.slf4j.MDC;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;
import java.util.function.Function;

/**
 * @author rishabh
 */
public class MappedDiagnosticContextFilter extends Filter {

    private final Executor exec;

    /**
     * @param mat  This object is needed to handle streaming of requests
     *             and responses.
     * @param exec This class is needed to execute code asynchronously.
     *             It is used below by the <code>thenAsyncApply</code> method.
     */
    @Inject
    public MappedDiagnosticContextFilter(Materializer mat, Executor exec) {
        super(mat);
        this.exec = exec;
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next,
                                         Http.RequestHeader requestHeader) {

        MDC.put("X-UUID", UUID.randomUUID().toString());
        return next.apply(requestHeader).thenApplyAsync(
                result -> {
                    MDC.remove("X-UUID");
                    return result;
                },
                exec
        );
    }
}
