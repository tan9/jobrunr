package org.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;

public class ILoadOperandInstruction extends VisitLocalVariableInstruction {

    public ILoadOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public String toDiagnosticsString() {
        return "ILOAD " + variable;
    }


}
