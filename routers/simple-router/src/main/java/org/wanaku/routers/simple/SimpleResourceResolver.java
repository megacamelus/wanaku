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

package org.wanaku.routers.simple;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.wanaku.api.resolvers.AsyncRequestHandler;
import org.wanaku.api.resolvers.ResourceResolver;
import org.wanaku.api.types.McpRequestStatus;
import org.wanaku.api.types.McpResource;
import org.wanaku.api.types.McpResourceData;
import org.wanaku.api.types.ResourceReference;

import static org.wanaku.core.util.IndexHelper.loadResourcesIndex;

class SimpleResourceResolver implements ResourceResolver {
    private static final Logger LOG = Logger.getLogger(SimpleResourceResolver.class);
    private String resourcesPath;

    public SimpleResourceResolver(String resourcesPath) {
        this.resourcesPath = resourcesPath;
    }

    @Override
    public File indexLocation() {
        return new File(resourcesPath, DEFAULT_RESOURCES_INDEX_FILE_NAME);
    }

    @Override
    public List<McpResource> list() {
        LOG.info("Resolving resources");
        List<McpResource> mcpResources = new ArrayList<>();

        File resourcesFile = indexLocation();

        try {
            List<ResourceReference> references = loadResourcesIndex(resourcesFile);

            for (ResourceReference reference : references) {
                McpResource mcpResource = new McpResource();

                mcpResource.uri = String.format("%s:%s", reference.getType(), reference.getLocation());
                mcpResource.name = reference.getName();
                mcpResource.mimeType = reference.getMimeType();
                mcpResource.description = reference.getDescription();

                mcpResources.add(mcpResource);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return mcpResources;
    }

    @Override
    public List<McpResourceData> read(String uri) {
        URI uriUri = URI.create(uri);
        String filePath = uriUri.getSchemeSpecificPart();

        File file = new File(filePath);
        try {
            McpResourceData data = new McpResourceData();
            data.uri = uri;
            data.text = Files.readString(file.toPath());
            data.mimeType = "text/plain";

            return List.of(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe(String uri, AsyncRequestHandler<McpRequestStatus<McpResourceData>> callback) {

    }
}
