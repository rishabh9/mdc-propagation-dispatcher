package com.github.rishabh9;

import akka.stream.Materializer;
import org.slf4j.MDC;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Filter;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * @author rishabh
 */
public class MappedDiagnosticContextFilter extends Filter {

    private final HttpExecutionContext ec;

    /**
     * @param mat  This object is needed to handle streaming of requests
     *             and responses.
     * @param ec This class is needed to execute code asynchronously.
     *             It is used below by the <code>thenAsyncApply</code> method.
     */
    @Inject
    public MappedDiagnosticContextFilter(Materializer mat, HttpExecutionContext ec) {
        super(mat);
        this.ec = ec;
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
                ec.current()
        );
    }
}
