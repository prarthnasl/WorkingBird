package com.maps.psabharw.maps;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;


public class DetailActivity extends ActionBarActivity {
    //variable for selection intent
    private final int PICKER = 1;
    private PicAdapter imgAdapt;
    //variable to store the currently selected image
    private int currentPic = 0;
    //gallery object
    private Gallery picGallery;
    //image view for larger display
    private ImageView picView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        picGallery = (Gallery) findViewById(R.id.gallery);
        picView=(ImageView)findViewById(R.id.picture);
        imgAdapt = new PicAdapter(this);

//set the gallery adapter
        picGallery.setAdapter(imgAdapt);
        picGallery.setOnItemLongClickListener(new OnItemLongClickListener() {
            //handle long clicks
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                //take user to choose an image
                currentPic = position;

//take the user to their chosen image selection app (gallery or file manager)
                Intent pickIntent = new Intent();
                pickIntent.setType("image/*");
                pickIntent.setAction(Intent.ACTION_GET_CONTENT);
//we will handle the returned data in onActivityResult
                startActivityForResult(Intent.createChooser(pickIntent, "Select Picture"), PICKER);

                return true;

            }
        });
        picGallery.setOnItemClickListener(new OnItemClickListener() {
            //handle clicks
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //set the larger image view to display the chosen bitmap calling method of adapter class
                picView.setImageBitmap(imgAdapt.getPic(position));
            }
        });
    }

    public class PicAdapter extends BaseAdapter {

        //use the default gallery background image
        int defaultItemBackground;

        //gallery context
        private Context galleryContext;

        //array to store bitmaps to display
        private Bitmap[] imageBitmaps;

        //placeholder bitmap for empty spaces in gallery
        Bitmap placeholder;

        public PicAdapter(Context c) {

            //instantiate context
            galleryContext = c;

            //create bitmap array
            imageBitmaps = new Bitmap[10];

            //decode the placeholder image
            placeholder = BitmapFactory.decodeResource(getResources(), R.mipmap.img);

            //more processing
            for (int i = 0; i < imageBitmaps.length; i++)
                imageBitmaps[i] = placeholder;

            //get the styling attributes - use default Andorid system resources
            TypedArray styleAttrs = galleryContext.obtainStyledAttributes(R.styleable.PicGallery);

//get the background resource
            defaultItemBackground = styleAttrs.getResourceId(
                    R.styleable.PicGallery_android_galleryItemBackground, 0);

//recycle attributes
            styleAttrs.recycle();


        }

        public int getCount() {
            return imageBitmaps.length;
        }

        //return item at specified position
        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }


        public View getView(int position, View convertView, ViewGroup parent) {

            //create the view
            ImageView imageView = new ImageView(galleryContext);
            //specify the bitmap at this position in the array
            imageView.setImageBitmap(imageBitmaps[position]);
            //set layout options
            imageView.setLayoutParams(new Gallery.LayoutParams(300, 200));
            //scale type within view area
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            //set default gallery item background
            imageView.setBackgroundResource(defaultItemBackground);
            //return the view
            return imageView;
        }
        public Bitmap getPic(int posn)
        {
            //return bitmap at posn index
            return imageBitmaps[posn];
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            //check if we are returning from picture selection
            if (requestCode == PICKER) {
                //import the image
                Uri pickedUri = data.getData();
                //declare the bitmap
                Bitmap pic = null;
                picView.setImageBitmap(pic);
//scale options
                picView.setScaleType(ImageView.ScaleType.FIT_CENTER);

//declare the path string
                String imgPath = "";
                //retrieve the string using media data
                String[] medData = {MediaStore.Images.Media.DATA};
//query the data
                Cursor picCursor = managedQuery(pickedUri, medData, null, null, null);
                if (picCursor != null) {
                    //get the path string
                    int index = picCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    picCursor.moveToFirst();
                    imgPath = picCursor.getString(index);
                } else
                    imgPath = pickedUri.getPath();


            }
        }
//superclass method
        super.onActivityResult(requestCode, resultCode, data);




    }
}//retrieve the string using media data

