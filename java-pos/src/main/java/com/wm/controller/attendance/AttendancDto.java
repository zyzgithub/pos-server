package com.wm.controller.attendance;

import java.util.ArrayList;
import java.util.List;

import com.wm.controller.takeout.vo.AttendanceVo;

public class AttendancDto {
	private String date;
	private List<AttendanceVo> attendanceList = new ArrayList<AttendanceVo>();
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<AttendanceVo> getAttendanceList() {
		return attendanceList;
	}
	public void setAttendanceList(List<AttendanceVo> attendanceList) {
		this.attendanceList = attendanceList;
	}
	
	public void addAttendRecord(AttendanceVo attendanceVo){
		attendanceList.add(attendanceVo);
	}
	
	
}
