package com.example.otherworld.Interface;

import java.util.List;

public interface IAllClubLoadListener {
    void onAllClubLoadSuccess(List<String> areaNameList);
    void onAllClubLoadFailed(String message);

}
