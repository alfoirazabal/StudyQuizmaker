package com.alfoirazabal.studyquizmaker.gui.test.recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.domain.Test;

import java.util.List;

public class AdapterTestView extends RecyclerView.Adapter<AdapterTestView.ViewHolder> {

    private final List<Test> tests;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtTestName;
        private final TextView txtTestDescription;

        public ViewHolder(View view) {
            super(view);

            txtTestName = view.findViewById(R.id.txt_test_name);
            txtTestDescription = view.findViewById(R.id.txt_test_description);
        }

        public TextView getTxtTestName() {
            return txtTestName;
        }

        public TextView getTxtTestDescription() {
            return txtTestDescription;
        }
    }

    public AdapterTestView(List<Test> tests) {
        this.tests = tests;

        this.setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recyclerview_adapter_test,
                parent,
                false
        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Test currentTest = tests.get(position);
        holder.getTxtTestName().setText(currentTest.name);
        if (currentTest.description.equals("")) {
            holder.getTxtTestDescription().setVisibility(View.GONE);
        }
        else {
            holder.getTxtTestDescription().setVisibility(View.VISIBLE);
            holder.getTxtTestDescription().setText(currentTest.description);
        }
    }

    @Override
    public int getItemCount() {
        return tests.size();
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
