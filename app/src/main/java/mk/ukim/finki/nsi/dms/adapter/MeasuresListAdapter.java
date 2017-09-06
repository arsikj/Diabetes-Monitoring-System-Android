package mk.ukim.finki.nsi.dms.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import mk.ukim.finki.nsi.dms.R;
import mk.ukim.finki.nsi.dms.model.Measure;

/**
 * Created by dejan on 06.9.2017.
 */

public class MeasuresListAdapter extends RecyclerView.Adapter<MeasuresListAdapter.ViewHolder>{

    private ArrayList<Measure> measures;
    private Context context;

    public MeasuresListAdapter(ArrayList<Measure> measures, Context context){
        this.measures = measures;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_measure, null);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Measure measure = measures.get(position);

        holder.level.setText(measure.getLevel() + "mg/dl");
        holder.dateAdded.setText(measure.getDateAdded().toString());

        //// TODO: 06.9.2017 change colors for different measure levels
        ((RelativeLayout) holder.itemView).setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));
    }

    @Override
    public int getItemCount() {
        return measures.size();
    }

    public void add(Measure measure){
        measures.add(measure);
        notifyItemInserted(measures.size());
    }

    public void delete(int position){
        measures.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView level;
        public TextView dateAdded;

        public ViewHolder(View itemView){
            super(itemView);
            level = (TextView)itemView.findViewById(R.id.measureText);
            dateAdded = (TextView)itemView.findViewById(R.id.date);
        }
    }

}
