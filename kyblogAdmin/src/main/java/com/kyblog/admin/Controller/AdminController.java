package com.kyblog.admin.Controller;

import com.kyblog.admin.Service.AdminService;
import com.kyblog.api.entity.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.kyblog.api.utils.BlogUtils.getJsonString;

@RestController
public class AdminController {
    @Autowired
    private AdminService adminService;
    @RequestMapping(value = "/profile/{username}", method = RequestMethod.GET)
    public Profile getProfile(@PathVariable("username") String username) {
        return adminService.getProfile(username);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public String updateProfile(@RequestBody Profile profile) {
        adminService.updateProfile(profile);
        return getJsonString(200);
    }

    @RequestMapping(value = "/profile/id/{uid}", method = RequestMethod.GET)
    public Profile getProfile(@PathVariable("uid") Integer uid) {
        return adminService.getProfile(uid);
    }

}
