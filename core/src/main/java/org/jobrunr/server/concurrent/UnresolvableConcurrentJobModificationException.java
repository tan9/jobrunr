package org.jobrunr.server.concurrent;

import org.jobrunr.JobRunrError;
import org.jobrunr.jobs.Job;
import org.jobrunr.storage.ConcurrentJobModificationException;

import java.util.List;
import java.util.stream.Collectors;

public class UnresolvableConcurrentJobModificationException extends ConcurrentJobModificationException implements JobRunrError.DiagnosticsAware {

    private List<ConcurrentJobModificationResolveResult> concurrentJobModificationResolveResults;

    public UnresolvableConcurrentJobModificationException(List<ConcurrentJobModificationResolveResult> concurrentJobModificationResolveResults) {
        super(concurrentJobModificationResolveResults.stream().map(ConcurrentJobModificationResolveResult::getLocalJob).collect(Collectors.toList()));
        this.concurrentJobModificationResolveResults = concurrentJobModificationResolveResults;
    }

    @Override
    public String getDiagnosticsInfo() {
        StringBuilder result = new StringBuilder();
        result.append("Concurrent modified jobs:").append("\n");
        concurrentJobModificationResolveResults.forEach(resolveResult -> appendDiagnosticsInfo(result, resolveResult));
        return result.toString();
    }

    private void appendDiagnosticsInfo(StringBuilder result, ConcurrentJobModificationResolveResult resolveResult) {
        Job localJob = resolveResult.getLocalJob();
        Job jobFromStorage = resolveResult.getJobFromStorage();

        result.append("\t").append("Job id: " + localJob.getId()).append("\n");
        result.append("\t\t").append("Local version: " + localJob.getVersion() + "; Storage version: " + jobFromStorage.getVersion()).append("\n");
        result.append("\t\t").append("Local state: " + localJob.getState() + "; Storage state: " + jobFromStorage.getState()).append("\n");
        result.append("\t\t").append("Local last updated: " + localJob.getUpdatedAt() + "; Storage last updated: " + jobFromStorage.getUpdatedAt()).append("\n");
    }
}
