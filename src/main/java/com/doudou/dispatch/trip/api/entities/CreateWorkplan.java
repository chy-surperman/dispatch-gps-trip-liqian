package com.doudou.dispatch.trip.api.entities;
import java.io.Serializable;
import java.util.Date;

public class CreateWorkplan implements Serializable,Comparable<CreateWorkplan>{

	private static final long serialVersionUID = 1L;
	private Long id;
	private Date date;
	private String driverId;
	private String driverName;
	private String vehicleId;
	private String plateNum;
	private String routeName;
	private Date scheduleTime;
	private Date departureTime;
	private Date dispatchTime;
	private Date arrivalTime;
	private String status; // 1 出发  2到达 3作废 4复位 5不需要复位的作废
	private String starttag;
	private String siteName;
	private float trip;

	public float getTripValue() {
		return tripValue;
	}

	public void setTripValue(float tripValue) {
		this.tripValue = tripValue;
	}

	private float tripValue;
	private String selfNum;
	private int comsuTime;
	private long late;//迟到时间或早到时间
	private double spacing;
	private int whetherSwipeDeparture;	//是否刷卡发车（1，是的）

	private int isAdvanceDeparture; //是否提前发车（2、提前发车，3、正常发车）
	private int departureWay;	//发车方式   2、GPS发车，3、手动发车
	private int arrivalWay;		//到达方式   2、GPS到达，3、手动到达
	private double mileage;
	private int reportSiteCount;
	private double reportSiteRate;
	private int isFinishTrip;			//是不是一个完整趟次

	private String scheduleString;
	private long scheduleTimestamp;
	private int departureTimeOut;
	private String departureString;
	private String arrivalString;
	private String dateString;

	private String property;			//null 正常排班，“1” 占位排班，“2”，包车

	private int workplanSource;		//班次产生来源

	private int isThroughUpMonitorSite;
	private int isThroughDownMonitorSite;
	private double insideFenceRatio;	//GPS在围栏内比例
	private int hasNextWorkplan;

	private String gpsSign;

	private CreateWorkplan nextWorkplan;

	private String createNewGps;
	private int isDeletedGps;

	public int getIsDeletedGps() {
		return isDeletedGps;
	}

	public void setIsDeletedGps(int isDeletedGps) {
		this.isDeletedGps = isDeletedGps;
	}

	public String getCreateNewGps() {
		return createNewGps;
	}

	public void setCreateNewGps(String createNewGps) {
		this.createNewGps = createNewGps;
	}

	public String getGpsSign() {
		return gpsSign;
	}

	public void setGpsSign(String gpsSign) {
		this.gpsSign = gpsSign;
	}

