package com.doudou.dispatch.trip.api.mappers;


import com.doudou.dispatch.trip.api.entities.LineInfo;

import java.util.List;

public interface LineInfoMapper {

    public List<LineInfo> selectLineInfo();

}
