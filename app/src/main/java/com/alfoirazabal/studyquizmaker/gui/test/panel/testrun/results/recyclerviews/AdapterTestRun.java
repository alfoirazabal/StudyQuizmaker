package com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.recyclerviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.testrun.TestRun;
import com.alfoirazabal.studyquizmaker.gui.test.panel.testrun.results.ViewRunResults;
import com.alfoirazabal.studyquizmaker.helpers.dates.DateTimeDifference;

import java.text.DateFormat;
import java.util.List;

public class AdapterTestRun extends RecyclerView.Adapter<AdapterTestRun.ViewHolder> {

    private final List<TestRun> testRuns;

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView txtScore;
        private final ProgressBar progressBarScore;
        private final TextView txtStartedOn;
        private final TextView txtDuration;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.txtScore = itemView.findViewById(R.id.txt_score);
            this.progressBarScore = itemView.findViewById(R.id.progressbar_score);
            this.txtStartedOn = itemView.findViewById(R.id.txt_started_on);
            this.txtDuration = itemView.findViewById(R.id.txt_duration);
            ImageView imgBtnDelete = itemView.findViewById(R.id.imgbtn_delete);

            this.progressBarScore.setMax(100);

            itemView.setOnClickListener(v -> {
                int itemPosition = getAdapterPosition();
                TestRun testRunInPosition = testRuns.get(itemPosition);
                Intent intentViewRunResults = new Intent(
                    itemView.getContext(), ViewRunResults.class
                );
                intentViewRunResults.putExtra("TESTRUNID", testRunInPosition.id);
                itemView.getContext().startActivity(intentViewRunResults);
            });

            imgBtnDelete.setOnClickListener(v -> {
                int itemPosition = getAdapterPosition();
                TestRun testRunInPosition = testRuns.get(itemPosition);
                Context context = itemView.getContext();
                String testrunDeletionDescription =
                        context.getString(R.string.msg_delete_confirmation_testrun);
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle(context.getString(R.string.delete_confirmation))
                        .setMessage(testrunDeletionDescription)
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                            new Thread(() -> {
                                AppDatabase db = Room.databaseBuilder(
                                        itemView.getContext(),
                                        AppDatabase.class,
                                        AppConstants.getDBLocation(context)
                                ).build();
                                db.testRunDAO().delete(testRunInPosition);
                            }).start();
                            testRuns.remove(testRunInPosition);
                            notifyDataSetChanged();
                        })
                        .setNegativeButton(context.getString(R.string.no), null)
                        .show();
            });
        }

        public TextView getTxtScore() {
            return txtScore;
        }

        public ProgressBar getProgressBarScore() {
            return progressBarScore;
        }

        public TextView getTxtStartedOn() {
            return txtStartedOn;
        }

        public TextView getTxtDuration() {
            return txtDuration;
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
        DateFormat dateFormat =
                android.text.format.DateFormat.getDateFormat(holder.itemView.getContext());
        holder.getTxtStartedOn().setText(dateFormat.format(currentTestRun.dateTimeStarted));
        DateTimeDifference dateTimeDifference = new DateTimeDifference(
                currentTestRun.dateTimeStarted, currentTestRun.dateTimeFinished
        );
        holder.getTxtDuration().setText(dateTimeDifference.toString());
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
