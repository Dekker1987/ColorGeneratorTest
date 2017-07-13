package com.example.sergii.colorgeneratortest.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.sergii.colorgeneratortest.R;
import com.example.sergii.colorgeneratortest.models.ColorModel;
import java.util.List;

/**
 * Created by Sergii on 11.07.2017.
 */

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ColorHolder> {

    private Context context;
    private List<ColorModel> colorList;

    public ColorAdapter(List<ColorModel> colorList,Context context){
        this.context = context;
        this.colorList = colorList;
    }

    @Override
    public ColorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.color_list_item,parent,false);
        return new ColorHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ColorHolder holder, int position) {
        ColorModel colorModel = colorList.get(position);
        holder.tv_color_code.setText(colorModel.getHexColorValue().toUpperCase());
        holder.card_view.setCardBackgroundColor(Color.parseColor(colorModel.getHexColorValue()));
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public void setColorList(List<ColorModel> colorList){
        this.colorList = colorList;
    }

    class ColorHolder extends RecyclerView.ViewHolder{

        private CardView card_view;
        private TextView tv_color_code;

        public ColorHolder(View itemView) {
            super(itemView);
            tv_color_code = (TextView)itemView.findViewById(R.id.tv_color_code);
            card_view = (CardView)itemView.findViewById(R.id.card_view);
        }
    }
}
