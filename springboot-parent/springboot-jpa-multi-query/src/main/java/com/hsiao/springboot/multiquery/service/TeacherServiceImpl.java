package com.hsiao.springboot.multiquery.service;

import com.hsiao.springboot.multiquery.entity.Address;
import com.hsiao.springboot.multiquery.entity.Teacher;
import com.hsiao.springboot.multiquery.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hsiao
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired AddressService addressService;
    @Override
    public Teacher getOneSavedTeacher() {
        Teacher teacher = new Teacher();
        Address address = addressService.getOneSavedAddress();
        teacher.setAddress(address);
        teacher.setName(CommonService.getRandomStringByLength(4) + "测试班级名称" + CommonService.getRandomStringByLength(4));
        teacherRepository.save(teacher);
        return teacher;
    }
}
