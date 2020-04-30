package com.ihrm.domain.system.response;

import com.ihrm.common.constants.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

/**
 * @author zhang
 * @version 1.0
 * @date 2020/4/30 9:21
 * 用户信息返回类
 */
@Data
@NoArgsConstructor
public class ProfileResult {

    private String mobile;
    private String username;
    private String company;
    private Map<String, Object> roles = new HashMap<>(0);

    public ProfileResult(User user) {
        if (user == null) {
            return;
        }
        this.mobile = user.getMobile();
        this.company = user.getCompanyName();
        this.username = user.getUsername();

        Set<Role> roles = user.getRoles();
        if (roles == null || roles.size() <= 0) {
            return;
        }
        Set<String> menus = new HashSet<>(0);
        Set<String> apis = new HashSet<>(0);
        Set<String> points = new HashSet<>(0);
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            if (permissions == null || permissions.size() <= 0) {
                return;
            }
            for (Permission permission : permissions) {
                if (permission == null) {
                    return;
                }
                Integer type = permission.getType();
                String code = permission.getCode();
                if (type == PermissionConstants.PY_MENU) {
                    menus.add(code);
                } else if (type == PermissionConstants.PY_API) {
                    apis.add(code);
                } else if (type == PermissionConstants.PY_POINT) {
                    points.add(code);
                }
            }
            this.roles.put("menus", menus);
            this.roles.put("apis", apis);
            this.roles.put("points", points);
        }
    }

    public ProfileResult(User user, List<Permission> list) {
        if (user == null) {
            return;
        }
        this.mobile = user.getMobile();
        this.company = user.getCompanyName();
        this.username = user.getUsername();

        Set<String> menus = new HashSet<>(0);
        Set<String> apis = new HashSet<>(0);
        Set<String> points = new HashSet<>(0);

        if (list != null && list.size() > 0) {
            for (Permission permission : list) {
                String code = permission.getCode();
                Integer type = permission.getType();
                if (PermissionConstants.PY_API == type) {
                    apis.add(code);
                }
                if (PermissionConstants.PY_MENU == type) {
                    menus.add(code);
                }
                if (PermissionConstants.PY_POINT == type) {
                    points.add(code);
                }
            }
            this.roles.put("menus", menus);
            this.roles.put("apis", apis);
            this.roles.put("points", points);
        }
    }
}
