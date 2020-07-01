package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import web.model.Role;
import web.model.User;
import web.service.userservice.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private UserService service;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String printListUser(Model model) {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Role> roleList = (List<Role>) SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        model.addAttribute("rolelist",roleList);
        model.addAttribute("userCurrent",user);
        model.addAttribute("userlist", service.getAllUsers());
        return "admin";
    }


    @RequestMapping(value = "/admin/insert", method = RequestMethod.POST)
    public String insertUser(@RequestParam String name, String password, int age, String roleInsert) {
        //String[] roleArrays = role.split(",");
        String[] roleArray = roleInsert.split(",");
        User user = new User.Builder()
                .withName(name)
                .withPassword(password)
                .withAge(age)
                .withRole(roleArray)
                .build();
        service.insertUser(user);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/delete", method = RequestMethod.POST)
    String deleteUsers(@RequestParam int idDelete, HttpServletRequest request) {
        service.deleteUser(idDelete);
        return "redirect:/admin";
    }

    @RequestMapping(value = "/admin/update", method = RequestMethod.GET)
    String updateUser(Model model, @RequestParam int idUpdate) {
        User user = service.getUserById(idUpdate);
        model.addAttribute("userupdate", user);
        model.addAttribute("roleListupdate", user.getRoleSet());
        return "admin";
    }

    @RequestMapping(value = "/admin/update", method = RequestMethod.POST)
    String updateUserPost(@RequestParam String id, String name, String password, String role, int age) {
        //String[] role_array = role.split(",");
        User user = service.getUserById(Integer.parseInt(id));
        user.setName(name);
        user.setAge(age);
        user.setPassword(password);
        if (role != null){
            String[] roleArray = role.split(",");
            user.userUpdateRole(roleArray);
        }
        service.updateUser(user);
        return "redirect:/admin";
    }
}
