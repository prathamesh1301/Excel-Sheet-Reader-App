package com.example.excelsheetreader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient client;
    Workbook wb;
    List<String> imageUrls;
    List<String> titles;
    List<String> desc;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String url="https://github.com/bikashthapa01/excel-reader-android-app/blob/master/story.xls?raw=true";

        recyclerView=findViewById(R.id.recyclerView);

        imageUrls=new ArrayList<>();
        titles=new ArrayList<>();
        desc=new ArrayList<>();
        client=new AsyncHttpClient();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        client.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Download Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "File downloaded", Toast.LENGTH_SHORT).show();
                WorkbookSettings ws=new WorkbookSettings();
                ws.setGCDisabled(true);
                if(file!=null){
                    try {
                        wb=wb.getWorkbook(file);
                        Sheet sheet=wb.getSheet(0);
                        for(int i=0;i<sheet.getRows();i++){
                            Cell[] row=sheet.getRow(i);
                            titles.add(row[0].getContents());
                            desc.add(row[1].getContents());
                            imageUrls.add(row[2].getContents());
                        }
                        updaterecyclerView();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }

                }

            }
        });


    }
    public void updaterecyclerView(){
        adapter=new Adapter(MainActivity.this,titles,desc,imageUrls);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}