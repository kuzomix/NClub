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
import android.widget.AdapterView;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CallFragment extends Fragment {

    private Spinner categorySpinner;  // 類別選擇 Spinner
    private Spinner tagSpinner;       // 標籤選擇 Spinner
    private static final int PICK_IMAGE_REQUEST = 1; // 圖片選擇請求代碼
    private TextView detailDate;      // 顯示日期
    private TextView detailTime;      // 顯示時間
    private Button selectDateBtn;     // 日期選擇按鈕
    private Button selectTimeBtn;     // 時間選擇按鈕
    private ImageView detailImage;    // 顯示圖片的 ImageView
    private Button selectImageBtn;    // 選擇圖片按鈕
    private Uri imageUri;             // 圖片 URI
    private EditText activityName, activityAddress, activityDescription, participantCount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);

        // 初始化界面元件
        Button create = view.findViewById(R.id.create);
        Button call_back = view.findViewById(R.id.call_back);
        selectDateBtn = view.findViewById(R.id.selectDateBtn);
        selectTimeBtn = view.findViewById(R.id.selectTimeBtn);
        detailDate = view.findViewById(R.id.detailDate);
        detailTime = view.findViewById(R.id.detailTime);
        detailImage = view.findViewById(R.id.detailImage);
        selectImageBtn = view.findViewById(R.id.selectImageBtn);
        activityName = view.findViewById(R.id.activityName);
        activityAddress = view.findViewById(R.id.detailIngredients);
        activityDescription = view.findViewById(R.id.detailDesc);
        participantCount = view.findViewById(R.id.participantCount);

        categorySpinner = view.findViewById(R.id.categorySpinner);
        tagSpinner = view.findViewById(R.id.tagSpinner);

        // 設置 categorySpinner 的適配器，從 strings.xml 中讀取字串陣列
        String[] categories = getResources().getStringArray(R.array.categories);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // 設置初始的 tagSpinner（空的選項，會根據類別更新）
        String[] defaultTags = {};
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, defaultTags);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        // 監聽 categorySpinner 的選擇，根據選擇更新 tagSpinner 的內容
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                String[] tags = getTagsForCategory(selectedCategory);
                ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tags);
                tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                tagSpinner.setAdapter(tagAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 沒有選擇時不做任何處理
            }
        });


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
            Log.e("Debug", "CallFragment- user: " + user);

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
                        .addOnSuccessListener(aVoid -> {
                            // 創建成功後顯示提示並返回主頁
                            Toast.makeText(getActivity(), "活動已創建", Toast.LENGTH_SHORT).show();

                            // 返回到主頁
                            FragmentManager fragmentManager = getParentFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame_layout, new HomeFragment());
                            fragmentTransaction.addToBackStack(null);  // 可以選擇添加到返回堆疊
                            fragmentTransaction.commit();
                        })
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
                detailImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 根據選擇的類別返回對應的標籤陣列
    private String[] getTagsForCategory(String category) {
        switch (category) {
            case "休閒娛樂":
                return getResources().getStringArray(R.array.leisure_tags);
            case "運動":
                return getResources().getStringArray(R.array.sports_tags);
            case "車聚":
                return getResources().getStringArray(R.array.car_meet_tags);
            case "旅遊":
                return getResources().getStringArray(R.array.travel_tags);
            case "美食":
                return getResources().getStringArray(R.array.food_tags);
            case "寵物":
                return getResources().getStringArray(R.array.pet_tags);
            case "學習":
                return getResources().getStringArray(R.array.learning_tags);
            default:
                return new String[]{}; // 默認返回空陣列
        }
    }
}
