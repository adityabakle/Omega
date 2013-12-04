package request;

import java.util.Collection;

import com.cellmania.cmreports.db.CMReportFactory;
import com.cellmania.cmreports.db.ICMReports;
import com.cellmania.cmreports.db.request.ExecutionLogDTO;
import com.cellmania.cmreports.db.request.FrequencyDTO;
import com.cellmania.cmreports.db.request.RequestDTO;
import com.cellmania.cmreports.db.request.RequestParams;

public class Executionlog {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ICMReports dao = CMReportFactory.getICMReports();
		ExecutionLogDTO dto = new ExecutionLogDTO();
		dto.setRequest(new RequestDTO());
		dto.getRequest().setRequestId(10l);
		dto.getRequest().setFrequency(new FrequencyDTO());
		dto.getRequest().getFrequency().setCode("O");
		dto.setFileName("TestCSV.csv");
		dto.setId(3l);
		//dao.completeJobExecution(dto);
		
		RequestParams rp = new RequestParams();
		rp.setUserId(2l);
		rp.setStartRow(5);
		rp.setNumRows(5);
		Collection<RequestDTO> req = dao.getRequestForUser(rp);
				for(RequestDTO r : req )
					System.out.println(r.getRequestId());

	}

}
