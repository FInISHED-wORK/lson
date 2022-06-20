package marco.lson;

import marco.lson.exception.LsonParserException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonSpecTest {

    @Test
    void validJsonTests() throws Throwable {
        runTests("./tests/pass/", testInfo -> {
            assertDoesNotThrow(() -> {
                System.out.printf("[%03d/%d] \u001b[32mTest:\u001b[0m %s\n", testInfo.index, testInfo.testsCount, testInfo.filePath);
                new LsonParser(testInfo.content).parse();
            });
            return null;
        });
    }

    @Test
    void invalidJsonTests() throws Throwable {
        runTests("./tests/fail/", testInfo -> {
            var ex = assertThrows(LsonParserException.class, () -> {
                System.out.printf("[%03d/%d] \u001b[32mTest:\u001b[0m %s \u001b[32mMessage:\u001b[0m ", testInfo.index, testInfo.testsCount, testInfo.filePath);
                var parser = new LsonParser(testInfo.content);
                System.out.println("None\n" + parser.parse());
            });
            System.out.printf("%s\n", ex.getMessage());
            return null;
        });
    }

    private void runTests(String at, Function<TestInfo, Void> doTheThing) throws Throwable {
        var allTests = Files.list(Paths.get(at)).map(Path::toFile).toList();
        int fileCnt = 0;
        for (var file : allTests) {
            var fileContents = isValidContent(file, fileCnt, allTests.size());
            if (fileContents.isEmpty()) {
                fileCnt++;
                continue;
            }
            doTheThing.apply(new TestInfo(fileContents, file.getAbsolutePath(), fileCnt, allTests.size()));
            fileCnt++;
        }
    }

    private String isValidContent(File file, int fileIndex, int testCount) {
        try {
            return Files.readString(file.toPath());
        } catch (Exception e) {
            System.out.printf("[%03d/%d] \u001b[31mIgnore:\u001b[0m Malformed input from file %s\n", fileIndex, testCount, file.getAbsolutePath());
            return "";
        }
    }

    private record TestInfo(String content, String filePath, int index, int testsCount) {
    }
}
