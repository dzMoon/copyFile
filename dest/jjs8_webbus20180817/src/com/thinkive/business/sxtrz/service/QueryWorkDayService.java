package com.thinkive.business.sxtrz.service;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.base.jdbc.JdbcTemplate;
import com.thinkive.base.service.BaseService;
import org.apache.log4j.Logger;
/**
 * @description ��ѯ������
 * @author jiaxr
 */
public class QueryWorkDayService extends BaseService {
    private static final Logger LOGGER = Logger.getLogger(QueryWorkDayService.class);

    public JdbcTemplate getJdbcTemplate() {
        return getJdbcTemplate("sxtrz");
    }
    
    /**
     * @description ��ѯ��ǰ��������
     */
    public String queryCurClearDay() {
        return queryWordDay("50000051");
    }
    /**
     * @description ��ѯ��ǰ������
     */
    public String queryCurWordDay() {
        return queryWordDay("50000002");
    }
    /**
     * @description ��ѯ��һ������
     */
    public String queryNextWordDay() {
        return queryWordDay("50000003");
    }
    /**
     * @description ��ѯϵͳ���õı���ʱ��
     */
    public String queryTransCloseTime() {
        return queryWordDay("50000005");
    }
    
    /**
     * @description ��������ز�ѯ
     */
    public String queryWordDay(String paraid) {
        String sql = "select paravalue from sys_param where paraid = ? ";
        return getJdbcTemplate().queryString( sql, new Object[]{paraid});
    }
    
    /**
     * @description ��ѯָ�����ڵ���һ����������
     * @param date ��ʽyyyyMMdd 
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
