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
 * @description 充值取现日期查询
 * @author jiaxr
 *
 */
public class Function9059093 extends BaseFunction {
	private static final Logger LOGGER = Logger.getLogger(Function9059093.class);
	@Override
	public ResultVo execute() throws InvokeException, Exception {
		LOGGER.info("======================充值取现到账日期查询开始========================");
		ResultVo resultVo = new ResultVo();
		DataRow data = new DataRow();
		QueryWorkDayService workdayService = new QueryWorkDayService();
		/*
		 * 获取查询类型 
		 */
		String type = this.getStrParameter("type");
		
		/*
		 * 当前工作日期-用于15点之前操作
		 */
		String curWorkDate = workdayService.queryCurWordDay();
		/*
		 * 下一工作日-用于15点之后操作
		 */
		String nextWorkDate = workdayService.queryNextWordDay();
		
		/*
		 * 充值相关日期查询 普通取现相关日期查询
		 */
		if("1".equals(type) || "3".equals(type)){
		    /*
		     * 15点之前 充值开始计算收益日期 普通取现到账日期
		     */
		    String beforeDate = workdayService.queryNextWordDayByDate(curWorkDate);
		    /*
		     * 15点之后 充值开始计算收益日期 普通取现到账日期
		     */
		    String afterDate = workdayService.queryNextWordDayByDate(nextWorkDate);
		    data.set("beforeDate", beforeDate);
		    data.set("afterDate", afterDate);
		    resultVo.setResult(data);
		    return resultVo;
		}
		
		
        /*
         * 当前清算日期
         */
        String curLiQuDate = workdayService.queryCurClearDay();
        /*
         * 系统设置的闭市时间
         */
        String transCloseTime = workdayService.queryTransCloseTime();
        /*
         * 当前日期-充值取现操作成功时间
         */
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String curDate = sdf.format(new Date());
        /*
         * 当前时间-充值取现操作成功时间
         */
        String curTime = new SimpleDateFormat("HHmmss").format(new Date());
        
		long liquDateTime = Long.parseLong(curLiQuDate.concat(curTime));
        long dateCloseTime = Long.parseLong(curLiQuDate.concat(transCloseTime));
        LOGGER.info("获取当前清算日+系统时间[" + liquDateTime + "]" + "获取当前清算日+闭市时间[" + dateCloseTime + "]");
        String workdate;
        if (liquDateTime <= dateCloseTime && curDate.equals(curLiQuDate) && curLiQuDate.equals(curWorkDate)) {
            // 已切日
            workdate = curWorkDate;
        } else {
            // 未切日
            workdate = nextWorkDate;
        }

		
		
		/*
		 * 充值成功日期查询
		 */
		if("2".equals(type)){
		    /*
		     * 计算收益日期
		     */
		    String revenueDate = workdayService.queryNextWordDayByDate(workdate);
		    /*
		     * 收益到账日期
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
		 * 普通取现日期查询
		 */
		if("4".equals(type)){
		    /*
		     * 取现成功时间
		     */
		    String operatetime = curDate.concat(curTime);
		    /*
             * 到账日期
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
