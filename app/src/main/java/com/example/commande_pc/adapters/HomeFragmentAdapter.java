package com.example.commande_pc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.commande_pc.R;
import com.example.commande_pc.ui.home.MenuByMe;

import java.util.List;

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.ViewHolder> {
    private List<MenuByMe> menuList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private FragmentManager fragmentManager;
    public HomeFragmentAdapter(List<MenuByMe> menuList,Context context,OnItemClickListener onItemClickListener,FragmentManager fragmentManager){
        this.menuList=menuList;
        this.context = context;
        this.onItemClickListener=onItemClickListener;
        this.fragmentManager= fragmentManager;

    }
    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listes_requesters_preview,parent,false);
        return new ViewHolder(view,onItemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuByMe menuByMe = menuList.get(position);
        holder.imageButton.setImageResource(menuByMe.getType());
        holder.textView.setText(menuByMe.getText());

    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
    public void clearItems(){
        menuList.clear();
    }
   public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageButton imageButton;
        TextView textView;

       public ViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
           super(itemView);
           imageButton = itemView.findViewById(R.id.image_button_8);
           textView = itemView.findViewById(R.id.text_9);
           imageButton.setOnClickListener(v -> {
               if(onItemClickListener!=null){
                   int position= getAdapterPosition();
                   if(position!=RecyclerView.NO_POSITION){
                       onItemClickListener.onItemClick(position);
                   }
               }

           });
       }
   }
}
