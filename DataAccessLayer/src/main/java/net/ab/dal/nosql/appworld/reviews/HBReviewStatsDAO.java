package net.ab.dal.nosql.appworld.reviews;

import java.math.BigDecimal;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;

/**
 * RowKey: <code>contentId-platformDeviceMapId-countryId-languageId-releaseId-reverseTimeStamp</code><br/><br/>
 * <code>reverseTimeStamp</code> - will be set by HBaseOps during add review.
 * @author abakle
 * @version BBW 4.4.0
 *
 */
@HBaseTable (tableName="review", columnFamily="stats")
public class HBReviewStatsDAO extends HBaseBean  {
	@HBaseRow (keyIndex=0)
	private Long contentId;
	@HBaseRow (keyIndex=1)
	private Long releaseId;
	@HBaseRow (keyIndex=2)
	private Integer platformDeviceMapId;
	@HBaseRow (keyIndex=3)
	private Long countryId;
	@HBaseRow (keyIndex=4)
	private Long languageId;
	
	@HBaseColumn
	private Long totalReviews;
	@HBaseColumn
	private Long totalRatings;
	@HBaseColumn
	private BigDecimal averageRating;
	@HBaseColumn
	private Long total1Star;
	@HBaseColumn
	private Long total2Star;
	@HBaseColumn
	private Long total3Star;
	@HBaseColumn
	private Long total4Star;
	@HBaseColumn
	private Long total5Star;
	
	public Long getContentId() {
		return contentId;
	}

	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}

	public Long getReleaseId() {
		return releaseId;
	}

	public void setReleaseId(Long releaseId) {
		this.releaseId = releaseId;
	}

	public Integer getPlatformDeviceMapId() {
		return platformDeviceMapId;
	}

	public void setPlatformDeviceMapId(Integer platformDeviceMapId) {
		this.platformDeviceMapId = platformDeviceMapId;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public Long getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(Long totalReviews) {
		this.totalReviews = totalReviews;
	}

	public Long getTotalRatings() {
		return totalRatings;
	}

	public void setTotalRatings(Long totalRatings) {
		this.totalRatings = totalRatings;
	}

	public BigDecimal getAverageRating() {
		if(this.averageRating==null){
			if(this.totalReviews!=null && this.totalReviews.longValue()>0){
				this.averageRating = BigDecimal.valueOf(this.totalRatings).divide(BigDecimal.valueOf(this.totalReviews));
			}
		}
		return averageRating;
	}

	public void setAverageRating(BigDecimal averageRating) {
		this.averageRating = averageRating;
	}

	public Long getTotal1Star() {
		return total1Star;
	}

	public void setTotal1Star(Long total1Star) {
		this.total1Star = total1Star;
	}

	public Long getTotal2Star() {
		return total2Star;
	}

	public void setTotal2Star(Long total2Star) {
		this.total2Star = total2Star;
	}

	public Long getTotal3Star() {
		return total3Star;
	}

	public void setTotal3Star(Long total3Star) {
		this.total3Star = total3Star;
	}

	public Long getTotal4Star() {
		return total4Star;
	}

	public void setTotal4Star(Long total4Star) {
		this.total4Star = total4Star;
	}

	public Long getTotal5Star() {
		return total5Star;
	}

	public void setTotal5Star(Long total5Star) {
		this.total5Star = total5Star;
	}

	@Override
	public boolean isRowKeyComplete() {
		if(null!=this.contentId || null!=this.countryId
				|| null!=this.languageId || null!=this.platformDeviceMapId 
				|| null!=this.releaseId)
			return true;
		else return false;
	}
	

}
