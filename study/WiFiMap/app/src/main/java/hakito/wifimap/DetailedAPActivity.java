package hakito.wifimap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import hakito.wifimap.POJO.Comment;
import hakito.wifimap.POJO.Point;
import hakito.wifimap.adapters.CommentsAdapter;

public class DetailedAPActivity extends AppCompatActivity {

    public static Point p;

    ListView listView;
    CommentsAdapter adapter;
        static final int COMM=3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_ap);

        ((TextView)findViewById(R.id.tSSID)).setText(p.getName());

        findViewById(R.id.bAddComment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailedAPActivity.this, AddCommentActivity.class);
                startActivityForResult(i, COMM);
            }
        });

        listView = (ListView)findViewById(R.id.listView);

        adapter = new CommentsAdapter(this);
        listView.setAdapter(adapter);
        adapter.addAll(p.getComments());


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==COMM)
        {
            p.getComments().add(new Comment(AddCommentActivity.res));
            //TODO: update comments
        }
    }
}
