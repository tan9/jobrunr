package org.jobrunr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class JobRunrError extends Error {

    private DiagnosticsAware diagnosticsAware;

    public static JobRunrError canNotHappenError(Exception e) {
        return new JobRunrError("JobRunr encountered an unexpected exception. Please create a bug report.", null, e);
    }

    public static JobRunrError shouldNotHappenError(String message, Exception e) {
        return new JobRunrError(message, null, e);
    }

    public static JobRunrError shouldNotHappenError(String message, DiagnosticsAware diagnosticsAware) {
        return new JobRunrError(message, diagnosticsAware);
    }

    public static JobRunrError shouldNotHappenError(String message, DiagnosticsAware diagnosticsAware, Throwable e) {
        return new JobRunrError(message, diagnosticsAware, e);
    }

    public JobRunrError(String message, DiagnosticsAware diagnosticsAware) {
        super(message);
        this.diagnosticsAware = diagnosticsAware;

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/jobrunr.diag"));
            writer.write(diagnosticsAware.getDiagnosticsInfo());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public JobRunrError(String message, DiagnosticsAware diagnosticsAware, Throwable cause) {
        super(message, cause);
        this.diagnosticsAware = diagnosticsAware;

        if (diagnosticsAware != null) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("/tmp/jobrunr.diag"));
                writer.write(diagnosticsAware.getDiagnosticsInfo());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getDiagnosticsData() {
        return "";
    }

    public interface DiagnosticsAware {
        String getDiagnosticsInfo();
    }
}
