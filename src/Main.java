import java.io.*;
import java.util.*;

public class Main {
    private static Map<String, FileWithDependencies> mapOfFilesWithDependencies = new LinkedHashMap<>();
    private static String startFolderPath = "";

    public static void main(String[] args) throws IOException {
        List<FileWithDependencies> fileWithDependenciesList = new LinkedList<>();
        StringBuilder concatenationText = new StringBuilder();
        System.out.println("Введите начальную директорию");
        Scanner scanner = new Scanner(System.in);
        startFolderPath = scanner.nextLine();
        File startFolder = new File(startFolderPath);
        listFilesForFolder(startFolder);
        //mapOfFilesWithDependencies = mapOfFilesWithDependencies.entrySet().stream().sorted(Map.Entry.comparingByKey()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (x, y) -> y, LinkedHashMap::new));
        while (mapOfFilesWithDependencies.size() != 0) {
            int mapSize = mapOfFilesWithDependencies.size();
            for (Map.Entry<String, FileWithDependencies> mapEntry : mapOfFilesWithDependencies.entrySet()) {
                boolean haveDependency = true;
                for (String dependency : mapEntry.getValue().getListOfDependencies()) {
                    if (mapOfFilesWithDependencies.containsKey(dependency)) {
                        haveDependency = false;
                        break;
                    }
                }
                if (haveDependency) {
                    fileWithDependenciesList.add(mapEntry.getValue());
                    mapOfFilesWithDependencies.remove(mapEntry.getKey());
                    break;
                }

            }
            if (mapSize == mapOfFilesWithDependencies.size()) {
                System.out.println("зацикленность между файлами");
                for (Map.Entry<String, FileWithDependencies> mapEntry : mapOfFilesWithDependencies.entrySet()) {
                    System.out.print(mapEntry.getKey() + " ");
                }
                break;
            }
        }
        if (mapOfFilesWithDependencies.size() == 0) {
            for (FileWithDependencies fileWithDependencies : fileWithDependenciesList) {
                concatenationText.append(fileWithDependencies.getText());
            }
            File file = new File(startFolderPath + "//concatenatedFile.txt");
            if (file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(concatenationText.toString());
                writer.close();
            } else {
                System.out.println("File already exists.");
            }
        }

    }

    public static void listFilesForFolder(final File folder) throws IOException {
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                String fileLine;
                StringBuilder text = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new FileReader(fileEntry.getAbsoluteFile().getAbsolutePath()));
                List<String> listOfDependencies = new ArrayList<>();
                while ((fileLine = bufferedReader.readLine()) != null) {
                    if (fileLine.startsWith("require '")) {
                        listOfDependencies.add(fileLine.replaceFirst("require '", "").replaceFirst("'", ""));
                    }
                    text.append(fileLine).append("\n");
                }
                mapOfFilesWithDependencies.put(fileEntry.getPath().substring(startFolderPath.length()), new FileWithDependencies(text.toString(), listOfDependencies));
            }
        }
    }
}
