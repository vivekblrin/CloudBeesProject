package com.org.cloudbees;

import io.qameta.allure.*;

import org.testng.annotations.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitRepoTestingTest {
	
	private static final Logger logger = LoggerFactory.getLogger(GitRepoTestingTest.class);


	private void runCommand(File directory, String... command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(directory);
        builder.redirectErrorStream(true);
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) System.out.println(line);
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
        	 logger.error("Command failed: " + String.join(" ", command));
        	throw new RuntimeException("Command failed: " + String.join(" ", command));
        }
    
    }
	
    @Test(description = "Clone repo, add file, commit and push")
    @Description("Clones a repo, adds a new file and pushes")
    @Severity(SeverityLevel.CRITICAL)
    public void testAddNewFileToRepo() throws Exception {
        String repoUrl = "https://github.com/vivekblrin/CloudbeesGitRepo.git";
        String fileName = "newfile-" + UUID.randomUUID() + ".txt";
        String content = "\r\n Hello from automated test! " + UUID.randomUUID();
        String commitMessage = "Add new file via automation";

        File cloneDir = new File("cloned-" + UUID.randomUUID());

        Allure.step("Cloning the repo", () -> {
        	logger.info("Cloning the repo from {}", repoUrl);
            runCommand(null, "git", "clone", repoUrl, cloneDir.getAbsolutePath());
        });

        Allure.step("Setting Git identity", () -> {
        	logger.info("Setting Git identity");
            runCommand(cloneDir, "git", "config", "user.email", "in21vivek@gmail.com");
            runCommand(cloneDir, "git", "config", "user.name", "vivekblrin");
        });

        Allure.step("Creating a new file", () -> {
        	logger.info("Creating a new file: {}", fileName);
            File file = new File(cloneDir, fileName);
            Files.writeString(file.toPath(), content);
        });

        Allure.step("Adding file to Git", () -> {
        	logger.info("Adding file to Git: {}", fileName);
            runCommand(cloneDir, "git", "add", fileName);
        });

        Allure.step("Committing file", () -> {
        	logger.info("Committing file with message: {}", commitMessage);
            runCommand(cloneDir, "git", "commit", "-m", commitMessage);
        });

        Allure.step("Pushing file", () -> {
        	logger.info("Pushing file to remote repository");
            runCommand(cloneDir, "git", "push");
        });
        Thread.sleep(2000);
        deleteDirectory(cloneDir.toPath());
        // After all Git operations, delete the cloned repository directory
        logger.info("Deleted cloned repository directory: {}", cloneDir.getAbsolutePath());
    }
    
    
    
    public void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        Files.setAttribute(file, "dos:readonly", false); // Remove read-only
                        Files.delete(file);
                    } catch (IOException e) {
                    	logger.error("Could not delete file: " + file + " (" + e.getMessage() + ")");
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    try {
                        Files.setAttribute(dir, "dos:readonly", false); // Remove read-only
                        Files.delete(dir);
                    } catch (IOException e) {
                        System.err.println("Could not delete dir: " + dir + " (" + e.getMessage() + ")");
                        logger.error("Could not delete directory: " + dir + " (" + e.getMessage() + ")");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }


    @Test(description = "Clone repo, modify existing file, commit and push")
    @Description("Clones a repo, updates an existing file by appending content and pushes")
    @Severity(SeverityLevel.NORMAL)
    public void testModifyExistingFileInRepo() throws Exception {
        String repoUrl = "https://github.com/vivekblrin/CloudbeesGitRepo.git";  // Replace with your repo URL
        String fileName = "README.md"; // Specify the file name to update (existing file in the repo)
        String contentToAppend = "\r\nAppended by automation - " + UUID.randomUUID();  // The content to append
        String commitMessage = "Append to existing file via automation";

        // Specify the base directory for cloning
        Path baseDir = Paths.get(System.getProperty("user.dir"));  // Or any other path of your choice
        if (!Files.exists(baseDir)) {
            Files.createDirectories(baseDir); // Create base directory if it doesn't exist
            logger.info("Created base directory: {}", baseDir);
        }

        // Ensure the clone directory is created inside the base directory
        Path cloneDir = baseDir.resolve("cloned-" + UUID.randomUUID()); 

        Allure.step("Cloning the repo", () -> {
        	logger.info("Cloning the repository from {}", repoUrl);
            runCommand(null, "git", "clone", repoUrl, cloneDir.toString());
        });

        Allure.step("Setting Git identity", () -> {
        	logger.info("Setting Git identity");
            runCommand(cloneDir.toFile(), "git", "config", "user.email", "in21vivek@gmail.com");
            runCommand(cloneDir.toFile(), "git", "config", "user.name", "vivekblrin");
        });

        Allure.step("Appending content to existing file", () -> {
        	logger.info("Appending content to file: {}", fileName);
            File file = new File(cloneDir.toFile(), fileName);
            if (!file.exists()) throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            Files.writeString(file.toPath(), contentToAppend, java.nio.file.StandardOpenOption.APPEND);
        });

        Allure.step("Adding file to Git", () -> {
        	logger.info("Adding file to Git: {}", fileName);
            runCommand(cloneDir.toFile(), "git", "add", fileName);
        });

        Allure.step("Committing changes", () -> {
        	logger.info("Committing changes with message: {}", commitMessage);
            runCommand(cloneDir.toFile(), "git", "commit", "-m", commitMessage);
        });

        Allure.step("Pushing changes", () -> {
        	logger.info("Pushing changes to remote repository");
            runCommand(cloneDir.toFile(), "git", "push");
        });
        Thread.sleep(2000);
        // After all Git operations, delete the cloned repository directory
        deleteDirectory(cloneDir);
        logger.info("Deleted cloned repository directory: {}", cloneDir);
    }
    
}
