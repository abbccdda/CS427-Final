package edu.ncsu.csc.itrust.beans.loaders;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc.itrust.beans.ObstetricsBean;

public class ObstetricsLoader implements BeanLoader<ObstetricsBean> {

	@Override
	public List<ObstetricsBean> loadList(ResultSet rs) throws SQLException {
		List<ObstetricsBean> list = new ArrayList<ObstetricsBean>();
		while (rs.next()) {
			list.add(loadSingle(rs));
		}
		return list;
	}
	
	@Override
	public ObstetricsBean loadSingle(ResultSet rs) throws SQLException {
		ObstetricsBean bean = new ObstetricsBean();
		bean.setMID(rs.getLong("MID"));
		bean.setDeliveryMethod(rs.getString("deliveryMethod"));
		bean.setYearOfConception(rs.getInt("yearOfConception"));
		bean.setHoursLabor(rs.getDouble("hoursLabor"));
		bean.setWeeksPregnant(rs.getString("weeksPregnant"));
		return bean;
	}

	@Override
	public PreparedStatement loadParameters(PreparedStatement ps,
			ObstetricsBean bean) throws SQLException {
		throw new IllegalStateException("unimplemented!");
	}

}