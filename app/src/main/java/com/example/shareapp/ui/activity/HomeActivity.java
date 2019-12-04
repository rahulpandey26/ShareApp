package com.example.shareapp.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import com.example.shareapp.R;
import com.example.shareapp.ui.fragment.HomeFragment;
import com.example.shareapp.util.DialogUtil;
import com.example.shareapp.util.FragmentHelper;
import java.util.ArrayList;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HomeActivity extends AppCompatActivity {

    private ArrayList<String> mPermissionsToRequest;
    private ArrayList<String> mPermissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();
    private final static int ALL_PERMISSIONS_RESULT = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        int checkWriteExternalStorage = checkCallingOrSelfPermission(WRITE_EXTERNAL_STORAGE);
        int checkReadExternalStorage = checkCallingOrSelfPermission(READ_EXTERNAL_STORAGE);
        if (checkWriteExternalStorage == PackageManager.PERMISSION_GRANTED &&
                checkReadExternalStorage == PackageManager.PERMISSION_GRANTED) {
            loadHomeFragment();
        } else {
            setPermission();
        }
    }

    private void setPermission() {
        permissions.add(WRITE_EXTERNAL_STORAGE);
        permissions.add(READ_EXTERNAL_STORAGE);

        mPermissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (mPermissionsToRequest.size() > 0)
                requestPermissions(mPermissionsToRequest.toArray(new String[mPermissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
    }

    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList<String> result = new ArrayList<>();
        for (String perm : wanted) {
            if (hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED);
            }
        }
        return false;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == ALL_PERMISSIONS_RESULT) {
            for (String perms : mPermissionsToRequest) {
                if (hasPermission(perms)) {
                    mPermissionsRejected.add(perms);
                } else {
                   loadHomeFragment();
                }
            }

            if (mPermissionsRejected.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(mPermissionsRejected.get(0))) {
                        DialogUtil.showAlertMessage(this, getString(R.string.permission_manadatory_msg),
                                (dialog, which) -> requestPermissions(mPermissionsRejected.toArray(new
                                        String[mPermissionsRejected.size()]), ALL_PERMISSIONS_RESULT));
                    }
                }

            }
        }

    }

    private void loadHomeFragment() {
        FragmentHelper.replaceFragment(this, HomeFragment.newInstance(),
                R.id.fragment_container, "Home_fragment");
    }
}
