package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.recyclerviews;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewRunResults;

import java.util.List;

public class AdapterTestRun extends RecyclerView.Adapter<AdapterTestRun.ViewHolder> {

    private final List<TestRun> testRuns;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtScore;
        private final ProgressBar progressBarScore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtScore = itemView.findViewById(R.id.txt_score);
            this.progressBarScore = itemView.findViewById(R.id.progressbar_score);
            this.progressBarScore.setMax(100);

            itemView.setOnClickListener(v -> {
                int itemPosition = getAdapterPosition();
                TestRun testRun = testRuns.get(itemPosition);
                Intent intentViewRunResults = new Intent(
                    itemView.getContext(), ViewRunResults.class
                );
                intentViewRunResults.putExtra("TESTRUNID", testRun.id);
                itemView.getContext().startActivity(intentViewRunResults);
            });
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public ProgressBar getProgressBarScore() {
            return progressBarScore;
        }
    }

    public AdapterTestRun(List<TestRun> testRuns) {
        this.testRuns = testRuns;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public AdapterTestRun.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_testrun,
                parent,
                false
        );

        return new AdapterTestRun.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTestRun.ViewHolder holder, int position) {
        TestRun currentTestRun = this.testRuns.get(position);
        String scoredPercentageIndicator = currentTestRun.scoredPercentage + "%";
        holder.getTxtScore().setText(scoredPercentageIndicator);
        holder.getProgressBarScore().setProgress((int)currentTestRun.scoredPercentage);
    }

    @Override
    public int getItemCount() {
        return this.testRuns.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
