package org.jobrunr.jobs.details.instructions;

import org.jobrunr.JobRunrError;
import org.jobrunr.jobs.details.JobDetailsFinderContext;

public abstract class AbstractJVMInstruction {

    protected final JobDetailsFinderContext jobDetailsBuilder;

    public AbstractJVMInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        this.jobDetailsBuilder = jobDetailsBuilder;
    }

    public abstract Object invokeInstruction();

    public void invokeInstructionAndPushOnStack() {
        try {
            Object result = invokeInstruction();
            jobDetailsBuilder.getStack().add(result);
        } catch (Exception e) {
            throw JobRunrError.shouldNotHappenError("Exception invoking instruction: " + this.getClass().getName(), jobDetailsBuilder, e);
        }
    }

    public abstract String toDiagnosticsString();
}
