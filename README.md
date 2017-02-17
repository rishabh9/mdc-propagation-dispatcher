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

    /**
     * @param mat This object is needed to handle streaming of requests
     *            and responses.
     */
    @Inject
    public MappedDiagnosticContextFilter(Materializer mat) {
        super(mat);
    }

    @Override
    public CompletionStage<Result> apply(Function<Http.RequestHeader, CompletionStage<Result>> next,
                                         Http.RequestHeader requestHeader) {
        try {
            return next.apply(requestHeader);
        } finally {
            MDC.remove("X-UserId");
        }
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
<pattern>%d{HH:mm:ss.SSS} %coloredLevel %logger{35} %mdc{X-UserId:--} - %msg%n%rootException</pattern>
```

##### Update your application.conf
```hocon
play {
  akka {
    actor {
      default-dispatcher = {
        type = "MDCPropagatingDispatcherConfigurator"
      }
    }
  }
}
```