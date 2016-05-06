package too.uet.com.todolist.models;

/**
 * Created by TooNies1810 on 4/25/16.
 */
public class NewToDoMessage extends ServerMessage {
    private ItemList item;

    public NewToDoMessage(String jsonString){
        this.jsonString = jsonString;
    }

    public NewToDoMessage(ItemList item) {
        this.item = item;
    }

    public ItemList getItem() {
        return item;
    }

    public void setItem(ItemList item) {
        this.item = item;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
