package org.jobrunr.utils;

import org.jobrunr.JobRunrError;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.jobrunr.utils.diagnostics.DiagnosticsBuilder.diagnostics;

public class ClassPathUtils {

    private ClassPathUtils() {
    }

    private static final Map<String, FileSystem> openFileSystems = new HashMap<>();

    public static Stream<Path> listAllChildrenOnClasspath(String... subFolder) {
        return listAllChildrenOnClasspath(ClassPathUtils.class, subFolder);
    }

    public static Stream<Path> listAllChildrenOnClasspath(Class<?> clazz, String... subFolder) {
        try {
            return toPathsOnClasspath(clazz, subFolder)
                    .flatMap(ClassPathUtils::listAllChildrenOnClasspath);
        } catch (Exception e) {
            throw JobRunrError.shouldNotHappenError(
                    "Error listing all children on the classpath",
                    diagnostics()
                            .with("clazz", clazz.getName())
                            .with("subFolder", String.join(", ", subFolder)),
                    e);
        }
    }

    public static Stream<Path> toPathsOnClasspath(String... subFolder) {
        final List<Path> collect = toPathsOnClasspath(ClassPathUtils.class, subFolder).collect(Collectors.toList());
        return collect.stream();
    }

    public static Stream<Path> toPathsOnClasspath(Class<?> clazz, String... subFolders) {
        return toPathsOnClasspath(clazz.getPackage(), subFolders);
    }

    public static Stream<Path> toPathsOnClasspath(Package pkg, String... subFolders) {
        final String joinedSubfolders = String.join("/", subFolders);
        if (joinedSubfolders.startsWith("/")) {
            return toUrls(joinedSubfolders.substring(1))
                    .map(ClassPathUtils::toPath);
        }
        return toUrls(pkg.getName().replace(".", "/") + "/" + joinedSubfolders)
                .map(ClassPathUtils::toPath);
    }

    private static Stream<URL> toUrls(String folder) {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            Enumeration<URL> resources = classLoader.getResources(folder);

            return Collections.list(resources).stream();
        } catch (IOException e) {
            throw JobRunrError.shouldNotHappenError(
                    "Error listing all resources as URL's in folder",
                    diagnostics()
                            .with("folder", folder),
                    e);
        }

    }

    private static Path toPath(URL url) {
        try {
            URI uri = url.toURI();
            if ("wsjar".equals(uri.getScheme())) { // support for openliberty
                uri = new URI(uri.toString().replace("wsjar", "jar"));
            }
            if ("jar".equals(uri.getScheme())) {
                String jarName = uri.toString().substring(0, uri.toString().indexOf('!'));
                loadFileSystemIfNecessary(uri, jarName);
                return openFileSystems.get(jarName).getPath(uri.toString().substring(uri.toString().indexOf('!') + 1));
            } else {
                return Paths.get(uri);
            }
        } catch (IOException | URISyntaxException e) {
            throw JobRunrError.shouldNotHappenError(
                    "Error transforming url to path",
                    diagnostics().with("url", url.toString()),
                    e);
        }
    }

    private static void loadFileSystemIfNecessary(URI uri, String jarName) throws IOException {
        if (!openFileSystems.containsKey(jarName)) {
            try {
                openFileSystems.put(jarName, FileSystems.newFileSystem(uri, Collections.emptyMap(), null));
            } catch (FileSystemAlreadyExistsException e) {
                openFileSystems.put(jarName, FileSystems.getFileSystem(uri));
            }
        }
    }

    private static Stream<Path> listAllChildrenOnClasspath(Path rootPath) {
        try {
            if (rootPath == null) return Stream.empty();
            if (Files.notExists(rootPath)) return Stream.empty();

            return Files.list(rootPath);
        } catch (IOException e) {
            throw JobRunrError.shouldNotHappenError(
                    "Error listing all children on classpath",
                    diagnostics().with("path", rootPath.toString()),
                    e);
        }
    }
}
