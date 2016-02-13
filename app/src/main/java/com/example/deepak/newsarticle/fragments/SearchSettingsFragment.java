package com.example.deepak.newsarticle.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v4.app.DialogFragment;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.deepak.newsarticle.R;
import com.example.deepak.newsarticle.models.FilterParameters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class SearchSettingsFragment extends DialogFragment implements  DatePickerDialog.OnDateSetListener, View.OnClickListener{

    Spinner sortOrder;

    Button saveSettings;

    String orderItems[];

    Calendar myCalendar = Calendar.getInstance();

    private EditText mEditText;

    public Context context;

    CheckBox arts, fs, sports;

    FilterParameters fp = new FilterParameters();

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDate();

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.currentDate:

                new DatePickerDialog(context, this,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.saveButton:
                saveSettings();
                break;

            /*case R.id.cbArts:
                saveSettings();
                break;

            case R.id.cbFandS:
                saveSettings();
                break;
            case R.id.cbSports:
                saveSettings();
                break;*/

        }
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(FilterParameters fp);
    }


    public SearchSettingsFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static SearchSettingsFragment newInstance() {
        SearchSettingsFragment frag = new SearchSettingsFragment();
        Bundle args = new Bundle();
        //args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog);

        View view = inflater.inflate(R.layout.search_settings, container);

        mEditText = (EditText) view.findViewById(R.id.currentDate);

        saveSettings = (Button) view.findViewById(R.id.saveButton);

        arts = (CheckBox) view.findViewById(R.id.cbArts);
        sports = (CheckBox) view.findViewById(R.id.cbSports);
        fs = (CheckBox) view.findViewById(R.id.cbFandS);

        // show soft keyboard
        //mEditText.requestFocus();
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sortOrder = (Spinner)view.findViewById(R.id.spinnerSortOrder);
        orderItems = new String[]{"oldest", "newest"};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
               R.array.orderItems, android.R.layout.simple_spinner_dropdown_item);

        //ArrayAdapter<String> pAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, orderItems);
        sortOrder.setAdapter(adapter);

        mEditText.setOnClickListener(this);
        saveSettings.setOnClickListener(this);
        arts.setOnClickListener(this);
        fs.setOnClickListener(this);
        sports.setOnClickListener(this);

        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle("Filter parameters");

        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        //getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    private void updateDate() {

        String requiredFormat = "yyyyMMdd";
        String displayFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf1 = new SimpleDateFormat(requiredFormat, Locale.US);
        SimpleDateFormat sdf2 = new SimpleDateFormat(displayFormat, Locale.US);
        String reqdDate = sdf1.format(myCalendar.getTime());
        fp.setBeginDate(reqdDate);
        String displayDate = sdf2.format(myCalendar.getTime());
        mEditText.setText(displayDate);
    }

    public void saveSettings() {

        EditNameDialogListener listener = (EditNameDialogListener) getActivity();

        HashMap<String,Boolean> newsDesk = new HashMap<>();
        newsDesk.put("arts",arts.isChecked());
        newsDesk.put("sports",sports.isChecked());
        newsDesk.put("fs",fs.isChecked());
        fp.setNewsDesk(newsDesk);
        fp.setSortOrder(sortOrder.getSelectedItem().toString());
        listener.onFinishEditDialog(fp);
        dismiss();
    }


    /*@Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            Log.i("info","done pressed");

            //Return input text to activity
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString());
            dismiss();
            return true;
        }
        return false;
    }*/
}
