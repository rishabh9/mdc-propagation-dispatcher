The Mapped Diagnostic Context (MDC) Propagation Akka Dispatcher
=============

[![](https://jitpack.io/v/rishabh9/mdc-propagation-dispatcher.svg)](https://jitpack.io/#rishabh9/mdc-propagation-dispatcher)

A Mapped Diagnostic Context (MDC) propagation Akka Dispatcher for the asynchronous environment of the Play Framework.

#### Links
1. http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/
2. https://github.com/jroper/thread-local-context-propagation/

#### How To Use

##### Configure Jitpack
1. Add the JitPack repository to your build file - build.sbt
```scala
resolvers += "jitpack" at "https://jitpack.io"
```
2. Add the dependency
```scala
libraryDependencies += "com.github.rishabh9" % "mdc-propagation-dispatcher" % "v0.0.1"	
```

##### Create a filter
```java
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
        MDC.put("X-UUID", java.util.UUID.randomUUID());
        return next.apply(requestHeader).thenApplyAsync(
                result -> {
                    MDC.remove("X-UUID");
                    return result;
                },
                exec
        );
    }
}
```

##### Add to Filters.java
```java
@Singleton
public class Filters implements HttpFilters {
    private final MappedDiagnosticContextFilter mdcFilter;
    
    @Inject
    public Filters(MappedDiagnosticContextFilter mdcFilter) {
        this.mdcFilter = mdcFilter;
    }

    @Override
    public EssentialFilter[] filters() {
        final EssentialFilter[] filters = {
                mdcFilter.asJava()
        };
        return filters;
    }
}
```

##### Update your logging configuration
```xml
<pattern>%d{HH:mm:ss.SSS} %coloredLevel %logger{35} %mdc{X-UUID:--} - %msg%n%rootException</pattern>
```

##### Update your application.conf
```hocon
play {
  akka {
    actor {
      default-dispatcher {
        type = "MDCPropagatingDispatcherConfigurator"
      }
    }
  }
}
```