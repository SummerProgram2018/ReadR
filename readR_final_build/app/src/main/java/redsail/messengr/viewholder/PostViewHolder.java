package redsail.messengr.viewholder;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import redsail.messengr.R;
import redsail.messengr.models.Post;
import redsail.messengr.profile.ProfileActivity;

public class PostViewHolder extends RecyclerView.ViewHolder {

    public TextView titleView;
    public TextView authorView;
    public ImageView starView;
    public TextView numStarsView;
    public TextView bodyView;
    public ImageView imageView;
    public TextView addressView;

    public PostViewHolder(View itemView) {
        super(itemView);

        titleView = itemView.findViewById(R.id.post_title);
        authorView = itemView.findViewById(R.id.post_author);
        starView = itemView.findViewById(R.id.star);
        numStarsView = itemView.findViewById(R.id.post_num_stars);
        bodyView = itemView.findViewById(R.id.post_body);
        imageView = itemView.findViewById(R.id.post_image);
        addressView = itemView.findViewById(R.id.post_address);
    }

    public void bindToPost(Post post, View.OnClickListener starClickListener) {
        titleView.setText(post.title);
        authorView.setText(post.author);
        numStarsView.setText(String.valueOf(post.starCount));
        bodyView.setText(post.body);

        setImage(post.imageURL);
        setLocation(post.address);

        starView.setOnClickListener(starClickListener);
    }

    public void setImage(String url){
        if (imageView == null)return;

        Log.e("load image: ", url);
        Glide.with(imageView.getContext())
                .load(url)
                .override(600, 600)
                .fitCenter()
                .into(imageView);
    }

    public void setLocation(String address){
        if(address != null){
            addressView.setText(address);
        }
    }

    public static String local(String latitudeFinal,String longitudeFinal){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal;
    }
}
