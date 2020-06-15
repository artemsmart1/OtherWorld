package com.example.otherworld.Database;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.util.Log;

import com.example.otherworld.Common.Common;
import com.example.otherworld.Interface.ICountItemInCartListener;

import java.sql.SQLClientInfoException;
import java.util.List;

public class DatabaseUtits {

    public static void getAllItemFromCart(CartDatabase db)
    {
        GetAllCartAsync task = new GetAllCartAsync (db);
        task.execute(Common.currentUser.getPhoneNumber());
    }

    public static void insertToCart(CartDatabase db,CartItem... cartItems)
    {
        InsertToCartAsync task = new InsertToCartAsync (db);
        task.execute(cartItems);
    }

    public static void countItemInCart(CartDatabase db, ICountItemInCartListener iCountItemInCartListener)
    {
        CountItemInCartAsync task = new CountItemInCartAsync (db,iCountItemInCartListener);
        task.execute();
    }

    ///////////////////ASYNC TASK DEFINE
    private static class GetAllCartAsync extends AsyncTask<String,Void,Void>{

        CartDatabase db;
        public GetAllCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(String... strings) {
            getAllItemFromCartByUserPhone(db,strings[0]);
            return null;
        }

        private void getAllItemFromCartByUserPhone(CartDatabase db, String userPhone) {
            List<CartItem> cartItems = db.cartDAO().getAllItemFromCart(userPhone);
            Log.d("COUNT_CART",""+cartItems.size());
        }
    }

    private static class InsertToCartAsync extends AsyncTask<CartItem,Void,Void>{

        CartDatabase db;
        public InsertToCartAsync(CartDatabase cartDatabase) {
            db = cartDatabase;
        }

        @Override
        protected Void doInBackground(CartItem... cartItems) {
            inserToCart(db,cartItems[0]);
            return null;
        }

        private void inserToCart(CartDatabase db, CartItem cartItem) {
            //если предмет уже в корзине ,увеличиваем число
            try{
                db.cartDAO().insert(cartItem);
            }catch (SQLiteConstraintException exeption)
            {
                CartItem updateCartItem = db.cartDAO().getProductInCart(cartItem.getProductId(),
                        Common.currentUser.getPhoneNumber());
                updateCartItem.setProductQuantity(updateCartItem.getProductQuantity()+1);
                db.cartDAO().update(updateCartItem);
            }
        }

    }

    private static class CountItemInCartAsync extends AsyncTask<Void,Void,Integer>{

        CartDatabase db;
        ICountItemInCartListener listener;
        public CountItemInCartAsync(CartDatabase cartDatabase,ICountItemInCartListener iCountItemInCartListener) {
            db = cartDatabase;
            listener = iCountItemInCartListener;
        }

        @Override
        protected Integer doInBackground(Void... voids) {

            return Integer.parseInt(String.valueOf(countItemInCartRun(db)));
        }

        protected void onPostExecute(Integer integer){
            super.onPostExecute(integer);

            listener.onCartItemCountSuccess(integer.intValue());
        }

        private int countItemInCartRun(CartDatabase db) {
            return db.cartDAO().countItemCart(Common.currentUser.getPhoneNumber());
        }
    }
}
