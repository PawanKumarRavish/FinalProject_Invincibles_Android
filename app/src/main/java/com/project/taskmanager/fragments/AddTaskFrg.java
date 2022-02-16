package com.project.taskmanager.fragments;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.project.taskmanager.DataConvertor;
import com.project.taskmanager.R;
import com.project.taskmanager.Utils;
import com.project.taskmanager.activities.DemoActivity;
import com.project.taskmanager.databasehelper.DbHelper;
import com.project.taskmanager.models.AddCategoryModel;
import com.project.taskmanager.models.ModeModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by admin on 05/03/2019.
 */

public class AddTaskFrg extends BaseFragment {
    private static final int REQUEST_IMAGE_CAPTURE = 1000;
    private static final int PICK_IMAGE = 2000;


    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog dialog1;

    @BindView(R.id.mTaskNameTv)
    EditText mTaskNameTv;

    @BindView(R.id.mCategoryTv)
    TextView mCategoryTv;

    @BindView(R.id.mCategoryLayout)
    RelativeLayout mCategoryLayout;

    @BindView(R.id.bottom_sheet)
    LinearLayout bottomSheet;
    @BindView(R.id.mStartRec)
    Button mStartRec;
    @BindView(R.id.mStopRec)
    Button mStopRec;

    private Calendar selectedCal;

    ArrayList<ModeModel> modeList;
    Dialog alertDialog;
    Dialog alertDialog2;
    CategoriesAdapter categoriesAdapter;


    @BindView(R.id.noDataFound_tv)
    TextView mNoDataFoundTv;

    private int mYear, mMonth, mDay;
    CoordinatorLayout coordinatorLayout;

    @BindView(R.id.saveBtn)
    Button mSaveBtn;


    Unbinder unbinder;

    @BindView(R.id.date_tv)
    TextView mDueDateTv;

    @BindView(R.id.dateLayout)
    RelativeLayout mDueDateRl;


    @BindView(R.id.description_et)
    EditText mDescriptionEt;

    int selectedCategoryId = 0;

    DbHelper dbHelper;
    List<AddCategoryModel> allCategories;

    private String fileName = "";
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder;
    Random random = new Random();



    Button mAddImagesBtn;
    ImageView imageSelected;
    RecyclerView rvList;
    List<byte[]> multiImageArray  = new ArrayList<byte[]>();
    byte[] imageArray = null;
    ImageAdapter adapter = new ImageAdapter();
    int selectType=-1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_task_layout, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAddImagesBtn=view.findViewById(R.id.mAddImagesBtn);
        imageSelected=view.findViewById(R.id.imageSelected);
        rvList=view.findViewById(R.id.rvList);

        rvList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvList.setAdapter(adapter);

        dbHelper = new DbHelper(getActivity());

        allCategories = dbHelper.getAllCategories();

        modeList = new ArrayList<>();

        alertDialog = new Dialog(getActivity());
        alertDialog2 = new Dialog(getActivity());

        sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        setCategoriesAdapter(allCategories);

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);


        mDueDateRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    String fmonth, fDate;
                    int month;

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                        try {
                            if (monthOfYear < 10 && dayOfMonth < 10) {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                fDate = "0" + dayOfMonth;
                                String paddedMonth = String.format("%02d", month);
                                mDueDateTv.setText(fDate + "-" + paddedMonth + "-" + year);

                            } else {

                                fmonth = "0" + monthOfYear;
                                month = Integer.parseInt(fmonth) + 1;
                                String paddedMonth = String.format("%02d", month);
                                mDueDateTv.setText(dayOfMonth + "-" + paddedMonth + "-" + year);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        // mDateTv.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        selectedCal = Calendar.getInstance();
                        selectedCal.set(Calendar.YEAR, year);
                        selectedCal.set(Calendar.MONTH, monthOfYear);
                        selectedCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
                datePickerDialog.show();
            }
        });


        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTaskNameTv.getText().toString().trim().isEmpty()
                        || mDescriptionEt.getText().toString().trim().isEmpty()
                        || mDueDateTv.getText().toString().trim().isEmpty()
                        || mCategoryTv.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "Please add all the fields", Toast.LENGTH_SHORT).show();
                } else {
                    long primaryKey=dbHelper.insertTask(mTaskNameTv.getText().toString().trim(), mDescriptionEt.getText().toString().trim(),
                            mDueDateTv.getText().toString().trim(), mCategoryTv.getText().toString(), selectedCategoryId, "false");
                    Log.e("Key", primaryKey + "");
                    if (primaryKey == -1) {
                        Toast.makeText(getActivity(), "Data is not inserted in database", Toast.LENGTH_SHORT).show();
                    } else {
                        if(selectType==1){
                            long l = dbHelper.insertImageByTaskId(imageArray, (int) primaryKey);
                            Log.e("Inserted",l+"");
                            Toast.makeText(getActivity(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                            HomeFragment homeFragment = new HomeFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();

                        }else{
                            for(int i =0;i<multiImageArray.size();i++){
                                long l = dbHelper.insertImageByTaskId(multiImageArray.get(i), (int) primaryKey);
                            }
                            Toast.makeText(getActivity(), "Data Successfully inserted", Toast.LENGTH_SHORT).show();
                            HomeFragment homeFragment = new HomeFragment();
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();


                        }

                    }
                }


            }
        });

        mCategoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.show();
            }
        });

        mStartRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPermission()){
                    fileName = CreateRandomAudioFileName(5) + "recording.3gp";
                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    fileName;
                    setMediaRecorderReady();
                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                        mStartRec.setEnabled(false);
                        mStopRec.setEnabled(true);
                    } catch (IllegalStateException e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(getActivity(), "Recording started", Toast.LENGTH_LONG).show();
                }else{
                    requestPermission();
                }

            }
        });

        mStopRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaRecorder.stop();
                    Log.e("Saved Path",AudioSavePathInDevice);
                    mStopRec.setEnabled(false);
                    mStartRec.setEnabled(true);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        mAddImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Choose image from ");
                builder.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        try {
                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                        }
                    }
                });
                builder.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        gallery.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        startActivityForResult(gallery, PICK_IMAGE);
                    }
                });

                AlertDialog ad = builder.create();
                ad.show();
            }
        });


    }

    public void setMediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, 3000);
    }


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getActivity(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 3000:
                if (grantResults.length > 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    private void setCategoriesAdapter(List<AddCategoryModel> allCategories) {

        View view = getLayoutInflater().inflate(R.layout.dialog_catgories, null);

        RecyclerView recyclerVieww = (RecyclerView) view.findViewById(R.id.mRecyclerView);
        recyclerVieww.setHasFixedSize(true);
        recyclerVieww.setLayoutManager(new LinearLayoutManager(getActivity()));

        TextView mNoDataFound = (TextView) view.findViewById(R.id.noDataFound_tvv);

        if (allCategories.size() == 0) {
            mNoDataFound.setVisibility(View.VISIBLE);
            recyclerVieww.setVisibility(View.GONE);
        } else {
            mNoDataFound.setVisibility(View.GONE);
            recyclerVieww.setVisibility(View.VISIBLE);
            categoriesAdapter = new CategoriesAdapter(getActivity(), allCategories);
            recyclerVieww.setAdapter(categoriesAdapter);
        }

        dialog1 = new BottomSheetDialog(getActivity());
        dialog1.setContentView(view);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        homeInteractiveListener.toggleBackArrowVisiblity(View.VISIBLE);
        homeInteractiveListener.setToolBarTitle(getString(R.string.add_task));
        homeInteractiveListener.toggleCalenderVisiblity(View.GONE);
        homeInteractiveListener.toggleTabVisiblity(View.GONE);
    }


    public String CreateRandomAudioFileName(int string) {
        String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
        StringBuilder stringBuilder = new StringBuilder(string);
        int i = 0;
        while (i < string) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++;
        }
        return stringBuilder.toString();
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
        multiImageArray.clear();
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageSelected.setImageBitmap(imageBitmap);
            selectType = 1;
            imageArray = DataConvertor.convertImageToByteArray(imageBitmap);
            imageSelected.setVisibility(View.VISIBLE);
            //binding.rvList.setVisibility(View.GONE);
        }
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            selectType = 2;
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            if(data.getData()!=null){


                try {
                    Uri mImageUri=data.getData();
                    mArrayUri.add(mImageUri);
                    Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                    Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 120, 120, false);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    multiImageArray.add(byteArray);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        try {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri mImageUri = item.getUri();
                            mArrayUri.add(mImageUri);
                            Bitmap bmp = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), mImageUri);
                            Bitmap bitmap = Bitmap.createScaledBitmap(bmp, 120, 120, false);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            multiImageArray.add(byteArray);
//                            multiImageArray.add(DataConvertor.convertUriToByteArray(mImageUri, this));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.v("TAG", "Selected Images" + mArrayUri.size());

                }
            }
            /*if(!mArrayUri.isEmpty()){
                binding.btnUploadImage.setEnabled(true);
            }*/
            imageSelected.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);

            adapter.updateList(mArrayUri);
        }
    }


    //---------------------------lEDGER lIST Adapter------------------------------------//
    public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.MyViewHolder> {

        Context context;
        List<AddCategoryModel> childFeedList;


        public CategoriesAdapter(Context context, List<AddCategoryModel> childFeedList) {
            this.context = context;
            this.childFeedList = childFeedList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_categories_design, parent, false);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            AddCategoryModel ledger = childFeedList.get(position);
            holder.mCategoryNameTv.setText(ledger.getCategoryName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCategoryTv.setText(ledger.getCategoryName());
                    selectedCategoryId = ledger.getId();
                    dialog1.dismiss();
                }
            });


        }


        @Override
        public int getItemCount() {
            return childFeedList.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView mCategoryNameTv;

            public MyViewHolder(View itemView) {
                super(itemView);

                mCategoryNameTv = (TextView) itemView.findViewById(R.id.mCategoryNameTv);

            }
        }
    }


    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
        List<Uri> dataList = new ArrayList<Uri>();

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_design, parent, false);
            return new ImageAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.image.setImageURI(dataList.get(position));

        }

        @Override
        public void setHasStableIds(boolean hasStableIds) {
            super.setHasStableIds(hasStableIds);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public void updateList(List<Uri> dlist) {
            dataList.clear();
            dataList = new ArrayList<>(dlist);
            notifyDataSetChanged();
        }

        public void clearList() {
            dataList.clear();
            notifyDataSetChanged();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);

                image = (ImageView) itemView.findViewById(R.id.image);

            }
        }
    }



}
