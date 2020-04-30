package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class RoleResult implements Serializable {
    private String id;
    /**
     * 角色名
     */
    private String name;
    /**
     * 说明
     */
    private String description;
    /**
     * 企业id
     */
    private String companyId;

    private List<String> permIds = new ArrayList<>();

    public RoleResult(Role role) {
        BeanUtils.copyProperties(role, this);
        Set<Permission> permissions = role.getPermissions();
        if (permissions != null && permissions.size() > 0) {
            this.permIds = permissions.stream().map(Permission::getId).collect(Collectors.toList());
        }
    }
}
