import { defineConfig } from 'orval';

export default defineConfig({
    wanaku: {
        input: '../routers/wanaku-router/src/main/webui/openapi.json',
        output: './src/api/wanaku-router-api.ts',
        hooks: {
            afterAllFilesWrite: 'prettier --write',
        },
    },
});