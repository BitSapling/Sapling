package com.ghostchu.sapling.manager;

public enum Permission {
    BYPASS_UPLOAD_RECORDING("system.tracker.bypass_upload_recording", "permission.system.tracker.bypass_upload_recording"),
    BYPASS_DOWNLOAD_RECORDING("system.tracker.download.recording", "permission.system.tracker.bypass_download_recording");

    private final String node;
    private final String nodeTranslationKey;

    Permission(String node, String nodeTranslationKey) {
        this.node = node;
        this.nodeTranslationKey = nodeTranslationKey;
    }

    public String getNode() {
        return node;
    }

    public String getNodeTranslationKey() {
        return nodeTranslationKey;
    }
}
