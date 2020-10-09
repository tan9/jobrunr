package org.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;

import static org.jobrunr.jobs.details.JobDetailsGeneratorUtils.getObjectViaField;

public class GetFieldInstruction extends VisitFieldInstruction {

    public GetFieldInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public Object invokeInstruction() {
        return getObjectViaField(jobDetailsBuilder.getStack().pollLast(), name);
    }

    @Override
    public String toDiagnosticsString() {
        return "GETFIELD " + owner + "." + name + ":" + descriptor;
    }
}
