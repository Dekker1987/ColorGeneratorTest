package com.example.sergii.colorgeneratortest;

import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.sergii.colorgeneratortest.helpers.ColorGeneratorHelper;
import com.example.sergii.colorgeneratortest.models.ColorModel;
import com.example.sergii.colorgeneratortest.adapters.ColorAdapter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener,View.OnTouchListener{

    private int backPressCounter;
    private RecyclerView rv_color_list;
    private ColorAdapter colorAdapter;
    private List<ColorModel> colorModelList;
    private ColorGeneratorHelper colorGeneratorHelper;
    private Thread colorGeneratorThread;
    private ActionBar actionBar;
    private Button btn_start;
    private Button btn_continue;
    private Button btn_pause;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setFullScreen();
        setContentView(R.layout.activity_main);

        colorModelList = new ArrayList<>();
        rv_color_list = (RecyclerView)findViewById(R.id.rv_color_list);
        rv_color_list.setHasFixedSize(true);
        rv_color_list.setLayoutManager(new LinearLayoutManager(this));
        colorAdapter = new ColorAdapter(colorModelList,getApplicationContext());
        rv_color_list.setAdapter(colorAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(rv_color_list);

        btn_start = (Button)findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        btn_continue = (Button)findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        btn_continue.setVisibility(View.INVISIBLE);

        btn_pause = (Button)findViewById(R.id.btn_pause);
        btn_pause.setOnClickListener(this);
        btn_pause.setVisibility(View.INVISIBLE);

        colorGeneratorHelper = new ColorGeneratorHelper();
        colorGeneratorHelper.addHandler(new Handler(){
            public void handleMessage(android.os.Message msg) {
                addColorValue((ColorModel)msg.obj);
                rv_color_list.smoothScrollToPosition(colorAdapter.getItemCount());
            }
        });
        colorGeneratorThread = new Thread(colorGeneratorHelper);
    }

    private void setFullScreen(){
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideActionBar();
    }

    private void hideActionBar(){
        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        ItemTouchHelper.SimpleCallback simpleItemCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemCallback;
    }

    private void deleteItem(int position){
        colorModelList.remove(position);
        colorAdapter.setColorList(colorModelList);
        colorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_start:
                if(btn_start.getText().toString().equals("Start")){
                    btn_start.setText("Stop");
                    btn_continue.setVisibility(View.VISIBLE);
                    btn_pause.setVisibility(View.VISIBLE);

                    colorGeneratorHelper = new ColorGeneratorHelper();
                    colorGeneratorHelper.addHandler(new Handler(){
                        public void handleMessage(android.os.Message msg) {
                            addColorValue((ColorModel)msg.obj);
                            rv_color_list.smoothScrollToPosition(colorAdapter.getItemCount());
                        }
                    });
                    colorGeneratorThread = new Thread(colorGeneratorHelper);
                    colorGeneratorThread.start();

                } else {
                    colorGeneratorHelper.stopGeneratingThread();
                    colorGeneratorHelper = null;
                    colorGeneratorThread = null;
                    colorModelList.clear();
                    colorAdapter.notifyDataSetChanged();
                    btn_continue.setVisibility(View.INVISIBLE);
                    btn_pause.setVisibility(View.INVISIBLE);
                    btn_start.setText("Start");
                }
                break;
            case R.id.btn_continue:
                resumeGeneratingColor();
                break;
            case R.id.btn_pause:
                pauseGeneratingColors();
                break;
            default:
                break;
        }
    }

    private void resumeGeneratingColor(){
        colorGeneratorHelper.resume();
    }

    private void pauseGeneratingColors(){
        colorGeneratorHelper.suspend();
    }

    private void addColorValue(ColorModel inColorModel){
        if(inColorModel!=null){
            colorModelList.add(inColorModel);
            colorAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed(){
        backPressCounter++;
        switch(backPressCounter){
            case 1:
                Toast.makeText(getApplicationContext(),"Press again to exit...",Toast.LENGTH_LONG).show();
                break;
            case 2:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                colorGeneratorHelper.decrementDelay();
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                 colorGeneratorHelper.incrementDelay();
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
