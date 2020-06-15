package com.example.otherworld.Interface;

import com.example.otherworld.Model.ShoppingItem;

import java.util.List;

public interface IShoppingDataLoadListener {
    void onShoppingLoadDataSuccess(List<ShoppingItem> shoppingItemList);
    void onShoppingLoadDataFailed(String message);

}
