package com.example.workoutbuilder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class ProgramRVAdapter extends ListAdapter<Program, ProgramRVAdapter.ViewHolder> {

    private OnItemClickListener listener;

    private Context context;

    public ProgramRVAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<Program> DIFF_CALLBACK = new DiffUtil.ItemCallback<Program>() {
        @Override
        public boolean areItemsTheSame(Program oldItem, Program newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Program oldItem, Program newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getDescription().equals(newItem.getDescription()) &&
                    oldItem.getWorkouts().equals(newItem.getWorkouts());
        }
    };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.program_rv_item, parent, false);
        return new ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Program model = getProgramAt(position);
        holder.nameTV.setText(model.getName());
        holder.descriptionTV.setText(model.getDescription());

        holder.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, DisplayedWorkout.class);
            try {
                intent.putExtra("programJsonString", model.toJSON().toString());
            } catch (JSONException e){
                e.printStackTrace();
            }
            context.startActivity(intent);
        });


    }

    public Program getProgramAt(int position) {
        return getItem(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, descriptionTV;
        Button viewButton;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.idTVName);
            descriptionTV = itemView.findViewById(R.id.idTVDesc);
            viewButton = itemView.findViewById(R.id.viewButton);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getItem(position));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Program model);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
