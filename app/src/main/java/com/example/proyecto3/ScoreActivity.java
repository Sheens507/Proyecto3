package com.example.proyecto3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewGoodConnections;
    private TextView textViewBadConnections;
    private Button inicio;
    private int goodConnections=0;
    private int badConnections=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textViewGoodConnections = findViewById(R.id.textViewGoodConnections);
        textViewBadConnections = findViewById(R.id.textViewBadConnections);
        inicio = findViewById(R.id.inicio);

        // Recibir los datos enviados desde MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            int goodConnections = intent.getIntExtra("goodConnections", 0);
            int badConnections = intent.getIntExtra("badConnections", 0);

            // Mostrar los datos en TextViews
            textViewGoodConnections.setText("Conexiones buenas: " + goodConnections);
            textViewBadConnections.setText("Conexiones malas: " + badConnections);
        }

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Iniciar la actividad principal
                Intent intent = new Intent(ScoreActivity.this, Home.class);
                startActivity(intent);
            }
        });
    }
}
