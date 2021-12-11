package todo.config

import io.undertow.Undertow.Builder
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.websocket.servlet.UndertowWebSocketServletWebServerCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowBuilderCustomizer
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory
import org.springframework.context.annotation.Configuration
import org.xnio.Options

/**
 * Konfiguriert den Undertow-Webserver, insbesondere dessen Worker-ThreadPool.
 *
 * Motivation: Undertows Worker-Thread-Pool vergrößert sich mit jedem Request, bis er die maximale
 * Thread-Anzahl erreicht hat (bei 8 CPU-Kernen immerhin 64 Threads). Dort verharrt der ThreadPool
 * und verbrät Heap-Space.
 * <p>
 * Ursache: Undertow setzt auf die alte XNIO-Version 3.3 mit suboptimaler
 * ThreadPool-Implementierung, welche `corePoolSize` und `maximumPoolSize` gleichsetzt (siehe
 * <a href=
 * "https://github.com/xnio/xnio/blob/3.3/api/src/main/java/org/xnio/XnioWorker.java">GitHub</a>).
 * Die Optionen org.xnio.Options.WORKER_TASK_CORE_THREADS und org.xnio.Options.WORKER_TASK_KEEPALIVE
 * sind wirkungslos.
 * <p>
 * Lösung: Mit der neuesten XNIO-Version 3.6 ist der ThreadPool ausgereifter. Die `corePoolSize`
 * kann konfiguriert werden, wovon diese Config-Klasse Gebrauch macht. Jedoch: Red Hat gibt weder
 * Release-Informationen über den Unterschied der XNIO-Versionen preis, noch lässt sich
 * herausfinden, welche Probleme die Kombination Undertow 1.4 und XNIO 3.6 mit sich bringt. Zudem
 * stimmt skeptisch, dass auch Undertow 2.0 auf XNIO 3.3 setzt. Wir probieren's trotzdem.
 * <p>
 */
@Configuration
class ConfigUndertow : UndertowWebSocketServletWebServerCustomizer() {

  @Value("\${server.undertow.worker-core-threads:-1}")
  var workerCoreThreads: Int = -1

  @Value("\${server.undertow.worker-keepalive:60000}")
  var workerKeepAlive: Int = 60000

  override fun customize(factory: UndertowServletWebServerFactory) {
    val cores = Runtime.getRuntime().availableProcessors()

    factory.addBuilderCustomizers(
      UndertowBuilderCustomizer { builder: Builder ->
        builder.setWorkerOption(
          Options.WORKER_TASK_CORE_THREADS,
          if (workerCoreThreads > -1) workerCoreThreads else 2 * cores
        )
        builder.setWorkerOption(Options.WORKER_TASK_KEEPALIVE, workerKeepAlive)
      }
    )
  }
}
