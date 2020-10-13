package org.jobrunr.jobs.details.instructions;

import org.jobrunr.JobRunrException;
import org.jobrunr.jobs.details.JobDetailsFinderContext;

import static org.jobrunr.JobRunrError.shouldNotHappenError;

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
        } catch (JobRunrException e) {
            throw e;
        } catch (Exception e) {
            throw shouldNotHappenError("Exception invoking instruction: " + this.getClass().getName(), jobDetailsBuilder, e);
        }
    }

    public abstract String toDiagnosticsString();
}
