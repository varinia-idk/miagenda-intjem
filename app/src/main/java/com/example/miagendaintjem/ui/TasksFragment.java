package com.example.miagendaintjem.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.miagendaintjem.DetalleTareaActivity;
import com.example.miagendaintjem.MainActivity;
import com.example.miagendaintjem.R;
import com.example.miagendaintjem.adapter.TareaAdapter;
import com.example.miagendaintjem.model.Prioridad;
import com.example.miagendaintjem.model.Tarea;
import com.example.miagendaintjem.notifications.NotificationHelper;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public final class TasksFragment extends Fragment
        implements TareaAdapter.OnTareaClickListener {

    private static final String STATE_FILTER_POSITION = "state_filter_position";

    private final List<Tarea> visibleTasks = new ArrayList<>();
    private ActivityResultLauncher<Intent> detailLauncher;
    private ActivityResultLauncher<String> notificationPermissionLauncher;
    private RecyclerView taskList;
    private Spinner filterSpinner;
    private TareaAdapter adapter;
    private int selectedFilterPosition;

    public TasksFragment() {
        super(R.layout.fragment_tasks);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        detailLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != android.app.Activity.RESULT_OK
                            || result.getData() == null) {
                        return;
                    }

                    Tarea updated = IntentCompat.getParcelableExtra(
                            result.getData(),
                            DetalleTareaActivity.EXTRA_TAREA,
                            Tarea.class
                    );
                    if (updated == null) {
                        return;
                    }

                    agendaActivity().updateTask(updated);
                    int visiblePosition = findVisiblePosition(updated.getId());
                    if (visiblePosition >= 0 && adapter != null) {
                        visibleTasks.set(visiblePosition, updated);
                        adapter.notifyItemChanged(visiblePosition);
                    }
                    Toast.makeText(
                            requireContext(),
                            R.string.task_updated_message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
        );

        notificationPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    // Notifications are optional; task management works when denied.
                }
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        taskList = view.findViewById(R.id.taskList);
        filterSpinner = view.findViewById(R.id.subjectFilterSpinner);
        FloatingActionButton addButton = view.findViewById(R.id.addTaskButton);

        selectedFilterPosition = savedInstanceState == null
                ? 0
                : savedInstanceState.getInt(STATE_FILTER_POSITION, 0);

        rebuildVisibleTasks();
        configureTaskList();
        configureFilter();
        addButton.setOnClickListener(unused -> showAddTaskDialog());
        requestNotificationPermission();
    }

    private void configureTaskList() {
        adapter = new TareaAdapter(visibleTasks, this);
        taskList.setLayoutManager(new LinearLayoutManager(requireContext()));
        taskList.setAdapter(adapter);
        taskList.setHasFixedSize(true);
    }

    private void configureFilter() {
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.subject_filters,
                android.R.layout.simple_spinner_item
        );
        filterAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        filterSpinner.setAdapter(filterAdapter);
        filterSpinner.setSelection(selectedFilterPosition, false);
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(
                    AdapterView<?> parent,
                    View view,
                    int position,
                    long id
            ) {
                selectedFilterPosition = position;
                applyFilter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // The first filter remains selected.
            }
        });
    }

    private void rebuildVisibleTasks() {
        visibleTasks.clear();
        for (Tarea task : agendaActivity().getTasks()) {
            if (matchesCurrentFilter(task)) {
                visibleTasks.add(task);
            }
        }
    }

    private void applyFilter() {
        int previousSize = visibleTasks.size();
        visibleTasks.clear();
        if (previousSize > 0) {
            adapter.notifyItemRangeRemoved(0, previousSize);
        }

        for (Tarea task : agendaActivity().getTasks()) {
            if (matchesCurrentFilter(task)) {
                visibleTasks.add(task);
            }
        }
        if (!visibleTasks.isEmpty()) {
            adapter.notifyItemRangeInserted(0, visibleTasks.size());
        }
    }

    private boolean matchesCurrentFilter(@NonNull Tarea task) {
        if (selectedFilterPosition == 0 || filterSpinner == null) {
            return true;
        }
        Object selected = filterSpinner.getSelectedItem();
        return selected != null && task.getMateria().contentEquals(selected.toString());
    }

    @Override
    public void onTareaClick(@NonNull Tarea task, int position) {
        Toast.makeText(
                requireContext(),
                getString(R.string.selected_task_message, task.getTitulo()),
                Toast.LENGTH_SHORT
        ).show();

        int sourcePosition = agendaActivity().findTaskIndex(task.getId());
        Intent intent = new Intent(requireContext(), DetalleTareaActivity.class)
                .putExtra(DetalleTareaActivity.EXTRA_TAREA, task)
                .putExtra(DetalleTareaActivity.EXTRA_POSICION, sourcePosition);
        detailLauncher.launch(intent);
    }

    @Override
    public void onTareaLongClick(@NonNull Tarea task, int position) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.delete_task_title)
                .setMessage(getString(
                        R.string.delete_task_message,
                        task.getTitulo()
                ))
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(
                        R.string.action_delete,
                        (dialog, which) -> deleteTask(task, position)
                )
                .show();
    }

    private void deleteTask(@NonNull Tarea task, int visiblePosition) {
        if (!agendaActivity().removeTask(task.getId())) {
            return;
        }
        if (visiblePosition >= 0 && visiblePosition < visibleTasks.size()) {
            visibleTasks.remove(visiblePosition);
            adapter.notifyItemRemoved(visiblePosition);
        }
        Toast.makeText(
                requireContext(),
                R.string.task_deleted_message,
                Toast.LENGTH_SHORT
        ).show();
    }

    private void showAddTaskDialog() {
        View content = getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        TextInputEditText titleInput = content.findViewById(R.id.newTaskTitleInput);
        TextInputEditText dateInput = content.findViewById(R.id.newTaskDateInput);
        Spinner subjectSpinner = content.findViewById(R.id.newTaskSubjectSpinner);
        Spinner prioritySpinner = content.findViewById(R.id.newTaskPrioritySpinner);

        dateInput.setText(R.string.default_new_task_date);
        configureSpinner(subjectSpinner, R.array.subjects);
        configureSpinner(prioritySpinner, R.array.priorities);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.new_task)
                .setView(content)
                .setNegativeButton(R.string.action_cancel, null)
                .setPositiveButton(R.string.action_save, null)
                .create();

        dialog.setOnShowListener(unused ->
                dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        .setOnClickListener(button -> {
                            String title = readInput(titleInput);
                            String date = readInput(dateInput);
                            if (title.isEmpty()) {
                                titleInput.setError(getString(R.string.empty_title_error));
                                return;
                            }
                            if (date.isEmpty()) {
                                dateInput.setError(getString(R.string.empty_date_error));
                                return;
                            }

                            String subject = subjectSpinner.getSelectedItem().toString();
                            Prioridad priority =
                                    Prioridad.values()[prioritySpinner.getSelectedItemPosition()];
                            addTask(title, subject, date, priority);
                            dialog.dismiss();
                        })
        );
        dialog.show();
    }

    private void configureSpinner(@NonNull Spinner spinner, int arrayResource) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                arrayResource,
                android.R.layout.simple_spinner_item
        );
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );
        spinner.setAdapter(spinnerAdapter);
    }

    private void addTask(
            @NonNull String title,
            @NonNull String subject,
            @NonNull String dueDate,
            @NonNull Prioridad priority
    ) {
        Tarea added = agendaActivity().createTask(title, subject, dueDate, priority);
        if (matchesCurrentFilter(added)) {
            int position = visibleTasks.size();
            visibleTasks.add(added);
            adapter.notifyItemInserted(position);
            taskList.scrollToPosition(position);
        }

        if (priority == Prioridad.ALTA) {
            NotificationHelper.notifyHighPriority(requireContext(), added);
        }
        Toast.makeText(
                requireContext(),
                R.string.task_added_message,
                Toast.LENGTH_SHORT
        ).show();
    }

    @NonNull
    private static String readInput(@NonNull TextInputEditText input) {
        return input.getText() == null ? "" : input.getText().toString().trim();
    }

    private int findVisiblePosition(int taskId) {
        for (int index = 0; index < visibleTasks.size(); index++) {
            if (visibleTasks.get(index).getId() == taskId) {
                return index;
            }
        }
        return RecyclerView.NO_POSITION;
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(
                    Manifest.permission.POST_NOTIFICATIONS
            );
        }
    }

    @NonNull
    private MainActivity agendaActivity() {
        return (MainActivity) requireActivity();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(STATE_FILTER_POSITION, selectedFilterPosition);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        if (taskList != null) {
            taskList.setAdapter(null);
        }
        taskList = null;
        filterSpinner = null;
        adapter = null;
        super.onDestroyView();
    }
}
