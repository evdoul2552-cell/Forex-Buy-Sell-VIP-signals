package com.aurum.trader;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import com.aurum.trader.helpers.Constants;
import com.aurum.trader.interfaces.TimeZoneListCallback;
import com.aurum.trader.models.SignalModel;
import com.aurum.trader.models.TimeZoneData;
import com.aurum.trader.network.TimeZoneCallingApi;

public final class MainViewModel extends ViewModel {
    private final String TAG = "MainViewModel";
    private MutableLiveData<ArrayList<SignalModel>> dataListLiveData = new MutableLiveData<>();
    private final DatabaseReference database;
    private long intersLimit;
    private ArrayList<SignalModel> newList = new ArrayList<>();
    private MutableLiveData<Boolean> progessLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> swipeRefreshLayoutLiveData = new MutableLiveData<>();
    private MutableLiveData<TimeZoneData> timeZoneLiveData = new MutableLiveData<>();

    public MainViewModel() {
        DatabaseReference reference = FirebaseDatabase.getInstance(Constants.db_instance).getReference();
        this.database = reference;
    }

    public final MutableLiveData<Boolean> getProgessLiveData() {
        return this.progessLiveData;
    }

    public final void setProgessLiveData(MutableLiveData<Boolean> mutableLiveData) {
        this.progessLiveData = mutableLiveData;
    }

    public final MutableLiveData<Boolean> getSwipeRefreshLayoutLiveData() {
        return this.swipeRefreshLayoutLiveData;
    }

    public final void setSwipeRefreshLayoutLiveData(MutableLiveData<Boolean> mutableLiveData) {
        this.swipeRefreshLayoutLiveData = mutableLiveData;
    }


    public final MutableLiveData<ArrayList<SignalModel>> getDataListLiveData() {
        return this.dataListLiveData;
    }

    public final void setDataListLiveData(MutableLiveData<ArrayList<SignalModel>> mutableLiveData) {
        this.dataListLiveData = mutableLiveData;
    }


    public final MutableLiveData<TimeZoneData> getTimeZoneLiveData() {
        return this.timeZoneLiveData;
    }

    public final void setTimeZoneLiveData(MutableLiveData<TimeZoneData> mutableLiveData) {
        this.timeZoneLiveData = mutableLiveData;
    }

    public final ArrayList<SignalModel> getNewList() {
        return this.newList;
    }

    public final void setNewList(ArrayList<SignalModel> arrayList) {
        this.newList = arrayList;
    }

    public final String getTAG() {
        return this.TAG;
    }

    public final DatabaseReference getDatabase() {
        return this.database;
    }

    public final long getIntersLimit() {
        return this.intersLimit;
    }

    public final void setIntersLimit(long j) {
        this.intersLimit = j;
    }


    public final void getAllTimeZone(Context context) {
        Log.e("REOPEN", "getAllTimeZone: ");
        TimeZoneCallingApi.Companion.myTimeZones(context, new TimeZoneListCallback() {
            @Override
            public void myTimeZoneList(TimeZoneData timeZoneData) {
                getTimeZoneLiveData().setValue(timeZoneData);
            }
        });
    }

    public final void retrieveData(String str) {
        Log.e("REOPEN", "retrieveData: ");
        this.swipeRefreshLayoutLiveData.setValue(false);
        this.progessLiveData.setValue(true);
        this.newList.clear();
        this.database.child("signals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot next : dataSnapshot.getChildren()) {
//                        Log.e("NAME", next.toString());
                        SignalModel signalModel = (SignalModel) next.getValue(new GenericTypeIndicator<SignalModel>() {
                        });
                        if (signalModel == null) {
//                            Log.e("Model1122", "onDataChange: model is null");
                        } else if (Intrinsics.areEqual(str, (Object) "new_signal")) {
                            String lowerCase = signalModel.getOld_new().toLowerCase(Locale.ROOT);
                            if (Intrinsics.areEqual((Object) lowerCase, (Object) "new")) {
                                MainViewModel.this.getNewList().add(signalModel);
                                String tag = MainViewModel.this.getTAG();
//                                Log.d(tag, "onDataChange: new signal: " + signalModel.getOld_new());
                            }
                        } else if (Intrinsics.areEqual(str, (Object) "history")) {
                            String lowerCase2 = signalModel.getOld_new().toLowerCase(Locale.ROOT);
                            if (Intrinsics.areEqual((Object) lowerCase2, (Object) "old")) {
                                MainViewModel.this.getNewList().add(signalModel);
                                String tag2 = MainViewModel.this.getTAG();
//                                Log.d(tag2, "onDataChange: history: " + signalModel.getOld_new());
                            }
                        } else {
                            MainViewModel.this.getNewList().add(signalModel);
                            String tag3 = MainViewModel.this.getTAG();
//                            Log.d(tag3, "onDataChange: home: " + signalModel.getOld_new());
                        }
                    }
                    CollectionsKt.reverse(MainViewModel.this.getNewList());
                    MainViewModel.this.getDataListLiveData().setValue(MainViewModel.this.getNewList());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                MainViewModel.this.getProgessLiveData().setValue(false);
            }
        });
    }

}
