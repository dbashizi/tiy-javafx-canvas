package sample;

/**
 * Created by Dominique on 4/21/2016.
 */
public class ToDoItem {
    public String text;
    public boolean isDone;
    public int id;

    public ToDoItem(int id, String text, boolean isDone) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
    }

    public ToDoItem(String text) {
        this.text = text;
        this.isDone = false;
    }

    public ToDoItem() {
    }

    @Override
    public String toString() {
        if (isDone) {
            return text + " (done)";
        } else {
            return text;
        }
        // A one-line version of the logic above:
        // return text + (isDone ? " (done)" : "");
    }
}
