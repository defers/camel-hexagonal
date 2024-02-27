# Project with Apache Camel example with hexagonal architecture

api: http://localhost:8090/api/
swagger ui: http://localhost:8090/swagger-ui/index.html
open api doc: http://localhost:8090/api/api-doc

## Run application

From shell:

```shell
java -jar camel-<version-number>.jar
```

For running OpenTelemetry java agent and export traces to jaeger start app with jvm arguments:
```shell
-javaagent:opentelemetry-javaagent.jar
-Dotel.traces.exporter=otlp
-Dotel.metrics.exporter=logging
-Dotel.logs.exporter=logging
-Dotel.exporter.jaeger.endpoint="http://localhost:14250" # Export to jaeger
-Dotel.exporter.otlp.traces.endpoint="http://localhost:4318/v1/traces" # Export to OTLP via http
-Dotel.service.name=camel
```

