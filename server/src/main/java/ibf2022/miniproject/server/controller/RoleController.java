package ibf2022.miniproject.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ibf2022.miniproject.server.model.Role;
import ibf2022.miniproject.server.service.RoleService;

@RestController
@RequestMapping
public class RoleController {

    @Autowired
    private RoleService roleService;


    @PostMapping(path = "/createNewRole")
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
    
}
