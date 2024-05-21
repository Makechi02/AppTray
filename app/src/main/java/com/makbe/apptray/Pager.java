package com.makbe.apptray;

import java.util.List;

public class Pager {
	private List<App> apps;

	public Pager(List<App> apps) {
		this.apps = apps;
	}

	public List<App> getApps() {
		return apps;
	}
}
