package org.jobrunr.utils.diagnostics;

import org.jobrunr.JobRunrError;
import org.jobrunr.configuration.JobRunr;
import org.jobrunr.server.BackgroundJobServer;
import org.jobrunr.utils.metadata.VersionRetriever;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class Diagnostics {

    private static Set<BackgroundJobServer> backgroundJobServers = new HashSet<>();

    public static void registerBackgroundJobServer(BackgroundJobServer backgroundJobServer) {
        backgroundJobServers.add(backgroundJobServer);
    }

    public static Optional<String> toDiagnosticsFile(JobRunrError jobRunrException) {
        return Optional.empty();
    }

    private static String createDiagnosticsContent(JobRunrError jobRunrException) {
        StringBuilder sb = new StringBuilder();
        sb.append("// ================ JobRunr diagnostics report ================");
        sb.append("// This file may contain details of your job.");
        sb.append("// If these are confidential do not attach this file to a Github issue but mail them to rona");
        sb.append("version: ").append(VersionRetriever.getVersion(JobRunr.class)).append("\n");
        sb.append("java version: ").append(System.getProperty("java.version")).append("\n");
        sb.append("storage provider: [").append(getStorageProviders()).append("]\n");
        sb.append("exception data:").append("\n");
        sb.append(jobRunrException.getDiagnosticsData());
        return sb.toString();
    }

    private static String getStorageProviders() {
        return backgroundJobServers.stream().map(bjs -> bjs.getStorageProvider().getClass().getName()).collect(Collectors.joining(", "));
    }

}
