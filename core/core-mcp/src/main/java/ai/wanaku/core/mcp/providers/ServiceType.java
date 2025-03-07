package ai.wanaku.core.mcp.providers;

/**
 * Defines types of downstream services
 */
public enum ServiceType {
    /**
     * Provides resources
     */
    RESOURCE_PROVIDER("resource-provider"),

    /**
     * Invokes tools
     */
    TOOL_INVOKER("tool-invoker");

    private String value;

    ServiceType(String value) {
        this.value = value;
    }

    /**
     * The string value representing the service type
     * @return
     */
    public String asValue() {
        return value;
    }
}
