package org.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;

public class DLoadOperandInstruction extends VisitLocalVariableInstruction {

    public DLoadOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    @Override
    public String toDiagnosticsString() {
        return "DLOAD " + variable;
    }

}