	/**
	 * @return the hasNextWorkplan
	 */
	public int getHasNextWorkplan() {
		return hasNextWorkplan;
	}
	/**
	 * @param hasNextWorkplan the hasNextWorkplan to set
	 */
	public void setHasNextWorkplan(int hasNextWorkplan) {
		this.hasNextWorkplan = hasNextWorkplan;
	}
	public double getInsideFenceRatio() {
		return insideFenceRatio;
	}
	public void setInsideFenceRatio(double insideFenceRatio) {
		this.insideFenceRatio = insideFenceRatio;
	}
	public int getIsThroughUpMonitorSite() {
		return isThroughUpMonitorSite;
	}
	public void setIsThroughUpMonitorSite(int isThroughUpMonitorSite) {
		this.isThroughUpMonitorSite = isThroughUpMonitorSite;
	}
	public int getIsThroughDownMonitorSite() {
		return isThroughDownMonitorSite;
	}
	public void setIsThroughDownMonitorSite(int isThroughDownMonitorSite) {
		this.isThroughDownMonitorSite = isThroughDownMonitorSite;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public int getWorkplanSource() {
		return workplanSource;
	}
	public void setWorkplanSource(int workplanSource) {
		this.workplanSource = workplanSource;
	}
	public long getLate() {
		return late;
	}
	public void setLate(long late) {
		this.late = late;
	}
	public double getSpacing() {
		return spacing;
	}
	public void setSpacing(double spacing) {
		this.spacing = spacing;
	}
	public int getWhetherSwipeDeparture() {
		return whetherSwipeDeparture;
	}
	public void setWhetherSwipeDeparture(int whetherSwipeDeparture) {
		this.whetherSwipeDeparture = whetherSwipeDeparture;
	}
	public Date getDispatchTime() {
		return dispatchTime;
	}
	public void setDispatchTime(Date dispatchTime) {
		this.dispatchTime = dispatchTime;
	}
	public int getComsuTime() {
		return comsuTime;
	}
	public void setComsuTime(int comsuTime) {
		this.comsuTime = comsuTime;
	}
	public int getDepartureWay() {
		return departureWay;
	}
	public void setDepartureWay(int departureWay) {
		this.departureWay = departureWay;
	}
	public int getArrivalWay() {
		return arrivalWay;
	}
	public void setArrivalWay(int arrivalWay) {
		this.arrivalWay = arrivalWay;
	}
	public double getMileage() {
		return mileage;
	}
	public void setMileage(double mileage) {
		this.mileage = mileage;
	}
	public int getReportSiteCount() {
		return reportSiteCount;
	}
	public void setReportSiteCount(int reportSiteCount) {
		this.reportSiteCount = reportSiteCount;
	}
	public double getReportSiteRate() {
		return reportSiteRate;
	}
	public void setReportSiteRate(double reportSiteRate) {
		this.reportSiteRate = reportSiteRate;
	}
	public int getIsFinishTrip() {
		return isFinishTrip;
	}
	public void setIsFinishTrip(int isFinishTrip) {
		this.isFinishTrip = isFinishTrip;
	}
	public int getIsAdvanceDeparture() {
		return isAdvanceDeparture;
	}
	public void setIsAdvanceDeparture(int isAdvanceDeparture) {
		this.isAdvanceDeparture = isAdvanceDeparture;
	}
	public String getDepartureString() {
		return departureString;
	}
	public void setDepartureString(String departureString) {
		this.departureString = departureString;
	}
	public String getArrivalString() {
		return arrivalString;
	}
	public void setArrivalString(String arrivalString) {
		this.arrivalString = arrivalString;
	}
	public String getDateString() {
		return dateString;
	}
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	public int getDepartureTimeOut() {
		return departureTimeOut;
	}
	public void setDepartureTimeOut(int departureTimeOut) {
		this.departureTimeOut = departureTimeOut;
	}
	public long getScheduleTimestamp() {
		return scheduleTimestamp;
	}
	public void setScheduleTimestamp(long scheduleTimestamp) {
		this.scheduleTimestamp = scheduleTimestamp;
	}
	public String getScheduleString() {
		return scheduleString;
	}
	public void setScheduleString(String scheduleString) {
		this.scheduleString = scheduleString;
	}
	public CreateWorkplan getNextWorkplan() {
		return nextWorkplan;
	}
	public void setNextWorkplan(CreateWorkplan nextWorkplan) {
		this.nextWorkplan = nextWorkplan;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDriverId() {
		return driverId;
	}
	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public String getRouteName() {
		return routeName;
	}
	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public Date getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}
	public Date getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(Date arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStarttag() {
		return starttag;
	}
	public void setStarttag(String starttag) {
		this.starttag = starttag;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public float getTrip() {
		return trip;
	}
	public void setTrip(float trip) {
		this.trip = trip;
	}
	public String getSelfNum() {
		return selfNum;
	}
	public void setSelfNum(String selfNum) {
		this.selfNum = selfNum;
	}
	@Override
	public String toString() {
		return "Workplan [id=" + id + ", date=" + date + ", driverId=" + driverId + ", driverName=" + driverName
				+ ", vehicleId=" + vehicleId + ", plateNum=" + plateNum + ", routeName=" + routeName + ", scheduleTime="
				+ scheduleTime + ", departureTime=" + departureTime + ", arrivalTime=" + arrivalTime + ", status="
				+ status + ", starttag=" + starttag + ", siteName=" + siteName + ", trip=" + trip + ", selfNum="
				+ selfNum + "]";
	}
	@Override
	public int compareTo(CreateWorkplan o) {
		if(null != this.scheduleTime
				&& o.getScheduleTime() != null)
			return this.scheduleTime.compareTo(o.getScheduleTime());
		return 0;
	}
}
