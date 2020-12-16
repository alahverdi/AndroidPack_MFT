package ir.allahverdi.digiapplication.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.entity.Music;

public class MusicAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private ArrayList<Music> list;
    private ArrayList<Music> temp;

    public MusicAdapter(Context context, ArrayList<Music> list) {
        this.context = context;
        this.list = list;
        this.temp = list;
    }

    // BaseAdapter Methods :
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.music_item, null);
        }

        final Music music = list.get(position);

        TextView tv_songName = convertView.findViewById(R.id.tv_music_name);
        ImageView iv_songPic = convertView.findViewById(R.id.iv_music_item);

        tv_songName.setText(String.valueOf(music.getSongName()));
        iv_songPic.setImageResource(music.getMusicImage());

        // set items Pics :
        if (iv_songPic != null) {

            String[] src_PICs = {
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/shajarian.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/ebi.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/hichkas.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/namjoo.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/damahi.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/habib.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/bomrani.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/sirvan.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/homayoun.jpg",
                    "https://alialahverdi.ir/wp-content/uploads/2020/12/shamloo.jpg"};

            for (int i = 0; i < list.size(); i++) {
                switch (music.getId()) {
                    case 1:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[0]);
                        break;
                    case 2:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[1]);
                        break;
                    case 3:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[2]);
                        break;
                    case 4:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[3]);
                        break;
                    case 5:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[4]);
                        break;
                    case 6:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[5]);
                        break;
                    case 7:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[6]);
                        break;
                    case 8:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[7]);
                        break;
                    case 9:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[8]);
                        break;
                    case 10:
                        new BitmapWorkerTask(iv_songPic).execute(src_PICs[9]);
                        break;
                } //end Switch
            } //end for loop
        } //end if

        return convertView;

    } // end GetView()

    // Filterable Methods :
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence filterValue) {
                FilterResults filterResults = new FilterResults();

                ArrayList<Music> filterList = new ArrayList<>();
                for (Music music : temp) {
                    if (music.getSongName().contains(filterValue)) {
                        filterList.add(music);
                    }
                }

                filterResults.count = filterList.size();
                filterResults.values = filterList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list = (ArrayList<Music>) results.values;
                notifyDataSetChanged();
            }
        };
    }

} ////////////////////////////////// end Adapter List View Class //////////////////////////////////

// Load bitmap in AsyncTask
// ref: http://developer.android.com/training/displaying-bitmaps/process-bitmap.html

class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String imageUrl;

    public BitmapWorkerTask(ImageView imageView) {
        // Use a WeakReference to ensure the ImageView can be garbage
        // collected
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    // Decode image in background.
    @Override
    protected Bitmap doInBackground(String... params) {
        imageUrl = params[0];
        return LoadImage(imageUrl);
    }

    // Once complete, see if ImageView is still around and set bitmap.
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private Bitmap LoadImage(String URL) {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
        }
        return bitmap;
    }

    private InputStream OpenHttpConnection(String strURL)
            throws IOException {
        InputStream inputStream = null;
        URL url = new URL(strURL);
        URLConnection conn = url.openConnection();

        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");
            httpConn.connect();

            if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpConn.getInputStream();
            }
        } catch (Exception ex) {
        }
        return inputStream;
    }
} ////////////////////////////////// end Bitmap Worker Task Class //////////////////////////////////

