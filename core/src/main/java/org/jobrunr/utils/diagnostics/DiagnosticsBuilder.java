package org.jobrunr.utils.diagnostics;

import org.jobrunr.JobRunrError;
import org.jobrunr.utils.io.IOUtils;

import java.io.StringWriter;
import java.net.URL;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static org.jobrunr.jobs.details.JobDetailsGeneratorUtils.getClassLocationOfLambda;

public class DiagnosticsBuilder implements JobRunrError.DiagnosticsAware {

    private final StringBuilder stringBuilder;

    private DiagnosticsBuilder() {
        this.stringBuilder = new StringBuilder();
    }

    public static DiagnosticsBuilder diagnostics() {
        return new DiagnosticsBuilder();
    }

    public DiagnosticsBuilder with(String key, String value) {
        stringBuilder.append(key).append(": ").append(value).append("\n");
        return this;
    }

    public DiagnosticsBuilder withTextBlock(String key, String textBlock) {
        stringBuilder.append(key).append(": ").append("\n").append(textBlock);
        return this;
    }

    public DiagnosticsBuilder withObject(Object object) {
        return this.withObject("object", object);
    }

    public DiagnosticsBuilder withObject(String key, Object object) {
        return this.with(key, object.toString() + "(" + object.getClass().getName() + ")");
    }

    public DiagnosticsBuilder withParameterTypes(Class<?>[] paramTypes) {
        return this.with("parameterTypes", stream(paramTypes).map(Class::getName).collect(joining(", ")));
    }

    public DiagnosticsBuilder withParameters(Object[] parameters) {
        return this.with("parameters", stream(parameters).map(o -> o.toString() + "(" + o.getClass().getName() + ")").collect(joining(", ")));
    }

    public DiagnosticsBuilder withLambda(Object lambda) {
        String location = getClassLocationOfLambda(lambda);
        URL resource = lambda.getClass().getResource(location);

        return this
                .with("lambda", lambda.toString())
                .with("lambda location", location)
                .withTextBlock("class file", disassembleClassFromJava(resource.toExternalForm()));
    }

    @Override
    public String getDiagnosticsInfo() {
        return stringBuilder.toString();
    }

    String disassembleClassFromJava(String resourceFile) {
        try {
            ProcessBuilder pb = new ProcessBuilder("javap", "-c", resourceFile)
                    .redirectErrorStream(true);

            final Process process = pb.start();
            final StringWriter writer = new StringWriter();
            new Thread(() -> IOUtils.copyStreamNoException(process.getInputStream(), writer)).start();

            final int exitValue = process.waitFor();
            final String processOutput = writer.toString();
            return processOutput;
        } catch (Throwable t) {
            return "Unable to run javap command (" + t.getMessage() + ").";
        }
    }
}
