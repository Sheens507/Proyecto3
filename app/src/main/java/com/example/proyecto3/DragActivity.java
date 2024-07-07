package com.example.proyecto3;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DragActivity extends AppCompatActivity {

    private ImageView imageViewDropArea;
    private ImageView imageView1, imageView2, imageView3;
    private Button btnCalculateStats;
    private TextView textViewGoodConnections;

    // Etiqueta esperada para comparar
    private String expectedTag = "imagen1"; // Cambia esto por la etiqueta que deseas comparar

    // Contadores de conexiones acumulados
    private int totalGoodConnections;
    private int totalBadConnections;

    // Contadores de conexiones locales (dentro de esta actividad)
    private int localGoodConnections;
    private int localBadConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag);

        imageViewDropArea = findViewById(R.id.imageViewDropArea);
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        textViewGoodConnections = findViewById(R.id.textViewGoodConnections);
        btnCalculateStats = findViewById(R.id.btnCalculateStats2);

        // Recibir los datos enviados desde MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            // Acumular los valores en las variables de clase
            totalGoodConnections = intent.getIntExtra("goodConnections", 0);
            totalBadConnections = intent.getIntExtra("badConnections", 0);

            // Mostrar los datos en TextViews
            textViewGoodConnections.setText("Conexiones buenas: " + totalGoodConnections);
            // textViewBadConnections.setText("Conexiones malas: " + totalBadConnections);
        }

        // Configura la etiqueta para cada ImageView
        imageView1.setTag("imagen1");
        imageView2.setTag("imagen2");
        imageView3.setTag("imagen3");

        imageView1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDrag(v);
                return true;
            }
        });

        imageView2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDrag(v);
                return true;
            }
        });

        imageView3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startDrag(v);
                return true;
            }
        });

        imageViewDropArea.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DROP:
                        // Resetear los contadores locales
                        localGoodConnections = 0;
                        localBadConnections = 0;

                        View view = (View) event.getLocalState();
                        if (view instanceof ImageView) {
                            ImageView draggedImageView = (ImageView) view;
                            String imageTag = (String) draggedImageView.getTag();

                            if (imageTag != null && imageTag.equals(expectedTag)) {
                                // La conexión es correcta
                                localGoodConnections++;
                            } else {
                                // La conexión es incorrecta
                                localBadConnections++;
                            }

                            // Actualiza la imagen en imageViewDropArea
                            Drawable drawable = draggedImageView.getDrawable();
                            imageViewDropArea.setImageDrawable(drawable);

                            // Mostrar los datos actualizados en TextViews
                            textViewGoodConnections.setText("Conexiones buenas: " + (totalGoodConnections + localGoodConnections));
                            // textViewBadConnections.setText("Conexiones malas: " + (totalBadConnections + localBadConnections));
                        }
                        break;
                }
                return true;
            }
        });

        btnCalculateStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sumar los contadores locales a los acumulados
                totalGoodConnections += localGoodConnections;
                totalBadConnections += localBadConnections;

                // Abrir la nueva actividad ScoreActivity y pasar los valores acumulados
                Intent intent = new Intent(DragActivity.this, ScoreActivity.class);
                intent.putExtra("goodConnections", totalGoodConnections);
                intent.putExtra("badConnections", totalBadConnections);
                startActivity(intent);
            }
        });
    }

    private void startDrag(View view) {
        view.startDrag(null, new View.DragShadowBuilder(view), view, 0);
    }
}
