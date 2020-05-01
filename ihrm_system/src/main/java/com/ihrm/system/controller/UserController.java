package com.ihrm.system.controller;

import com.ihrm.common.bean.QueryPageBean;
import com.ihrm.domain.constants.UserLevelConstants;
import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;

import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.common.utils.PageUtils;
import com.ihrm.common.utils.ParamCheckUtils;
import com.ihrm.common.utils.QueryResultUtils;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.domain.system.response.UserResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author zhang
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/sys")
public class UserController extends BaseController {

    private final UserService userService;
    private final JwtUtils jwtUtils;
    private final PermissionService permissionService;

    public UserController(UserService userService, JwtUtils jwtUtils, PermissionService permissionService) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.permissionService = permissionService;
    }

    /**
     * 添加用户
     *
     * @param user 用户操作参数
     * @return Result
     */
    @PostMapping("/user")
    public Result add(@RequestBody User user) {
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        userService.add(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 用户头像上传
     * @param id 用户id
     * @param file 用户头像图片
     * @return Result
     */
    @RequestMapping("/user/upload/{id}")
    public Result upload(@PathVariable("id") String id, @RequestParam(name = "file") MultipartFile file) throws Exception {
        if (file == null) {
            return new Result(ResultCode.FILE_ERROR);
        }
        String imageUrl = userService.upload(id, file);
        return new Result(ResultCode.SUCCESS, imageUrl);
    }

    /**
     * 根据id删除用户
     *
     * @param id 用户id
     * @return Result
     */
    @RequiresPermissions(value = "API-USER-DELETE")
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE, name = "API-USER-DELETE")
    public Result deleteById(@PathVariable("id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 更新用户信息
     *
     * @param id   用户id
     * @param user 用户操作参数
     * @return Result
     */
    @PutMapping("/user/{id}")
    public Result update(@PathVariable("id") String id, @RequestBody User user) {
        user.setId(id);
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param id 用户id
     * @return Result
     */
    @GetMapping("/user/{id}")
    public Result findById(@PathVariable("id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS, new UserResult(user));
    }

    /**
     * 条件分页查询用户
     *
     * @param page 当前页码
     * @param size 每页显示的条数
     * @param map  查询条件
     * @return Result
     */
    @GetMapping("/user")
    public Result findPage(Integer page, Integer size, @RequestParam(value = "map", required = false) Map<String, Object> map) {
        QueryPageBean queryPageBean = PageUtils.checkPage(new QueryPageBean(page, size, null));
        Page<User> userPage = userService.findPage(queryPageBean, map);
        return QueryResultUtils.checkQueryPageResult(userPage);
    }

    /**
     * 分配角色
     *
     * @param map 参数
     * @return Result
     */
    @PutMapping(value = "/user/assignRoles")
    public Result assignRoles(@RequestBody Map<String, Object> map) {

        if (map == null || map.size() <= 0) {
            return new Result(ResultCode.PARAMETER_ERROR);
        }
        String userId = (String) map.get("id");
        List<String> roleIds = (List<String>) map.get("roleIds");
        userService.assignRoles(userId, roleIds);
        return new Result(ResultCode.SUCCESS);
    }


    public static void main(String[] args) {
        String md5Password = new Md5Hash("123456", "13800000003", 3).toString();
        System.out.println(md5Password);
    }


    /**
     * 用户登录
     *
     * @param map 登录参数
     * @return Result
     * @throws CommonException 自定义异常
     */
    @PostMapping("/login")
    public Result login(@RequestBody Map<String, String> map, HttpServletResponse response) throws CommonException {
        ParamCheckUtils.checkMapParam(map);
        String mobile = map.get("mobile");
        String password = map.get("password");
        if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return new Result(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        //对密码加密
        password = new Md5Hash(password, mobile, 3).toString();
        UsernamePasswordToken upToken = new UsernamePasswordToken(mobile, password);
        //获取subject
        Subject subject = SecurityUtils.getSubject();
        subject.login(upToken);
        Serializable id = subject.getSession().getId();
        return new Result(ResultCode.SUCCESS, id);
        //jwt方式登录
     /*   if (StringUtils.isEmpty(mobile) || StringUtils.isEmpty(password)) {
            return new Result(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }
        User user = userService.findUserByMobilePhone(mobile);
        if (user != null && password.equals(user.getPassword())) {
            String permissonApis = null;
            Map<String, Object> paramMap = new HashMap<>(0);
            //封装此用户具有的api信息
            Set<Role> roles = user.getRoles();
            if (roles != null && roles.size() > 0) {
                StringBuilder sb = new StringBuilder();
                for (Role role : roles) {
                    Set<Permission> permissions = role.getPermissions();
                    if (permissions != null && permissions.size() > 0) {
                        for (Permission permission : permissions) {
                            if (PermissionConstants.PY_API == permission.getType()) {
                                sb.append(permission.getCode()).append(",");
                            }
                        }
                    }
                }
                permissonApis = sb.toString();
            }
            //这里设置这些是在拦截器中和BaseController中需要所使用
            paramMap.put("companyId", user.getCompanyId());
            paramMap.put("companyName", user.getCompanyName());
            paramMap.put("departmentId", user.getDepartmentId());
            paramMap.put("departmentName", user.getDepartmentName());
            paramMap.put("enableState", user.getEnableState());
            paramMap.put("permissonApis", permissonApis);
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), paramMap);
            //将生成的jwt令牌添加到请求头中,注意,后期别的项目中可以采用这种方式,这个项目中只要将token返回给前端就好,
            // 前端代码中已经手动添加了请求头,所以不用在后台操作cookie了
            *//*Cookie cookie = new Cookie("Authorization", "Bearer-" + token);
            cookie.setPath("/");
            //默认单位是秒,这里设置存活时间为24个小时
            cookie.setMaxAge(60 * 60 * 24);
            response.addCookie(cookie);*//*
            return new Result(ResultCode.SUCCESS, token);
        } else {
            return new Result(ResultCode.USERNAME_OR_PASSWORD_ERROR);
        }*/
    }

    /**
     * 用户登录后钩子函数查询用户信息
     *
     * @param request request
     * @return Result
     * @throws CommonException 自定义异常
     */
    @PostMapping(value = "/profile")
    public Result profile(HttpServletRequest request) throws CommonException {
        //使用jwt方式获取用户信息
       /* String userId = claims.getId();
        User user = userService.findById(userId);
        if (user == null) {
            return new Result(ResultCode.UNAUTHENTICATED);
        }

        ProfileResult profileResult = null;
        String userLevel = user.getLevel();
        //企业普通用户-->查询的是自己拥有角色的权限列表
        if (UserLevelConstants.USER.equals(userLevel)) {
            profileResult = new ProfileResult(user);
        } else {
            Map<String, Object> map = new HashMap<>(0);
            //企业管理员-->查询的是企业可见的权限列表
            if (UserLevelConstants.COADIM.equals(userLevel)) {
                map.put("enVisible", "1");
            }
            List<Permission> permissionList = permissionService.findAll(map);
            profileResult = new ProfileResult(user, permissionList);
        }*/
        //使用shiro框架获取安全数据
        //获取session中的安全数据
        Subject subject = SecurityUtils.getSubject();
        //1.subject获取所有的安全数据集合
        PrincipalCollection principals = subject.getPrincipals();
        //2.获取安全数据
        ProfileResult result = (ProfileResult) principals.getPrimaryPrincipal();
        return new Result(ResultCode.SUCCESS, result);
    }
}
