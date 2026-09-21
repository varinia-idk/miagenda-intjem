package com.example.miagendaintjem.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagendaintjem.R;
import com.example.miagendaintjem.model.Tarea;

import java.util.List;

public final class TareaAdapter
        extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    public interface OnTareaClickListener {
        void onTareaClick(@NonNull Tarea tarea, int position);

        void onTareaLongClick(@NonNull Tarea tarea, int position);
    }

    @NonNull
    private final List<Tarea> tasks;
    @NonNull
    private final OnTareaClickListener listener;

    public TareaAdapter(
            @NonNull List<Tarea> tasks,
            @NonNull OnTareaClickListener listener
    ) {
        this.tasks = tasks;
        this.listener = listener;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public long getItemId(int position) {
        return tasks.get(position).getId();
    }

    public final class TareaViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleText;
        private final TextView subjectText;
        private final TextView dueDateText;
        private final View priorityIndicator;

        TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.taskTitleText);
            subjectText = itemView.findViewById(R.id.taskSubjectText);
            dueDateText = itemView.findViewById(R.id.taskDueDateText);
            priorityIndicator = itemView.findViewById(R.id.priorityIndicator);

            itemView.setOnClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTareaClick(tasks.get(position), position);
                }
            });

            itemView.setOnLongClickListener(view -> {
                int position = getBindingAdapterPosition();
                if (position == RecyclerView.NO_POSITION) {
                    return false;
                }
                listener.onTareaLongClick(tasks.get(position), position);
                return true;
            });
        }

        void bind(@NonNull Tarea task) {
            titleText.setText(task.getTitulo());
            subjectText.setText(task.getMateria());
            dueDateText.setText(itemView.getContext().getString(
                    R.string.task_due_date_format,
                    task.getFechaEntrega()
            ));

            int priorityColor = ContextCompat.getColor(
                    itemView.getContext(),
                    task.getPrioridad().getColorRes()
            );
            priorityIndicator.setBackgroundColor(priorityColor);
            priorityIndicator.setContentDescription(itemView.getContext().getString(
                    R.string.priority_indicator_description,
                    itemView.getContext().getString(task.getPrioridad().getLabelRes())
            ));
        }
    }
}
