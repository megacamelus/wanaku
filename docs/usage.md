# Using the Wanaku MCP Router CLI

## Overview

The Wanaku MCP Router CLI provides a simple way to manage resources and tools for your Wanaku MCP Router instance.

## Ways to run the CLI

There are three ways to run the CLI. Choose the one that fits your the best: 

1. Using the container: `podman run quay.io/megacamelus/cli`. This is the recommended way.
2. Using the `wanaku` launcher script from the tarball generated during the build
3. Using the `wanaku` native binary generated during the build.

*NOTE*: the commands below will use `wanaku` as the alias representing any of the above options.

## Quick Getting Started 

Wanaku needs providers and tools to serve and route. The first step is to launch them. Using the containers, you can run

```shell
podman run -p 9000:9000 quay.io/megacamelus/wanaku-routing-http-service
podman run -p 9001:9000 quay.io/megacamelus/wanaku-routing-yaml-route-service
podman run -p 9002:9000 quay.io/megacamelus/wanaku-provider-file
```

Then, launch the Wanaku router 

```shell
podman run -p 8080:8080 quay.io/megacamelus/wanaku-router
```

Then, link the tools and providers to the router:

```shell
wanaku targets tools link --service=http --target=host.docker.internal:9000
wanaku targets tools link --service=camel-route --target=host.docker.internal:9001
wanaku targets resources link --service=file --target=host.docker.internal:9002
```

After the tools and resource providers have been linked with the router, then you can 
start adding tools and resources to be served via MCP using the commands described below.

## Supported Commands

The following commands are currently supported by the Wanaku MCP Router CLI:

### List Resources

* **Command**: `wanaku resources list`
* **Description**: Lists available resources exposed by the Wanaku MCP Router instance.
* **Usage**: Run this command to view a list of available resources, including their names and descriptions.

```markdown
wanaku resources list
```

### Expose Resource

* **Command**: `wanaku resources expose <resource-id>`
* **Description**: Exposes an existing resource to the Wanaku MCP Router instance.
* **Usage**: Replace `<resource-id>` with the ID of the resource you want to expose. For example:

#### Example

Suppose you have a file named `test-mcp-2.txt` on your home directory, and you want to expose it. 
This is how you can do it:

```shell
wanaku resources expose --location=$HOME/test-mcp-2.txt --mimeType=text/plain --description="Sample resource added via CLI" --name="test mcp via CLI" --type=file
```

### List Tools

* **Command**: `wanaku tools list`
* **Description**: Lists available tools on the Wanaku MCP Router instance.
* **Usage**: Run this command to view a list of available tools, including their names and descriptions.

```markdown
wanaku tools list
```

#### Example

```shell
Name               Type               URI
meow-facts      => http            => https://meowfacts.herokuapp.com?count={count}
dog-facts       => http            => https://dogapi.dog/api/v2/facts?limit={count}
```

### Add Tool

* **Command**: `wanaku tools add ${parameters}`
* **Description**: Adds an existing tool to the Wanaku MCP Router instance.
* **Usage**: Replace `<tool-id>` with the ID of the tool you want to add, and `<tool-credentials>` with the credentials for that tool. For example:

#### Example

Here's how you could add a new tool to a Wanaku MCP router instance running locally on http://localhost:8080:

```shell
wanaku tools add -n "meow-facts" --description "Retrieve random facts about cats" --uri "https://meowfacts.herokuapp.com?count={count}" --type http --property "count:int,The count of facts to retrieve" --required count
```

NOTE: For remote instances, you can use the parameter `--host` to point to the location of the instance.

### API Note

All CLI commands use the Wanaku management API under the hood. If you need more advanced functionality or want to automate tasks, you may be able to use this API directly.

By using these CLI commands, you can manage resources and tools for your Wanaku MCP Router instance.

## Tools

### Running Camel Routes as Tools

You can design the routes visually, using [Kaoto](https://kaoto.io/). You need to make sure that the start endpoint for the 
route is `direct:start`. If in doubt, check the [hello-quote.camel.yaml](../samples/routes/camel-route/hello-quote.camel.yaml)
file in the `samples` directory.

To add that route as a tool, you can run something similar to this: 

```shell
wanaku tools add -n "camel-rider-quote-generator" --description "Generate a random quote from a Camel rider" --uri "file:///$(HOME)/code/java/wanaku/samples/routes/camel-route/hello-quote.camel.yaml" --type camel-route --property "_body:string,The data to be passed to the route"
```


## Supported/Tested Client 

Wanaku implements the MCP protocol and, by definition, should support any client that is compliant to the protocol. 

The details below describe how Wanaku MCP router can be used with some prominent MCP clients: 

### LibreChat

For [LibreChat](https://www.librechat.ai/docs) search for `mcpServers` on the `librechat.yml` file and include something similar to this:
   
```
mcpServers:
    everything:
        url: http://host.docker.internal:8080/sse
```

*NOTE*: make sure to point to the correct address of your Wanaku MCP instance.

In LibreChat, you can access Wanaku MCP tools using [Agents](https://www.librechat.ai/docs/features/agents).