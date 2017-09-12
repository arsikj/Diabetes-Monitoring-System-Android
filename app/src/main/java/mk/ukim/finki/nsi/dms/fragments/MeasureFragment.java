package mk.ukim.finki.nsi.dms.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;
import mk.ukim.finki.nsi.dms.R;
import mk.ukim.finki.nsi.dms.adapter.MeasuresListAdapter;
import mk.ukim.finki.nsi.dms.model.Measure;
import mk.ukim.finki.nsi.dms.utils.Constants;
import mk.ukim.finki.nsi.dms.utils.HTTPRequestService;
import mk.ukim.finki.nsi.dms.utils.PreferencesManager;

/**
 * Created by dejan on 04.9.2017.
 */

public class MeasureFragment extends Fragment {

    private RecyclerView measuresList;
    private FloatingActionButton addMeasure;
    private LineChartView lineChartView;

    private ArrayList<Measure> measures;
    private MeasuresListAdapter measuresListAdapter;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measures, container, false);

        initComponents(view);
        initListeners();

        return view;
    }

    private void initComponents(View view){
        measuresList = (RecyclerView) view.findViewById(R.id.list_measures);
        addMeasure = (FloatingActionButton) view.findViewById(R.id.add_measure);
        lineChartView = (LineChartView) view.findViewById(R.id.chart);
        lineChartView.setInteractive(true);
        lineChartView.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
        lineChartView.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);

        measures = new ArrayList<>();
        measuresListAdapter = new MeasuresListAdapter(measures, getContext());
        measuresList.setLayoutManager(new LinearLayoutManager(getActivity()));
        measuresList.setAdapter(measuresListAdapter);
        measuresList.setItemAnimator(new DefaultItemAnimator());
        measuresList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
    }

    private void initListeners(){
        addMeasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(getContext());
                View addMeasureDialog = layoutInflater.inflate(R.layout.dialog_add_measure, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setView(addMeasureDialog);
                alertDialogBuilder.setTitle("Add new measure");

                final EditText levelInput = (EditText) addMeasureDialog.findViewById(R.id.levelInput);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SendMeasure().execute(Constants.ADD_MEASURE_LINK, PreferencesManager.getInstance(getContext()).getStringValue(Constants.USERNAME), levelInput.getText().toString());
                                measuresListAdapter.add(new Measure(Integer.parseInt(levelInput.getText().toString()), new Date()));
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void initChart() {
        List<PointValue> values = new ArrayList<PointValue>();

        for(int i = 0; i < measures.size(); i++){
            values.add(new PointValue(i, measures.get(i).getLevel()));
        }

        Line line = new Line(values).setColor(ContextCompat.getColor(getContext(), R.color.chartColor)).setCubic(true).setStrokeWidth(5);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);

        LineChartData data = new LineChartData();
        Axis axisX = new Axis();
        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("Level");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        data.setBaseValue(Float.NEGATIVE_INFINITY);

        data.setLines(lines);

        lineChartView.setLineChartData(data);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(measures.size() == 0) {
            new GetMeasures().execute(Constants.MEASURES_LINK+"/"+ PreferencesManager.getInstance(getContext()).getStringValue(Constants.USERNAME));
        }
    }

    private class GetMeasures extends AsyncTask<String, List<Measure>, List<Measure>> {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            super.onPreExecute();
        }

        @Override
        protected List<Measure> doInBackground(String... params) {
            return HTTPRequestService.getInstance().getMeasures(params[0]);
        }

        @Override
        protected void onPostExecute(List<Measure> returnedMeasures) {
            progressDialog.dismiss();
            measures.addAll(returnedMeasures);
            measuresListAdapter.notifyItemRangeInserted(0, measures.size());
            initChart();
            super.onPostExecute(measures);
        }
    }

    private class SendMeasure extends AsyncTask<String, String, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length == 3) {
                return HTTPRequestService.getInstance().addMeasure(params[0], params[1], params[2]);
            }
            return false;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
