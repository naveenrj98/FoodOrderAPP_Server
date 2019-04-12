package developers.bmsce.mank.com.foodorderserver;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import developers.bmsce.mank.com.foodorderserver.Interface.ItemClickListener;
import developers.bmsce.mank.com.foodorderserver.Models.Category;
import developers.bmsce.mank.com.foodorderserver.Models.Food;
import developers.bmsce.mank.com.foodorderserver.R;
import developers.bmsce.mank.com.foodorderserver.ViewHolder.FoodViewHolder;
import developers.bmsce.mank.com.foodorderserver.ViewHolder.MenuViewHolder;

public class FoodList extends AppCompatActivity {



    FirebaseDatabase db;
    DatabaseReference foodlist;
    FirebaseStorage storage;
    StorageReference storageReference;

    FloatingActionButton fab;


    RecyclerView recycler_view;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Food,FoodViewHolder> adpter;

    String categoryId="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);



        //Auth
        db = FirebaseDatabase.getInstance();
        foodlist = db.getReference("Foods");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        recycler_view = findViewById(R.id.recycler_food);
        recycler_view.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        
        fab = findViewById(R.id.fabfl);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");

        }
        if (!categoryId.isEmpty() && categoryId != null) {
            loadListFood(categoryId);
        }

    }

    private void loadListFood(String categoryId) {

        adpter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                R.layout.food_item,
                FoodViewHolder.class,
                foodlist.orderByChild("menuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {


                viewHolder.food_name.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.food_image);
                final Food local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Toast.makeText(FoodList.this,""+local.getName(),Toast.LENGTH_SHORT).show();

//                        Intent foodlist = new Intent(FoodList.this, FoodDetail.class);
//                        foodlist.putExtra("FoodId", adpter.getRef(position).getKey());
//                        startActivity(foodlist);


                    }
                });

            }
        };
adpter.notifyDataSetChanged();
        recycler_view.setAdapter(adpter);
    }
}
