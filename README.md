The Mapped Diagnostic Context (MDC) Propagation Akka Dispatcher
=============

A Mapped Diagnostic Context (MDC) propagation Akka Dispatcher for the asynchronous environment of the Play Framework.

Took the idea from here:

1. http://yanns.github.io/blog/2014/05/04/slf4j-mapped-diagnostic-context-mdc-with-play-framework/
2. https://github.com/jroper/thread-local-context-propagation/

The provided code logs an UUID, per request, by default.

Refer the code and customize to your whims and fancy.

> Created this project so that I can have the dispatcher in a separate jar, to make my development easier.

#### How To Use

###### Add the dependency
```scala
libraryDependencies += "com.github.rishabh9" %% "mdc-propagation-dispatcher" % "0.0.5"	
```

###### Either add 'MappedDiagnosticContextFilter' to Filters.java
```java
import com.github.rishabh9.MappedDiagnosticContextFilter;

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

###### Or annotate your controllers/methods with 'EnableMDC' annotation
```java
import com.github.rishabh9.EnableMDC;
import play.mvc.Controller;

@EnableMDC
public class MyController extends Controller {
 // ...
}
```

###### Update your logging configuration
```xml
<pattern>%d{HH:mm:ss.SSS} %coloredLevel %logger{35} %mdc{X-UUID:--} - %msg%n%rootException</pattern>
```

###### Update your application.conf
```hocon
play {
  akka {
    actor {
      default-dispatcher {
        type = "com.github.rishabh9.MDCPropagatingDispatcherConfigurator"
      }
    }
  }
}
```