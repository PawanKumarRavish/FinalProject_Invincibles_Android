package com.project.taskmanager.fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.project.taskmanager.R;
import com.project.taskmanager.SharedPreference;
import com.project.taskmanager.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;
import static com.project.taskmanager.interfaces.HomeInteractiveListener.PROFILE_TAB;

/**
 * Created by Gaganjot Singh on 10/04/2019.
 */

public class Profile extends BaseFragment {

    BottomSheetDialog dialog;
    String path, fileName;
    Bitmap bitmap = null;

    private final int REQUEST_CODE_IME = 20;
    protected static final int GALLERY_PICTURE_0 = 10;
    protected static final int CAMERA_REQUEST_0 = 00;

    @BindView(R.id.name_tv)
    TextView mNameTv;

    Unbinder unbinder;

    @BindView(R.id.mProfileImage)
    CircleImageView mProfileImage;

    @BindView(R.id.save_btn)
    Button mSaveBtn;


    @BindView(R.id.editImg)
    ImageView mEditImg;

    private static Profile mInstance;
    @BindView(R.id.edit_ll)
    LinearLayout mEditLl;

    public static Profile getInstance() {
        if (Utils.isSingelton) {
            mInstance = null;
            mInstance = new Profile();
            return mInstance;
        }
        if (mInstance == null) {
            mInstance = new Profile();
        }

        return mInstance;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNameTv.setText(SharedPreference.getName());

        initEditNameLayout();

        if (SharedPreference.getFileName() != null) {
            readFromPrivateStorage(SharedPreference.getFileName());
        }


        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (checkPermission()) {
                        startDialog0();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_IME);
                    }

                } else {
                    startDialog0();

                }
            }
        });


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mProfileImage.getDrawable() == null) {
                    Toast.makeText(getActivity(), "Please select image", Toast.LENGTH_LONG).show();
                } else {
                    //saveToInternalStorage(bitmap);

                }
            }
        });


        mEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog.show();

            }
        });

        mEditLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                dialog.show();
            }
        });
    }

    private void initEditNameLayout() {
        View view = getLayoutInflater().inflate(R.layout.edit_profile_layout, null);

        EditText mNewName = (EditText) view.findViewById(R.id.newName_et);
        Button mSaveBtn = (Button) view.findViewById(R.id.saveBtn);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                if (mNewName.getText().toString().length() == 0) {
                    Toast.makeText(getActivity(), "Please enter name", Toast.LENGTH_SHORT).show();
                } else {
                    mNameTv.setText(mNewName.getText().toString().trim());
                    SharedPreference.setName(mNewName.getText().toString().trim());
                    Toast.makeText(getActivity(), "Your name has been changed successfully", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });

        dialog = new BottomSheetDialog(getActivity());
        dialog.setContentView(view);
    }

    private Bitmap readFromPrivateStorage(String fileName) {
        FileInputStream fileInputStream;
        Bitmap bitmap = null;
        try {
            fileInputStream = getActivity().openFileInput(fileName);
            bitmap = BitmapFactory.decodeStream(fileInputStream);
            mProfileImage.setImageBitmap(bitmap);
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    public boolean checkPermission() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            return false;
        }
        return true;

    }


    private void startDialog0() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Upload Pictures Option");
        myAlertDialog.setMessage("How do you want to set your picture?");

        myAlertDialog.setPositiveButton("Gallery",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent pictureActionIntent = null;

                        pictureActionIntent = new Intent(
                                Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(
                                pictureActionIntent,
                                GALLERY_PICTURE_0);

                    }
                });

       /* myAlertDialog.setNegativeButton("Camera",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_0);


                    }
                });*/
        myAlertDialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == REQUEST_CODE_IME && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
            startDialog0();
        } else {
            startDialogForPermissionOnly();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && requestCode == GALLERY_PICTURE_0) {

            if (resultCode == RESULT_OK) {
                Uri targetUri = data.getData();

                path = getRealPathFromUri(targetUri);
                fileName = getFileName(targetUri);

                try {
                    insertInPrivateStorage(fileName, path);
                    bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(targetUri));
                    mProfileImage.setImageBitmap(bitmap);
                    mProfileImage.setVisibility(View.VISIBLE);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else if (requestCode == CAMERA_REQUEST_0) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                mProfileImage.setImageBitmap(photo);
                mProfileImage.setVisibility(View.VISIBLE);
            }


        }

    }

    private void insertInPrivateStorage(String name, String path) throws FileNotFoundException {

        FileOutputStream fileOutputStream = getActivity().openFileOutput(name, MODE_APPEND);
        File file = new File(path);
        try {
            byte[] bytes = getBytesFromFile(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            SharedPreference.setFileName(name);
            Log.e("Get File Name", SharedPreference.getFileName());
            Toast.makeText(getActivity(), "Profile image saved successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private byte[] getBytesFromFile(File file) throws IOException {
        byte[] data = FileUtils.readFileToByteArray(file);
        return data;
    }


    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private String getRealPathFromUri(Uri targetUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(targetUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return null;

    }

    private void startDialogForPermissionOnly() {
        AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                getActivity());
        myAlertDialog.setTitle("Permission is necessary");
        myAlertDialog.setMessage("Please allow permission in order to access this feature. After clicking on OKAY button, go to permissions tab and check all permissions.");


        myAlertDialog.setNegativeButton("Okay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);

                    }
                });
        myAlertDialog.show();
    }


    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.selectTab(PROFILE_TAB);
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle("Profile");
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.VISIBLE);
    }

    public static String getTagName() {
        return Profile.class.getSimpleName();
    }
}
