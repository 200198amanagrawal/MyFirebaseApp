package com.example.myfirebaseapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//this class will always be extedned with MyHolder as holder defined below at line 30.
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyHolder> implements Filterable {

    private Context mContext;
    private List<User> mDataList;//the data whoch we will show i.e. User POJO created
    private List<User> mDataListFull;

    public UserAdapter(Context mContext, List<User> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mDataListFull=new ArrayList<>(mDataList);
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new MyHolder(view);//this will takeup the view of the list of items and place over recyler view
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final User user=mDataList.get(position);//will take up the firebase data at that position
        holder.textView.setText(user.getName()+" "+user.getAge());
        holder.textView.setOnClickListener(new View.OnClickListener() {//from the adaptors this partcilar function we set and perform actions over the recvyler view items
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,DetailActivity.class);
                intent.putExtra("user_ID",user.getUid());
                mContext.startActivity(intent);
            }
        });

        holder.textView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                String uid=user.getUid();
                DatabaseReference databaseReference= FirebaseDatabase.getInstance()
                        .getReference().child("users").child(uid);
                databaseReference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        //this class is an important class because it is always used to hold the recycler view each list item
        TextView textView;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.user_name);
        }
    }

    @Override
    public Filter getFilter() {//yhis is the function which is generated using the Filterable implements
        return userDatFilter;
    }
    private Filter userDatFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {//thid function is used to filter out the element and applies the filter logic
            List<User> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(mDataListFull);//mDataListFull is a backup arraylist which contains the following searched filter
            }else {

                String filter=constraint.toString().toLowerCase().trim();

                for(User dataItem:mDataListFull){
                    if(dataItem.getName().toLowerCase().contains(filter)){
                        filteredList.add(dataItem);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values = filteredList;

            return results;//this result will be called in the below method.
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mDataList.clear();
            mDataList.addAll((Collection<? extends User>) results.values);
            notifyDataSetChanged();
        }
    };
}
