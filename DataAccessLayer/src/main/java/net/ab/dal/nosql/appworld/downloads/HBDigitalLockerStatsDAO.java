package net.ab.dal.nosql.appworld.downloads;

import net.ab.dal.nosql.hbase.HBaseBean;
import net.ab.dal.nosql.hbase.annotations.HBaseColumn;
import net.ab.dal.nosql.hbase.annotations.HBaseRow;
import net.ab.dal.nosql.hbase.annotations.HBaseTable;

/**
 * @author abakle
 * @version BBW 4.x
 *
 */
@HBaseTable(tableName="digitallocker", columnFamily="stats")
public class HBDigitalLockerStatsDAO extends HBaseBean {
	@HBaseRow (keyIndex=0)
	private Long customerId;
	
	@HBaseColumn
	private Long app;
	@HBaseColumn
	private Long freeApp;
	@HBaseColumn
	private Long freeGame;
	@HBaseColumn
	private Long game;
	@HBaseColumn
	private Long music;
	@HBaseColumn
	private Long video;
	// For POC purpose just get the Count for these product Type
	@HBaseColumn
	private Long theme;
	@HBaseColumn
	private Long rt;
	@HBaseColumn
	private Long magazine;
	@HBaseColumn
	private Long book;
	
	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getApp() {
		return app;
	}

	public void setApp(Long apps) {
		this.app = apps;
	}

	public Long getFreeApp() {
		return freeApp;
	}

	public void setFreeApp(Long freeApps) {
		this.freeApp = freeApps;
	}

	public Long getFreeGame() {
		return freeGame;
	}

	public void setFreeGame(Long freeGames) {
		this.freeGame = freeGames;
	}

	public Long getGame() {
		return game;
	}

	public void setGame(Long games) {
		this.game = games;
	}

	public Long getMusic() {
		return music;
	}

	public void setMusic(Long music) {
		this.music = music;
	}

	public Long getVideo() {
		return video;
	}

	public void setVideo(Long video) {
		this.video = video;
	}

	public Long getTheme() {
		return theme;
	}

	public void setTheme(Long theme) {
		this.theme = theme;
	}

	public Long getRt() {
		return rt;
	}

	public void setRt(Long ringtone) {
		this.rt = ringtone;
	}

	public Long getMagazine() {
		return magazine;
	}

	public void setMagazine(Long magazine) {
		this.magazine = magazine;
	}

	public Long getBook() {
		return book;
	}

	public void setBook(Long book) {
		this.book = book;
	}

	
	@Override
	public boolean isRowKeyComplete() {
		if(null!= this.customerId)
			return true;
		else 
			return false;
					
	}
}
