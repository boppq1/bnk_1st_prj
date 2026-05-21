package com.example.demo.admin.dao;

import com.example.demo.admin.dto.AdminEventDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IAdminEventDao {

    int insertEvent(AdminEventDto dto);
    AdminEventDto selectAll(Long event_no);
    int increaseEvent(Long event_no);
    int deleteEvent(Long event_no);
    int updateEvent(AdminEventDto dto);
    List<AdminEventDto> selectList();
    int countEvents();
    int countAdmins();
}
