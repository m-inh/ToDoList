package too.uet.com.todolist;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import too.uet.com.todolist.models.ItemList;

/**
 * Created by TooNies1810 on 4/22/16.
 */
public class NewItemPopUp extends Dialog {

    private MainActivity mainActivity;

    public NewItemPopUp(Context context) {
        super(context);
        setContentView(R.layout.dialog_newitem);
        setTitle("Add new activity");

        final EditText edtTitle = (EditText) findViewById(R.id.edt_title);
        final EditText edtContent = (EditText) findViewById(R.id.edt_content);

        Button btnAdd = (Button) findViewById(R.id.btn_add);
        Button btnCancel = (Button) findViewById(R.id.btn_cancel);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) getContext();
                mainActivity.sendMessage(new ItemList(edtTitle.getText().toString(), edtContent.getText().toString()));
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
