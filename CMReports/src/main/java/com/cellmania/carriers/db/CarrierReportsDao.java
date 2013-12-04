package com.cellmania.carriers.db;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.log4j.Logger;

import com.cellmania.carriers.sqlSession.CarrierSessionExecutor;
import com.cellmania.carriers.sqlSession.CarrierSqlSessionConfig;
import com.cellmania.carriers.util.CSVWriter;
import com.cellmania.cmreports.common.ObjectConvertor;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.web.util.CMDBService;
import com.cellmania.cmreports.web.util.ServerSettingsConstants;

@SuppressWarnings("unchecked")
public class CarrierReportsDao implements ICarrierReports {
	Logger log = Logger.getLogger(CarrierReportsDao.class);
	
	public CarrierReportsDao(){
		super();
	}

	
	public String createLiveItemReport(RequestDTO rp) throws Exception{
		log.info("In Method createLiveItemReport for params "+rp.getCarrier().getDisplayName()+":" + rp.getReport().getDisplayName());
		log.debug("In Method createLiveItemReport for params " + rp);
		
		HashMap<String, Collection<ReportDataDTO>> hm = new HashMap<String, Collection<ReportDataDTO>>();
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		if("telstra".equals(rp.getCarrier().getName())){
			if("liveItemGames".equals(rp.getReport().getName()))
				rp.setXelement(1048);
			else if("liveItemApps".equals(rp.getReport().getName()))
				rp.setXelement(1085);
		}
		log.info(rp.getXelement());
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".getItemListing", rp);
		hm.put("Rpt1", col);
		if(col!=null)
			log.info("Item Listing Rec Size : "+rp.getCarrier().getDisplayName()+":" + rp.getReport().getDisplayName()+": "+col.size());
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".getItemCountByOS", rp);
		hm.put("Rpt2", col);
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selItemCatDevice", rp);
		hm.put("Rpt7", col);
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".getCatItemCount", rp);
		hm.put("Rpt3", col);
		
		
		ArrayList<String> lsHeader = (ArrayList<String>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selPricePointHeader");
		
		Collection <ReportDataDTO> colCatItem = new ArrayList<ReportDataDTO>();
		for(ReportDataDTO r : col) {
			Map<String,Integer> hmParam = new HashMap<String,Integer>(); 
			hmParam.put("catId", r.getCatId());
			if("telstra".equals(rp.getCarrier().getName())){
				hmParam.put("pmId", 1001);
			} else {
				hmParam.put("pmId", 1005);
			}
			hmParam.put("xelement",rp.getXelement());
			r.setNoOfZeroOrders(r.getNoOfOrders());
			Collection<ReportDataDTO> tmp = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selCatItemPriceCount", hmParam);
			if(tmp!=null && tmp.size()>0) {
				for(ReportDataDTO tr : tmp)
					r.addToPriceCount(tr.getDeviceDisplayName(), tr.getNoOfOrders());
			}
			colCatItem.add(r);
		}
		
		hm.put("Rpt6", colCatItem);
	
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".getDeviceItemCount", rp);
		hm.put("Rpt4", col);
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".getItemFreePaidCount", rp);
		hm.put("Rpt5", col);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		
		
		
		Class<?> parTypes[] = new Class[2];
		parTypes[0] = HashMap.class;
		parTypes[1] = ArrayList.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		log.info("XLS Writer Method Name : "+met.getName());
		
		Object arglist[] = new Object[2];
        arglist[0] = hm;
        arglist[1] = lsHeader;
        
        Object obj =  cls.newInstance();
        
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        return (String) met.invoke(obj, arglist);
	}
	

	// Free Item 
	
	public String createFreeItemInvoiceReport(RequestDTO rp)
			throws Exception {
		HashMap<String, Collection<ReportDataDTO>> hmReport = new HashMap<String, Collection<ReportDataDTO>>();
		
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selFreeItemInvoiceReport", rp);
		hmReport.put("rptDetails", col);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = hmReport.getClass();
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = hmReport;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}
	
	// Paid Item 
	
	public String createPaidItemInvoiceReport(RequestDTO rp)
			throws Exception {
		log.debug("In Method invoiceReportPaidItem for params ");
		HashMap<String, Collection<ReportDataDTO>> hmReport = new HashMap<String, Collection<ReportDataDTO>>();
		
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selPaidItemInvoiceReport", rp);
		hmReport.put("rptDetails", col);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = hmReport.getClass();
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = hmReport;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}


	public String createdSQLResult(RequestDTO rp)
			throws Exception {
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		//String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		String sql = rp.getSql();
		
		if(rp.getSql().contains("{0}"))
			sql = sql.replace("{0}", ObjectConvertor.simpleDate(rp.getStartDate(), "dd-MMM-yyyy"));
		
		if(rp.getSql().contains("{1}"))
			sql = sql.replace("{1}", ObjectConvertor.simpleDate(rp.getEndDate(), "dd-MMM-yyyy"));
		
		rp.setSql(sql);	
		Collection<String> col = (Collection<String>) CarrierSessionExecutor.selectList(factory, "Common.sqlExecutor", rp);
		return CSVWriter.writeCSV(rp,col);
	}

	
	public String createRevShareReport(RequestDTO rp) throws Exception {
		log.debug("In Rev Share Report");
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		log.debug("Aquired Session Factory");
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		log.debug("Executing Query for Carrier Report");
		HashMap<String, Collection<ReportDataDTO>> hmReport = new HashMap<String, Collection<ReportDataDTO>>();
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selRevShareReportData", rp);
		hmReport.put("rptDetails", col);
		
		col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selRevShareReportSummaryData", rp);
		hmReport.put("grandTotal", col);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = hmReport.getClass();
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = hmReport;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        log.debug("Generating report for Carriers");
		return (String) met.invoke(obj, arglist);
	}

	
	public String createRevShareReportPaidApps(RequestDTO rp) throws Exception {
		rp.setYelement(1005);
		return createRevShareReport(rp);
	}

	public String createExceptionReport(RequestDTO rp) throws Exception {
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = "Common";
		
		Collection<ReportDataDTO> colRevTotal = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selExceptionRptRevShareTotal", rp);
		
		Collection<ReportDataDTO> colRevChange = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selExceptionRptRevChange", rp);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[2];
		parTypes[0] = Collection.class;
		parTypes[1] = Collection.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[2];
        arglist[0] = colRevTotal;
        arglist[1] = colRevChange;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}

	
	public String createTopHandsetReport(RequestDTO rp) throws Exception {
		log.debug("In Top Handset Report");
		// Current Report portal this report is no longer used. If required can be set as Sql 
		return null;
	}
	
	
	
	public String createJumptapReport(RequestDTO rp) throws Exception {
		log.debug("In Method createJumptapReport for params ");
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selJumtapReportData", rp);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = Collection.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = col;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}

	public String createRingtonePurchaseReport(RequestDTO rp) throws Exception {
		log.debug("In Method createRingtonePurchaseReport for params ");
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selRingtoneReportData", rp);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = Collection.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = col;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}



	public String createCreditCardReport(RequestDTO rp) throws Exception {
		log.debug("In Method createCreditCardReport for params ");
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selCreditCardReportData", rp);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = Collection.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = col;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}



	public String createSimpleRevShareReport(RequestDTO rp) throws Exception {
		log.debug("In Method createSimpleRevShareReport for params ");
		SqlSessionFactory factory = CarrierSqlSessionConfig.getSqlMapClient(rp.getCarrier());
		String mapperNameSpace = rp.getCarrier().getSqlMapperNamespace();
		
		Collection<ReportDataDTO> col = (Collection<ReportDataDTO>) CarrierSessionExecutor.selectList(factory, mapperNameSpace+".selSimpleRevShareReportData", rp);
		
		String xlsClassPackage = CMDBService.getServerSettingsValue(ServerSettingsConstants._CARRIER_XLSCLASS_PACKAGE);
		Class<?> cls = Class.forName(xlsClassPackage + rp.getCarrier().getXlsClassName());
		Class<?> parTypes[] = new Class[1];
		parTypes[0] = Collection.class;
		
		Method met = cls.getMethod(rp.getReport().getXlsApiName(), parTypes);
		Object arglist[] = new Object[1];
        arglist[0] = col;
        
        Object obj =  cls.newInstance();
        Method rqSetter = cls.getMethod("setReqReport", RequestDTO.class);
        rqSetter.invoke(obj, rp);
        
		return (String) met.invoke(obj, arglist);
	}

}
