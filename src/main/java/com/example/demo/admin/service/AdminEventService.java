package com.example.demo.admin.service;

import com.example.demo.admin.dao.IAdminEventDao;
import com.example.demo.admin.dto.AdminEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventService {

    private final IAdminEventDao dao;

    public int insertEvent(AdminEventDto dto) {
        return dao.insertEvent(dto);
    }

    @Transactional
    public AdminEventDto selectEvent(Long event_no) {
        dao.increaseEvent(event_no);
        return dao.selectAll(event_no);
    }

    public int updateEvent(AdminEventDto dto) {
        return dao.updateEvent(dto);
    }

    public int deleteEvent(Long event_no) {
        return dao.deleteEvent(event_no);
    }

    public List<AdminEventDto> selectList() {
        return dao.selectList();
    }

    public int countEvents() {
        return dao.countEvents();
    }

    public int countAdmins() {
        return dao.countAdmins();
    }


}
