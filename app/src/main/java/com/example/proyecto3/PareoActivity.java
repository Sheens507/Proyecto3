package com.example.proyecto3;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Pair;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PareoActivity extends AppCompatActivity {

    private List<Pair<ImageView, TextView>> connections = new ArrayList<>();
    private DrawingView lineView;
    private Button btnCalculateStats;
    private int initialGoodConnections;
    private int initialBadConnections;
    private int currentGoodConnections; // Variable para mantener el conteo actualizado
    private int currentBadConnections;  // Variable para mantener el conteo actualizado

    private final int TOTAL_CONNECTIONS = 5; // Total de conexiones requeridas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pareo);

        // Obtener los datos enviados desde SecondActivity
        Intent intent = getIntent();
        initialGoodConnections = intent.getIntExtra("goodConnections", 0);
        initialBadConnections = intent.getIntExtra("badConnections", 0);

        // Inicializar los contadores actuales con los valores iniciales recibidos
        currentGoodConnections = initialGoodConnections;
        currentBadConnections = initialBadConnections;

        // Resto del código de inicialización de vistas y listeners...
        ImageView imageViewTrain = findViewById(R.id.imageViewTrain);
        imageViewTrain.setTag("Train");
        TextView textViewTrain = findViewById(R.id.textViewTrain);

        ImageView imageViewCar = findViewById(R.id.imageViewCar);
        imageViewCar.setTag("Car");
        TextView textViewCar = findViewById(R.id.textViewCar);

        ImageView imageViewPlane = findViewById(R.id.imageViewPlane);
        imageViewPlane.setTag("Airplane");
        TextView textViewPlane = findViewById(R.id.textViewPlane);

        ImageView imageViewBoat = findViewById(R.id.imageViewBoat);
        imageViewBoat.setTag("Boat");
        TextView textViewBoat = findViewById(R.id.textViewBoat);

        ImageView imageViewBike = findViewById(R.id.imageViewBike);
        imageViewBike.setTag("Bike");
        TextView textViewBike = findViewById(R.id.textViewBike);

        btnCalculateStats = findViewById(R.id.btnCalculateStats);
        lineView = new DrawingView(this);
        ViewGroup rootView = findViewById(android.R.id.content);
        rootView.addView(lineView);

        setupDragAndDrop(imageViewTrain, textViewTrain);
        setupDragAndDrop(imageViewCar, textViewCar);
        setupDragAndDrop(imageViewPlane, textViewPlane);
        setupDragAndDrop(imageViewBoat, textViewBoat);
        setupDragAndDrop(imageViewBike, textViewBike);

        btnCalculateStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateStats();
            }
        });
    }

    private void setupDragAndDrop(final ImageView imageView, final TextView textView) {
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
                    v.startDragAndDrop(null, shadowBuilder, imageView, 0);
                    return true;
                } else {
                    return false;
                }
            }
        });

        textView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        return true;
                    case DragEvent.ACTION_DROP:
                        ImageView draggedImage = (ImageView) event.getLocalState();

                        // Remove any existing connection for this image
                        Iterator<Pair<ImageView, TextView>> iterator = connections.iterator();
                        while (iterator.hasNext()) {
                            Pair<ImageView, TextView> connection = iterator.next();
                            if (connection.first == draggedImage) {
                                iterator.remove();
                                break;  // Assuming only one connection per image
                            }
                        }

                        // Create new connection
                        Pair<ImageView, TextView> newConnection = new Pair<>(draggedImage, textView);
                        connections.add(newConnection);

                        lineView.invalidate(); // Redraw lines
                        return true;
                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void calculateStats() {
        // Reiniciar contadores cada vez que se calcula el nuevo estado
        currentGoodConnections = initialGoodConnections;
        currentBadConnections = initialBadConnections;

        for (Pair<ImageView, TextView> connection : connections) {
            ImageView imageView = connection.first;
            TextView textView = connection.second;

            // Obtener la etiqueta de la imagen
            String imageTag = (String) imageView.getTag();

            // Comparar el texto de la TextView con la etiqueta de la imagen para determinar la conexión
            String textViewText = textView.getText().toString();

            if (textViewText.equals(imageTag)) {
                // La conexión es correcta
                currentGoodConnections++;
            } else {
                // La conexión es incorrecta
                currentBadConnections++;
            }
        }

        // Calcular conexiones faltantes y sumarlas a badConnections
        int missingConnections = TOTAL_CONNECTIONS - connections.size();
        currentBadConnections += missingConnections;

        // Crear un Intent para iniciar DragActivity y pasar datos
        Intent intent = new Intent(PareoActivity.this, DragActivity.class);
        intent.putExtra("goodConnections", currentGoodConnections);
        intent.putExtra("badConnections", currentBadConnections);
        startActivity(intent);
        finish();
    }

    private class DrawingView extends View {
        private Paint paint;

        public DrawingView(Context context) {
            super(context);
            paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(5);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            for (Pair<ImageView, TextView> connection : connections) {
                float startX = getViewCenterX(connection.first);
                float startY = getViewCenterY(connection.first);
                float endX = getViewCenterX(connection.second);
                float endY = getViewCenterY(connection.second);
                canvas.drawLine(startX, startY, endX, endY, paint);
            }
        }

        private float getViewCenterX(View view) {
            return view.getX() + view.getWidth() / 2;
        }

        private float getViewCenterY(View view) {
            return view.getY() + view.getHeight() / 2;
        }
    }
}
