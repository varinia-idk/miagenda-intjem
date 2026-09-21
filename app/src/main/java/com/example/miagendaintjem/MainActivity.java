package com.example.miagendaintjem;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.BundleCompat;
import androidx.fragment.app.Fragment;

import com.example.miagendaintjem.model.Materia;
import com.example.miagendaintjem.model.Prioridad;
import com.example.miagendaintjem.model.Tarea;
import com.example.miagendaintjem.ui.CalendarFragment;
import com.example.miagendaintjem.ui.ProfileFragment;
import com.example.miagendaintjem.ui.TasksFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MainActivity extends AppCompatActivity {
    private static final String STATE_TASKS = "state_tasks";
    private static final String STATE_NEXT_ID = "state_next_id";
    private static final String STATE_NOTIFICATION_PERMISSION_REQUESTED =
            "state_notification_permission_requested";

    private final ArrayList<Tarea> tasks = new ArrayList<>();
    private int nextTaskId = 1;
    private boolean notificationPermissionRequested;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreTasks(savedInstanceState);
        configureToolbar();
        configureNavigation(savedInstanceState);
    }

    private void restoreTasks(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            loadSampleTasks();
            return;
        }

        notificationPermissionRequested = savedInstanceState.getBoolean(
                STATE_NOTIFICATION_PERMISSION_REQUESTED,
                false
        );

        ArrayList<Tarea> restored = BundleCompat.getParcelableArrayList(
                savedInstanceState,
                STATE_TASKS,
                Tarea.class
        );
        if (restored != null) {
            tasks.addAll(restored);
        }
        nextTaskId = savedInstanceState.getInt(STATE_NEXT_ID, tasks.size() + 1);
    }

    private void configureToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void configureNavigation(@Nullable Bundle savedInstanceState) {
        BottomNavigationView navigation = findViewById(R.id.bottomNavigation);
        navigation.setOnItemSelectedListener(item -> showSection(item.getItemId()));

        if (savedInstanceState == null) {
            navigation.getMenu().findItem(R.id.nav_tasks).setChecked(true);
            showSection(R.id.nav_tasks);
        }
    }

    private boolean showSection(int itemId) {
        Fragment target;
        Class<? extends Fragment> targetClass;

        if (itemId == R.id.nav_tasks) {
            target = new TasksFragment();
            targetClass = TasksFragment.class;
        } else if (itemId == R.id.nav_calendar) {
            target = new CalendarFragment();
            targetClass = CalendarFragment.class;
        } else if (itemId == R.id.nav_profile) {
            target = new ProfileFragment();
            targetClass = ProfileFragment.class;
        } else {
            return false;
        }

        Fragment current = getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainer);
        if (current != null && targetClass.isInstance(current)) {
            return true;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, target)
                .commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            Toast.makeText(
                    this,
                    getString(R.string.about_message, getString(R.string.app_name)),
                    Toast.LENGTH_LONG
            ).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    public List<Tarea> getTasks() {
        return Collections.unmodifiableList(tasks);
    }

    @NonNull
    public Tarea createTask(
            @NonNull String title,
            @NonNull Materia subject,
            @NonNull String dueDate,
            @NonNull Prioridad priority
    ) {
        Tarea task = new Tarea(nextTaskId++, title, subject, dueDate, priority);
        tasks.add(task);
        return task;
    }

    public void updateTask(@NonNull Tarea updatedTask) {
        int index = findTaskIndex(updatedTask.getId());
        if (index >= 0) {
            tasks.set(index, updatedTask);
        }
    }

    public boolean removeTask(int taskId) {
        int index = findTaskIndex(taskId);
        if (index < 0) {
            return false;
        }
        tasks.remove(index);
        return true;
    }

    public boolean hasRequestedNotificationPermission() {
        return notificationPermissionRequested;
    }

    public void markNotificationPermissionRequested() {
        notificationPermissionRequested = true;
    }

    public int findTaskIndex(int taskId) {
        for (int index = 0; index < tasks.size(); index++) {
            if (tasks.get(index).getId() == taskId) {
                return index;
            }
        }
        return -1;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(STATE_TASKS, new ArrayList<>(tasks));
        outState.putInt(STATE_NEXT_ID, nextTaskId);
        outState.putBoolean(
                STATE_NOTIFICATION_PERMISSION_REQUESTED,
                notificationPermissionRequested
        );
        super.onSaveInstanceState(outState);
    }

    private void loadSampleTasks() {
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_1),
                Materia.MOVILES,
                getString(R.string.sample_date_1),
                Prioridad.ALTA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_2),
                Materia.MOVILES,
                getString(R.string.sample_date_2),
                Prioridad.ALTA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_3),
                Materia.BASE_DATOS,
                getString(R.string.sample_date_3),
                Prioridad.MEDIA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_4),
                Materia.SOFTWARE,
                getString(R.string.sample_date_4),
                Prioridad.BAJA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_5),
                Materia.REDES,
                getString(R.string.sample_date_5),
                Prioridad.MEDIA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_6),
                Materia.MOVILES,
                getString(R.string.sample_date_6),
                Prioridad.ALTA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_7),
                Materia.SOFTWARE,
                getString(R.string.sample_date_7),
                Prioridad.BAJA
        ));
        tasks.add(new Tarea(
                nextTaskId++,
                getString(R.string.sample_task_8),
                Materia.MOVILES,
                getString(R.string.sample_date_8),
                Prioridad.MEDIA
        ));
    }
}
