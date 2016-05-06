package too.uet.com.todolist.models;

import java.io.Serializable;

/**
 * Created by TooNies1810 on 4/22/16.
 */
public class ItemList implements Serializable{
    private String id;
    private String title;
    private String content;
    private boolean isLoad;

    public ItemList(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public void setIsLoad(boolean isLoad) {
        this.isLoad = isLoad;
    }
}
