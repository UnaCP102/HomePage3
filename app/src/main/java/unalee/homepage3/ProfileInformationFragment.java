package unalee.homepage3;

import android.Manifest;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;



public class ProfileInformationFragment extends Fragment {
    public static final String  TAG = "ProfileInformationFragment";
    private static final int RESULT_OK = -1;
    private ImageView imageView;
    private ImageButton ibChange;
    private FloatingActionButton fabSetting, fabLogOut;
    private FragmentActivity activity;
    private static final int REQUEST_TAKE_PICTURE_SMALL = 0;
    private static final int REQUEST_PICK_PICTURE = 1;
    private CommonTask userFindTask;
    private TextView txMyMemberNumber, txMyName, txMemberEmail, txPhoneNumber;
    private byte[] image;




    public ProfileInformationFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = getActivity();
        return inflater.inflate(R.layout.fragment_profile_information, container, false);

    }

    @Override
    public void onStart() {
        super.onStart();
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        Common.askPermissions(getActivity(), permissions, Common.REQ_EXTERNAL_STORAGE);
        fillprofile();


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        findview();

        ibChange.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_PICK_PICTURE);
                ibChange.bringToFront(); //把相機那張圖一直放在最上面
            }
        });

        fabLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = activity.getSharedPreferences
                        (Common.PREF_FILE, MODE_PRIVATE);
                preferences.edit().putBoolean("login", false)
                        .putString("email", "")
                        .putString("password", "")
                        .apply();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        fabSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileSettingActivity.class);
                startActivity(intent);
            }
        });

    }

    private void findview() {
        imageView = getActivity().findViewById(R.id.ivProfilePicture);
        ibChange = getActivity().findViewById(R.id.ibChange);
        fabLogOut = getActivity().findViewById(R.id.fabLogOut);
        fabSetting = getActivity().findViewById(R.id.fabSetting);
        txMyMemberNumber = getActivity().findViewById(R.id.txMyMemberNumber);
        txMyName = getActivity().findViewById(R.id.txMyName);
        txMemberEmail = getActivity().findViewById(R.id.txMemberEmail);
        txPhoneNumber = getActivity().findViewById(R.id.txPhoneNumber);
    }


    //以下為選照片method
    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            int newSize = 512;
            switch (requestCode) {
                case REQUEST_TAKE_PICTURE_SMALL:  //縮圖
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {
                        Bitmap picture = (Bitmap) bundle.get("data");
                        imageView.setImageBitmap(picture);
                    }
                    break;
                case REQUEST_PICK_PICTURE:    //挑圖
                    Uri uri = intent.getData();
                    if (uri != null) {
                        String[] columns = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(uri, columns,
                                null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String imagePath = cursor.getString(0);
                            cursor.close();
                            Bitmap srcImage = BitmapFactory.decodeFile(imagePath);
                            Bitmap downsizedImage = Common.downSize(srcImage, newSize);
                            imageView.setImageBitmap(downsizedImage);
                            ByteArrayOutputStream out = new ByteArrayOutputStream();
                            srcImage.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            image = out.toByteArray();
                        }
                    }
                    break;
            }
        }

            String imageBase64 = "";
            if (image != null) imageBase64 = Base64.encodeToString(image, Base64.DEFAULT);
        SharedPreferences preferences = activity.getSharedPreferences
                (Common.PREF_FILE, MODE_PRIVATE);
            int idCustomer = preferences.getInt("IdCustomer", 0);

            if (Common.networkConnected(activity)) {
                String url = Common.URL + "/CustomerServlet";
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("action", "updateImage");
                jsonObject.addProperty("IdCustomer", idCustomer);
                jsonObject.addProperty("imageBase64", imageBase64);

                String jsonOut = jsonObject.toString();
                userFindTask = new CommonTask(url, jsonOut);
                Customer customer = null;
                try {
                    String result = userFindTask.execute().get();
                    Log.e(TAG, "result:" + result);
                    customer = new Gson().fromJson(result, Customer.class);
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                if (customer == null) {
                    Common.showToast(activity, R.string.msg_NoProfileFound);

                }
            }
        }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case Common.REQ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ibChange.setEnabled(true);
                } else {
                    ibChange.setEnabled(false);
                }
                break;
        }
    }

    @SuppressLint("LongLogTag")
    private void fillprofile() {
        SharedPreferences preferences = activity.getSharedPreferences
                (Common.PREF_FILE, MODE_PRIVATE);
        int idCustomer = preferences.getInt("IdCustomer", 0);

        if (idCustomer == 0){
            Common.showToast(activity, R.string.msg_NoProfileFound);

        }

        if (Common.networkConnected(activity)) {
            String url = Common.URL + "/CustomerServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "findById");
            jsonObject.addProperty("IdCustomer", idCustomer);

            String jsonOut = jsonObject.toString();
            userFindTask = new CommonTask(url, jsonOut);
            Customer customer = null;
            try {
                String result = userFindTask.execute().get();
                Log.e(TAG, "result:" + result);
                customer = new Gson().fromJson(result, Customer.class);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (customer == null) {
                Common.showToast(activity, R.string.msg_NoProfileFound);

            } else {
                customer.setIdCustomer(idCustomer);
                txMyMemberNumber.setText(String.valueOf(customer.getIdCustomer()));
                customerImage(customer.getIdCustomer());
                txMyName.setText(customer.getName());
                txMemberEmail.setText(customer.getEmail());
                txPhoneNumber.setText(customer.getPhone());
            }
        }else {
            Common.showToast(activity, R.string.msg_NoNetwork);
        }



    }

    @SuppressLint("LongLogTag")
    private void customerImage(int IdCustomer) {
        SharedPreferences preferences = activity.getSharedPreferences
                (Common.PREF_FILE, MODE_PRIVATE);
        int idCustomer = preferences.getInt("IdCustomer", 0);
        String url = Common.URL + "/CustomerServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "getImage");
        jsonObject.addProperty("IdCustomer", idCustomer);
        Bitmap bitmap = null;

        try {
            bitmap = new ImageTask(url, IdCustomer).execute().get();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } else {
            imageView.setImageResource(R.drawable.man128);
        }
    }

}
