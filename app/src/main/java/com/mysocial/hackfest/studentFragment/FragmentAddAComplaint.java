package com.mysocial.hackfest.studentFragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appitup.R;
import com.example.appitup.classes.Data;
import com.example.appitup.classes.StudentsInfo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

public class FragmentAddAComplaint extends Fragment implements View.OnClickListener {

    private Context context;
    private Uri photoUri;
    private ImageView imageView;
    private EditText title, message;
    private TextView send , discard;
    private DatabaseReference databaseReference, studentDatabase;
    private StorageReference storageReference;
    private StorageReference photoRef;
    private String Email;
    private Spinner Dropdown;
    private ChildEventListener childEventListener;
    private Data complaintData = new Data();
    private boolean isDone = false;

    public FragmentAddAComplaint() {
        // Required empty public constructor
    }

    public FragmentAddAComplaint(Context context) {
        this.context = context;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Complaints");
        studentDatabase = FirebaseDatabase.getInstance().getReference().child("Student Info");
        storageReference = FirebaseStorage.getInstance().getReference().child("Complaint_Photos");
        Email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                StudentsInfo studentsInfo = snapshot.getValue(StudentsInfo.class);
                if (studentsInfo.getEmail().equalsIgnoreCase(Email)) {

                    complaintData.setStudentName(studentsInfo.getName());
                    complaintData.setStudentProfilePicUrl(studentsInfo.getProfilePicUri());
                    complaintData.setRollNo(studentsInfo.getAdmNo());
                    complaintData.setEmail(studentsInfo.getEmail());
                    isDone = true;
                }
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        };

