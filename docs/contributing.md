# Contributing

To contribute new features and connectors, read the [Wanaku MCP Router Internals](wanaku-router-internals.md) guide.

If you want to understand what each of the components do, then read the [Wanaku Components and Architecture](architecture.md) guide.

## Tools and Providers 

A tool is anything that can operate in a request/reply mode.
A provider is anything that can read a resource.
Tools typically mean that some processing is performed on the provided input. 
Providers typically facilitate access to a resource (such as file) without necessarily processing an input (expect, of course, evaluating the name of the resource and how that matches with the underlying system storing the resource).

Here are some examples: 

* Producing a record to Kafka and waiting for a response in another topic is a tool
* Reading the last record on a Kafka topic is a provider 
* Running an SQL query on a database is a tool (i.e., the query is the request, and returned rows are the response).
* Reading a data object in a S3 bucket is a provider 
* Exchanging data using request/reply over JMS is a tool

**NOTE**: this is a generic explanation and the distinction may be specific to the problem domain. Therefore, there may be cases where this doesn't apply. 

## Creating New Tools

To create a new tool for Wanaku, you can start by creating a new project. For instance, to create one for Kafka:
 
```shell
mvn -B archetype:generate -DarchetypeGroupId=org.wanaku -DarchetypeArtifactId=wanaku-tool-service-archetype   -DarchetypeVersion=1.0.0-SNAPSHOT -DgroupId=org.wanaku -Dpackage=org.wanaku.routing.service -DartifactId=wanaku-routing-kafka-service -Dname=Kafka
```

**NOTE**: this can be used both to create a core tool, part of the Wanaku MCP router project, or to create a custom one for your own needs.

Then, open the `pom.xml` file to add the dependencies for your project. Using the example above, we would include the following dependencies:

```xml
    <dependency>
        <groupId>org.apache.camel.quarkus</groupId>
        <artifactId>camel-quarkus-kafka</artifactId>
    </dependency>
```

Adjust the gPRC port in the `application.properties` file by adjusting the `quarkus.grpc.server.port` property.

**NOTE**: you can also provide the port when launching (i.e., `java -Dquarkus.grpc.server.port=9190 -jar target/quarkus-app/quarkus-run.jar`)

Then, build the project:

```shell
mvn clean package
```

And run it: 

```shell
java -jar target/quarkus-app/quarkus-run.jar
```


After launching, then link the instance 

```shell
wanaku targets tools link --service=kafka --target=localhost:9190
```

**NOTE**: make sure to replace `kafka` with the actual service type you are exposing.

## Creating new Providers


To create a new resource for Wanaku, you can start by creating a new project. For instance, to create one for S3:

```shell
mvn -B archetype:generate -DarchetypeGroupId=org.wanaku -DarchetypeArtifactId=wanaku-provider-archetype -DarchetypeVersion=1.0.0-SNAPSHOT  -DgroupId=org.wanaku -Dpackage=org.wanaku.provider -DartifactId=wanaku-provider-s3 -Dname=S
3
```

**NOTE**: this can be used both to create a core provider, part of the Wanaku MCP router project, or to create a custom one for your own needs.

Then, open the `pom.xml` file to add the dependencies for your project. Using the example above, we would include the following dependencies:

```xml
<dependency>
    <groupId>org.apache.camel.quarkus</groupId>
    <artifactId>camel-quarkus-aws-s3</artifactId>
</dependency>
```

Adjust the gPRC port in the `application.properties` file by adjusting the `quarkus.grpc.server.port` property.

**NOTE**: you can also provide the port when launching (i.e., `java -Dquarkus.grpc.server.port=9190 -jar target/quarkus-app/quarkus-run.jar`)

Then, build the project:

```shell
mvn clean package
```

And run it:

```shell
java -jar target/quarkus-app/quarkus-run.jar
```

After launching, then link the instance

```shell
wanaku targets resources link --service=s3 --target=localhost:9190
```

**NOTE**: make sure to replace `s3` with the actual service type you are exposing.


## Building Containers


You can also build containers using:

```shell
mvn -Pdist -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true clean package
```

For custom containers, please make sure you set the properties `quarkus.container-image.registry` and `quarkus.container-image.group`.
You can do that `pom.xml`:

```xml
<quarkus.container-image.registry>quay.io</quarkus.container-image.registry>
<quarkus.container-image.group>my-group</quarkus.container-image.group>
```

Or in the CLI:

```shell
mvn -Pdist -Dquarkus.container-image.registry=quay.io -Dquarkus.container-image.group=my-group -Dquarkus.container-image.build=true -Dquarkus.container-image.push=true clean package
```