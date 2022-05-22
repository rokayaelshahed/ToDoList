package com.example.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.Adapter.ToDoAdapter;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;
    public RecyclerItemTouchHelper(ToDoAdapter adapter){
        super(0,ItemTouchHelper.RIGHT |ItemTouchHelper.LEFT);
        this.adapter=adapter;

    }
    @Override
    public boolean onMove( RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if(direction==ItemTouchHelper.LEFT){
            AlertDialog.Builder builder=new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete task");
            builder.setMessage("Are you sure you want to delete this Task?");
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        }else if(direction==ItemTouchHelper.RIGHT) {
            adapter.editItem(position);
        }

    }
    @Override
    public void onChildDraw(Canvas c ,RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder,float dx, float dy,int actionState,boolean isCurrentlyAvtive){
        super.onChildDraw(c , recyclerView, viewHolder, dx,  dy, actionState, isCurrentlyAvtive);
        Drawable icon ;
        ColorDrawable background;
        View itemView=viewHolder.itemView;
        int backgroundCornerOffset=20;
        if (dx>0){
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_edit);
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext(),R.color.dark_pink));
        }else {
            icon= ContextCompat.getDrawable(adapter.getContext(),R.drawable.ic_baseline_delete);
            background = new ColorDrawable(Color.RED);
        }
        int iconMargin=(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconTop = itemView.getTop()+(itemView.getHeight()-icon.getIntrinsicHeight())/2;
        int iconBottom = iconTop+icon.getIntrinsicHeight();

        if(dx>0){
            int iconLeft = itemView.getLeft()+iconMargin;
            int iconRight = itemView.getLeft()+iconMargin+icon.getIntrinsicWidth();
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int) dx)+backgroundCornerOffset,itemView.getBottom());
        }else if(dx<0){
            int iconLeft = itemView.getRight()-iconMargin-icon.getIntrinsicWidth();
            int iconRight = itemView.getRight()-iconMargin;
            icon.setBounds(iconLeft,iconTop,iconRight,iconBottom);

            background.setBounds(itemView.getRight()+((int) dx)-backgroundCornerOffset,itemView.getTop(),itemView.getRight(),itemView.getBottom());
        }else {
            background.setBounds(0,0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }
}