        studentDatabase.addChildEventListener(childEventListener);
        AlertDialog.Builder b = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.turndown_layout, null);
        b.setView(dialogView);

        b.setTitle("Alert !");
        b.setMessage("While posting Anonymously, be respectful.\nPosting improper content can lead to SERIOUS PUNISHMENT");
        b.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //manage the intent from here
                dialog.dismiss();
            }
        });
        b.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_a_complaint, container, false);

        Dropdown = view.findViewById(R.id.spinner1);
        imageView = view.findViewById(R.id.image_view);
        title = view.findViewById(R.id.title);
        message = view.findViewById(R.id.message);
        send = view.findViewById(R.id.buttonSend);
        discard = view.findViewById(R.id.buttonDiscard);

        String[] items = new String[]{"Academic", "Administrative", "Hostel"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, items);
        Dropdown.setAdapter(adapter);
        send.setOnClickListener(this::onClick);
        discard.setOnClickListener(this::onClick);
        imageView.setOnClickListener(this::onClick);
        Dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Academic"))
                {
                    complaintData.setCategory("academic");
                }

                if(selectedItem.equals("Administrative"))
                {
                    complaintData.setCategory("administrative");
                }

                if(selectedItem.equals("Hostel"))
                {
                    complaintData.setCategory("hostel");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            photoUri = data.getData();
            imageView.setImageURI(photoUri);
            photoRef = storageReference.child(photoUri.getLastPathSegment());
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.image_view:

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                break;

            case R.id.buttonSend:

                // alert2 dialog declared
                AlertDialog.Builder alert2 = new AlertDialog.Builder(context)
                        .setMessage("Select whether you wish to send complaint by your name or Anonymously")

                        // positive onclick listener for alert dialog 2
                        .setPositiveButton("My Name", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (photoUri != null) {

                                    if (isDone) {
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Please Wait");
                                        progressDialog.show();
                                        complaintData.setComplaintTitle(title.getText().toString());
                                        complaintData.setComplaintMessage(message.getText().toString());

                                        photoRef.putFile(photoUri).addOnSuccessListener((Activity) context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        complaintData.setComplaintPicUrl(uri.toString());
                                                        databaseReference.push().setValue(complaintData);
                                                        Toast.makeText(context, "Data Send successfully", Toast.LENGTH_SHORT).show();
                                                        progressDialog.cancel();
                                                        Toast.makeText(context, "Complaint sent successfully", Toast.LENGTH_SHORT).show();
                                                        title.setText("");
                                                        message.setText("");
                                                        imageView.setImageResource(R.drawable.sample_pic);
                                                    }
                                                });

                                            }
                                        })
                                                .addOnFailureListener((Activity) context, new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener((Activity) context, new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(context, "Wait a moment and then again click send", Toast.LENGTH_LONG).show();
                                    }
                                }

                                if (photoUri == null) {
                                    if (isDone) {
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Please Wait");
                                        progressDialog.show();
                                        complaintData.setComplaintTitle(title.getText().toString());
                                        complaintData.setComplaintMessage(message.getText().toString());
                                        complaintData.setComplaintPicUrl("");
                                        databaseReference.push().setValue(complaintData);
                                        progressDialog.cancel();
                                        title.setText("");
                                        message.setText("");
                                        imageView.setImageResource(R.drawable.sample_pic);
                                    } else {
                                        Toast.makeText(context, "Wait a moment and then again click send", Toast.LENGTH_LONG).show();
                                    }
                                }


                            }
                        })

                        // negative onclick listener for alert dialog 2
                        .setNegativeButton("Anonymous", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                complaintData.setStudentName("Anonymous");
                                complaintData.setStudentProfilePicUrl("");
                                complaintData.setRollNo("");

                                if (photoUri != null) {

                                    if (isDone) {
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Please Wait");
                                        progressDialog.show();
                                        complaintData.setComplaintTitle(title.getText().toString());
                                        complaintData.setComplaintMessage(message.getText().toString());

                                        photoRef.putFile(photoUri).addOnSuccessListener((Activity) context, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        complaintData.setComplaintPicUrl(uri.toString());
                                                        databaseReference.push().setValue(complaintData);
                                                        Toast.makeText(context, "Data Send successfully", Toast.LENGTH_SHORT).show();
                                                        progressDialog.cancel();
                                                        title.setText("");
                                                        message.setText("");
                                                        imageView.setImageResource(R.drawable.sample_pic);
                                                    }
                                                });

                                            }
                                        })
                                                .addOnFailureListener((Activity) context, new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                                    }
                                                })
                                                .addOnFailureListener((Activity) context, new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull @NotNull Exception e) {
                                                        Toast.makeText(context, (CharSequence) e, Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                    } else {
                                        Toast.makeText(context, "Wait a moment and then again click send", Toast.LENGTH_LONG).show();
                                    }
                                }

                                if (photoUri == null) {
                                    if (isDone) {
                                        ProgressDialog progressDialog = new ProgressDialog(context);
                                        progressDialog.setMessage("Please Wait");
                                        progressDialog.show();
                                        complaintData.setComplaintTitle(title.getText().toString());
                                        complaintData.setComplaintMessage(message.getText().toString());
                                        complaintData.setComplaintPicUrl("");
                                        databaseReference.push().setValue(complaintData);
                                        progressDialog.cancel();
                                        title.setText("");
                                        message.setText("");
                                        imageView.setImageResource(R.drawable.sample_pic);
                                    } else {
                                        Toast.makeText(context, "Wait a moment and then again click send", Toast.LENGTH_LONG).show();
                                    }
                                }


                            }
                        });

                //alert dialog 1 declared

                AlertDialog.Builder alert1 = new AlertDialog.Builder(context)
                        .setMessage("Select whether you wish to send complaint publicly or privately")

                        //positive onclick listener for alert dialog 1
                        .setPositiveButton("Public", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                complaintData.setPublicPrivate(true);
                                alert2.show();
                            }
                        })

                        // negative onclick listener for alert dialog 2
                        .setNegativeButton("Private", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                complaintData.setPublicPrivate(false);
                                alert2.show();
                            }
                        });

                alert1.show();

                break;

            case R.id.buttonDiscard:


                title.setText("");
                message.setText("");
                imageView.setImageResource(R.drawable.sample_pic);
                break;
        }
    }
}