package com.testyle.service.impl;

import com.testyle.dao.IStationDao;
import com.testyle.model.Station;
import com.testyle.service.IStationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("stationService")
public class StationServiceImpl implements IStationService {

    @Resource
    private IStationDao stationDao;
    public List<Station> selectAll() {
        return stationDao.selectAll();
    }
    public List<Station> selectChildren(long pStaID){
        return stationDao.selectChildren(pStaID);
    }

    public Station selectStaton(long staID) {
        return stationDao.selectStaton(staID);
    }

    public int insertStation(Station station) {
        return stationDao.insertStation(station);
    }

    public int deleteStation(long staID) {
        return stationDao.deleteStation(staID);
    }

    public int updateStation(Station station) {
        return stationDao.updateStation(station);
    }
}
