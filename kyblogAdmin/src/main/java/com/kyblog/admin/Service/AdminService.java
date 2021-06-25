package com.kyblog.admin.Service;

import com.kyblog.admin.Dao.AdminDao;
import com.kyblog.api.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    @Autowired
    private AdminDao adminDao;
    public Profile getProfile(String username) {
        return adminDao.findProfileByUsername(username);
    }
    public Profile getProfile(Integer uid) {
        return adminDao.findProfileByUid(uid);
    }

    public void updateProfile(Profile profile) {
        adminDao.updateProfile(profile);
    }
}
