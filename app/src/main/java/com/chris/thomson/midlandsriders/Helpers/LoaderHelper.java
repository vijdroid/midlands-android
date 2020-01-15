package com.chris.thomson.midlandsriders.Helpers;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;

import com.chris.thomson.midlandsriders.R;

public class LoaderHelper {

    private Activity activity;
    private Dialog dialog;
    //..we need the context else we can not create the dialog so get context in constructor
    public LoaderHelper(Activity activity) {
        this.activity = activity;
    }

    public void showDialog() {

        dialog  = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //...set cancelable false so that it's never get hidden
        dialog.setCancelable(false);
        //...that's the layout i told you will inflate later
        dialog.setContentView(R.layout.fill_dialog);

        //...initialize the imageView form infalted layout
      /*  ImageView gifImageView = dialog.findViewById(R.id.custom_loading_imageView);
        gifImageView.setBackgroundColor(Color.TRANSPARENT);

        DrawableImageViewTarget imageViewTarget = new DrawableImageViewTarget(gifImageView);


        Glide.with(activity)
                .load(R.drawable.mr_loader2)
                .apply(new RequestOptions().placeholder(R.drawable.mr_loader2))
                .into(gifImageView);
            */
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
    }

    //..also create a method which will hide the dialog when some work is done
    public void hideDialog(){
        dialog.dismiss();
    }


    public void hideDialog(int sec){

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        hideDialog();
                    }
                },
                sec * 100);
    }

}