package com.example.miagendaintjem;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.IntentCompat;

import com.example.miagendaintjem.model.Tarea;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public final class DetalleTareaActivity extends AppCompatActivity {
    public static final String EXTRA_TAREA =
            "com.example.miagendaintjem.EXTRA_TAREA";
    public static final String EXTRA_POSICION =
            "com.example.miagendaintjem.EXTRA_POSICION";

    private Tarea task;
    private int sourcePosition;
    private TextInputEditText titleInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        task = IntentCompat.getParcelableExtra(
                getIntent(),
                EXTRA_TAREA,
                Tarea.class
        );
        sourcePosition = getIntent().getIntExtra(EXTRA_POSICION, -1);
        if (task == null) {
            finish();
            return;
        }

        configureToolbar();
        bindTask();
    }

    private void configureToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(view ->
                getOnBackPressedDispatcher().onBackPressed());
    }

    private void bindTask() {
        titleInput = findViewById(R.id.detailTitleInput);
        TextView subjectText = findViewById(R.id.detailSubjectText);
        TextView dueDateText = findViewById(R.id.detailDueDateText);
        TextView priorityText = findViewById(R.id.detailPriorityText);
        MaterialButton shareButton = findViewById(R.id.shareButton);
        MaterialButton saveButton = findViewById(R.id.saveButton);

        titleInput.setText(task.getTitulo());
        subjectText.setText(getString(
                R.string.detail_subject_format,
                task.getMateria()
        ));
        dueDateText.setText(getString(
                R.string.detail_due_date_format,
                task.getFechaEntrega()
        ));
        priorityText.setText(getString(
                R.string.detail_priority_format,
                getString(task.getPrioridad().getLabelRes())
        ));
        priorityText.setTextColor(ContextCompat.getColor(
                this,
                task.getPrioridad().getColorRes()
        ));

        shareButton.setOnClickListener(view -> shareTask());
        saveButton.setOnClickListener(view -> returnEditedTask());
    }

    private void returnEditedTask() {
        String editedTitle = readTitle();
        if (editedTitle.isEmpty()) {
            titleInput.setError(getString(R.string.empty_title_error));
            return;
        }

        Tarea updated = task.withTitulo(editedTitle);
        Intent result = new Intent()
                .putExtra(EXTRA_TAREA, updated)
                .putExtra(EXTRA_POSICION, sourcePosition);
        setResult(RESULT_OK, result);
        finish();
    }

    private void shareTask() {
        String visibleTitle = readTitle();
        if (visibleTitle.isEmpty()) {
            visibleTitle = task.getTitulo();
        }

        String body = getString(
                R.string.share_body,
                visibleTitle,
                task.getMateria(),
                task.getFechaEntrega(),
                getString(task.getPrioridad().getLabelRes())
        );

        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(
                        Intent.EXTRA_SUBJECT,
                        getString(R.string.share_subject, visibleTitle)
                )
                .putExtra(Intent.EXTRA_TEXT, body);

        startActivity(Intent.createChooser(
                shareIntent,
                getString(R.string.share_chooser_title)
        ));
    }

    @NonNull
    private String readTitle() {
        return titleInput.getText() == null
                ? ""
                : titleInput.getText().toString().trim();
    }
}
