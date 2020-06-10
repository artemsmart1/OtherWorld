package com.example.otherworld.Interface;

import com.example.otherworld.Model.Club;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Club> areaNameList);
    void onBranchLoadFailed(String message);
}
