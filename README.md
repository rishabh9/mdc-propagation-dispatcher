The Mapped Diagnostic Context (MDC) Propagation Akka Dispatcher
=============

[![](https://jitpack.io/v/rishabh9/mdc-propagation-dispatcher.svg)](https://jitpack.io/#rishabh9/mdc-propagation-dispatcher)

A Mapped Diagnostic Context (MDC) propagation Akka Dispatcher for the asynchronous environment of the Play Framework.

#### Links
1. http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/
2. https://github.com/jroper/thread-local-context-propagation/

#### How To Use

1. Add the JitPack repository to your build file - build.sbt
```scala
resolvers += "jitpack" at "https://jitpack.io"
```
2. Add the dependency
```scala
libraryDependencies += "com.github.rishabh9" % "mdc-propagation-dispatcher" % "v0.0.2"	
```

3. Either add 'MappedDiagnosticContextFilter' to Filters.java
```java
import zyx.cba.MappedDiagnosticContextFilter;

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

Or annotate your controllers/methods with 'EnableMDC' annotation
```java
import zyx.cba.EnableMDC;
import play.mvc.Controller;

@EnableMDC
public class MyController extends Controller {
 // ...
}
```

4. Update your logging configuration
```xml
<pattern>%d{HH:mm:ss.SSS} %coloredLevel %logger{35} %mdc{X-UUID:--} - %msg%n%rootException</pattern>
```

5. Update your application.conf
```hocon
play {
  akka {
    actor {
      default-dispatcher {
        type = "zyx.cba.MDCPropagatingDispatcherConfigurator"
      }
    }
  }
}
```