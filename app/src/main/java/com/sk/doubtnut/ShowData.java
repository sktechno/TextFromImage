package com.sk.doubtnut;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Created by sk on 20/07/17.
 */

public class ShowData extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdata);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyleView);

        ArrayList<DataModel> list = DataBase.getInstance(this).getdata();
        Adapter adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);


    }


}
