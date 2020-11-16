package com.epam.rd.izh.service;

import com.epam.rd.izh.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class RoleService {

    RoleRepository roleRepository;

    private Map<String, String> rolesMap;
    private List<String> rolesTitles;

    public RoleService(RoleRepository roleRepository) {
        if(roleRepository != null) {
            this.roleRepository = roleRepository;
            rolesMap = roles();
            rolesTitles = new ArrayList<>(rolesMap.keySet());
        }
    }

    private Map<String, String> roles(){
        return roleRepository.getRoles();
    }

    public String getRole(String roleTitle){
        return rolesMap.get(roleTitle);
    }

    public List<String> getRolesTitles() { return rolesTitles;}

    public int getRoleId(String role) { return roleRepository.getRoleId(role); }
}
