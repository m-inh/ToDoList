package too.uet.com.todolist.models;

/**
 * Created by TooNies1810 on 4/25/16.
 */
public class DeleteToDoMessage extends ServerMessage {

    public DeleteToDoMessage(String jsonString){
        this.jsonString = jsonString;
    }
    public String getJsonString() {
        return jsonString;
    }

    public void setJsonString(String jsonString) {
        this.jsonString = jsonString;
    }
}
