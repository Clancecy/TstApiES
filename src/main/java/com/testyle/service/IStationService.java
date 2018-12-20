package com.testyle.service;

import com.testyle.model.Station;

import java.util.List;

public interface IStationService {
    List<Station> selectAll();
    List<Station> selectChildren(long pStaID);
    Station selectStaton(long staID);
    int insertStation(Station station);
    int deleteStation(long staID);
    int updateStation(Station station);
}
