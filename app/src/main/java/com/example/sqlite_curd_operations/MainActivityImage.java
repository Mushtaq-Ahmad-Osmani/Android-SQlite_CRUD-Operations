package com.example.sqlite_curd_operations;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivityImage extends AppCompatActivity {

    private DatabaseConnection databaseConnection;
    private String id;
    private ImageView imageView;
    private Button show;
    private EditText user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageView = findViewById(R.id.imageView);
        id = getIntent().getStringExtra("id");
        show = findViewById(R.id.buttonImageShow);
        try {
            databaseConnection = new DatabaseConnection(this);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Button btn = findViewById(R.id.button2);
        btn.setOnClickListener(m->{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100 , stream);
            byte[] bytes = stream.toByteArray();
            boolean state = databaseConnection.addImage(bytes, id);
            if (state){
                Toast.makeText(this, "Insert Image", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            show.setOnClickListener(m->{
                    imageView.setImageBitmap(bitmap);

            });

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}