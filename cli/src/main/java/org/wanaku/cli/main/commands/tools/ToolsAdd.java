/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wanaku.cli.main.commands.tools;

import java.net.URI;
import java.util.List;

import io.quarkus.rest.client.reactive.QuarkusRestClientBuilder;
import org.jboss.logging.Logger;
import org.wanaku.api.types.ToolReference;
import org.wanaku.cli.main.commands.BaseCommand;
import org.wanaku.cli.main.services.ToolsService;
import org.wanaku.cli.main.support.PropertyHelper;
import picocli.CommandLine;

@CommandLine.Command(name = "add",description = "Add tools")
public class ToolsAdd extends BaseCommand {
    private static final Logger LOG = Logger.getLogger(ToolsAdd.class);

    @CommandLine.Option(names = {"--host"}, description = "The API host", defaultValue = "http://localhost:8080",
            arity = "0..1")
    protected String host;

    @CommandLine.Option(names = {"-n", "--name"}, description="Name of the tool reference", required = true)
    private String name;

    @CommandLine.Option(names = {"-d", "--description"}, description="Description of the tool reference", required = true)
    private String description;

    @CommandLine.Option(names = {"-u", "--uri"}, description="URI of the tool", required = true)
    private String uri;

    @CommandLine.Option(names = {"--type"}, description="Type of the tool reference (i.e: http)", required = true)
    private String type;

    @CommandLine.Option(names = {"--input-schema-type"},
            description = "Type of input schema (e.g., 'string', 'object')",
            defaultValue = "object")
    private String inputSchemaType;

    @CommandLine.Option(names = {"-p", "--property"},
            description = "Property name and value (e.g., '--property name:type,description)")
    private List<String> properties;

    @CommandLine.Option(names = {"-r", "--required"},
            description = "List of required property names (e.g., '-r foo bar')", arity = "0..*")
    private List<String> required;

    ToolsService toolsService;

    @Override
    public void run() {
        ToolReference toolReference = new ToolReference();

        toolReference.setName(name);
        toolReference.setDescription(description);
        toolReference.setUri(uri);
        toolReference.setType(type);

        ToolReference.InputSchema inputSchema = new ToolReference.InputSchema();
        inputSchema.setType(inputSchemaType);

        if (properties != null) {
            for (String propertyStr : properties) {
                PropertyHelper.PropertyDescription result = PropertyHelper.parseProperty(propertyStr);

                ToolReference.Property property = new ToolReference.Property();
                property.setType(result.dataType());
                property.setDescription(result.description());

                inputSchema.getProperties().put(result.propertyName(), property);
            }
        }

        toolReference.setInputSchema(inputSchema);

        if (required != null) {
            inputSchema.setRequired(required);
        }

        toolsService = QuarkusRestClientBuilder.newBuilder()
                .baseUri(URI.create(host))
                .build(ToolsService.class);

        toolsService.add(toolReference);
    }

}
