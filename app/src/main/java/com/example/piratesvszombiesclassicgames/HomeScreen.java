package com.example.piratesvszombiesclassicgames;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeScreen extends AppCompatActivity{

    ImageView play;
    ImageView settings;
    ImageView store;
    ImageView exit;
    FirebaseAuth firebaseAuth;
    Dialog d;
    EditText email;
    EditText pass;
    TextView ok;
    TextView newUser;
    ProgressDialog progressDialog;
    Typeface custom_font;
    EditText useret;
    EditText passet;
    EditText passcet;
    EditText emailet;
    TextView bdet;
    TextView oldUser;
    boolean isBDRight = false;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference userReference;
    DatabaseReference dref;
    ArrayList<User> usersList;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/ARCENA.ttf");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");
        sp = getSharedPreferences("details",0);
        this.retriveData();

        getSupportActionBar().hide();
        Intent i3 = new Intent(this,BackgroundMusic.class);
        startService(i3);
        play = (ImageView) findViewById(R.id.imageView2);
        store = (ImageView) findViewById(R.id.imageView4);
        settings = (ImageView) findViewById(R.id.imageView5);
        exit = (ImageView) findViewById(R.id.imageView6);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setTitle("Exit");
                builder.setMessage("Do you want to exit?");
                builder.setPositiveButton("Yes. Exit now!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        System.exit(0);
                        pubFun.clickSound(getBaseContext());
                    }
                });
	            builder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                    dialogInterface.dismiss();
                    pubFun.clickSound(getBaseContext());
                    }
	            });

            AlertDialog dialog = builder.create();
            dialog.show();
            pubFun.clickSound(getBaseContext());
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),GameList.class);
                startActivityForResult(in,0);
                pubFun.clickSound(getBaseContext());
            }
        });
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(getBaseContext(),Store.class);
                startActivity(in);
                pubFun.clickSound(getBaseContext());
            }
        });
        final SettingsDialog sd = new SettingsDialog(this,"Back");
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sd.show();
                pubFun.clickSound(getBaseContext());
            }
        });
        pubFun.sound=sp.getBoolean("sound",true);
        if(firebaseAuth.getCurrentUser()==null){
            logInDialog();
        }
        else{
            retriveUser();
            Intent intent = getIntent();
            if(intent!=null) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    if (bundle.getBoolean("isStop")) {
                        Intent i = new Intent(getApplicationContext(), GameList.class);
                        i.putExtra("isStop", true);
                        startActivity(i);
                    }
                }
            }
        }
    }

    public void logIn(){
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String key = findKey(email.getText().toString());
                            User u = findUser(email.getText().toString());
                            if(key!=null) {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("key",key);
                                editor.commit();
                                pubFun.user = u;
                                retriveUser();
                                d.dismiss();
                            }
                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                            builder.setCancelable(true);
                            builder.setTitle("Error");
                            builder.setMessage("Email or Password is incorrect!");
                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            builder.show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void logInDialog(){
        d = new Dialog(this);
        d.setContentView(R.layout.activity_log_in);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        email = (EditText) d.findViewById(R.id.emailet);
        email.setTypeface(custom_font);
        pass = (EditText) d.findViewById(R.id.passet);
        pass.setTypeface(custom_font);
        ok = (TextView) d.findViewById(R.id.ok);
        ok.setTypeface(custom_font);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if(!email.getText().toString().equals("")&&!pass.getText().toString().equals(""))
                    logIn();
            }
        });
        newUser = (TextView) d.findViewById(R.id.newUser);
        newUser.setTypeface(custom_font);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                d.dismiss();
                registerDialog();
                pubFun.clickSound(getBaseContext());
            }
        });
        progressDialog = new ProgressDialog(this);
        d.show();
    }

    public void registerDialog(){
        d = new Dialog(this);
        d.setContentView(R.layout.activity_register);
        d.setCancelable(false);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        useret = (EditText) d.findViewById(R.id.useret);
        useret.setTypeface(custom_font);
        useret.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String str = userCheck();
                    if(str!="")
                        errorAlert(str);
                }
            }
        });
        passet = (EditText) d.findViewById(R.id.passet);
        passet.setTypeface(custom_font);
        passet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String str = passCheck();
                    if(str!="")
                        errorAlert(str);
                }
            }
        });
        passcet = (EditText) d.findViewById(R.id.passcet);
        passcet.setTypeface(custom_font);
        passcet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String str = passcCheck();
                    if(str!="")
                        errorAlert(str);
                }
            }
        });
        emailet = (EditText) d.findViewById(R.id.emailet);
        emailet.setTypeface(custom_font);
        emailet.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    String str = emailCheck();
                    if(str!="")
                        errorAlert(str);
                }
            }
        });
        ok = (TextView) d.findViewById(R.id.ok);
        ok.setTypeface(custom_font);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                if (check())
                    register();

            }
        });
        bdet = (TextView) d.findViewById(R.id.bdet);
        bdet.setTypeface(custom_font);
        bdet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                birthdayDialog();
            }
        });
        oldUser = (TextView) d.findViewById(R.id.oldUser);
        oldUser.setTypeface(custom_font);
        oldUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pubFun.clickSound(getBaseContext());
                d.dismiss();
                logInDialog();
            }
        });
        progressDialog = new ProgressDialog(this);
        d.show();
    }

    public boolean check(){
        String errors = "";
        errors+=userCheck();
        errors+=passCheck();
        errors+=passcCheck();
        errors+=emailCheck();
        errors+=bdCheck();

        if(errors=="")
            return true;
        else{
            errorAlert(errors);
            return false;
        }
    }

    public boolean isNumbers(String str){
        String numbers = "1234567890";
        for (int i = 0; i < str.length(); i++)
            for (int j = 0; j < numbers.length(); j++)
                if (str.charAt(i) == numbers.charAt(j))
                    return true;
        return false;
    }
    public boolean isEnglish(String str){
        String english = "abcdefghijklmnopqurstuvwxyzABCDEFGHIJKLMNOPQURSTUVWXYZ";
        for (int i = 0; i < str.length(); i++)
            for (int j = 0; j < english.length(); j++)
                if (str.charAt(i) == english.charAt(j))
                    return true;
        return false;
    }
    public boolean onlyNumEng(String str){
        for (int i = 0; i < str.length(); i++) {
            if(!(isNumbers(str.charAt(i)+"")||isEnglish(str.charAt(i)+"")))
                return false;
        }
        return true;
    }
    public boolean email(String str){
        if ((str.split("@").length == 2) &&
                (str.indexOf("@") != 0) &&
                (str.indexOf(".") != 0) &&
                (str.lastIndexOf(".") != str.length() - 1) &&
                (str.indexOf(".") != -1)&&
                ((str.split("\\.")[1] != "com")|| (str.split("\\.")[1] != "co")) &&
                (str.split("@")[1].indexOf(".") != 0) &&
                (str.length() > 7))
            return false;
        else
            return true;
    }

    public String userCheck(){
        String errors = "";
        if(useret.getText().toString().length()<4)
            errors+="Username must be at least four chars\n";
        if(!onlyNumEng(useret.getText().toString()))
            errors+="Write username only with english letters and numbers\n";
        if(!isEnglish(useret.getText().toString()))
            errors+="Write username with at least one english letter\n";
        if(!isNumbers(useret.getText().toString()))
            errors+="Write username with at least one number\n";
        if(isUsed(useret.getText().toString()))
            errors+="Username is already taken\n";
        return errors;
    }
    public String passCheck(){
        String errors="";
        if(passet.getText().toString().length()<6)
            errors+="Password must be at least six chars\n";
        if(!onlyNumEng(passet.getText().toString()))
            errors+="Write password only with english letters and numbers\n";
        if(!isEnglish(passet.getText().toString()))
            errors+="Write password with at least one english letter\n";
        if(!isNumbers(passet.getText().toString()))
            errors+="Write password with at least one number\n";
        return errors;
    }
    public String passcCheck(){
        String errors = "";
        if(!passet.getText().toString().equals(passcet.getText().toString()))
            errors+="Write the same password\n";
        return errors;
    }
    public String emailCheck(){
        String errors="";
        if(email(emailet.getText().toString())){
            errors+="Write correct email\n";
        }
        return errors;
    }
    public String bdCheck(){
        String errors = "";
        if(!isBDRight)
            errors+="Enter correct birthday date";
        return errors;
    }

    public void birthdayDialog(){
        Calendar systemCalender = Calendar.getInstance();
        int year = systemCalender.get(Calendar.YEAR);
        int month = systemCalender.get(Calendar.MONTH);
        int day = systemCalender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,new HomeScreen.SetDate(),year,month,day);
        datePickerDialog.show();
        pubFun.clickSound(getBaseContext());
    }
    public  class SetDate implements DatePickerDialog.OnDateSetListener
    {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear +1;

            String str = dayOfMonth + "/" + monthOfYear +"/" + year;
            bdet.setText(str);
            Calendar systemCalender = Calendar.getInstance();
            int yearr = systemCalender.get(Calendar.YEAR);
            int month = systemCalender.get(Calendar.MONTH);
            int day = systemCalender.get(Calendar.DAY_OF_MONTH);
            String now = ""+yearr+month+day;
            String then = ""+year+monthOfYear+dayOfMonth;
            isBDRight = Integer.parseInt(now)>Integer.parseInt(then);
            String strr = bdCheck();
            pubFun.clickSound(getBaseContext());
            if(strr!="")
                errorAlert(strr);
        }
    }

    public void errorAlert(String errors){
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
        builder.setCancelable(true);
        builder.setTitle("Please");
        builder.setMessage(errors);
        builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pubFun.clickSound(getBaseContext());
            }
        });
        builder.show();
    }

    public void register(){
        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailet.getText().toString(),passet.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(useret.getText().toString()).build();
                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                    String uid = firebaseAuth.getCurrentUser().getUid();
                    User u = new User("",uid,useret.getText().toString(),bdet.getText().toString(),emailet.getText().toString(),passet.getText().toString());
                    userReference = firebaseDatabase.getReference("Users").push();
                    u.key = userReference.getKey();
                    userReference.setValue(u);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("key",u.key);
                    editor.commit();
                    pubFun.user = u;
                    retriveUser();
                    d.dismiss();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                    builder.setCancelable(true);
                    builder.setTitle("Please");
                    builder.setMessage("Email is already taken");
                    builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.show();
                }
                progressDialog.dismiss();
            }
        });
    }

    public void retriveData(){
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren())
                {
                    User u = data.getValue(User.class);
                    usersList.add(u);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public String findKey(String email){
        for (int i = 0; i < usersList.size(); i++) {
            if(email.equals(usersList.get(i).email))
                return usersList.get(i).key;
        }
        return null;
    }
    public User findUser(String email){
        for (int i = 0; i < usersList.size(); i++) {
            if(email.equals(usersList.get(i).email))
                return usersList.get(i);
        }
        return null;
    }

    public boolean isUsed(String user){
        for (int i = 0; i < usersList.size(); i++) {
            if(user.equals(usersList.get(i).user.toString()))
                return true;
        }
        return false;
    }

    public void retriveUser(){
        String key = sp.getString("key",null);
        if(key !=null) {
            dref = firebaseDatabase.getReference("Users/" + key);
            pubFun.dref = dref;
            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pubFun.user = dataSnapshot.getValue(User.class);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            finish();
            startActivity(getIntent());
        }
    }
}
