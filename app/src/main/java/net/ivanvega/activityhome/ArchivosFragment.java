package net.ivanvega.activityhome;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.stream.Stream;

import static android.os.Environment.DIRECTORY_MUSIC;

public class ArchivosFragment extends Fragment {

    private static final int REQUEST_CODE_WRITEEXTERNAL = 1001;
    private ArchivosViewModel mViewModel;
    TextView textView;
    Button btnAbrir;
    Button btnGuadar;

    public static ArchivosFragment newInstance() {
        return new ArchivosFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.archivos_fragment, container, false);
        textView =  layout.findViewById(R.id.txt);
        btnAbrir =  layout.findViewById(R.id.btnOpen);
        btnGuadar =  layout.findViewById(R.id.btnSave);

        btnGuadar.setOnClickListener( view -> validarPermiso() );
        btnAbrir.setOnClickListener(view -> abrirArchivo());
        return layout;
    }

    private void validarPermiso() {

        if (ContextCompat.checkSelfPermission(
                getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
             guardarArchivoExterna();
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.

            Toast.makeText(getActivity(),
                    "Es necesario para escribir archivos",
                    Toast.LENGTH_LONG
                    ).show();



            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_WRITEEXTERNAL);

        } else {
            // You can directly ask for the permission.
            requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_WRITEEXTERNAL);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode==ArchivosFragment.REQUEST_CODE_WRITEEXTERNAL){

            if(permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    guardarArchivoExterna();
                }else{
                    Toast.makeText(getActivity(),
                            "Inhabilitada por falta de permisos",
                            Toast.LENGTH_LONG
                    ).show();
                    btnGuadar.setEnabled(false);
                }
            }

        }

    }

    private void guardarArchivoExterna() {


        Toast.makeText(getActivity(),
                "Archivo almacenado",
                Toast.LENGTH_LONG
        ).show();




        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "mifile.txt");
        startActivityForResult(intent, 999);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001){
            Toast.makeText(getActivity(),
                    data.getData().toString(), Toast.LENGTH_LONG
                    ) .show();

        }else if (requestCode==999){

            Uri uriDc = data.getData();

            Toast.makeText(getActivity(),
                    uriDc.toString(), Toast.LENGTH_LONG
            ) .show();

            try {
                OutputStream outputStream =
                        getActivity().getContentResolver().openOutputStream(uriDc);
                outputStream.write(textView.getText().toString().getBytes());
                outputStream.close();
                Toast.makeText(getActivity(),
                        "Archiv actualizado", Toast.LENGTH_LONG
                ) .show();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

    }

    private void abrirArchivo() {
        String contenido ="";
        try {
            FileInputStream fileInputStream =
                    getActivity().openFileInput("mitxt");

            int dato=-1;

            do{
                dato = fileInputStream.read();
                if(dato!=-1)
                    contenido += (char)dato;
            }
            while(  dato != -1);

            textView.setText(contenido);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void guardarArchivo (){
        try {
            FileOutputStream  fileOutputStream =
                    getActivity().openFileOutput("mitxt", Context.MODE_APPEND  );

            fileOutputStream.write(textView.getText().toString().getBytes());

            fileOutputStream.close();



            Toast.makeText(getActivity() , "Archivo salvado",
                    Toast.LENGTH_LONG).show();
            textView.setText("");
             for(String file :  getActivity().getFilesDir().list()){
                 Log.i("XXXZZZ", file);
             }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ArchivosViewModel.class);
        // TODO: Use the ViewModel
    }

}