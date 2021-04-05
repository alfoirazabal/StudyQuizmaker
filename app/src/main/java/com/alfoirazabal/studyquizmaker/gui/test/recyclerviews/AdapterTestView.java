package com.alfoirazabal.studyquizmaker.gui.test.recyclerviews;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.alfoirazabal.studyquizmaker.AppConstants;
import com.alfoirazabal.studyquizmaker.R;
import com.alfoirazabal.studyquizmaker.db.AppDatabase;
import com.alfoirazabal.studyquizmaker.domain.Test;
import com.alfoirazabal.studyquizmaker.domain.Topic;
import com.alfoirazabal.studyquizmaker.gui.test.UpdateTest;

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

            view.setOnLongClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(view.getContext(), txtTestName);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    popupMenu.setForceShowIcon(true);
                }
                popupMenu.inflate(R.menu.menu_test);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((menuItem) -> {
                    int menuItemId = menuItem.getItemId();
                    if (menuItemId == R.id.item_edit) {
                        handleEdit();
                        return true;
                    }
                    else if (menuItemId == R.id.item_delete) {
                        handleDelete();
                        return true;
                    }
                    else {
                        return false;
                    }
                });
                return false;
            });
        }

        private void handleEdit() {
            int testPosition = getAdapterPosition();
            Test test = tests.get(testPosition);
            Intent intentUpdateTest = new Intent(itemView.getContext(), UpdateTest.class);
            intentUpdateTest.putExtra("TESTID", test.id);
            itemView.getContext().startActivity(intentUpdateTest);
        }

        private void handleDelete() {
            int testPosition = getAdapterPosition();
            Test test = tests.get(testPosition);
            Context context = itemView.getContext();
            String testDeletionDescription =
                    context.getString(R.string.msg_delete_confirmation_test) + "\n" +
                            test.toString(context);
            new AlertDialog.Builder(itemView.getContext())
                    .setTitle(context.getString(R.string.delete_confirmation))
                    .setMessage(testDeletionDescription)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(context.getString(R.string.yes), (dialog, which) -> {
                        new Thread(() -> {
                            AppDatabase db = Room.databaseBuilder(
                                    itemView.getContext(),
                                    AppDatabase.class,
                                    AppConstants.getDBLocation(context)
                            ).build();
                            db.testDAO().delete(test);
                        }).start();
                        tests.remove(test);
                        notifyItemRangeRemoved(testPosition, tests.size());
                    })
                    .setNegativeButton(context.getString(R.string.no), null)
                    .show();
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
