package org.jobrunr;

public class JobRunrError extends Error {

    public static JobRunrError shouldNotHappenError(String message, Exception e) {
        return new JobRunrError(message, e);
    }

    public static JobRunrError shouldNotHappenError(String message, DiagnosticsAware diagnosticsAware) {
        return new JobRunrError(message);
    }

    public static JobRunrError shouldNotHappenError(String message, DiagnosticsAware diagnosticsAware, Exception e) {
        return new JobRunrError(message, e);
    }

    public JobRunrError(String message) {
        super(message);
    }

    public JobRunrError(String message, Throwable cause) {
        super(message, cause);
    }

    public String getDiagnosticsData() {
        return "";
    }

    public interface DiagnosticsAware {

        String getDiagnosticsInfo();

    }
}
