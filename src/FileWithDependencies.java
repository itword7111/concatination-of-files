import java.util.ArrayList;
import java.util.List;

public class FileWithDependencies {
    private String text;
    private List<String> listOfDependencies;

    public FileWithDependencies(String text, List<String> listOfDependencies) {
        this.text = text;
        this.listOfDependencies = listOfDependencies;
    }

    public FileWithDependencies() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getListOfDependencies() {
        return listOfDependencies;
    }

    public void setListOfDependencies(List<String> listOfDependencies) {
        this.listOfDependencies = listOfDependencies;
    }
}
