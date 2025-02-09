package org.wanaku.routers.camel.proxies.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.wanaku.api.types.McpTool;
import org.wanaku.api.types.McpToolContent;
import org.wanaku.api.types.McpToolStatus;
import org.wanaku.api.types.ToolReference;
import org.wanaku.routers.camel.proxies.ToolsProxy;

import static org.wanaku.core.util.IndexHelper.loadToolsIndex;

public class CamelEndpointProxy implements ToolsProxy {
    private final ProducerTemplate producer;

    public CamelEndpointProxy(CamelContext context) {
        this.producer = context.createProducerTemplate();
    }


    @Override
    public List<McpTool> list(File index) {
        final List<McpTool> tools = new ArrayList<>();

        try {
            final List<ToolReference> toolReferences = loadToolsIndex(index);

            for (ToolReference toolReference : toolReferences) {
                McpTool tool = new McpTool();

                tool.description = toolReference.getDescription();
                tool.name = toolReference.getName();
                tool.uri = toolReference.getUri();
                tool.type = toolReference.getType();
                tool.inputSchema = new McpTool.InputSchema();
                tool.inputSchema.type = toolReference.getInputSchema().getType();
                tool.inputSchema.properties = new ArrayList<>();

                for (var refProperty : toolReference.getInputSchema().getProperties().entrySet()) {
                    McpTool.InputSchema.Property schemaProperty = new McpTool.InputSchema.Property();

                    schemaProperty.name = refProperty.getKey();
                    schemaProperty.type = refProperty.getValue().getType();
                    schemaProperty.description = refProperty.getValue().getDescription();
                    tool.inputSchema.properties.add(schemaProperty);
                }

                tools.add(tool);
            }

            return tools;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public McpToolStatus call(McpTool tool, Map<String, Object> properties) {
        McpToolStatus status = new McpToolStatus();
        try {
            producer.start();

            String uri = tool.uri;

            for (var t : tool.inputSchema.properties) {
                Object o = properties.get(t.name);
                uri = uri.replace(String.format("{%s}", t.name), o.toString());
            }

            McpToolContent content = new McpToolContent();
            content.text = producer.requestBody(uri, null,   String.class);
            content.type = "text";

            status.content = List.of(content);
            status.isError = false;
        } finally {
            status.isError = true;
            producer.stop();
        }
        return status;
    }

    @Override
    public String name() {
        return "camel-endpoint";
    }
}
