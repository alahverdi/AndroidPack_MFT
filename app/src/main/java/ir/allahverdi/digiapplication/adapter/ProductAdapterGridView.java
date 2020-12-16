package ir.allahverdi.digiapplication.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

import ir.allahverdi.digiapplication.FaNum;
import ir.allahverdi.digiapplication.R;
import ir.allahverdi.digiapplication.entity.Product;

public class ProductAdapterGridView extends BaseAdapter implements Filterable {

    private final String sharedPref = "ir.allahverdi.digiapplication_preferences";

    private Context context;
    private ArrayList<Product> list;
    private ArrayList<Product> temp;

    public ProductAdapterGridView(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
        this.temp = list;
    }

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
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gridview_items_layout, null);
        }

        Product product = this.list.get(position);

        ImageView imageView = view.findViewById(R.id.iv_item);
        TextView tv_id = view.findViewById(R.id.tv_id);
        TextView tv_name = view.findViewById(R.id.tv_name);
        TextView tv_price = view.findViewById(R.id.tv_price);
        TextView tv_model = view.findViewById(R.id.tv_model);

        tv_id.setText(FaNum.convert(String.valueOf(product.getId())));
        tv_name.setText(product.getName());

        //FaNum.convert() and separator :
        DecimalFormat decimalFormat = new DecimalFormat("0,000");
        String price_separator = decimalFormat.format(product.getPrice());
        tv_price.setText(String.valueOf(FaNum.convert(price_separator)));

        tv_model.setText(product.getModel());
        // use picasso library:
        //Picasso.get().load(product.getImgId()).into(imageView);
        Picasso.get()
                .load(product.getImgId())
                .placeholder(R.drawable.user_placeholder)
                .error(R.drawable.user_placeholder_error)
                .into(imageView);


        // set tv_name COLOR from shared Preference :
        SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPref, context.MODE_PRIVATE);
        if (sharedPreferences.contains("switch_preference_tittle_color")) {
            boolean name_color = sharedPreferences.getBoolean("switch_preference_tittle_color", false);
            if (name_color) {
                tv_name.setTextColor(context.getResources().getColor(R.color.teal_200));
            } else if (!name_color) {
                tv_name.setTextColor(context.getResources().getColor(R.color.normal_text_color));
            }
        }

        // set tv_name FONT SIZE from shared Preference :
        if (sharedPreferences.contains("switch_preference_tittle_size")) {
            boolean name_color = sharedPreferences.getBoolean("switch_preference_tittle_size", false);
            if (name_color) {
                tv_name.setTextSize(16);
            } else if (!name_color) {
                tv_name.setTextSize(11);
            }
        }

        // set tv_name FONT TYPE from shared Preference :
        if (sharedPreferences.contains("switch_preference_tittle_font")) {
            boolean name_color = sharedPreferences.getBoolean("switch_preference_tittle_font", false);
            if (name_color) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "myfonts/nastaliq_font.ttf");
                tv_name.setTypeface(typeface);
            } else if (!name_color) {
                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "myfonts/digikala_font.ttf");
                tv_name.setTypeface(typeface);
            }
        }

        // set item background from shared Preference :
        if (sharedPreferences.contains("switch_preference_tittle_bgColor")) {
            boolean name_color = sharedPreferences.getBoolean("switch_preference_tittle_bgColor", false);
            if (name_color) {
                view.setBackgroundResource(R.color.dark_item);
            } else if (!name_color) {
                view.setBackgroundResource(R.color.white);
            }
        }

        return view;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults filterResults = new FilterResults();
                ArrayList<Product> filterList = new ArrayList<>();
                for (Product item : temp) {
                    if (item.getName().contains(constraint)) {
                        filterList.add(item);
                    }
                }

                filterResults.count = filterList.size();
                filterResults.values = filterList;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                list = (ArrayList<Product>) results.values;
                notifyDataSetChanged();
            }
        };

    } // end get Filter()
} // end Adapter
