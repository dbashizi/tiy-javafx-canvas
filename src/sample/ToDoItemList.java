package sample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominique on 4/22/2016.
 */
public class ToDoItemList {
    public ArrayList<ToDoItem> todoItems = new ArrayList<ToDoItem>();

    public ToDoItemList(List<ToDoItem> incomingList) {
        todoItems = new ArrayList<ToDoItem>(incomingList);
    }

    public ToDoItemList() {

    }
}
