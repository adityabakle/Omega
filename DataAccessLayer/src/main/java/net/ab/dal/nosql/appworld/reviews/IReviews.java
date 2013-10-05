package net.ab.dal.nosql.appworld.reviews;

import java.util.List;

import net.ab.dal.nosql.hbase.common.AWHBaseException;

public interface IReviews {
	public HBReviewDAO getReview(HBReviewParam nsparam) throws AWHBaseException;
	public List<HBReviewDAO> getReviews(HBReviewParam nsparam) throws AWHBaseException;
	public HBReviewDAO isReviewPresent(HBReviewParam nsparam) throws AWHBaseException;
	public void addReview(HBReviewDAO review) throws AWHBaseException;
	public void updateReview(HBReviewDAO review) throws AWHBaseException;
	public HBReviewStatsDAO getReviewStats(HBReviewStatsDAO nsreviewStat) throws AWHBaseException;
}
