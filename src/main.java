import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

public class main {
    public static void main(String[] args) {
        try (BufferedReader br = Files.newBufferedReader(Paths.get("rsc/file.txt"))) {
            String line = "";

            LinkedList<String> knownPaths = new LinkedList<>();

            while ((line = br.readLine()) != null) {
                String psString = "New-ItemProperty -Path ";
                String type = "";
                line = line.substring(0, 4) + "§" + line.substring(4);
                String path = line.substring(0, line.indexOf(':')).replace(':', ' ').trim();
                String literalPath = path.substring(0, path.lastIndexOf("\\"));
                if (!knownPaths.contains(literalPath)) {
                    knownPaths.add(literalPath);
                    System.out.println("New-Item -Path \"" + literalPath.replace("§", ":") + "\" -ItemType Directory -Force");
                }
                psString += "\"" + literalPath + "\" -PropertyType ";
                String value = line.substring(line.indexOf(':')).trim();
                value = value.substring(1).trim();
                char first = value.toCharArray()[0];
                String name = path.substring(path.lastIndexOf("\\")).replace('\\', ' ').trim();
                if (first == '\"') {
                    type = "String";
                } else {
                    type = "DWord";
                }
                psString += "\"" + type + "\"" + " -Name ";
                psString += "\"" + name + "\" -Value " + value;

                System.out.println(psString.replace("§", ":"));


            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}