package hakito.graphplayer;


import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

/**
 * Created by Oleg on 15-Dec-15.
 */
public class GraphFragment extends Fragment implements OnClickListener, GraphView.OnUserItteractListener {

    GraphView graphView;
    View menu;
    float[] points;

    public GraphFragment() {
        super();
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.graph_fragment, container,false);
        graphView = (GraphView)v.findViewById(R.id.graph);
        if(points!=null)
        {
            graphView.setPoints(points);
        }
        menu = v.findViewById(R.id.lMenu);
        v.findViewById(R.id.bMore).setOnClickListener(this);
        graphView.setListener(this);
        v.findViewById(R.id.bMirrorH).setOnClickListener(this);
        v.findViewById(R.id.bMirrorV).setOnClickListener(this);
        v.findViewById(R.id.bLinear).setOnClickListener(this);
        return v;
    }

    public void setPoints(float[] points)
    {
                this.points = points;
    }

    public float[] getValues()
    {
        return graphView.getValues();
    }

    public float[] getPoints()
    {
        return graphView.getPoints();
    }



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bMore:
                menu.setVisibility(menu.getVisibility()==View.VISIBLE?View.GONE:View.VISIBLE);
                break;
            case R.id.bLinear:
                graphView.linear();
                break;
            case R.id.bMirrorH:
                graphView.mirrorHorizontal();break;
            case R.id.bMirrorV:
                graphView.mirrorVertical();break;
        }
    }

    @Override
    public void Itterac(View v) {
        menu.setVisibility(View.GONE);
    }
}
