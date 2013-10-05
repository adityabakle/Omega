package net.ab.dal.nosql.appworld.downloads;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;

/**
 * RowKey: <code>customerId-contentId-fileBundleId-reverseTimeStamp</code><br/><br/>
 * <code>reverseTimeStamp</code> - will be set by HBaseOps during add purchase.
 *
 */
@HBaseTable (tableName="download", columnFamily="stats")
public class HBDownloadStatsDAO extends HBaseBean {
	@HBaseRow (keyIndex=0)
	private Long contentId;
	@HBaseRow (keyIndex=1)
	private Long platformDeviceTypeMapId;
	
	@HBaseColumn
	private Long count;
	
	
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getPlatformDeviceTypeMapId() {
		return platformDeviceTypeMapId;
	}

	public void setPlatformDeviceTypeMapId(Long platformDeviceTypeMapId) {
		this.platformDeviceTypeMapId = platformDeviceTypeMapId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public boolean isRowKeyComplete() {
		if (null != this.contentId)
			return true;
		else 
			return false;
	}
}
