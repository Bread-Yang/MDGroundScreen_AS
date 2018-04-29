package org.mdground.api.base;

public enum BusinessType {
	Global(1), Clinic(2), WeChat(3), FILE(4);

	private int type;

	BusinessType(int type) {
		this.setType(type);
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
