package too.uet.com.todolist.models;

/**
 * Created by TooNies1810 on 4/25/16.
 */
public class FirstLoginMessage extends ServerMessage {
    private ItemList itemArr[];

    public FirstLoginMessage(ItemList[] itemArr) {
        this.itemArr = itemArr;
    }

    public FirstLoginMessage(String jsonString) {
        this.jsonString = jsonString;
    }

    public ItemList[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(ItemList[] itemArr) {
        this.itemArr = itemArr;
    }

    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
