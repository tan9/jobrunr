package org.jobrunr.jobs.details.instructions;

import org.jobrunr.jobs.details.JobDetailsFinderContext;

public class SingleIntOperandInstruction extends AbstractJVMInstruction {

    private int intValue;

    public SingleIntOperandInstruction(JobDetailsFinderContext jobDetailsBuilder) {
        super(jobDetailsBuilder);
    }

    public void load(int intValue) {
        this.intValue = intValue;
        jobDetailsBuilder.pushInstructionOnStack(this);
    }

    @Override
    public Object invokeInstruction() {
        return intValue;
    }

    @Override
    public String toDiagnosticsString() {
        if (-128 < intValue && intValue < 127) {
            return "BIPUSH " + intValue;
        }
        return "SIPUSH " + intValue;
    }
}
