package com.ihrm.domain.company.response;

import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author zhang
 */
@Getter
@Setter
@NoArgsConstructor
public class DeptListResult implements Serializable {

    private static final long serialVersionUID = 6397150848439914376L;
    private String companyId;
    private String companyName;

    /**
     * 公司联系人
     */
    private String companyManage;
    private List<Department> depts;

    public DeptListResult(Company company, List depts) {
        this.companyId = company.getId();
        this.companyName = company.getName();
        this.companyManage = company.getLegalRepresentative();//公司联系人
        this.depts = depts;
    }
}
