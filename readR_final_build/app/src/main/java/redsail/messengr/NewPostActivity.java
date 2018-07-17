package redsail.messengr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import redsail.messengr.models.Post;
import redsail.messengr.models.User;


public class NewPostActivity extends BaseActivity {

    private static final String TAG = "NewPostActivity";
    private static final String REQUIRED = "Required";

    // [START declare_database_ref]
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    // [END declare_database_ref]

    private EditText mTitleField;
    private EditText mBodyField;
    private Switch mSwitch;
    private TextView mText;

    private ImageButton mSelectImage;
    private Uri mImageUri;

    private String address;
    private HashMap<String, String> latLng = new HashMap<>();

    private ProgressDialog mprogressbar;

    private FloatingActionButton mSubmitButton;

    private static final int GALLERY_REQUEST = 1;
    private static final int PLACE_PICKER_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        mprogressbar = new ProgressDialog(this);

        // [START initialize_database_ref]
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        // [END initialize_database_ref]

        mTitleField = findViewById(R.id.field_title);
        mBodyField = findViewById(R.id.field_body);
        mSubmitButton = findViewById(R.id.fab_submit_post);
        mSelectImage = (ImageButton) findViewById(R.id.imageButton2);
        mSwitch = (Switch) findViewById(R.id.includePlaces);
        mText = findViewById(R.id.placeTag);

        clickEvents();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitPost();
            }
        });

    }

    private void submitPost() {
        final String title = mTitleField.getText().toString();
        final String body = mBodyField.getText().toString();

        // Title is required
        if (TextUtils.isEmpty(title)) {
            mTitleField.setError(REQUIRED);
            return;
        }

        // Body is required
        if (TextUtils.isEmpty(body)) {
            mBodyField.setError(REQUIRED);
            return;
        }

        // Disable button so there are no multi-posts
        setEditingEnabled(false);
        mprogressbar.setMessage("Posting...");
        mprogressbar.show();

        // [START single_value_read]
        final String userId = getUid();

        StorageReference filepath = mStorageRef.child("Images").child(mImageUri.getLastPathSegment());
        filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                final Task downloadUrl = taskSnapshot
                        .getMetadata().getReference().getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>(){

                            @Override
                            public void onSuccess(final Uri uri) {
                                mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        // Get user value
                                        User user = dataSnapshot.getValue(User.class);

                                        // [START_EXCLUDE]
                                        if (user == null) {
                                            // User is null, error out
                                            Log.e(TAG, "User " + userId + " is unexpectedly null");
                                            Toast.makeText(NewPostActivity.this,
                                                    "Error: could not fetch user.",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            // Write new post
                                            writeNewPost(userId, user.username, title, body, uri.toString());
                                        }

                                        // Finish this Activity, back to the stream
                                        setEditingEnabled(true);
                                        finish();
                                        // [END_EXCLUDE]
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                                        // [START_EXCLUDE]
                                        setEditingEnabled(true);
                                        // [END_EXCLUDE]
                                    }
                                });
                            }

                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                            }
                        });

                mprogressbar.dismiss();
                Toast.makeText(NewPostActivity.this, "Posted Successfully!!!!!",
                        Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mprogressbar.dismiss();
                Toast.makeText(NewPostActivity.this, "Unable to post Please TRY AGAIN!!!!",
                        Toast.LENGTH_LONG).show();
            }
        });
        // [END single_value_read]
    }

    private void setEditingEnabled(boolean enabled) {
        mTitleField.setEnabled(enabled);
        mBodyField.setEnabled(enabled);
        if (enabled) {
            mSubmitButton.setVisibility(View.VISIBLE);
        } else {
            mSubmitButton.setVisibility(View.GONE);
        }
    }

    // [START write_fan_out]
    private void writeNewPost(String userId, String username, String title, String body, String image) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("posts").push().getKey();
        Post post = new Post(userId, username, title, body, image, latLng, address);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);
    }
    // [END write_fan_out]

    private void clickEvents() {
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    locationPlacesIntent();
                }else{
                    mText.setVisibility(View.INVISIBLE);
                    mText.setText("");
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK) {
            mImageUri = data.getData();
            mSelectImage.setImageURI(mImageUri);
        }

        else if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(data, this);
            String stringPlace = String.format("Place: %s", place.getAddress());

            address = place.getAddress().toString();
            latLng.put("lat", Double.toString(place.getLatLng().latitude));
            latLng.put("lng", Double.toString(place.getLatLng().longitude));

            mText.setVisibility(View.VISIBLE);
            mText.setText(stringPlace);
        }
    }

    private void locationPlacesIntent(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

}