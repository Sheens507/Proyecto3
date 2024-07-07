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
    private TextView textViewMesaje;
    private Button inicio;
    private int goodConnections=0;
    private int badConnections=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        textViewGoodConnections = findViewById(R.id.textViewGoodConnections);
        textViewBadConnections = findViewById(R.id.textViewBadConnections);
        textViewMesaje = findViewById(R.id.mensaje);
        inicio = findViewById(R.id.inicio);

        // Recibir los datos enviados desde MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            int goodConnections = intent.getIntExtra("goodConnections", 0);
            int badConnections = intent.getIntExtra("badConnections", 0);

            // Mostrar los datos en TextViews
            textViewGoodConnections.setText("Respuestas Correctas: " + goodConnections);
            textViewBadConnections.setText("Respuesatas Incorrectas: " + badConnections);
            if (goodConnections == 6) {
                textViewMesaje.setText("\uD83C\uDF89Felicidades, has hecho un buen trabajo\uD83C\uDF89");
            } else if (goodConnections >= 3 && goodConnections < 6) {
                textViewMesaje.setText("Buen trabajo, pero puedes mejorar\uD83D\uDCAA");
            } else {
                textViewMesaje.setText("Más suerte la próxima vez, tú puedes todo lo que te propones\uD83D\uDC4F");
            }

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
