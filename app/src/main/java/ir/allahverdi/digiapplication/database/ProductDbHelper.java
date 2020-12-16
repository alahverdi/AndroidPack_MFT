package ir.allahverdi.digiapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import ir.allahverdi.digiapplication.Const;
import ir.allahverdi.digiapplication.entity.Product;

public class ProductDbHelper extends DbHelper {

    private final String TABLE_NAME = Product.class.getSimpleName();
    private final String FIELD_ID = "id";
    private final String FIELD_NAME = "name";
    private final String FIELD_PRICE = "price";
    private final String FIELD_IMG_ID = "imgId";
    private final String FIELD_MODEL = "model";
    private final String FIELD_SCORE = "score";

    public ProductDbHelper(@Nullable Context context) {
        super(context);
    }

    public long insert(Product product) {
        Log.e(Const.TAG_DB, "Insert()");

        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;

        try {
            ContentValues cv = new ContentValues();
            //cv.put(FIELD_ID , product.getId());
            cv.put(FIELD_NAME, product.getName());
            cv.put(FIELD_PRICE, product.getPrice());
            cv.put(FIELD_IMG_ID, product.getImgId());
            cv.put(FIELD_MODEL, product.getModel());
            cv.put(FIELD_SCORE , product.getScore());

            result = db.insert(TABLE_NAME, null, cv);

        } catch (Exception ex) {

        } finally {
            db.close();
        }

        return result;
    }

    public List<Product> select() {
        List<Product> productList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = {FIELD_ID, FIELD_NAME, FIELD_PRICE, FIELD_IMG_ID, FIELD_MODEL , FIELD_SCORE};
        Cursor cursor = db.query(TABLE_NAME, col, null, null, null, null, null);

        if (cursor.getCount() == 0) {
            db.close();
            return productList;
        }

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
            String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(FIELD_PRICE));
            String imgId = cursor.getString(cursor.getColumnIndex(FIELD_IMG_ID));
            String model = cursor.getString(cursor.getColumnIndex(FIELD_MODEL));
            float score = cursor.getFloat(cursor.getColumnIndex(FIELD_SCORE));

            Product product = new Product(id, name, price, imgId, model, score);
            productList.add(product);
        }

        cursor.close();
        db.close();
        return productList;
    }

    public List<Product> select(String param) {
        List<Product> productList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = {FIELD_ID, FIELD_NAME, FIELD_PRICE, FIELD_IMG_ID, FIELD_MODEL, FIELD_SCORE};
        Cursor cursor = db.query(TABLE_NAME, col, "name like ?", new String[]{"%" + param + "%"}, null, null, null);

        if (cursor.getCount() == 0)
            return productList;

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
            String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(FIELD_PRICE));
            String imgId = cursor.getString(cursor.getColumnIndex(FIELD_IMG_ID));
            String model = cursor.getString(cursor.getColumnIndex(FIELD_MODEL));
            float score = cursor.getFloat(cursor.getColumnIndex(FIELD_SCORE));

            Product product = new Product(id, name, price, imgId, model, score);
            productList.add(product);
        }

        cursor.close();
        return productList;
    }

    public Product select(int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] col = {FIELD_ID, FIELD_NAME, FIELD_PRICE, FIELD_IMG_ID, FIELD_MODEL, FIELD_SCORE};
        Cursor cursor = db.query(TABLE_NAME, col, "id=?", new String[]{String.valueOf(productId)}, null, null, null);

        if (cursor.getCount() == 0)
            return null;

        while (cursor.moveToNext()) {

            int id = cursor.getInt(cursor.getColumnIndex(FIELD_ID));
            String name = cursor.getString(cursor.getColumnIndex(FIELD_NAME));
            int price = cursor.getInt(cursor.getColumnIndex(FIELD_PRICE));
            String imgId = cursor.getString(cursor.getColumnIndex(FIELD_IMG_ID));
            String model = cursor.getString(cursor.getColumnIndex(FIELD_MODEL));
            int score = cursor.getInt(cursor.getColumnIndex(FIELD_SCORE));

            Product product = new Product(id, name, price, imgId, model, score);

            cursor.close();
            return product;
        }
        return null;
    }

    public int update(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
//        cv.put(FIELD_NAME, product.getName());
//        cv.put(FIELD_PRICE, product.getPrice());
        cv.put(FIELD_SCORE, product.getScore());
        return db.update(TABLE_NAME, cv, "id=?", new String[]{String.valueOf(product.getId())});
    }

    public int delete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        return db.delete(TABLE_NAME, "id=?", new String[]{String.valueOf(id)});
    }
}
