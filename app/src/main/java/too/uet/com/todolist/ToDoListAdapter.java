package too.uet.com.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import too.uet.com.todolist.models.ItemList;

/**
 * Created by TooNies1810 on 4/22/16.
 */
public class ToDoListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater lf;
    private ArrayList<ItemList> itemArr;

    public ToDoListAdapter(Context mContext) {
        this.mContext = mContext;
        lf = LayoutInflater.from(mContext);
        itemArr = new ArrayList<>();
    }

    public void addNewItem(ItemList newItem){
        itemArr.add(newItem);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemArr.size();
    }

    @Override
    public Object getItem(int position) {
        return itemArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = lf.inflate(R.layout.item_list, null);
        }

        int revertPosition = itemArr.size() - position - 1;
        if (revertPosition < 0){
            revertPosition = 0;
        }

        final ItemList item = itemArr.get(revertPosition);

        TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) convertView.findViewById(R.id.tv_content);

        tvTitle.setText(item.getTitle());
        tvContent.setText(item.getContent());

        ImageView ivDone = (ImageView) convertView.findViewById(R.id.iv_done);
        ivDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) mContext;
                mainActivity.deleteMess(item.getId());
                deleteMessUI(item.getId());
            }
        });

        if (!item.isLoad()){
            Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.item_list_appear);
            convertView.startAnimation(anim);
            item.setIsLoad(true);
        }

        return convertView;
    }

    public void deleteMessUI(String id) {
        for (int i = 0; i < itemArr.size(); i++) {
            if (itemArr.get(i).getId().equalsIgnoreCase(id)){
                itemArr.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void setItemArr(String first_login) {
        try {
            JSONArray json = new JSONArray(first_login);
            for (int i = 0; i < json.length(); i++) {
                String title = json.getJSONObject(i).getString("title");
                String content = json.getJSONObject(i).getString("content");
                String id = json.getJSONObject(i).getString("_id");
                ItemList item = new ItemList(title, content);
                item.setId(id);
                addNewItem(item);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
