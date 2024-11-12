package com.example.bottom_main;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;   //11.10添加
import android.widget.ArrayAdapter;  //11.10添加
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CallFragment extends Fragment {

    private Spinner tagSpinner;       //11.10添加
    private static final int PICK_IMAGE_REQUEST = 1; // 常量用於圖片選擇請求
    private TextView detailDate; // 用於顯示選擇的日期
    private TextView detailTime; // 用於顯示選擇的時間
    private Button selectDateBtn; // 用於選擇日期的按鈕
    private Button selectTimeBtn; // 用於選擇時間的按鈕
    private ImageView detailImage; // 用於顯示選擇的圖片
    private Button selectImageBtn; // 用於選擇圖片的按鈕
    private Uri imageUri; // 用於存儲圖片的 URI
    private Spinner categorySpinner; // 新增 Spinner 元件
    private EditText activityName, activityAddress, activityDescription, participantCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        // 初始化界面元件
        Button create = view.findViewById(R.id.create);
        Button call_back = view.findViewById(R.id.call_back);
        selectDateBtn = view.findViewById(R.id.selectDateBtn); // 日期選擇按鈕
        selectTimeBtn = view.findViewById(R.id.selectTimeBtn); // 時間選擇按鈕
        detailDate = view.findViewById(R.id.detailDate); // 顯示日期
        detailTime = view.findViewById(R.id.detailTime); // 顯示時間
        detailImage = view.findViewById(R.id.detailImage); // 顯示圖片
        selectImageBtn = view.findViewById(R.id.selectImageBtn); // 選擇圖片按鈕
        activityName = view.findViewById(R.id.activityName);
        activityAddress = view.findViewById(R.id.detailIngredients);
        activityDescription = view.findViewById(R.id.detailDesc);
        participantCount = view.findViewById(R.id.participantCount);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        tagSpinner = view.findViewById(R.id.tagSpinner);

        // 設置選擇日期的功能
        selectDateBtn.setOnClickListener(view1 -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                    (datePicker, selectedYear, selectedMonth, selectedDay) -> {
                        String date = String.format("%04d/%02d/%02d", selectedYear, selectedMonth + 1, selectedDay);
                        detailDate.setText(date);
                    }, year, month, day);
            datePickerDialog.show();
        });

        // 設置選擇時間的功能
        selectTimeBtn.setOnClickListener(view12 -> {
            Calendar calendar1 = Calendar.getInstance();
            int hour = calendar1.get(Calendar.HOUR_OF_DAY);
            int minute = calendar1.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                    (timePicker, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        detailTime.setText(time);
                    }, hour, minute, true);
            timePickerDialog.show();
        });

        // 設置選擇圖片的功能
        selectImageBtn.setOnClickListener(view13 -> openFileChooser());

        // 創建活動的按鈕
        create.setOnClickListener(v -> {
            String nameString = activityName.getText().toString();
            String addressString = activityAddress.getText().toString();
            String descriptionString = activityDescription.getText().toString();
            String perticipantCountString = participantCount.getText().toString();
            int activityBed = 5;
            if (!perticipantCountString.isEmpty()) {
                activityBed = Integer.parseInt(perticipantCountString);
            }
            String selectedDate = detailDate.getText().toString();
            String selectedTime = detailTime.getText().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();
            String selectedTag = tagSpinner.getSelectedItem().toString();
            String imageUriString = imageUri == null ? "https://example.com/default_image.jpg" : imageUri.toString();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseUser user = auth.getCurrentUser();

            if (user != null) {
                String itemId = FirebaseDatabase.getInstance().getReference("items").push().getKey();
                Map<String, Object> itemData = new HashMap<>();
                itemData.put("address", addressString);
                itemData.put("bed", activityBed);
                itemData.put("dateTour", selectedDate);
                itemData.put("timeTour", selectedTime);
                itemData.put("description", descriptionString);
                itemData.put("pic", imageUriString);
                itemData.put("title", nameString);
                itemData.put("category", selectedCategory);
                itemData.put("tag", selectedTag);
                itemData.put("ownUser", user.getUid());

                DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference("Items").child(itemId);
                itemsRef.setValue(itemData)
                        .addOnSuccessListener(aVoid -> Toast.makeText(getActivity(), "活動已創建", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(getActivity(), "創建活動失敗", Toast.LENGTH_SHORT).show());
            }
        });

        // 返回主頁按鈕
        call_back.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    // 打開文件選擇器以選擇圖片
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "選擇圖片"), PICK_IMAGE_REQUEST);
    }

    // 處理圖片選擇的結果
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                detailImage.setImageBitmap(bitmap); // 將選擇的圖片顯示在 ImageView 中
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "選擇圖片失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
