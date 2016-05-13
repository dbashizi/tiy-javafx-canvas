package sample;

public class ToDoItem {
    public String text;
    public boolean isDone;
    public int id;

    public ToDoItem(String text) {
        this.text = text;
        this.isDone = false;
    }

    public ToDoItem(int id, String text, boolean isDone) {
        this.text = text;
        this.isDone = isDone;
        this.id = id;
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
