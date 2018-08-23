package com.thinkive.business.sxtrz.service;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.service.BaseService;
import org.apache.log4j.Logger;
/**
 * @description 查询工作日
 * @author jiaxr
 */
public class QueryWorkDayService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(QueryWorkDayService.class);

    public JdbcTemplate getJdbcTemplate() {
        return getJdbcTemplate("sxtrz");
    }
    
    /**
     * @description 查询当前清算日期
     */
    public String queryCurClearDay() {
        return queryWordDay("50000051");
    }
    /**
     * @description 查询当前工作日
     */
    public String queryCurWordDay() {
        return queryWordDay("50000002");
    }
    /**
     * @description 查询下一工作日
     */
    public String queryNextWordDay() {
        return queryWordDay("50000003");
    }
    /**
     * @description 查询系统设置的闭市时间
     */
    public String queryTransCloseTime() {
        return queryWordDay("50000005");
    }
    
    /**
     * @description 工作日相关查询
     */
    public String queryWordDay(String paraid) {
        String sql = "select paravalue from sys_param where paraid = ? ";
        return getJdbcTemplate().queryString( sql, new Object[]{paraid});
    }
    
    /**
     * @description 查询指定日期的下一工作日日期
     * @param date 格式yyyyMMdd 
     * @return
     */
    public String queryNextWordDayByDate(String date) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ");
        sql.append("     MIN(b.workday) nextworkdate ");
        sql.append(" FROM ");
        sql.append("     sys_workday_set b ");
        sql.append(" LEFT JOIN sys_workday_pgm c ON b.pgmno = c.pgmno ");
        sql.append(" WHERE ");
        sql.append("     c.pgmtype = '1' ");
        sql.append(" AND b.workday > ? ");
        
        return getJdbcTemplate().queryString(sql.toString(), new Object[]{date});
    }

}
