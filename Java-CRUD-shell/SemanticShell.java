/**
Create
    -"create a folder called test"
    -"create a file named notes.txt"
Read
    -"list files in test"
    -"show file notes.txt"    
Update
    -"rename file notes.txt to data.txt"
    -"move file data.txt to test"
Delete
    -"delete file data.txt"
    -"delete folder test"
 */

import java.io.*;
import java.util.*;

public class SemanticShell {
    private static final String os = System.getProperty("os.name").toLowerCase();
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter command in english... or (type exit to quit)");

        while(true){
            System.out.println(">");
            String input = sc.nextLine().toLowerCase();
            if(input.equals("exit")) break;

            String os = System.getProperty("os.name").toLowerCase();
            String command = parseToCommand(input,os);

            if(command != null){
                executeCommand(command);
            }
            else {
                System.out.println("Sorry, I didnt understand that...");
            }
        }
    }

    private static String parseToCommand(String input, String os){
        // CREATE
        if (input.contains("create")) {
            if (input.contains("folder")) {
                String folderName = input.replaceAll(".*(called|named) ", "").trim();
                return "mkdir " + folderName;
            } else if (input.contains("file")) {
                String fileName = input.replaceAll(".*file (called|named)? ", "").trim();
                return os.contains("win") ? "type nul > " + fileName : "touch " + fileName;
            }
        }

        // READ
        if (input.contains("list") || input.contains("show")) {
            if (input.contains("folder") || input.contains("directory")) {
                String folder = input.replaceAll(".*(in|of) ", "").trim();
                return os.contains("win") ? "dir " + folder : "ls " + folder;
            } else if (input.contains("file")) {
                String file = input.replaceAll(".*file ", "").trim();
                return os.contains("win") ? "type " + file : "cat " + file;
            }
        }

        // UPDATE (rename / move)
        if (input.contains("rename")) {
            String[] parts = input.split("to");
            if (parts.length == 2) {
                String oldName = parts[0].replaceAll(".*file ", "").trim();
                String newName = parts[1].trim();
                return os.contains("win") ? "rename " + oldName + " " + newName 
                                          : "mv " + oldName + " " + newName;
            }
        } else if (input.contains("move")) {
            String[] parts = input.split("to");
            if (parts.length == 2) {
                String file = parts[0].replaceAll(".*file ", "").trim();
                String folder = parts[1].trim();
                return os.contains("win") ? "move " + file + " " + folder 
                                          : "mv " + file + " " + folder;
            }
        }

        // DELETE
        if (input.contains("delete") || input.contains("remove")) {
            if (input.contains("folder")) {
                String folder = input.replaceAll(".*folder ", "").trim();
                return os.contains("win") ? "rmdir /s /q " + folder : "rm -r " + folder;
            } else if (input.contains("file")) {
                String file = input.replaceAll(".*file ", "").trim();
                return os.contains("win") ? "del " + file : "rm " + file;
            }
        }

        return null; // Unknown command
    }

    private static void executeCommand(String command){
        try {
            ProcessBuilder pb;
            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd.exe", "/c", command);
            } else {
                pb = new ProcessBuilder("bash", "-c", command);
            }
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Capture output line by line
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
            System.out.println("Done.");
        } catch (Exception e) {
            System.out.println("Error!: " + e.getMessage());
        }
    }
}
