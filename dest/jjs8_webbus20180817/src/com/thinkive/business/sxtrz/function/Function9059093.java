package com.thinkive.business.sxtrz.function;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

import com.thinkive.base.jdbc.DataRow;
import com.thinkive.business.sxtrz.service.QueryWorkDayService;
import com.thinkive.server.BaseFunction;
import com.thinkive.server.InvokeException;
import com.thinkive.server.ResultVo;
/**
 * @description ��ֵȡ�����ڲ�ѯ
 * @author jiaxr
 *
 */
public class Function9059093 extends BaseFunction {
	private static final Logger LOGGER = Logger.getLogger(Function9059093.class);
	@Override
	public ResultVo execute() throws InvokeException, Exception {
		LOGGER.info("======================��ֵȡ�ֵ������ڲ�ѯ��ʼ========================");
		ResultVo resultVo = new ResultVo();
		DataRow data = new DataRow();
		QueryWorkDayService workdayService = new QueryWorkDayService();
		/*
		 * ��ȡ��ѯ���� 
		 */
		String type = this.getStrParameter("type");
		
		/*
		 * ��ǰ��������-����15��֮ǰ����
		 */
		String curWorkDate = workdayService.queryCurWordDay();
		/*
		 * ��һ������-����15��֮�����
		 */
		String nextWorkDate = workdayService.queryNextWordDay();
		
		/*
		 * ��ֵ������ڲ�ѯ ��ͨȡ��������ڲ�ѯ
		 */
		if("1".equals(type) || "3".equals(type)){
		    /*
		     * 15��֮ǰ ��ֵ��ʼ������������ ��ͨȡ�ֵ�������
		     */
		    String beforeDate = workdayService.queryNextWordDayByDate(curWorkDate);
		    /*
		     * 15��֮�� ��ֵ��ʼ������������ ��ͨȡ�ֵ�������
		     */
		    String afterDate = workdayService.queryNextWordDayByDate(nextWorkDate);
		    data.set("beforeDate", beforeDate);
		    data.set("afterDate", afterDate);
		    resultVo.setResult(data);
		    return resultVo;
		}
		
		
        /*
         * ��ǰ��������
         */
        String curLiQuDate = workdayService.queryCurClearDay();
        /*
         * ϵͳ���õı���ʱ��
         */
        String transCloseTime = workdayService.queryTransCloseTime();
        /*
         * ��ǰ����-��ֵȡ�ֲ����ɹ�ʱ��
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String curDate = sdf.format(new Date());
        /*
         * ��ǰʱ��-��ֵȡ�ֲ����ɹ�ʱ��
         */
        String curTime = new SimpleDateFormat("HHmmss").format(new Date());
        
		long liquDateTime = Long.parseLong(curLiQuDate.concat(curTime));
        long dateCloseTime = Long.parseLong(curLiQuDate.concat(transCloseTime));
        LOGGER.info("��ȡ��ǰ������+ϵͳʱ��[" + liquDateTime + "]" + "��ȡ��ǰ������+����ʱ��[" + dateCloseTime + "]");
        String workdate;
        if (liquDateTime <= dateCloseTime && curDate.equals(curLiQuDate) && curLiQuDate.equals(curWorkDate)) {
            // ������
            workdate = curWorkDate;
        } else {
            // δ����
            workdate = nextWorkDate;
        }

		
		
		/*
		 * ��ֵ�ɹ����ڲ�ѯ
		 */
		if("2".equals(type)){
		    /*
		     * ������������
		     */
		    String revenueDate = workdayService.queryNextWordDayByDate(workdate);
		    /*
		     * ���浽������
		     */
		    Calendar calendar = new GregorianCalendar();
		    calendar.setTime(new Date());
		    calendar.add(Calendar.DATE,1);
		    String receivedDate = sdf.format(calendar.getTime());
		    
		    data.set("operatetime", curDate.concat(curTime));
		    data.set("revenueDate", revenueDate);
		    data.set("receivedDate", receivedDate);
		    resultVo.setResult(data);
		    return resultVo;
		}
		
		/*
		 * ��ͨȡ�����ڲ�ѯ
		 */
		if("4".equals(type)){
		    /*
		     * ȡ�ֳɹ�ʱ��
		     */
		    String operatetime = curDate.concat(curTime);
		    /*
             * ��������
             */
            String revenueDate = workdayService.queryNextWordDayByDate(workdate);
            data.set("operatetime", operatetime);
            data.set("revenueDate", revenueDate);
            resultVo.setResult(data);
            return resultVo;
		}
		
		return resultVo;
	}
	

}
