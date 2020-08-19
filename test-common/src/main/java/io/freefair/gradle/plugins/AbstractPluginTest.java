package io.freefair.gradle.plugins;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.TypeSpec;
import io.freefair.gradle.plugins.builder.gradle.GradleConfigurationBuilder;
import io.freefair.gradle.plugins.builder.io.FileBuilder;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import org.apache.commons.io.FileUtils;
import org.gradle.testkit.runner.BuildResult;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

public class AbstractPluginTest {

    @Rule
    public final TemporaryFolder testProjectDir = new TemporaryFolder();
    protected File buildFile;

    @Before
    public void setup() throws IOException {
        buildFile = testProjectDir.newFile("build.gradle");
    }

    protected void loadBuildFileFromClasspath(String name) throws IOException {
        InputStream resourceAsStream = getClass().getResourceAsStream(name);

        try (BufferedSource source = Okio.buffer(Okio.source(resourceAsStream));
             BufferedSink sink = Okio.buffer(Okio.sink(buildFile, false))) {
            source.readAll(sink);
        }
    }

    protected File getTemporaryDirectory() {
        return testProjectDir.getRoot();
    }

    protected FileBuilder createFile(String fileName) {
        try {
            return new FileBuilder(testProjectDir.newFile(fileName));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected FileBuilder createFile(String directory, String fileName) {
        try {
            File file = getFile(directory, fileName);
            return new FileBuilder(file);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private File getFile(String directory, String fileName) throws IOException {
        File root = new File(testProjectDir.getRoot(), directory);
        if (!root.exists() && !root.mkdirs())
            throw new RuntimeException("Error while creating directories");

        File file = new File(root, fileName);
        if (!file.exists() && !file.createNewFile())
            throw new RuntimeException("Error while creating file");
        return file;
    }

    protected void createJavaClass(String sourceSet, String packageName, TypeSpec typeSpec) {
        try {

            String replace = "src/" + sourceSet + "/java/" + packageName.replace(".", "/");
            FileBuilder file = createFile(replace, typeSpec.name + ".java");
            file.append(JavaFile.builder(packageName, typeSpec).build().toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String readJavaClassFromDirectory(String direcotry, String packageName, String className) {
        try {
            if(!direcotry.endsWith("/"))
                direcotry += "/";
            String path = direcotry + packageName.replace(".", "/");
            return FileUtils.readFileToString(getFile(path, className + ".java"), Charset.defaultCharset());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected String readJavaClass(String sourceSet, String packageName, String className) {
        try {
            String replace = "src/" + sourceSet + "/java/" + packageName.replace(".", "/");
            File file = getFile(replace, className + ".java");
            return FileUtils.readFileToString(file, Charset.defaultCharset());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected GradleConfigurationBuilder createGradleConfiguration() {
        return new GradleConfigurationBuilder(new FileBuilder(buildFile));
    }

    protected BuildResult executeTask(String... taskNames) {
        String[] parameters = Arrays.copyOf(taskNames, taskNames.length + 1);
        parameters[taskNames.length] = "--stacktrace";
        return GradleRunner.create()
                .withProjectDir(testProjectDir.getRoot())
                .withPluginClasspath()
                .withDebug(true)
                .withArguments(parameters).build();
    }
}
