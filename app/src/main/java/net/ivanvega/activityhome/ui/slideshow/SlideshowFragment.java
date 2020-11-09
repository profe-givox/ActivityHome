package net.ivanvega.activityhome.ui.slideshow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import net.ivanvega.activityhome.R;

import static android.app.Activity.RESULT_OK;

public class SlideshowFragment extends Fragment
implements View.OnClickListener
{

    private SlideshowViewModel slideshowViewModel;
    private Button btnMin, btnImgFull;
    ImageView img;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);



        inicilizarUI(root);





        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {

            }
        });
        return root;
    }

    private void inicilizarUI(View root) {
        btnMin = root.findViewById(R.id.btnMin);
        btnImgFull = root.findViewById(R.id.btnIgFull);
        img = root.findViewById(R.id.imgFoto);

        btnImgFull.setOnClickListener(this);
        btnMin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
         if(view.getId()== R.id.btnMin) {
            dispatchTakePictureIntent();
        }
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager() )!= null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode ==  RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            img.setImageBitmap(imageBitmap);
        }
    }
}